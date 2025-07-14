package com.whs.dev2.controller;

import com.whs.dev2.dto.LoginRequestDto;
import com.whs.dev2.dto.RegisterRequestDto;
import com.whs.dev2.entity.User;
import com.whs.dev2.jwt.JwtUtil;
import com.whs.dev2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    //회원가입 API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto dto) {
        try {
            userService.register(dto);
            User user = userService.authenticate(dto.getUsername(), dto.getPassword());
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.ok()
            //.body("회원가입 성공"); 추후 수정 필요
            .body(Map.of(
                "message", "회원가입이 완료되었습니다.",
                "token", token,
                "username", user.getUsername()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        try {
            User user = userService.authenticate(dto.getUsername(), dto.getPassword());
            if (user != null) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(Map.of(
                        "message", "로그인 성공",
                        "username", user.getUsername()
                    ));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 중 오류가 발생했습니다.");
        }
    }

    //로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }

    //현재 로그인한 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.validateAndExtractUsername(token);
        
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        try {
            User user = userService.findByUsername(username);
            Map<String, String> response = Collections.singletonMap("username", user.getUsername());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
    
    // 포인트 조회 API
    @GetMapping("/point")
    public ResponseEntity<?> getPoint(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.validateAndExtractUsername(token);
        
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        try {
            Integer point = userService.getPointByUsername(username);
            String rank = userService.getRankByUsername(username);
            return ResponseEntity.ok(Map.of(
                "point", point,
                "rank", rank
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
    
    
    // VIP 쿠폰 플래그 API
    @GetMapping("/vip-coupon")
    public ResponseEntity<?> getVipCouponFlag(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.validateAndExtractUsername(token);
        
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        try {
            User user = userService.findByUsername(username);
            String rank = userService.getRankByUsername(username);
            
            // VIP 등급이 아니면 접근 거부
            if (!"vip".equals(rank)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("VIP 등급만 접근 가능합니다.");
            }
            
            // VIP 등급이면 플래그 제공
            return ResponseEntity.ok(Map.of(
                "flag", "VUL{W3Lc0me_to_V1p!1!}",
                "message", "VIP 쿠폰이 발급되었습니다!"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
}
