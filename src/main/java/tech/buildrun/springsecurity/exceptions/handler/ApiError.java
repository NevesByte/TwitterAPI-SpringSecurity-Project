package tech.buildrun.springsecurity.exceptions.handler;

import java.time.Instant;

public record ApiError(
    int status,
    String error,
    String message,
    Instant timestamp,
    String path
) {
}