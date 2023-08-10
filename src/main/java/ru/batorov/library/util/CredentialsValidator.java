package ru.batorov.library.util;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.batorov.library.models.Credentials;
import ru.batorov.library.services.CredentialsService;

@Component
public class CredentialsValidator implements Validator {
	private final CredentialsService credentialsService;

	public CredentialsValidator(CredentialsService credentialsService) {
		this.credentialsService = credentialsService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Credentials credentials = (Credentials) target;
		Optional<Credentials> opCredentionals = credentialsService.getCredentialsByUsername(credentials.getUsername());
		if (opCredentionals.isPresent() && credentials.getPersonId() != opCredentionals.get().getPersonId())
			errors.rejectValue("username", "", "this username is already taken");
	}
}
