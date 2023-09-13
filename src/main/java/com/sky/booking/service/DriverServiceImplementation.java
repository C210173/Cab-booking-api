package com.sky.booking.service;

import com.sky.booking.config.JwtUtil;
import com.sky.booking.domain.RideStatus;
import com.sky.booking.domain.UserRole;
import com.sky.booking.exception.DriverException;
import com.sky.booking.model.Driver;
import com.sky.booking.model.License;
import com.sky.booking.model.Ride;
import com.sky.booking.model.Vehicle;
import com.sky.booking.repository.DriverRepository;
import com.sky.booking.repository.LicenseRepository;
import com.sky.booking.repository.VehicleRepository;
import com.sky.booking.request.DriverSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverServiceImplementation implements DriverService{
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VehicleRepository vehicleRepository;
    private final LicenseRepository licenseRepository;
    private final Calculators calculators;

    @Override
    public Driver registerDriver(DriverSignupRequest driverSignupRequest) {
        License license = driverSignupRequest.getLicense();
        Vehicle vehicle = driverSignupRequest.getVehicle();

        License createLicense = new License();
        createLicense.setLicenseState(license.getLicenseState());
        createLicense.setLicenseNumber(license.getLicenseNumber());
        createLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
        createLicense.setId(license.getId());
        License savedLicense = licenseRepository.save(createLicense);

        Vehicle createVehicle = new Vehicle();
        createVehicle.setCapacity(vehicle.getCapacity());
        createVehicle.setColor(vehicle.getColor());
        createVehicle.setId(vehicle.getId());
        createVehicle.setLicensePlate(vehicle.getLicensePlate());
        createVehicle.setMake(vehicle.getMake());
        createVehicle.setModel(vehicle.getModel());
        createVehicle.setYear(vehicle.getYear());
        Vehicle savedVehicle = vehicleRepository.save(createVehicle);

        Driver driver = new Driver();
        driver.setEmail(driverSignupRequest.getEmail());
        driver.setName(driverSignupRequest.getName());
        driver.setMobile(driverSignupRequest.getMobile());
        driver.setPassword(passwordEncoder.encode(driverSignupRequest.getPassword()));
        driver.setLicense(savedLicense);
        driver.setVehicle(savedVehicle);
        driver.setRole(UserRole.DRIVER);
        driver.setLatitude(driverSignupRequest.getLatitude());
        driver.setLongitude(driverSignupRequest.getLongitude());

        Driver createDriver = driverRepository.save(driver);

        savedLicense.setDriver(createDriver);
        savedVehicle.setDriver(createDriver);

        licenseRepository.save(savedLicense);
        vehicleRepository.save(savedVehicle);

        return createDriver;
    }

    @Override
    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, Ride ride) {
        List<Driver> allDrivers = driverRepository.findAll();
        List<Driver> availableDriver = new ArrayList<>();

        for (Driver driver:allDrivers){
            if (driver.getCurrentRide() != null && driver.getCurrentRide().getStatus() != RideStatus.COMPLETED){
                continue;
            }
            if (ride.getDeclinedDrivers().contains(driver.getId())){
                System.out.println("Its container");
                continue;
            }
//            double driverLatitude = driver.getLatitude();
//            double driverLongitude = driver.getLongitude();

//            double distance= calculators.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);
//            if (distance <= radius) {
                availableDriver.add(driver);
//            }
        }
        return availableDriver;
    }

    @Override
    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {
        double min = Double.MAX_VALUE;
        Driver nearestDriver = null;

        for (Driver driver:availableDrivers){
            double driverLatitude = driver.getLatitude();
            double driverLongitude = driver.getLongitude();

            double distance= calculators.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);
            if (min>distance){
                min = distance;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    @Override
    public Driver getReqDriverProfile(String jwt) throws DriverException {
        String email = jwtUtil.getEmailFromToken(jwt);
        Driver driver = driverRepository.findByEmail(email);
        if (driver==null){
            throw new DriverException("Driver not exist with email " + email);
        }
        return driver;
    }

    @Override
    public Ride getDriversCurrentRide(Integer driverId) throws DriverException {
        Driver driver = findDriverById(driverId);
        return driver.getCurrentRide();
    }

    @Override
    public List<Ride> getAllocatedRides(Integer driverId) throws DriverException {
        List<Ride> allocatedRides = driverRepository.getAllocatedRides(driverId);
        if (allocatedRides!=null){
            return allocatedRides;
        }
        throw new DriverException("allocatedRides is null");
    }

    @Override
    public Driver findDriverById(Integer driverId) throws DriverException {
        Optional<Driver> opt = driverRepository.findById(driverId);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new DriverException("driver not exist with id " + driverId);
    }

    @Override
    public List<Ride> completedRides(Integer driverId) throws DriverException {
        List<Ride> completedRides = driverRepository.getCompletedRides(driverId);
        if (completedRides!=null){
            return completedRides;
        }
        throw new DriverException("completedRides is null");
    }
}
