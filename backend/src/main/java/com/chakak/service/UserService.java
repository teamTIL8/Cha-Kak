package com.chakak.service;

import com.chakak.domain.User;
import com.chakak.dto.request.UserRegisterDto;
import com.chakak.dto.request.UserUpdateDto;
import com.chakak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserIdAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User register(UserRegisterDto registerDto, PasswordEncoder passwordEncoder) {
        // 사용자 ID 중복 체크
        if (userRepository.existsByUserIdAndIsDeletedFalse(registerDto.getUserId())) {
            throw new RuntimeException("이미 사용 중인 사용자 ID입니다.");
        }

        // 이메일 사용 가능 여부 확인 (탈퇴 회원 30일 대기 포함)
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

    public User findByUserId(String userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public User updateUser(String userId, UserUpdateDto updateDto, PasswordEncoder passwordEncoder) {
        User user = findByUserId(userId);

        if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
            user.setName(updateDto.getName());
        }

        if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
            // 이메일 변경 시 중복 체크
            if (!user.getEmail().equals(updateDto.getEmail())
                    && userRepository.existsByEmailAndIsDeletedFalse(updateDto.getEmail())) {
                throw new RuntimeException("이미 사용 중인 이메일입니다.");
            }
            user.setEmail(updateDto.getEmail());
        }

        if (updateDto.getPassword() != null && !updateDto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        User user = findByUserId(userId);
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean checkPassword(String userId, String rawPassword, PasswordEncoder passwordEncoder) {
        User user = findByUserId(userId);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserIdAndIsDeletedFalse(userId);
    }

    public boolean isEmailAvailable(String email) {
        return isEmailAvailableForRegistration(email);
    }

    /**
     * 회원탈퇴 처리 (소프트 삭제)
     */
    public void withdrawUser(String userId) {
        User user = findByUserId(userId);

        if (user.getIsDeleted()) {
            throw new RuntimeException("이미 탈퇴한 회원입니다.");
        }

        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    /**
     * 이메일 사용 가능 여부 확인 (탈퇴 회원 고려)
     */
    public boolean isEmailAvailableForRegistration(String email) {
        // 활성 사용자 중에 같은 이메일이 있는지 확인
        if (userRepository.existsByEmailAndIsDeletedFalse(email)) {
            return false;
        }

        // 탈퇴한 사용자 중에 같은 이메일이 있는지 확인
        Optional<User> deletedUser = userRepository.findByEmailAndIsDeletedTrue(email);

        if (deletedUser.isPresent()) {
            LocalDateTime deletedAt = deletedUser.get().getDeletedAt();
            LocalDateTime now = LocalDateTime.now();

            // 30일이 지났는지 확인 (테스트용은 1분)
            if (deletedAt.plusDays(30).isBefore(now)) {
                // 30일이 지났으면 재가입 가능
                return true;
            } else {
                // 30일이 안 지났으면 재가입 불가
                return false;
            }
        }

        // 해당 이메일로 가입한 적이 없으면 사용 가능
        return true;
    }

    /**
     * 탈퇴한 회원의 재가입 대기 시간 계산
     */
    public String getReRegistrationWaitTime(String email) {
        Optional<User> deletedUser = userRepository.findByEmailAndIsDeletedTrue(email);

        if (deletedUser.isEmpty()) {
            return null;
        }

        LocalDateTime deletedAt = deletedUser.get().getDeletedAt();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime canReRegisterAt = deletedAt.plusDays(30);

        if (canReRegisterAt.isBefore(now)) {
            return null; // 이미 재가입 가능
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
