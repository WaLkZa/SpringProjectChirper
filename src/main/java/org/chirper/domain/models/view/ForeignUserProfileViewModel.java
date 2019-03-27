package org.chirper.domain.models.view;

import org.chirper.domain.entities.Chirp;

import java.util.Set;

public class ForeignUserProfileViewModel {
    private String id;

    private String username;

    private Set<Chirp> chirps;

    private Integer chirpsCounter;

    public ForeignUserProfileViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Chirp> getChirps() {
        return chirps;
    }

    public void setChirps(Set<Chirp> chirps) {
        this.chirps = chirps;
    }

    public Integer getChirpsCounter() {
        return chirpsCounter;
    }

    public void setChirpsCounter(Integer chirpsCounter) {
        this.chirpsCounter = chirpsCounter;
    }
}
