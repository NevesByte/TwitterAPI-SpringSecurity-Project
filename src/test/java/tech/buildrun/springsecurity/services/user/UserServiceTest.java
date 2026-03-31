package tech.buildrun.springsecurity.services.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tech.buildrun.springsecurity.dtos.CreateUserDto;
import tech.buildrun.springsecurity.entity.Role;
import tech.buildrun.springsecurity.exceptions.ConflictException;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithEncodedPassword() {
        var dto = new CreateUserDto("victor", "123456");
        var role = new Role();
        role.setRoleId(2L);
        role.setName("BASIC");

        when(userRepository.findByUsername("victor")).thenReturn(Optional.empty());
        when(roleRepository.findByName("BASIC")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("123456")).thenReturn("hashed");

        userService.createUser(dto);

        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void shouldThrowConflictWhenUsernameAlreadyExists() {
        var dto = new CreateUserDto("victor", "123456");

        when(userRepository.findByUsername("victor")).thenReturn(Optional.of(new tech.buildrun.springsecurity.entity.User()));

        assertThrows(ConflictException.class, () -> userService.createUser(dto));
    }
}