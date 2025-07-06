package com.chakak.service;

import com.chakak.dto.request.UserLoginDto;
import com.chakak.domain.User;
import com.chakak.repository.UserRepository;
import com.chakak.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserIdAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public String login(UserLoginDto loginDto) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(loginDto.getUserId())
                .orElseThrow(() -> new BadCredentialsException("사용자 ID 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 ID 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtUtil.generateToken(user);
    }
}
