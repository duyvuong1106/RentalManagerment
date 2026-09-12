package com.nldv.rentalroom.service;

import com.nldv.rentalroom.dto.LoginRequest;
import com.nldv.rentalroom.dto.LoginResponse;
import com.nldv.rentalroom.dto.RegisterRequest;
import com.nldv.rentalroom.dto.UserResponse;
import com.nldv.rentalroom.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthService(
            AuthenticationManager authenticationManager,
            UserService userService) {

        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public UserResponse register(RegisterRequest request) {

        User user = userService.register(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone(),
                request.getAddress()
        );

        return UserResponse.fromUser(user);
    }

    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        User user = userService.findByUsername(
                request.getUsername()
        );

        return new LoginResponse(
                "Đăng nhập thành công",
                UserResponse.fromUser(user)
        );
    }
}