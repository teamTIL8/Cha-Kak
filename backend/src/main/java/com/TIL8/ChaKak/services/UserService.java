package com.TIL8.ChaKak.services;

import com.TIL8.ChaKak.domain.User;
import com.TIL8.ChaKak.dto.request.SignUpRequestDto;
import com.TIL8.ChaKak.dto.request.UpdateUserRequestDto;
import com.TIL8.ChaKak.repository.UserRepository;
import com.TIL8.ChaKak.common.constants.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * UserDetailsService의 loadUserByUsername 메서드를 구현. Spring Security에서 사용자 인증 시
	 * 호출된다.
	 * 
	 * @param userId 사용자 ID
	 * @return Spring Security UserDetails 객체
	 * @throws UsernameNotFoundException ID에 해당하는 사용자를 찾을 수 없을 때 발생
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

		return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(),
				Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(
						AuthConstants.ROLE_PREFIX + user.getRole().name()))
		);
	}

	/**
	 * 새로운 사용자 회원가입 처리
	 * 
	 * @param signUpRequestDto 회원가입 요청 DTO
	 * @return 저장된 사용자 엔티티
	 * @throws IllegalArgumentException 이메일 또는 ID가 이미 존재할 경우 발생
	 */
	@Transactional
	public User signUp(SignUpRequestDto signUpRequestDto) {
		Optional<User> deletedUserById = userRepository.findDeletedByUserId(signUpRequestDto.getUserId());
		if (deletedUserById.isPresent()) {
			User user = deletedUserById.get();
			if (user.getDeletedAt() != null && user.getDeletedAt().plusDays(30).isAfter(LocalDateTime.now())) {
				throw new IllegalArgumentException("탈퇴 후 30일간 동일한 아이디나 이메일로는 회원가입이 불가능합니다.");
			} else {
				user.reactivateUser();
				user.updateProfile(signUpRequestDto.getName(), passwordEncoder.encode(signUpRequestDto.getPassword()));
				user.setEmail(signUpRequestDto.getEmail());
				return userRepository.save(user);
			}
		}

		Optional<User> deletedUserByEmail = userRepository.findDeletedByEmail(signUpRequestDto.getEmail());
		if (deletedUserByEmail.isPresent()) {
			User user = deletedUserByEmail.get();
			if (user.getDeletedAt() != null && user.getDeletedAt().plusDays(30).isAfter(LocalDateTime.now())) {
				throw new IllegalArgumentException("탈퇴 후 30일간 동일한 아이디나 이메일로는 회원가입이 불가능합니다.");
			} else {
				user.reactivateUser();
				user.updateProfile(signUpRequestDto.getName(), passwordEncoder.encode(signUpRequestDto.getPassword()));
				user.setUserId(signUpRequestDto.getUserId());
				return userRepository.save(user);
			}
		}

		if (userRepository.existsByUserId(signUpRequestDto.getUserId())) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}
		if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

		User newUser = new User(signUpRequestDto.getUserId(), encodedPassword, signUpRequestDto.getEmail(),
				signUpRequestDto.getName(), User.Role.USER
		);
		return userRepository.save(newUser);
	}

	/**
	 * 이메일로 사용자 정보를 조회합니다.
	 * 
	 * @param email 사용자 이메일
	 * @return Optional<User>
	 */
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * ID로 사용자 정보를 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @return Optional<User>
	 */
	public Optional<User> findUserByUserId(String userId) {
		return userRepository.findByUserId(userId);
	}

	/**
	 * 아이디 중복 확인
	 * @param userId 확인할 아이디
	 * @return true: 사용 가능, false: 중복
	 */
	public boolean isUserIdAvailable(String userId) {
		if (userRepository.existsByUserId(userId)) {
			return false;
		}
		Optional<User> deletedUser = userRepository.findDeletedByUserId(userId);
		if (deletedUser.isPresent()) {
			User user = deletedUser.get();
			return !(user.getDeletedAt() != null && user.getDeletedAt().plusDays(30).isAfter(LocalDateTime.now()));
		}
		return true;
	}

	/**
	 * 사용자 정보 수정 처리
	 * 
	 * @param userId               수정할 사용자의 ID (인증된 사용자)
	 * @param updateUserRequestDto 수정 요청 DTO (이름, 비밀번호)
	 * @return 업데이트된 사용자 엔티티
	 * @throws IllegalArgumentException 사용자 정보를 찾을 수 없거나 비밀번호가 유효하지 않을 경우
	 */
	@Transactional
	public User updateUser(String userId, UpdateUserRequestDto updateUserRequestDto) {
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		String newName = updateUserRequestDto.getName();
		String newPassword = null;

		if (updateUserRequestDto.getPassword() != null && !updateUserRequestDto.getPassword().trim().isEmpty()) {
			newPassword = passwordEncoder.encode(updateUserRequestDto.getPassword());
		}

		user.updateProfile(newName, newPassword);

		return user;
	}

	/**
	 * 사용자 회원 탈퇴 처리 (소프트 삭제)
	 * @param userId 탈퇴할 사용자의 ID
	 * @throws IllegalArgumentException 사용자를 찾을 수 없을 경우
	 */
	@Transactional
	public void withdrawUser(String userId) {
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException("탈퇴할 사용자를 찾을 수 없습니다."));
		user.deactivateUser();
	}
}
