package tech.buildrun.springsecurity.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.buildrun.springsecurity.dtos.CreateTweetsDto;
import tech.buildrun.springsecurity.dtos.FeedDto;
import tech.buildrun.springsecurity.services.tweet.TweetService;

@RestController
public class TwitterController {

    private final TweetService tweetService;

    public TwitterController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody @Valid CreateTweetsDto dto,
                                            JwtAuthenticationToken token) {
        tweetService.createTweet(dto, UUID.fromString(token.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long id,
                                            JwtAuthenticationToken token) {
        tweetService.deleteTweet(id, UUID.fromString(token.getName()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(tweetService.getFeed(page, pageSize));
    }
}