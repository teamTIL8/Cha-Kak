package com.chakak.service;

import com.chakak.dto.request.UserLoginDto;
import com.chakak.common.constants.AuthConstants;
import com.chakak.domain.User;
import com.chakak.repository.UserRepository;
import com.chakak.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserIdAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        log.debug("AuthService: User '{}' loaded. Returning CustomUserDetails instance.", username);
        return customUserDetails;
    }

    @Transactional
    public String login(UserLoginDto loginDto) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(loginDto.getUserId())
                .orElseThrow(() -> new BadCredentialsException(AuthConstants.MSG_INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(AuthConstants.MSG_INVALID_CREDENTIALS);
        }

        return jwtUtil.generateToken(user);
    }
}
