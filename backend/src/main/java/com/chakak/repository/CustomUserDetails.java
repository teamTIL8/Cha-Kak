package com.chakak.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chakak.domain.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails{
	@Autowired
	private User user;
	
	//로그인 된 사용자 id 꺼내기 
	public String getUserId() {
		return user.getUserId();
	}
	
	public String getUsername() {
		return user.getUserId(); 
	}
	
	public String getPassword() {
		return user.getPassword();
	}
	
	//사용자 권한 리턴
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));
	}
	
	// 계정 만료 여부 리턴
	public boolean isAccountNonExpired() {
		return true; //만료 x
	}
	
	// 계정의 잠김 여부 리턴
	public boolean isAccountNonLocked() {
		return true; // 잠기지 x
	}
	

}
