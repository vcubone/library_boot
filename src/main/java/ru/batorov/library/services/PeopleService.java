package ru.batorov.library.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.models.Role;
import ru.batorov.library.repositories.PeopleRepository;
import ru.batorov.library.security.PersonDetails;
import ru.batorov.library.util.CopyHelper;

@Service
@Transactional(readOnly = true)
public class PeopleService implements UserDetailsService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesService rolesService;

    @Lazy
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder,
            RolesService rolesService) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesService = rolesService;
    }

    public List<Person> all() {
        return peopleRepository.findAll();
    }

    public List<Person> allWithRoles() {
        return peopleRepository.findAllWithRoles();
    }

    public Person show(int person_id) {
        return peopleRepository.findById(person_id).orElse(null);
    }

    public Person showWithRoles(int person_id) {
        Person person = peopleRepository.findById(person_id).orElse(null);
        if (person != null)
            Hibernate.initialize(person.getRoles());
        return person;
    }
    
    

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }
    
    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Set.of(rolesService.getRoleByName("ROLE_USER").get()));
        save(person);
    }

    @Transactional
    public void update(int person_id, Person person) {
        Person personToBeUpdated = show(person_id);
        if (person.getPassword() != null)
            person.setPassword(passwordEncoder.encode(person.getPassword()));
        CopyHelper.copyNotNullProperties(person, personToBeUpdated);
        save(personToBeUpdated);
    }
    @Transactional
    public void delete(int person_id) {
        peopleRepository.deleteById(person_id);
    }

    public Collection<Book> getBooksByPersonId(int person_id) {
        Person person = show(person_id);
        if (person != null) {
            Hibernate.initialize(person.getBooks());

            person.getBooks().forEach(book -> {
                long time = Math.abs(new Date().getTime() - book.getTakeTime().getTime());
                book.setExpired(864000000 < time);
            });
            return person.getBooks();
        }
        return Collections.emptyList();
    }

    public Collection<Person> getPersonByFullName(String name) {
        return peopleRepository.findByFullName(name);
    }

    public Optional<Person> getPersonByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    @Transactional
    public void addRole(int personId, Role role) {
        Person person = show(personId);
        if (person != null && role.getName() != null) {
            if (person.getRoles() == null)
                person.setRoles(Collections.emptyList());
            Role roleToAdd = rolesService.getRoleByName(role.getName()).get();
            person.getRoles().add(roleToAdd);
            save(person);
        }
    }

    //TODO разлогинивать человека при изменении ролей(сессия не меняется при изменении бд)
    @Transactional
    public void deleteRole(int personId, Role roleToDelete) {
        Person person = show(personId);
        if (person != null && !roleToDelete.getName().equals("ROLE_USER")) {
            if (person.getRoles() == null)
                return;
            person.getRoles().remove(roleToDelete);
            save(person);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        Hibernate.initialize(person.get().getRoles());

        return new PersonDetails(person.get());
    }
}
