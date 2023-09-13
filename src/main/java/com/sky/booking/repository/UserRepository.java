package com.sky.booking.repository;

import com.sky.booking.model.Ride;
import com.sky.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("SELECT r FROM Ride r WHERE r.status='COMPLETED' AND r.user.id=:userId")
    List<Ride> getCompletedRides(@Param("userId") Integer userId);
}
