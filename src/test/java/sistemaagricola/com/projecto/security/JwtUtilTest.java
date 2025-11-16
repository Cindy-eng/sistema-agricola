package sistemaagricola.com.projecto.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", 
            "mySecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLongForHS256Algorithm");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 hours

        userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void should_generateToken_when_validUserDetails() {
        String token = jwtUtil.generateToken(userDetails);
        
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    void should_extractUsername_when_validToken() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        
        assertThat(username).isEqualTo("test@example.com");
    }

    @Test
    void should_extractExpiration_when_validToken() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        
        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());
    }

    @Test
    void should_validateToken_when_validTokenAndUserDetails() {
        String token = jwtUtil.generateToken(userDetails);
        Boolean isValid = jwtUtil.validateToken(token, userDetails);
        
        assertThat(isValid).isTrue();
    }

    @Test
    void should_notValidateToken_when_differentUsername() {
        String token = jwtUtil.generateToken(userDetails);
        
        UserDetails differentUser = User.builder()
                .username("different@example.com")
                .password("password")
                .roles("USER")
                .build();
        
        Boolean isValid = jwtUtil.validateToken(token, differentUser);
        
        assertThat(isValid).isFalse();
    }

    @Test
    void should_notValidateToken_when_expiredToken() throws Exception {
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1000L); // 1 second
        
        String token = jwtUtil.generateToken(userDetails);
        
        // Wait for token to expire
        Thread.sleep(2000);
        
        Boolean isValid = jwtUtil.validateToken(token, userDetails);
        
        assertThat(isValid).isFalse();
    }

    @Test
    void should_extractClaim_when_validToken() {
        String token = jwtUtil.generateToken(userDetails);
        String subject = jwtUtil.extractClaim(token, claims -> claims.getSubject());
        
        assertThat(subject).isEqualTo("test@example.com");
    }
}

