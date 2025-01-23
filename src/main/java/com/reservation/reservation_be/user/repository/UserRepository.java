package com.reservation.reservation_be.user.repository;

import com.reservation.reservation_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,UUID>{
    Optional<User> findByUserName(String userName);
    Optional<User> findByUserId(String userId);
    Optional<User> findUserByUserId(String userId);
    List<User> findAll();
}
