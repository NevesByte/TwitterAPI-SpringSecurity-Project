package tech.buildrun.springsecurity.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;
import tech.buildrun.springsecurity.entity.Role;
import tech.buildrun.springsecurity.entity.User;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

@Configuration
public class AdminUserConifg implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConifg(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name())
            .orElseThrow(() -> new IllegalStateException("Role ADMIN nao encontrada"));

        userRepository.findByUsername("admin").ifPresentOrElse(
            user -> System.out.println("Admin ja cadastrado"),
            () -> {
                var user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRoles(Set.of(roleAdmin));
                userRepository.save(user);
            }
        );
    }
}