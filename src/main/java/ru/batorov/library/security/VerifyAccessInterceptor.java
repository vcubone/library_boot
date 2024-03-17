package ru.batorov.library.security;

import static ru.batorov.library.util.AuthenticationHelper.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;

@Component
public class VerifyAccessInterceptor implements HandlerInterceptor {
    private final PeopleService peopleService;
    
    public VerifyAccessInterceptor(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                System.out.println("\ninterceptor\n");
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication != null && currentAuthentication.getPrincipal() instanceof PersonDetails) {
            Integer version = ((PersonDetails) currentAuthentication.getPrincipal()).getPerson().getVersion();
            Person person = peopleService.getPersonWithRoles(getUserIdByAuthentication(currentAuthentication));
            if (version != null && version < person.getVersion()) {
                PersonDetails personDetails = new PersonDetails(person);
                UsernamePasswordAuthenticationToken updatedAuthentication = new UsernamePasswordAuthenticationToken(
                        personDetails,
                        personDetails.getPassword(), personDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
            }
        }
        return true;
    }
}
