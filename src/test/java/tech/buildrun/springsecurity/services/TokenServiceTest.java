package tech.buildrun.springsecurity.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import tech.buildrun.springsecurity.dtos.LoginRequest;
import tech.buildrun.springsecurity.entity.Role;
import tech.buildrun.springsecurity.entity.User;
import tech.buildrun.springsecurity.exceptions.UnauthorizedException;
import tech.buildrun.springsecurity.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        var role = new Role();
        role.setRoleId(2L);
        role.setName("BASIC");

        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("victor");
        user.setPassword("hashed");
        user.setRoles(Set.of(role));

        var jwt = Jwt.withTokenValue("token123")
            .header("alg", "none")
            .claim("scope", "BASIC")
            .build();

        when(userRepository.findByUsername("victor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        var response = tokenService.login(new LoginRequest("victor", "123456"));

        assertEquals("token123", response.accessToken());
    }

    @Test
    void shouldThrowUnauthorizedForInvalidPassword() {
        var user = new User();
        user.setPassword("hashed");

        when(userRepository.findByUsername("victor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(UnauthorizedException.class,
            () -> tokenService.login(new LoginRequest("victor", "wrong")));
    }
}