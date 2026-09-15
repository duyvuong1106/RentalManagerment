package com.nldv.rentalroom.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nldv.rentalroom.dto.ProfileUpdateRequest;
import com.nldv.rentalroom.dto.UserResponse;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.UserRepository;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private static final long MAX_AVATAR_SIZE = 5 * 1024 * 1024;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    public ProfileService(UserRepository userRepository, Cloudinary cloudinary){this.userRepository=userRepository;this.cloudinary=cloudinary;}

    @Transactional
    public UserResponse updateProfile(Integer userId, ProfileUpdateRequest request) {
        User user = find(userId);
        if (request.getEmail()!=null && !request.getEmail().equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        user.setFirstName(request.getFirstName()); user.setLastName(request.getLastName());
        if (request.getEmail()!=null) user.setEmail(request.getEmail());
        user.setPhone(request.getPhone()); user.setAddress(request.getAddress());
        return UserResponse.fromUser(userRepository.save(user));
    }

    @Transactional
    public UserResponse uploadAvatar(Integer userId, MultipartFile file) throws IOException {
        User user = find(userId);
        if (file==null || file.isEmpty()) throw new IllegalArgumentException("Ảnh đại diện không được để trống");
        if (file.getSize()>MAX_AVATAR_SIZE) throw new IllegalArgumentException("Ảnh đại diện không vượt quá 5MB");
        String type=file.getContentType();
        if (type==null || !type.startsWith("image/")) throw new IllegalArgumentException("File phải là hình ảnh");
        Map<?,?> result=cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder","rental-room/avatars"));
        String url=(String)result.get("secure_url"); String publicId=(String)result.get("public_id");
        String oldPublicId=user.getAvatarPublicId();
        user.setAvatarUrl(url); user.setAvatarPublicId(publicId); User saved=userRepository.save(user);
        if(oldPublicId!=null && !oldPublicId.isBlank()) cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());
        return UserResponse.fromUser(saved);
    }

    private User find(Integer id){return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));}
}
