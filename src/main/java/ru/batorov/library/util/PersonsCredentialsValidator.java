package ru.batorov.library.util;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;


@Component
public class PersonsCredentialsValidator implements Validator {
	private final PeopleService peopleService;

	public PersonsCredentialsValidator(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		Optional<Person> opPerson = peopleService.getPersonByUsername(person.getUsername());
		if (opPerson.isPresent() && person.getId() != opPerson.get().getId())
			errors.rejectValue("username", "", "this username is already taken");
	}
}
