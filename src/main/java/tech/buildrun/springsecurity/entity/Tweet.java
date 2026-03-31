package tech.buildrun.springsecurity.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "tb_tweets")
@Getter
@Setter
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tweet_id")
    private Long tweetId;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 280)
    private String content;

    @CreationTimestamp
    private Instant creationTimestamp;
}
