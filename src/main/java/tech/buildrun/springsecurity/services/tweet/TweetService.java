package tech.buildrun.springsecurity.services.tweet;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tech.buildrun.springsecurity.dtos.CreateTweetsDto;
import tech.buildrun.springsecurity.dtos.FeedDto;
import tech.buildrun.springsecurity.dtos.FeedItemDto;
import tech.buildrun.springsecurity.entity.Tweet;
import tech.buildrun.springsecurity.exceptions.ForbiddenException;
import tech.buildrun.springsecurity.exceptions.ResourceNotFoundException;
import tech.buildrun.springsecurity.repository.TweetRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

@Service
public class TweetService {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public TweetService(UserRepository userRepository, TweetRepository tweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public void createTweet(CreateTweetsDto dto, UUID userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        var tweet = new Tweet();
        tweet.setUser(user);
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);
    }

    public void deleteTweet(Long tweetId, UUID loggedUserId) {
        var tweet = tweetRepository.findById(tweetId)
            .orElseThrow(() -> new ResourceNotFoundException("Tweet nao encontrado"));

        if (!tweet.getUser().getId().equals(loggedUserId)) {
            throw new ForbiddenException("Sem permissao para excluir este tweet");
        }

        tweetRepository.delete(tweet);
    }

    public FeedDto getFeed(int page, int pageSize) {
        var tweets = tweetRepository.findAll(
            PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
            .map(tweet -> new FeedItemDto(
                tweet.getTweetId(),
                tweet.getContent(),
                tweet.getUser().getUsername()
            ));

        return new FeedDto(
            tweets.getContent(),
            page,
            pageSize,
            tweets.getTotalPages(),
            (int) tweets.getTotalElements()
        );
    }
}
