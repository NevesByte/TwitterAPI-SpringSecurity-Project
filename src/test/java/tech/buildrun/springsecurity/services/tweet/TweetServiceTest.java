package tech.buildrun.springsecurity.services.tweet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.buildrun.springsecurity.dtos.CreateTweetsDto;
import tech.buildrun.springsecurity.entity.Tweet;
import tech.buildrun.springsecurity.entity.User;
import tech.buildrun.springsecurity.exceptions.ForbiddenException;
import tech.buildrun.springsecurity.exceptions.ResourceNotFoundException;
import tech.buildrun.springsecurity.repository.TweetRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private TweetService tweetService;

    @Test
    void shouldCreateTweetWhenUserExists() {
        var userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        tweetService.createTweet(new CreateTweetsDto("novo tweet"), userId);

        verify(tweetRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowWhenTweetNotFoundOnDelete() {
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tweetService.deleteTweet(1L, UUID.randomUUID()));
    }

    @Test
    void shouldThrowWhenDeletingTweetFromAnotherUser() {
        var ownerId = UUID.randomUUID();
        var loggedId = UUID.randomUUID();

        var user = new User();
        user.setId(ownerId);

        var tweet = new Tweet();
        tweet.setTweetId(1L);
        tweet.setUser(user);

        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        assertThrows(ForbiddenException.class, () -> tweetService.deleteTweet(1L, loggedId));
    }
}
