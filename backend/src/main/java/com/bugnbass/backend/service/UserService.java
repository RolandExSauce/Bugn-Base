package com.bugnbass.backend.service;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    };

    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No registered user under this E-mail address")));
    };

    public void updateCustomer(User user) {
        userRepository.save(user);
    }

};
