package com.reservation.reservation_be.booking.repository;

import com.reservation.reservation_be.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.awt.print.Book;
import java.util.List;
import java.util.UUID;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, UUID>{
    List<Booking> findAll();

    Optional<Booking> findBookingsByBookingId(UUID bookingId);
    List<Booking> findBookingsByUserId(String userId);
    Optional<Booking> findByBookingId(UUID bookingId);

}
