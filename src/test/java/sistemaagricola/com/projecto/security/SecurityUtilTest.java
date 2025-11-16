package sistemaagricola.com.projecto.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import sistemaagricola.com.projecto.models.User;
import sistemaagricola.com.projecto.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityUtil securityUtil;

    private User appUser;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        
        appUser = sistemaagricola.com.projecto.util.TestDataBuilder.user()
                .id(1L)
                .email("test@example.com")
                .build();

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void should_getCurrentUser_when_authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(appUser));

        User result = securityUtil.getCurrentUser();

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void should_getCurrentUserId_when_authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(appUser));

        Long userId = securityUtil.getCurrentUserId();

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void should_throwException_when_notAuthenticated() {
        SecurityContextHolder.clearContext();

        assertThrows(IllegalStateException.class, () -> {
            securityUtil.getCurrentUser();
        });

        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void should_throwException_when_userNotFound() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            securityUtil.getCurrentUser();
        });
    }

    @Test
    void should_throwException_when_principalNotUserDetails() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "notUserDetails", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(IllegalStateException.class, () -> {
            securityUtil.getCurrentUser();
        });

        verify(userRepository, never()).findByEmail(anyString());
    }
}

