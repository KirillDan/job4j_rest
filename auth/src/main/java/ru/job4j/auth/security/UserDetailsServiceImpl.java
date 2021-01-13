package ru.job4j.auth.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private PersonRepository repository;
	private AuthoritiesService authoritiesService;

	public UserDetailsServiceImpl(PersonRepository repository, AuthoritiesService authoritiesService) {
		this.repository = repository;
		this.authoritiesService = authoritiesService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person user = this.repository.findPersonByLogin(username);
		if (user == null) {
            throw new UsernameNotFoundException(username);
        }
		return new User(user.getLogin(), user.getPassword(), authoritiesService.authorities(user));
	}
}
