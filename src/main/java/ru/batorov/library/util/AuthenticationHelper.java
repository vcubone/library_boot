package ru.batorov.library.util;

import org.springframework.security.core.Authentication;

import ru.batorov.library.security.PersonDetails;

public class AuthenticationHelper {
	public static int getUserIdByAuthentication(Authentication authentication) {
		return ((PersonDetails) authentication.getPrincipal()).getPerson().getId();
	}
	
	public static String getUsernameByAuthentication(Authentication authentication) {
		return ((PersonDetails) authentication.getPrincipal()).getUsername();
	}

	public static boolean hasRoleByAuthentication(Authentication authentication, String role) {
		return authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
	}
}
