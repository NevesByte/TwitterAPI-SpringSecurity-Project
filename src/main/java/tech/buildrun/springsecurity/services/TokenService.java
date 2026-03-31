package tech.buildrun.springsecurity.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import tech.buildrun.springsecurity.dtos.LoginRequest;
import tech.buildrun.springsecurity.dtos.LoginResponse;
import tech.buildrun.springsecurity.entity.Role;
import tech.buildrun.springsecurity.exceptions.UnauthorizedException;
import tech.buildrun.springsecurity.repository.UserRepository;

@Service
public class TokenService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public TokenService(UserRepository userRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new UnauthorizedException("Credenciais invalidas"));

        if (!user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new UnauthorizedException("Credenciais invalidas");
        }

        var now = Instant.now();
        var expiresIn = 300;
        var scopes = user.getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
            .issuer("twitterclone-api")
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("scope", scopes)
            .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiresIn);
    }
}