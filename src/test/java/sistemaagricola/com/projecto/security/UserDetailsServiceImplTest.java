package sistemaagricola.com.projecto.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = sistemaagricola.com.projecto.util.TestDataBuilder.user()
                .id(1L)
                .email("test@example.com")
                .nome("Test User")
                .role(User.Role.USER)
                .enabled(true)
                .build();
    }

    @Test
    void should_loadUserByUsername_when_userExists() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("test@example.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("test@example.com");
        assertThat(result.getAuthorities()).isNotEmpty();
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void should_throwException_when_userNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void should_loadUserByUsername_when_adminUser() {
        User adminUser = sistemaagricola.com.projecto.util.TestDataBuilder.admin()
                .id(2L)
                .build();

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(adminUser));

        UserDetails result = userDetailsService.loadUserByUsername("admin@example.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("admin@example.com");
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }
}

