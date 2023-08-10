package ru.batorov.library.services;

import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Credentials;
import ru.batorov.library.repositories.CredentialsRepository;
import ru.batorov.library.security.CredentialsDetails;
import ru.batorov.library.util.CopyHelper;

@Service
@Transactional(readOnly = true)
public class CredentialsService implements UserDetailsService {
	private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public CredentialsService(CredentialsRepository credentialsRepository, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<Credentials> getCredentialsByUsername(String username)
    {
        return credentialsRepository.findByUsername(username);
    }
    public Credentials show(int personId)
    {
        return credentialsRepository.findById(personId).orElse(null);
    }
    @Transactional
    public void save(Credentials credentials)
    {
        credentialsRepository.save(credentials);
    }
    
    @Transactional
    public void register(Credentials credentials)
    {
        enrichCredentials(credentials);
        save(credentials);
    }
    
    @Transactional
    public void update(int person_id, Credentials credentials) {
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        Credentials credentialsToBeUpdated = show(person_id);
        CopyHelper.copyNotNullProperties(credentials, credentialsToBeUpdated);
        credentialsRepository.save(credentialsToBeUpdated);
    }
    
    public void enrichCredentials(Credentials credentials)
    {
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        credentials.setRole("ROLE_USER");
    }
    //method from UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Credentials> credentials = credentialsRepository.findByUsername(username);
         
         if (credentials.isEmpty())
             throw new UsernameNotFoundException("User not found");
         
         return new CredentialsDetails(credentials.get());

    }
}
