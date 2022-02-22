package com.fils.backend.security;

import com.fils.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.fils.backend.domain.User> userByRepo = userRepository.findByUsername(username);

        userByRepo.orElseThrow(() -> new UsernameNotFoundException("MyUserDetailsService:  NOT FOUND USER for username:" + username));
        List<GrantedAuthority> roles = new ArrayList<>();
        try {
            Arrays.stream(userByRepo.get().getRoles().split(",")).forEach(role -> roles.add(new SimpleGrantedAuthority(role)));
        } catch(Exception e) {
            System.out.println("ERROR at loadByUsername @MyUserDetailsService, on ArraysStream ROLES");
        }
        User securityUser = new org.springframework.security.core.userdetails.User(
                userByRepo.get().getUsername(), userByRepo.get().getPassword(), roles);

        return securityUser;
    }
}
