package com.chakak.service;

import com.chakak.domain.User;
import com.chakak.dto.request.UserUpdateDto;
import com.chakak.repository.UserRepository;
import com.chakak.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findByUserId(String userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public User updateUser(String userId, UserUpdateDto updateDto, boolean changePassword) {
        User user = findByUserId(userId);

        BindingResult bindingResult = new BeanPropertyBindingResult(updateDto, "updateDto");

        ValidationUtils.validateBasicInfo(updateDto, bindingResult);
        ValidationUtils.validateCurrentPassword(updateDto, user, this, passwordEncoder, bindingResult);

        if (changePassword) {
            ValidationUtils.validateNewPassword(updateDto, bindingResult);
        } else {
            updateDto.setPassword(null);
        }

        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
            user.setName(updateDto.getName());
        }

        if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
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

    public void withdrawUser(String userId) {
        User user = findByUserId(userId);

        if (user.getIsDeleted()) {
            throw new RuntimeException("이미 탈퇴한 회원입니다.");
        }

        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(String userId, String rawPassword) {
        User user = findByUserId(userId);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
