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


@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
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
    public void update(int person_id, Person person)
    {
        person.setPersonId(person_id);
        peopleRepository.save(person);
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
