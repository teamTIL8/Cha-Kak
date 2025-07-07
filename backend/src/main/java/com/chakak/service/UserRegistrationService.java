package com.chakak.service;

import com.chakak.common.constants.AuthConstants;
import com.chakak.domain.User;
import com.chakak.dto.request.UserRegisterDto;
import com.chakak.exception.DuplicateEmailException;
import com.chakak.exception.DuplicateUserIdException;
import com.chakak.exception.WithdrawnEmailException;
import com.chakak.exception.DuplicateEntryException;
import com.chakak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public User register(UserRegisterDto registerDto) {
		try {
			if (userRepository.existsByUserIdAndIsDeletedFalse(registerDto.getUserId())) {
				throw new DuplicateUserIdException(AuthConstants.MSG_DUPLICATE_USER_ID);
			}

			if (!isEmailAvailableForRegistration(registerDto.getEmail())) {
				String waitTime = getReRegistrationWaitTime(registerDto.getEmail());
				if (waitTime != null) {
					throw new WithdrawnEmailException(
							String.format(AuthConstants.MSG_WITHDRAWN_EMAIL_RE_REGISTRATION_WAIT, waitTime));
				} else {
					throw new DuplicateEmailException(AuthConstants.MSG_DUPLICATE_EMAIL);
				}
			}

			User user = User.builder().userId(registerDto.getUserId()).email(registerDto.getEmail())
					.name(registerDto.getName()).password(passwordEncoder.encode(registerDto.getPassword()))
					.role(User.Role.USER).isDeleted(false).build();

			User savedUser = userRepository.save(user);
			entityManager.flush();

			return savedUser;

		} catch (DuplicateUserIdException | DuplicateEmailException | WithdrawnEmailException e) {
			throw e;
		} catch (ConstraintViolationException e) {
			handleConstraintViolation(registerDto, e.getMessage());
			throw new DuplicateEntryException(AuthConstants.MSG_DUPLICATE_ENTRY);
		} catch (DataIntegrityViolationException e) {
			Throwable rootCause = e.getRootCause();
			if (rootCause instanceof java.sql.SQLException) {
				java.sql.SQLException sqlException = (java.sql.SQLException) rootCause;
				handleConstraintViolation(registerDto, sqlException.getMessage());
			}
			throw new DuplicateEntryException(AuthConstants.MSG_DUPLICATE_ENTRY);
		} catch (Exception e) {
			throw new RuntimeException(AuthConstants.MSG_REGISTRATION_UNEXPECTED_ERROR, e);
		}
	}

	private void handleConstraintViolation(UserRegisterDto registerDto, String errorMessage) {
		try {
			boolean userIdExists = userRepository.existsByUserId(registerDto.getUserId());
			boolean emailExists = userRepository.existsByEmail(registerDto.getEmail());

			if (userIdExists) {
				throw new DuplicateUserIdException(AuthConstants.MSG_DUPLICATE_USER_ID);
			} else if (emailExists) {
				throw new DuplicateEmailException(AuthConstants.MSG_DUPLICATE_EMAIL);
			} else {
				throw new DuplicateEntryException(AuthConstants.MSG_DUPLICATE_ENTRY);
			}
		} catch (DuplicateUserIdException | DuplicateEmailException | DuplicateEntryException e) {
			throw e;
		} catch (Exception e) {
			throw new DuplicateEntryException(AuthConstants.MSG_CONSTRAINT_VIOLATION_CHECK_ERROR);
		}
	}

	@Transactional(readOnly = true)
	public boolean isUserIdAvailable(String userId) {
		try {
			return !userRepository.existsByUserIdAndIsDeletedFalse(userId);
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional(readOnly = true)
	public boolean isEmailAvailable(String email) {
		try {
			return isEmailAvailableForRegistration(email);
		} catch (Exception e) {
			return false;
		}
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