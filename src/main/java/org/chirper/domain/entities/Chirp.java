package org.chirper.domain.entities;

import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Timestamp.*;

@Entity
@Table(name="chirps")
public class Chirp extends BaseEntity {

    private String content;

    private LocalDateTime dateAdded;

    private User author;

    private List<User> userLikes;

    public Chirp() {
        this.userLikes = new ArrayList<>();
    }

    public Chirp(String content, LocalDateTime dateAdded, User author) {
        this.content = content;
        this.dateAdded = dateAdded;
        this.author = author;
    }

    @Column(name = "content", columnDefinition = "TEXT", nullable = false, updatable = true)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "date_added", nullable = false, updatable = true)
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @ManyToOne()
    @JoinColumn(name = "author_id", nullable = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToMany(mappedBy="chirpLikes")
    public List<User> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(List<User> userLikes) {
        this.userLikes = userLikes;
    }

    @Transient
    public int getUserLikesCounter() {
        return this.userLikes.size();
    }

    @Transient
    public String getTimeAgo() {
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(valueOf(this.dateAdded));
    }
}
