package ru.batorov.library.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import ru.batorov.library.util.exceptions.PersonNotFoundException;

//TODO check if get find variations inserted int the right way
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

    public Person findPersonById(int person_id) {
        return peopleRepository.findById(person_id).orElse(null);
    }

    public Person getPersonById(int person_id) {
        return peopleRepository.findById(person_id).orElseThrow(() -> new PersonNotFoundException(person_id));
    }

    public Person findPersonWithRoles(int person_id) {
        Person person = findPersonById(person_id);
        if (person != null)
            Hibernate.initialize(person.getRoles());
        return person;
    }

    public Person getPersonWithRoles(int person_id) {
        Person person = getPersonById(person_id);
        Hibernate.initialize(person.getRoles());
        return person;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Set.of(rolesService.getRoleByName("ROLE_USER")));
        save(person);
    }

    @Transactional
    public void update(int person_id, Person person) {
        Person personToBeUpdated = getPersonById(person_id);
        update(person, personToBeUpdated);
    }

    @Transactional
    public void update(String username, Person person) {
        Person personToBeUpdated = getPersonByUsername(username);
        update(person, personToBeUpdated);
    }

    private void update(Person person, Person personToBeUpdated) {
        if (person.getPassword() != null)
            person.setPassword(passwordEncoder.encode(person.getPassword()));
        CopyHelper.copyNotNullProperties(person, personToBeUpdated);
        save(personToBeUpdated);
    }

    @Transactional
    public void delete(int person_id) {
        peopleRepository.deleteById(person_id);
    }

    public Collection<Book> findBooksByPersonId(int person_id) {
        Person person = getPersonById(person_id);
        if (person != null) {
            Hibernate.initialize(person.getBooks());

            person.getBooks().forEach(book -> {
                long time = Math.abs(new Date().getTime() - book.getTakeTime().getTime());
                book.setExpired(864000000 < time);
            });
            return person.getBooks();
        }
        return new ArrayList<>();
    }

    public Collection<Person> findPersonsByFullName(String name) {
        return peopleRepository.findByFullName(name);
    }

    public Person getPersonByUsername(String username) {
        return peopleRepository.findByUsername(username).get();
    }

    public Person findPersonByUsername(String username) {
        return peopleRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void addRole(int personId, Role role) {
        if (role.getName() != null) {
            Person person = getPersonWithRoles(personId);
            if (person.getRoles() == null)
                person.setRoles(new HashSet<>());
            Role roleToAdd = rolesService.getRoleByName(role.getName());
            person.getRoles().add(roleToAdd);
            save(person);
        }
    }

    // TODO interceptor for role change
    @Transactional
    public void deleteRole(int personId, Role roleToDelete) {
        if (roleToDelete != null && !roleToDelete.getName().equals("ROLE_USER")) {
            Person person = getPersonWithRoles(personId);
            person.getRoles().remove(roleToDelete);
            save(person);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> opPerson = peopleRepository.findByUsername(username);

        if (opPerson.isEmpty())
            throw new UsernameNotFoundException("User not found");
        Person person = opPerson.get();

        Hibernate.initialize(person.getRoles());

        return new PersonDetails(person);
    }
}
