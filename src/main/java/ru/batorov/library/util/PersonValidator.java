package ru.batorov.library.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;



@Component
public class PersonValidator implements Validator{

    private final PeopleService peopleService;
    
    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    //показывает к какой сущности относится валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        //есть ли человек с таким же ФИО
        Optional<Person> opPerson = peopleService.getPersonByFullName(person.getFullName()).stream().findAny();
        if (opPerson.isPresent() && person.getPersonId() != opPerson.get().getPersonId())
        {
            errors.rejectValue("fullName", "", "this full_name is already taken");
        }

    }  
}
