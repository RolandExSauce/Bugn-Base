package com.bugnbass.backend.config;

import com.bugnbass.backend.model._interface.IBaseUser;
import com.bugnbass.backend.repository.AdminRepository;
import com.bugnbass.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//TODO: assigned by Ines

@Service
@RequiredArgsConstructor
public class CustUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find the user in the user repository
        IBaseUser user = userRepository.findByUsername(username)
                .map(u -> (IBaseUser) u)
                .orElseGet(() -> adminRepository.findByUsername(username)
                        .map(a -> (IBaseUser) a)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found with email: " + username)
                        )
                );

        return user; // BaseUser implements UserDetails, so this is valid
    }
}
