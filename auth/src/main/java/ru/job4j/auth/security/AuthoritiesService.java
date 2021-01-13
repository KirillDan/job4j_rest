package ru.job4j.auth.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

@Service
public class AuthoritiesService {
	private PersonRepository repository;
	
	public AuthoritiesService(PersonRepository repository) {
		this.repository = repository;
	}

	public Collection<? extends GrantedAuthority> authorities(Person user) {
		String role = this.repository.findPersonByLogin(user.getLogin()).getRole().getAuthority();
		return AuthorityUtils.createAuthorityList(role);
	}
}
