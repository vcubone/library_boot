package ru.batorov.library.security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import ru.batorov.library.services.JwtBlackListService;

public class JwtBlackListFilter extends OncePerRequestFilter {
	private final JwtBlackListService jwtBlackListService;
	

	public JwtBlackListFilter(JwtBlackListService jwtBlackListService) {
		this.jwtBlackListService = jwtBlackListService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");// мы в этот хедер будет класть jwt
		if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer "))
		// jwt принято передавать под хедеров вверху и начинать с "Bearer "
		{
			String jwt = authHeader.substring(7);// откидываем Bearer

			if (jwt.isBlank() || getBannedJwts().contains(jwt))
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid jwt token in Bearer Header");
		}
		filterChain.doFilter(request, response);
	}
	
	private Set<String> getBannedJwts()
	{
		return jwtBlackListService.all().stream().map(blacklist -> blacklist.getName()).collect(Collectors.toSet());
	}
}