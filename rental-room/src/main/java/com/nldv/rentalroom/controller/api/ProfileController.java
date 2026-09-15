package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.ProfileUpdateRequest;
import com.nldv.rentalroom.dto.UserResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.ProfileService;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService service;
    public ProfileController(ProfileService service){this.service=service;}
    @PutMapping
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal CustomUserDetails u, @Valid @RequestBody ProfileUpdateRequest r){return ResponseEntity.ok(service.updateProfile(u.getUser().getId(),r));}
    @PostMapping("/avatar")
    public ResponseEntity<UserResponse> avatar(@AuthenticationPrincipal CustomUserDetails u, @RequestParam("file") MultipartFile file) throws IOException {return ResponseEntity.ok(service.uploadAvatar(u.getUser().getId(),file));}
}
