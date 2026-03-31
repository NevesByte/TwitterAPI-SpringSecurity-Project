package tech.buildrun.springsecurity.services.user;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tech.buildrun.springsecurity.dtos.CreateUserDto;
import tech.buildrun.springsecurity.entity.Role;
import tech.buildrun.springsecurity.entity.User;
import tech.buildrun.springsecurity.exceptions.ConflictException;
import tech.buildrun.springsecurity.exceptions.ResourceNotFoundException;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ConflictException("Usuario ja existe");
        }

        var roleBasic = roleRepository.findByName(Role.Values.BASIC.name())
            .orElseThrow(() -> new ResourceNotFoundException("Role BASIC nao encontrada"));

        var newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRoles(Set.of(roleBasic));

        userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}