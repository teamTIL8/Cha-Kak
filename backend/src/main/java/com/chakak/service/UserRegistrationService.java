package com.chakak.service;

import com.chakak.domain.User;
import com.chakak.dto.request.UserRegisterDto;
import com.chakak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegisterDto registerDto) {
        if (userRepository.existsByUserIdAndIsDeletedFalse(registerDto.getUserId())) {
            throw new RuntimeException("이미 사용 중인 사용자 ID입니다.");
        }

        if (!isEmailAvailableForRegistration(registerDto.getEmail())) {
            String waitTime = getReRegistrationWaitTime(registerDto.getEmail());
            if (waitTime != null) {
                throw new RuntimeException("탈퇴한 회원의 이메일입니다. " + waitTime + " 후에 재가입이 가능합니다.");
            } else {
                throw new RuntimeException("이미 사용 중인 이메일입니다.");
            }
        }

        User user = User.builder()
                .userId(registerDto.getUserId())
                .email(registerDto.getEmail())
                .name(registerDto.getName())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(User.Role.USER)
                .isDeleted(false)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserIdAndIsDeletedFalse(userId);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return isEmailAvailableForRegistration(email);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailableForRegistration(String email) {
        if (userRepository.existsByEmailAndIsDeletedFalse(email)) {
            return false;
        }

        Optional<User> deletedUser = userRepository.findByEmailAndIsDeletedTrue(email);

        if (deletedUser.isPresent()) {
            LocalDateTime deletedAt = deletedUser.get().getDeletedAt();
            LocalDateTime now = LocalDateTime.now();

            return !deletedAt.plusDays(30).isAfter(now);
        }

        return true;
    }

    @Transactional(readOnly = true)
    public String getReRegistrationWaitTime(String email) {
        Optional<User> deletedUser = userRepository.findByEmailAndIsDeletedTrue(email);

        if (deletedUser.isEmpty()) {
            return null;
        }

        LocalDateTime deletedAt = deletedUser.get().getDeletedAt();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime canReRegisterAt = deletedAt.plusDays(30);

        if (canReRegisterAt.isBefore(now)) {
            return null;
        }

        Duration duration = Duration.between(now, canReRegisterAt);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초";
        } else {
            long minutes = seconds / 60;
            return minutes + "분";
        }
    }
}
