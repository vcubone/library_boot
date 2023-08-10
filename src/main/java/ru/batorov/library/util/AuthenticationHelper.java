package ru.batorov.library.util;

import org.springframework.security.core.Authentication;

import ru.batorov.library.security.CredentialsDetails;

public class AuthenticationHelper {
	public static int getUserIdByAuthentication(Authentication authentif) {
		return ((CredentialsDetails) authentif.getPrincipal()).getCredentials().getPersonId();
	}

	public static boolean hasRoleByAuthentication(Authentication authentif, String role) {
		return authentif.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
	}
}
