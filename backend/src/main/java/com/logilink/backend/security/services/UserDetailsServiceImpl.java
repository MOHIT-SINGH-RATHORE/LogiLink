package com.logilink.backend.security.services;

import com.logilink.backend.User;
import com.logilink.backend.UserRepository;
import com.logilink.backend.entity.Transporter;
import com.logilink.backend.repository.TransporterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransporterRepository transporterRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            return UserDetailsImpl.build(user.get());
        }

        Optional<Transporter> transporter = transporterRepository.findByUsernameIgnoreCase(username);
        if (transporter.isPresent()) {
            return UserDetailsImpl.build(transporter.get());
        }

        throw new UsernameNotFoundException("User Not Found with username: " + username);
    }
}
