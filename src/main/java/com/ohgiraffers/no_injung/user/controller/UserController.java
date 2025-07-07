package com.ohgiraffers.no_injung.user.controller;

import com.ohgiraffers.no_injung.auth.dto.UserInfoResponse;
import com.ohgiraffers.no_injung.user.dto.UserRequestDTO;
import com.ohgiraffers.no_injung.user.dto.UserResponseDTO;
import com.ohgiraffers.no_injung.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "사용자 관리", description = "사용자 정보 조회, 수정, 삭제 등의 사용자 관리 API")
public class UserController {

    private final UserService userService;

    /**
     * 기존 메서드 - 내 정보 조회 (일반 사용자 가능)
     */
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails));
    }

    /**
     * 단일 사용자 조회 (ID로 조회) - 인증 제거 (개발용)
     */
    @Operation(summary = "사용자 조회", description = "사용자 ID로 특정 사용자의 정보를 조회합니다. (관리자 권한 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "조회할 사용자의 ID", example = "1")
            @PathVariable Long userId) {
        try {
            UserResponseDTO user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 전체 사용자 조회 (논리적 삭제 제외) - 인증 제거 (개발용)
     */
    @Operation(summary = "전체 사용자 조회", description = "모든 사용자 목록을 조회합니다. 논리적으로 삭제된 사용자는 제외됩니다. (관리자 권한 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 정보 수정 - 인증 제거 (개발용)
     */
    @Operation(summary = "사용자 정보 수정", description = "특정 사용자의 정보를 수정합니다. (관리자 권한 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "수정할 사용자의 ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "수정할 사용자 정보")
            @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(userId, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 내 정보 수정 (현재 로그인한 사용자) - 일반 사용자 가능
     */
    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "수정할 내 정보")
            @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateMyInfo(userDetails, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 사용자 논리적 삭제 - 인증 제거 (개발용)
     */
    @Operation(summary = "사용자 삭제", description = "특정 사용자를 논리적으로 삭제합니다. (관리자 권한 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "삭제할 사용자의 ID", example = "1")
            @PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 내 계정 삭제 (논리적 삭제) - 일반 사용자 가능
     */
    @Operation(summary = "내 계정 삭제", description = "현재 로그인한 사용자의 계정을 논리적으로 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "내 계정 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "계정 삭제 처리 중 오류"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            userService.deleteMyAccount(userDetails);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 사용자 존재 여부 확인 - 인증 제거 (개발용)
     */
    @Operation(summary = "사용자 존재 여부 확인", description = "특정 사용자가 존재하는지 확인합니다. (관리자 권한 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 존재 여부 확인 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @GetMapping("/{userId}/exists")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> checkUserExists(
            @Parameter(description = "존재 여부를 확인할 사용자의 ID", example = "1")
            @PathVariable Long userId) {
        boolean exists = userService.existsById(userId);
        return ResponseEntity.ok(exists);
    }
}
