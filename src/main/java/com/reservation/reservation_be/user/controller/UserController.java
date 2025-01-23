package com.reservation.reservation_be.user.controller;

import com.reservation.reservation_be.user.dto.ApiResponseDTO;
import com.reservation.reservation_be.user.dto.LoginRequestDTO;
import com.reservation.reservation_be.user.dto.UserRequestDTO;
import com.reservation.reservation_be.user.dto.UserResponseDTO;
import com.reservation.reservation_be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Create User (C)
     */
    @Operation(summary = "create user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사용자 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping
    public ApiResponseDTO<UserResponseDTO> createUser(@RequestBody UserRequestDTO request){
        try {
            UserResponseDTO user = userService.createUser(request);
            return ApiResponseDTO.success("User created successfully", user);
        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Read User (R)
     * - 단일 User 조회
     *   (식별자를 어떻게 관리하는지에 따라 @PathVariable 타입을 맞춰주세요)
     */
    @Operation(summary = "get user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 미존재"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/{userId}")
    public ApiResponseDTO<UserResponseDTO> getUser(@PathVariable String userId) {
        try {
            UserResponseDTO user = userService.getUserById(userId);
            return ApiResponseDTO.success("User retrieved successfully", user);
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 로그인 검증을 위한 API
     */
    @Operation(summary = "login user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (비밀번호 불일치 등)"),
            @ApiResponse(responseCode = "404", description = "사용자 미존재"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/login")
    public ApiResponseDTO<UserResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            // userService.authenticate(...) 내에서
            //  1) userId로 사용자 조회,
            //  2) password 비교,
            //  3) 일치하면 true, 아니면 false 반환
            UserResponseDTO userDto = userService.authenticate(request.getUserId(), request.getPassword());

            // 로그인 성공
            return ApiResponseDTO.success("Login successful", userDto);
        } catch (UsernameNotFoundException e) {
            // 사용자 없음
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (IllegalArgumentException e) {
            // 비밀번호 불일치 등
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    /**
     * (Optional) 모든 User 조회가 필요한 경우
     */
    @Operation(summary = "get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 사용자 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping
    public ApiResponseDTO<List<UserResponseDTO>> getAllUsers() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return ApiResponseDTO.success("All users retrieved successfully", users);
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Update User (U)
     */
    @Operation(summary = "update user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "사용자 미존재"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PutMapping("/{userId}")
    public ApiResponseDTO<UserResponseDTO> updateUser(@PathVariable String userId, @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(userId, request);
            return ApiResponseDTO.success("User updated successfully", updatedUser);
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Delete User (D)
     */
    @Operation(summary = "delete user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 미존재"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @DeleteMapping("/{userId}")
    public ApiResponseDTO<String> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return ApiResponseDTO.success("User deleted successfully", "Success");
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            return ApiResponseDTO.error("Server Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
