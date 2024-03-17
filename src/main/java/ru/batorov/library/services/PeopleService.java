package ru.batorov.library.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.OptimisticLockingFailureException;
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
import ru.batorov.library.util.exceptions.RoleNotFoundException;

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

    /**
     * Return list of all persons without lazy fields.
     * 
     * @return list of all persons.
     */
    public List<Person> all() {
        return peopleRepository.findAll();
    }

    /**
     * Return list of all persons with roles without remaining lazy fields.
     * 
     * @return list of all persons with roles.
     */
    public List<Person> allWithRoles() {
        return peopleRepository.findAllWithRoles();
    }

    /**
     * Return person by its person_id if present, otherwise return null.
     * 
     * @param person_id must not be {@literal null}.
     * @return person if present, otherwise {@literal null}.
     * @throws IllegalArgumentException - if {@literal person_id} is
     *                                  {@literal null}.
     */
    public Person findPersonById(Integer person_id) {
        return peopleRepository.findById(person_id).orElse(null);
    }

    /**
     * Return person by its person_id if present, otherwise throw
     * PersonNotFoundException.
     * 
     * @param person_id must not be {@literal null}.
     * @return person.
     * @throws PersonNotFoundException  if no person is present.
     * @throws IllegalArgumentException - if {@literal person_id} is
     *                                  {@literal null}.
     */
    public Person getPersonById(Integer person_id) {
        return peopleRepository.findById(person_id).orElseThrow(() -> new PersonNotFoundException(person_id));
    }

    // return only person_id username mb roles
    @Deprecated
    public Person getAuthenticationsInformation(Integer personId) {
        return peopleRepository.getAuthenticationsInformation(personId);
    }

    /**
     * Return person by its person_id with roles if present, otherwise return null.
     * 
     * @param person_id must not be {@literal null}.
     * @return person with roles if present, otherwise {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal person_id} is
     *                                  {@literal null}.
     */
    public Person findPersonWithRoles(Integer person_id) {
        Person person = findPersonById(person_id);
        if (person != null)
            Hibernate.initialize(person.getRoles());
        return person;
    }

    /**
     * Return person by its person_id with roles if present, otherwise throw
     * PersonNotFoundException.
     * 
     * @param person_id must not be {@literal null}.
     * @return person with roles.
     * @throws PersonNotFoundException  if no person is present.
     * @throws IllegalArgumentException in case the given {@literal person_id} is
     *                                  {@literal null}.
     */
    public Person getPersonWithRoles(Integer person_id) {
        Person person = getPersonById(person_id);
        Hibernate.initialize(person.getRoles());
        return person;
    }

    /**
     * Return person by its person_id with roles and books if present, otherwise
     * throw PersonNotFoundException.
     * 
     * @param person_id must not be {@literal null}.
     * @return person with roles and books.
     * @throws PersonNotFoundException  if no person is present.
     * @throws IllegalArgumentException in case the given {@literal person_id} is
     *                                  {@literal null}.
     */
    public Person getPersonWithRolesAndBooks(Integer person_id) {
        Person person = getPersonById(person_id);
        Hibernate.initialize(person.getRoles());
        Hibernate.initialize(person.getBooks());
        setExpired(person.getBooks());
        return person;
    }

    /**
     * Save the given person.
     * 
     * @param person must not be {@literal null}.
     * @return the saved person; will never be {@literal null}.
     * @throws IllegalArgumentException          in case the given {@literal person}
     *                                           is {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    @Transactional
    public Person save(Person person) {
        peopleRepository.save(person);
        return person;
    }

    /**
     * Register the person. Processes the object before saving.
     * 
     * @param person must not be {@literal null}.
     * @return the saved person; will never be {@literal null}.
     * @throws IllegalArgumentException          in case the given {@literal person}
     *                                           is {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    @Transactional
    public Person register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Set.of(rolesService.getRoleByName("ROLE_USER")));
        person.setCreated_at(LocalDateTime.now());
        person.setUpdated_at(person.getCreated_at());
        person.setVersion(1);
        save(person);
        return person;
    }

    /**
     * Update existing person with fields of incoming person.
     * 
     * @param person_id of existing person, must not be {@literal null}.
     * @param person    with changed fields, must not be {@literal null}.
     * @throws IllegalArgumentException          in case the given {@literal person}
     *                                           or {@literal person_id} or both
     *                                           are {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    @Transactional
    public void update(Integer person_id, Person person) {
        Person personToBeUpdated = getPersonById(person_id);
        update(person, personToBeUpdated);
    }

    /**
     * Update existing person with fields of incoming person.
     * 
     * @param username of existing person, must not be {@literal null}.
     * @param person   with changed fields, must not be {@literal null}.
     * @throws IllegalArgumentException          in case the given {@literal person}
     *                                           or {@literal username} or both
     *                                           are {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    @Transactional
    public void update(String username, Person person) {
        if (username == null)
            throw new IllegalArgumentException("Username must not be null!");
        Person personToBeUpdated = getPersonByUsername(username);
        update(person, personToBeUpdated);
    }

    /**
     * Update existing person with fields of incoming person.
     * 
     * @param person            of existing person, must not be {@literal null}.
     * @param personToBeUpdated with changed fields, must not be {@literal null}.
     * @throws IllegalArgumentException          in case the given
     *                                           {@literal personToBeUpdated}
     *                                           or {@literal person} or both
     *                                           are {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    private void update(Person person, Person personToBeUpdated) {
        if (person.getPassword() != null)
            person.setPassword(passwordEncoder.encode(person.getPassword()));
        CopyHelper.copyNotNullProperties(person, personToBeUpdated);
    }

    /**
     * Deletes the person with the given person_id.
     * <p>
     * If the person is not found it is silently ignored.
     * 
     * @param person_id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal person_id} is
     *                                  {@literal null}
     */
    @Transactional
    public void delete(Integer person_id) {
        peopleRepository.deleteById(person_id);
    }

    /**
     * Return a person's books, if books exist, otherwise return empty collection.
     * But is person is not exists throw PersonNotFoundException
     * 
     * @param person_id must not be {@literal null}.
     * @return person's books, if books exist, otherwise return empty collection.
     * @throws PersonNotFoundException  if no person is present.
     * @throws IllegalArgumentException - if {@literal person_id} is
     *                                  {@literal null}.
     */
    public Collection<Book> findBooksByPersonId(Integer person_id) {
        Person person = getPersonById(person_id);
        if (person != null) {
            Hibernate.initialize(person.getBooks());

            setExpired(person.getBooks());
            return person.getBooks();
        }
        return new ArrayList<>();
    }

    /**
     * Return person by its fullName if present, otherwise return null.
     * 
     * @param fullName must not be {@literal null}.
     * @return person
     * @throws IllegalArgumentException - if {@literal person_id} is
     *                                  {@literal null}.
     */
    public Collection<Person> findPersonsByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    /**
     * Return person by its username if present, otherwise throw
     * PersonNotFoundException.
     * 
     * @param username must not be {@literal null}.
     * @return person.
     * @throws PersonNotFoundException  if no value is present.
     * @throws IllegalArgumentException - if {@literal username} is
     *                                  {@literal null}.
     */
    public Person getPersonByUsername(String username) {
        if (username == null)
            throw new IllegalArgumentException("Username must not be null!");
        return peopleRepository.findByUsername(username).get();
    }

    /**
     * Return person by its username if present, otherwise null.
     * 
     * @param username must not be {@literal null}.
     * @return person if exist, othewise {@literal null}.
     * @throws IllegalArgumentException - if {@literal username} is
     *                                  {@literal null}.
     */
    public Person findPersonByUsername(String username) {
        return peopleRepository.findByUsername(username).orElse(null);
    }

    /**
     * Add role to the person with required person_id.
     * 
     * @param person_id must not be null.
     * @param role      must not be null
     * @throws IllegalArgumentException - if {@literal person_id}, {@literal role} or {@literal role.name} is
     *                                  {@literal null}.
     * @throws PersonNotFoundException if no person is present.
     * @throws RoleNotFoundException    if given role is not present.
     */
    @Transactional
    public void addRole(Integer person_id, Role role) {
        if (role == null)
            throw new IllegalArgumentException("The given role must not be null");
        if (role.getName() == null)
            throw new IllegalArgumentException("Role's name is null");
        Person person = getPersonWithRoles(person_id);
        if (person.getRoles() == null)
            person.setRoles(new HashSet<>());
        Role roleToAdd = rolesService.getRoleByName(role.getName());
        person.getRoles().add(roleToAdd);
        person.setVersion(person.getVersion() + 1);
    }

    /**
     * Delete person's role
     * <p>
     * If the person don't have given role it is silently ignored.
     * 
     * @param personId     person's id. Must not be {@literal null}.
     * @param roleToDelete must not be {@literal null}.
     * @throws IllegalArgumentException - if {@literal person_id},
     *                                  {@literal roleToDelete} or {@literal roleToDelete.name}
     *                                  is
     *                                  {@literal null}.
     * @throws PersonNotFoundException  if no person is present.
     */
    @Transactional
    public void deleteRole(Integer personId, Role roleToDelete) {
        if (roleToDelete == null)
            throw new IllegalArgumentException("The given role must not be null");
        if (roleToDelete.getName() == null)
            throw new IllegalArgumentException("Role's name is null");
        if (roleToDelete.getName().equals("ROLE_USER"))
            return;
        Person person = getPersonWithRoles(personId);
        if (person.getRoles().remove(roleToDelete))
            person.setVersion(person.getVersion() + 1);
    }

    /**
     * Locates the user based on the username.
     * 
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user
     *                                   has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> opPerson = peopleRepository.findByUsername(username);

        if (opPerson.isEmpty())
            throw new UsernameNotFoundException("User not found");
        // надо ли это?
        if (opPerson.get().getRoles() == null)
            throw new UsernameNotFoundException("User has no roles");
        Person person = opPerson.get();

        Hibernate.initialize(person.getRoles());

        return new PersonDetails(person);
    }

    /**
     * Set expired field if the deadline has expired.
     * 
     * @param books must not be null.
     * @throws IllegalArgumentException in case the given {@literal books}
     * 
     */
    private void setExpired(Collection<Book> books) {
        if (books == null)
            throw new IllegalArgumentException("Books must not be null!");
        books.forEach(book -> {
            long time = Math.abs(new Date().getTime() - book.getTakeTime().getTime());
            book.setExpired(864000000 < time);
        });
    }
}