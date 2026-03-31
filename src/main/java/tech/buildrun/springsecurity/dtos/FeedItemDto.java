package tech.buildrun.springsecurity.dtos;

public record FeedItemDto(long tweetId, String content, String username) {
}