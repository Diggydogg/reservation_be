package com.reservation.reservation_be.booking.service;

import com.reservation.reservation_be.booking.dto.BookingRequestDTO;
import com.reservation.reservation_be.booking.dto.BookingResponseDTO;
import com.reservation.reservation_be.booking.entity.Booking;
import com.reservation.reservation_be.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;

    /**
     * 예약 생성
     *
     * @param request 예약 생성 요청 DTO
     * @return 생성된 예약에 대한 ResponseDTO
     */
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        // 1. DTO -> Entity 변환
        Booking booking = Booking.builder()
                .bookingTime(request.getBookingTime())
                .bookingInfo(request.getBookingInfo())
                .date(request.getDate())
                .name(request.getName())
                .userId(request.getUserId())  // userId가 UUID 타입이라고 가정
                .noPeople(request.getNoPeople())
                .build();

        // 2. DB에 저장
        Booking savedBooking = bookingRepository.save(booking);

        // 3. Entity -> DTO 변환 후 반환
        return BookingResponseDTO.from(savedBooking);
    }

    /**
     * userId(문자열)로 예약 단건 조회
     * (DB에 userId를 UUID로 저장하는 경우, 파싱 후 사용)
     *
     * @param userId 문자열 형태의 유저 아이디
     * @return 해당 userId로 조회된 예약정보의 ResponseDTO
     */
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingByUserId(String userId) {
//        // userId가 UUID라면 아래처럼 변환
//        UUID userUUID;
//        try {
//            userUUID = UUID.fromString(userId);
//        } catch (IllegalArgumentException e) {
//            throw new UsernameNotFoundException("Invalid userId format. userId must be UUID string.");
//        }

        // 예시) 단일 Booking만 존재한다고 가정할 때(또는 첫 번째 Booking만 가져옴)
        // 만약 여러 건이라면 findAllByUserId(...) 등으로 받아 List로 처리 가능
        return bookingRepository.findBookingsByUserId(userId).stream()
                .map(BookingResponseDTO::from)
                .collect(Collectors.toList());

    }


    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingBookingId(UUID bookingId) {

        Booking existBooking = bookingRepository.findBookingsByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));



        // 3. 저장
        Booking updatedBooking = bookingRepository.save(existBooking);

        // 4. Entity -> DTO 변환 후 반환
        return BookingResponseDTO.from(updatedBooking);
    }




    // 모든 유저 가져오기
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingAll() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingResponseDTO::from)
                .collect(Collectors.toList());
    }



    /**
     * 예약 정보 업데이트
     *
     * @param bookingId PathVariable 로 넘어온 예약 식별자 (UUID)
     * @param request   업데이트할 BookingRequestDTO
     * @return 업데이트된 예약 정보의 ResponseDTO
     */
    public BookingResponseDTO updateBooking(UUID bookingId, BookingRequestDTO request) {
        // 1. 기존 Booking 조회
        Booking existingBooking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // 2. 필드 업데이트
        existingBooking.setBookingTime(request.getBookingTime());
        existingBooking.setBookingInfo(request.getBookingInfo());
        existingBooking.setDate(request.getDate());
        existingBooking.setName(request.getName());
        existingBooking.setUserId(request.getUserId());
        existingBooking.setNoPeople(request.getNoPeople());

        // 3. 저장
        Booking updatedBooking = bookingRepository.save(existingBooking);

        // 4. Entity -> DTO 변환 후 반환
        return BookingResponseDTO.from(updatedBooking);
    }

    /**
     * 예약 삭제
     *
     * @param bookingId 예약 식별자 (UUID)
     */
    public void deleteBooking(UUID bookingId) {
        // 1. 예약 존재 여부 확인
        Booking existingBooking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // 2. 삭제
        bookingRepository.delete(existingBooking);
    }
}
