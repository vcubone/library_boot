package ru.batorov.library.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.repositories.PeopleRepository;
import ru.batorov.library.util.CopyHelper;


@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final CredentialsService credentialsService;
    
    public PeopleService(PeopleRepository peopleRepository, CredentialsService credentialsService) {
        this.peopleRepository = peopleRepository;
        this.credentialsService = credentialsService;
    }

    public List<Person> all()
    {
        return peopleRepository.findAll();
    }
    
    public Person show(int person_id)
    {
        return peopleRepository.findById(person_id).orElse(null);
    }
    
    @Transactional
    public void save(Person person)
    {
        peopleRepository.save(person);
    }
    @Transactional
    public void register(Person person)
    {
        credentialsService.enrichCredentials(person.getCredentials());
        save(person);
    }
        
    @Transactional
    public void update(int person_id, Person person)
    {
        Person personToBeUpdated = show(person_id);
        CopyHelper.copyNotNullProperties(person, personToBeUpdated);
        peopleRepository.save(personToBeUpdated);
    }
    
    @Transactional
    public void delete(int person_id)
    {
        peopleRepository.deleteById(person_id);
    }
    
    public List<Book> getBooksByPersonId(int person_id)
    {
        Person person = show(person_id);
        if (person != null)
        {
            Hibernate.initialize(person.getBooks());
            
            person.getBooks().forEach(book->{
                long time = Math.abs(new Date().getTime() - book.getTakeTime().getTime());
                book.setExpired(864000000 < time);
            });
            return person.getBooks();
        }
        return Collections.emptyList();
    }
    
    public List<Person> getPersonByFullName(String name){
        return peopleRepository.findByFullName(name);
    }
}
