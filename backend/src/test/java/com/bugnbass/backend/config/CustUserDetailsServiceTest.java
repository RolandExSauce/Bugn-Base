package com.bugnbass.backend.config;

import com.bugnbass.backend.model.ibaseinterface.IbaseUser;
import com.bugnbass.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustUserDetailsServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    CustUserDetailsService custUserDetailsService;

    @Test
    void loadUserByUsername_returnsUserDetails_whenUserExists() {
        String email = "max@test.com";

        IbaseUser userDetails = mock(IbaseUser.class);
        when(userService.findByEmail(email)).thenReturn(Optional.of(userDetails));

        UserDetails result = custUserDetailsService.loadUserByUsername(email);

        assertThat(result).isSameAs(userDetails);
        verify(userService).findByEmail(email);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFound_whenUserMissing() {
        String email = "missing@test.com";
        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> custUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(email);

        verify(userService).findByEmail(email);
        verifyNoMoreInteractions(userService);
    }
}
