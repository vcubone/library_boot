package ru.batorov.library.security;

import java.io.IOException;

import static ru.batorov.library.util.AuthenticationHelper.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;

public class AuthenticationCheckFilter extends OncePerRequestFilter {
    private final PeopleService peopleService;

    public AuthenticationCheckFilter(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication != null && currentAuthentication.getPrincipal() instanceof PersonDetails) {
            // оптимизировать загрузку только version username roles
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
        filterChain.doFilter(request, response);
    }

}
