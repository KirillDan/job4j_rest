package ru.job4j.auth.security;

import static ru.job4j.auth.security.JWTAuthenticationFilter.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import ru.job4j.auth.repository.PersonRepository;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private PersonRepository repository;
	private AuthoritiesService authoritiesService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, PersonRepository repository, AuthoritiesService authoritiesService) {
		super(authenticationManager);
		this.repository = repository;
		this.authoritiesService = authoritiesService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);		
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
					.verify(token.replace(TOKEN_PREFIX, "")).getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, authoritiesService.authorities(repository.findPersonByLogin(user)));
			}
			return null;
		}
		return null;
	}
}
