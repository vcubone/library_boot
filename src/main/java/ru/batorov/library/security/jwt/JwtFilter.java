package ru.batorov.library.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import ru.batorov.library.services.PeopleService;

public class JwtFilter extends OncePerRequestFilter {
	private final JwtProvider jwtTokenProvider;
	private final PeopleService peopleService;

	public JwtFilter(JwtProvider jwtTokenProvider, PeopleService peopleService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.peopleService = peopleService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");// мы в этот хедер будет класть jwt
		if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer "))
		// jwt принято передавать под хедеров вверху и начинать с "Bearer "
		{
			String jwt = authHeader.substring(7);// откидываем Bearer

			if (jwt.isBlank())
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid jwt token in Bearer Header");
			else {
				try {
					String username = jwtTokenProvider.ValidateTokenAndRetrieveClaim(jwt);
					UserDetails userDetails = peopleService.loadUserByUsername(username);

					UsernamePasswordAuthenticationToken authTokern = new UsernamePasswordAuthenticationToken(
							userDetails,
							userDetails.getPassword(), userDetails.getAuthorities());

					if (SecurityContextHolder.getContext().getAuthentication() == null) {
						SecurityContextHolder.getContext().setAuthentication(authTokern);
					}
				} catch (JWTVerificationException exc) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid jwt token");
				}
			}
		}
		filterChain.doFilter(request, response);// фильтров много, продвигаем дальше
	}
}
