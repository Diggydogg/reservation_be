package com.reservation.reservation_be.user.service;

import com.reservation.reservation_be.user.dto.UserRequestDTO;
import com.reservation.reservation_be.user.dto.UserResponseDTO;
import com.reservation.reservation_be.user.entity.User;
import com.reservation.reservation_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * C: Create User
     */
    public UserResponseDTO createUser(UserRequestDTO request){
        // 이미 동일 userName이 존재하는지 확인
        Optional<User> userExist  = userRepository.findByUserName(request.getUserName());
        if(userExist.isPresent()) {
            throw new InputMismatchException("User already exists with userName: " + request.getUserName());
        }

        // DTO -> 엔티티 변환
        User user = User.builder()
                .userId(request.getUserId())      // 예: String userId
                .password(request.getPassword())
                .userName(request.getUserName())
                .build();

        // DB 저장
        User savedUser = this.userRepository.save(user);

        // Entity -> DTO 변환
        return UserResponseDTO.from(savedUser);
    }

    /**
     * 로그인 검증
     * @param userId 사용자 ID
     * @param rawPassword 사용자가 입력한 평문 비밀번호
     * @return UserResponseDTO (로그인 성공 시 사용자 정보)
     * @throws UsernameNotFoundException 유저가 없을 때
     * @throws IllegalArgumentException 비밀번호 불일치 시
     */
    @Transactional(readOnly = true)
    public UserResponseDTO authenticate(String userId, String rawPassword) {
        // 1) 유저 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        // 2) 비밀번호 검증 (평문 비교 - 매우 위험)
        //    일반적으로 user.getPassword()는 해시가 저장됨.
        //    만약 해시가 아닌 평문 저장이라면, 아래처럼 비교 가능(권장 X).
        if (!user.getPassword().equals(rawPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }

        // 3) 성공 시 UserResponseDTO 반환
        return UserResponseDTO.from(user);
    }


    /**
     * R: Read User (단일 조회)
     *  - 여기서는 userId(문자열)를 기준으로 조회한다고 가정
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String userId) {
        // userId 로 유저 찾기
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new InputMismatchException("User not found with userId: " + userId));

        return UserResponseDTO.from(user);
    }

    /**
     * R: (Optional) 모든 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        // 모든 사용자 조회
        List<User> userList = userRepository.findAll();

        // Entity -> DTO 변환
        return userList.stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * U: Update User
     *  - userId로 기존 유저 찾아서, RequestDTO 정보로 업데이트
     */
    public UserResponseDTO updateUser(String userId, UserRequestDTO request) {
        // 기존 User 조회
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new InputMismatchException("User not found with userId: " + userId));

        // 필요에 따라 중복 userName 체크 로직이 추가로 필요할 수도 있음 (createUser와 동일하게)
        // 예:
        // if (!existingUser.getUserName().equals(request.getUserName())) {
        //     Optional<User> userExist = userRepository.findByUserName(request.getUserName());
        //     if(userExist.isPresent()) {
        //         throw new InputMismatchException("User already exists with userName: " + request.getUserName());
        //     }
        // }

        // 필드 업데이트
        existingUser.setUserName(request.getUserName());
        existingUser.setPassword(request.getPassword());
        // userId도 바꿀지 여부는 비즈니스 로직에 따라 결정 (보통 Primary Key는 바꾸지 않음)

        // DB 저장
        User updatedUser = userRepository.save(existingUser);

        return UserResponseDTO.from(updatedUser);
    }

    /**
     * D: Delete User
     *  - userId로 기존 유저 찾아서 삭제
     */
    public void deleteUser(String userId) {
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new InputMismatchException("User not found with userId: " + userId));

        userRepository.delete(existingUser);
    }
}
