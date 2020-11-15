package com.ahasan.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.ahasan.auth.model.AuthUserDetail;
import com.ahasan.auth.model.User;
import com.ahasan.auth.repository.UserDetailRepository;

import java.util.Optional;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailRepository userDetailRepository;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Optional<User> optionalUser = userDetailRepository.findByUsername(name);

		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));

		UserDetails userDetails = new AuthUserDetail(optionalUser.get());
		new AccountStatusUserDetailsChecker().check(userDetails);
		return userDetails;
	}
	
//	public static void main(String[] args) {
//			String generatedHash = BCrypt.hashpw("ahasan", BCrypt.gensalt(12));
//			System.out.println(generatedHash);
//	}
}
