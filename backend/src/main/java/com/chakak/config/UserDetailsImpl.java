package com.chakak.config;
// ReportSpecification과 비슷 ( 좀 더 추가된 느낌 추후 상황 보고 삭제 여부 결정 ) 
/*import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.streetcheck.Entity.User;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	
	private final String userId;
	private final String password;
	private final User user;
	
	public UserDetailsImpl(User user) {
		this.userId = user.getUserId();
		this.password = user.getPassword();
	
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));
	}
	
	public String getUsername() {
		return userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    
    public boolean isAccountNonLocked() {
        return true; // 잠김 여부
    }

 
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

   
    public boolean isEnabled() {
        return true; // 활성화 여부
    }
}*/