package com.sky.booking.repository;

import com.sky.booking.model.Driver;
import com.sky.booking.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByEmail(String email);

    @Query("SELECT R FROM Ride R WHERE R.status='REQUESTED' AND R.driver.id=:driverId")
    List<Ride> getAllocatedRides(@Param("driverId")Integer driverId);

    @Query("SELECT R FROM Ride R WHERE R.status='COMPLETED' AND R.driver.id=:driverId")
    List<Ride> getCompletedRides(@Param("driverId")Integer driverId);
}
