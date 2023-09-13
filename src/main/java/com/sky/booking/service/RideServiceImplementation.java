package com.sky.booking.service;

import com.sky.booking.domain.RideStatus;
import com.sky.booking.exception.DriverException;
import com.sky.booking.exception.RideException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import com.sky.booking.repository.DriverRepository;
import com.sky.booking.repository.RideRepository;
import com.sky.booking.request.RideRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImplementation implements RideService{

    private final DriverService driverService;
    private final RideRepository rideRepository;
    private final Calculators calculators;
    private final DriverRepository driverRepository;


    @Override
    public Ride requestRide(RideRequest rideRequest, User user) throws DriverException {
        double pickupLatitude = rideRequest.getPickupLatitude();
        double pickupLongitude = rideRequest.getPickupLongitude();
        double destinationLatitude = rideRequest.getDestinationLatitude();
        double destinationLongitude = rideRequest.getDestinationLongitude();
        String pickupArea = rideRequest.getPickupArea();
        String destinationArea = rideRequest.getDestinationArea();

        Ride existingRide = new Ride();
        List<Driver> availableDrivers = driverService.getAvailableDrivers(pickupLatitude, pickupLongitude, existingRide);

        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, pickupLatitude, pickupLongitude);
        if (nearestDriver == null){
            throw new DriverException("Driver not available");
        }

        System.out.println("duration ----- before ride");
        return createRideRequest(user, nearestDriver, pickupLatitude,
                pickupLongitude, destinationLatitude, destinationLongitude,
                pickupArea, destinationArea);
    }

    @Override
    public Ride createRideRequest(User user, Driver nearesDriver, double pickupLatitude,
                                  double pickupLongitude, double destinationLatitude,
                                  double destinationLongitude, String pickupArea, String destinationArea) {
        Ride ride = new Ride();
        ride.setDriver(nearesDriver);
        ride.setUser(user);
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setDestinationLatitude(destinationLatitude);
        ride.setDestinationLongitude(destinationLongitude);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setPickupArea(pickupArea);
        ride.setDestinationArea(destinationArea);
        return rideRepository.save(ride);
    }

    @Override
    public void acceptRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.ACCEPTED);
        Driver driver = ride.getDriver();
        driver.setCurrentRide(ride);
        Random random = new Random();
        int otp = random.nextInt(9000) + 1000;
        ride.setOtp(otp);

        driverRepository.save(driver);
        rideRepository.save(ride);
    }

    @Override
    public void declineRide(Integer rideId, Integer driverId) throws RideException {
        Ride ride = findRideById(rideId);
        System.out.println(ride.getId());
        ride.getDeclinedDrivers().add(driverId);
        System.out.println(ride.getId()+"----"+ride.getDeclinedDrivers());
        List<Driver> availableDriver=driverService.getAvailableDrivers(ride.getPickupLatitude(), ride.getPickupLongitude(), ride);

        Driver nearestDriver = driverService.findNearestDriver(availableDriver, ride.getPickupLatitude(), ride.getPickupLongitude());

        ride.setDriver(nearestDriver);
        rideRepository.save(ride);
    }

    @Override
    public void startRide(Integer rideId, int otp) throws RideException {
        Ride ride = findRideById(rideId);
        if (otp!=ride.getOtp()){
            throw new RideException("Please provide a valid otp");
        }
        ride.setStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    @Override
    public void completeRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());

        double distance= calculators.calculateDistance(ride.getDestinationLatitude(),
                ride.getDestinationLongitude(), ride.getPickupLatitude(), ride.getPickupLongitude());

        LocalDateTime start = ride.getStartTime();
        LocalDateTime end = ride.getEndTime();
        Duration duration = Duration.between(start, end);
        long milliSecond = duration.toMillis();

        System.out.println("Duration ---- "+ milliSecond);
        double fare = calculators.calculateFare(distance);

        ride.setDistance(Math.round(distance*100.0)/100.0);
        ride.setFare((int)Math.round(fare));
        ride.setDuration(milliSecond);
        ride.setEndTime(LocalDateTime.now());

        Driver driver = ride.getDriver();
        driver.getRides().add(ride);
        driver.setCurrentRide(null);

        Integer driversRevenue=(int)(driver.getTotalRevenue()+Math.round(fare*0.8));
        driver.setTotalRevenue(driversRevenue);

        System.out.println("drivers revenue --- "+driversRevenue);

        driverRepository.save(driver);
        rideRepository.save(ride);
    }

    @Override
    public void cancelRide(Integer rideId) throws RideException {
        Ride ride = findRideById(rideId);
        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);
    }

    @Override
    public Ride findRideById(Integer rideId) throws RideException {
        Optional<Ride> ride = rideRepository.findById(rideId);
        if (ride.isPresent()){
            return ride.get();
        }
        throw new RideException("ride not exist with id "+rideId);
    }
}
