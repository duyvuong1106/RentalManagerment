package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.ChangePasswordRequest;
import com.nldv.rentalroom.dto.LoginRequest;
import com.nldv.rentalroom.dto.LoginResponse;
import com.nldv.rentalroom.dto.RegisterRequest;
import com.nldv.rentalroom.dto.UserResponse;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.AuthService;
import com.nldv.rentalroom.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthService authService,
            UserService userService,
            PasswordEncoder passwordEncoder) {

        this.authService = authService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        return ResponseEntity.ok(
                authService.login(
                        request,
                        httpRequest,
                        httpResponse
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(
                "Đăng xuất thành công"
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            Authentication authentication) {

        User user = userService.findByUsername(
                authentication.getName()
        );

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        User user = userService.findByUsername(
                authentication.getName()
        );

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            return ResponseEntity.badRequest()
                    .body("Mật khẩu hiện tại không chính xác");
        }

        userService.changePassword(
                user,
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                "Đổi mật khẩu thành công"
        );
    }
}