package ru.batorov.library.services;

import java.util.Collection;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Role;
import ru.batorov.library.security.PersonDetails;

@Service
@Transactional(readOnly = true)
public class SessionService {
    private final SessionRegistry sessionRegistry;
    private final PeopleService peopleService;

    public SessionService(SessionRegistry sessionRegistry, PeopleService peopleService) {
        this.sessionRegistry = sessionRegistry;
        this.peopleService = peopleService;
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

    public void updateRolesInSessions(String username) {// TODO but not getauthorities in authenticate
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof PersonDetails) {
                PersonDetails userDetails = (PersonDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    for (SessionInformation information : sessionRegistry
                            .getAllSessions(userDetails, true)) {

                        Collection<Role> roles = peopleService.getPersonWithRoles(userDetails.getPerson().getId())
                                .getRoles();
                        ((PersonDetails) information.getPrincipal()).getPerson().setRoles(roles);
                    }
                }
            }
        }
    }
}
