package tech.buildrun.springsecurity.dtos;

public record LoginResponse(String accessToken, int expiresIn) {
}
