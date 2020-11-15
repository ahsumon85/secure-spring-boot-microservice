package com.ahasan.item.common.security;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
*
* @author Ahasan Habib
* @since 03 06 20
*/


@Configuration
public class AccessTokenProvider {

	
	private static Optional<OAuth2AuthenticationDetails> currentOAuth2Details() {
		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.filter(OAuth2Authentication.class::isInstance).map(OAuth2Authentication.class::cast)
				.map(OAuth2Authentication::getDetails).map(OAuth2AuthenticationDetails.class::cast);
	}

	public static String provideToken() {
		Optional<OAuth2AuthenticationDetails> currentOAuth2Details = currentOAuth2Details();
		return currentOAuth2Details.get().getTokenValue();
	}


}
