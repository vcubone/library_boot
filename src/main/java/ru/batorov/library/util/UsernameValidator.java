package ru.batorov.library.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;

/**
 * Validator checks if persons username is unique.
 */
@Component
public class UsernameValidator implements Validator {
	private final PeopleService peopleService;

	public UsernameValidator(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person personToValidate = (Person) target;
		Person existingPerson = peopleService.findPersonByUsername(personToValidate.getUsername());
		if (existingPerson != null)
			errors.rejectValue("username", "", "this username is already taken");
	}
}
