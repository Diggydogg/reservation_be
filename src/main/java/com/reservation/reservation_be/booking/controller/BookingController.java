package com.reservation.reservation_be.booking.controller;
import com.reservation.reservation_be.booking.dto.BookingRequestDTO;
import com.reservation.reservation_be.booking.dto.BookingResponseDTO;
import com.reservation.reservation_be.booking.dto.ApiResponseDTO;

import com.reservation.reservation_be.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "create booking")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사용자 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping
    public ApiResponseDTO<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO request){
        try{
            BookingResponseDTO booking = bookingService.createBooking(request);
            return ApiResponseDTO.success("user created successfully", booking);

        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occured.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @GetMapping("/get")
    public ResponseEntity<List<BookingResponseDTO>> getBooking(@RequestParam("user_id") String userId) {
        try {
            List<BookingResponseDTO> booking =  bookingService.getBookingByUserId(userId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getBookingId")
    public ResponseEntity<BookingResponseDTO> getBookingByBookingId(@RequestParam("booking_id") UUID bookingId) {
        try {
            BookingResponseDTO booking =  bookingService.getBookingBookingId(bookingId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<BookingResponseDTO>> getAllBooking() {
        try {
            List<BookingResponseDTO> booking =  bookingService.getBookingAll();
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PatchMapping("/updateBooking")
    public ApiResponseDTO<BookingResponseDTO> updateBooking(
            @RequestParam ("booking_id") UUID bookingId,
            @RequestBody BookingRequestDTO request
    ) {
        try {
            BookingResponseDTO booking = bookingService.updateBooking (bookingId, request);
            return ApiResponseDTO.success("Booking updated successfully", booking);
        } catch (Exception e) {
            return ApiResponseDTO.error("Error updating booking", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @DeleteMapping("/deleteBooking")
    public ApiResponseDTO<String> deleteBooking(@RequestParam UUID bookingId) {
        try {
            bookingService.deleteBooking(bookingId);
            return ApiResponseDTO.success("Booking deleted successfully", "Success");
        } catch (Exception e) {
            return ApiResponseDTO.error("Error deleting booking", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }



}
