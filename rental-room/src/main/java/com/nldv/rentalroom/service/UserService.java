package com.nldv.rentalroom.service;

import com.nldv.rentalroom.enums.UserRole;
import com.nldv.rentalroom.enums.UserStatus;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nldv.rentalroom.dto.UserUpdateRequest;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User register(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String phone,
            String address) {

        if (existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username đã tồn tại"
            );
        }

        if (existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email đã tồn tại"
            );
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);

        // Người dùng đăng ký từ website luôn là CUSTOMER.
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User updateUser(
            Integer id,
            UserUpdateRequest request) {

        User user = findById(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy người dùng"
            );
        }

        if (request.getEmail() != null
                && !request.getEmail().equals(user.getEmail())
                && existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Email đã tồn tại"
            );
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        if (request.getRole() != null) {
            try {
                user.setRole(
                        UserRole.valueOf(
                                request.getRole().toUpperCase()
                        )
                );
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Role không hợp lệ"
                );
            }
        }

        return userRepository.save(user);
    }

    public User changeStatus(
            Integer id,
            UserStatus status) {

        User user = findById(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy người dùng"
            );
        }

        user.setStatus(status);

        return userRepository.save(user);
    }
}
