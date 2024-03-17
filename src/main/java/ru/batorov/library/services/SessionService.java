package ru.batorov.library.services;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.security.PersonDetails;

@Service
@Transactional(readOnly = true)
public class SessionService {
    private final SessionRegistry sessionRegistry;

    public SessionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void expireUserSessions(int personId) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof PersonDetails) {
                PersonDetails userDetails = (PersonDetails) principal;
                if (userDetails.getPerson().getId().equals(personId)) {
                    for (SessionInformation information : sessionRegistry
                            .getAllSessions(userDetails, true)) {

                        information.expireNow();
                    }
                }
            }
        }
    }

    public void expireUserSessions(String username) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    for (SessionInformation information : sessionRegistry
                            .getAllSessions(userDetails, true)) {

                        information.expireNow();
                    }
                }
            }
        }
    }
}
