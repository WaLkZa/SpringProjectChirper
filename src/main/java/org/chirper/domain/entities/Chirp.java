package org.chirper.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="chirps")
public class Chirp extends BaseEntity {

    private String content;

    private LocalDateTime dateAdded;

    private User author;

    public Chirp() {
    }

    public Chirp(String content, LocalDateTime dateAdded, User author) {
        this.content = content;
        this.dateAdded = dateAdded;
        this.author = author;
    }

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "date_added", nullable = false)
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
}
