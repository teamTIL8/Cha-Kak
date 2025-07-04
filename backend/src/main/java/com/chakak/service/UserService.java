<<<<<<< HEAD
package com.chakak.service;

import com.chakak.domain.User;
import com.chakak.dto.request.SignUpRequestDto;
import com.chakak.dto.request.UpdateUserRequestDto;
import com.chakak.repository.UserRepository;
import com.chakak.common.constants.AuthConstants;
import com.chakak.common.constants.Role;
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
				signUpRequestDto.getName(), Role.USER
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
=======
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

		// 이메일 사용 가능 여부 확인
		if (!isEmailAvailableForRegistration(registerDto.getEmail())) {
			String waitTime = getReRegistrationWaitTime(registerDto.getEmail());
			if (waitTime != null) {
				throw new RuntimeException("탈퇴한 회원의 이메일입니다. " + waitTime + " 후에 재가입이 가능합니다.");
			} else {
				throw new RuntimeException("이미 사용 중인 이메일입니다.");
			}
		}

		User user = User.builder().userId(registerDto.getUserId()).email(registerDto.getEmail())
				.name(registerDto.getName()).password(passwordEncoder.encode(registerDto.getPassword()))
				.role(User.Role.USER).isDeleted(false).build();

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

			// 1분(테스트용) 또는 30일이 지났는지 확인
			// 실제 운영시에는 30일로 변경: deletedAt.plusDays(30).isBefore(now)
			if (deletedAt.plusMinutes(1).isBefore(now)) {
				// 1분이 지났으면 재가입 가능
				return true;
			} else {
				// 1분이 안 지났으면 재가입 불가
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
		LocalDateTime canReRegisterAt = deletedAt.plusMinutes(1); // 실제로는 plusDays(30)

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

		// 실제 30일 버전
		// long days = duration.toDays();
		// long hours = duration.toHours() % 24;
		// return days + "일 " + hours + "시간";
	}
}
>>>>>>> main
