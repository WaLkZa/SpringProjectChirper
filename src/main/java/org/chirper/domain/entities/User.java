package org.chirper.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private String username;

    private String password;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private Set<Role> authorities;

    private List<Chirp> chirps;

    private List<User> followers;

    private List<User> following;

    private List<Chirp> chirpLikes;

    public User() {
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.chirpLikes = new ArrayList<>();
    }

    @Override
    @Column(name = "username", nullable = false, unique = true)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @ManyToMany(cascade = CascadeType.ALL
            , targetEntity = Role.class
            , fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @OneToMany(targetEntity = Chirp.class, mappedBy = "author")
    @OrderBy(value = "dateAdded DESC")
    public List<Chirp> getChirps() {
        return chirps;
    }

    public void setChirps(List<Chirp> chirps) {
        this.chirps = chirps;
    }

    @ManyToMany(mappedBy = "following")
    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "followers",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id", referencedColumnName = "id"))
    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chirp_id", referencedColumnName = "id"))
    public List<Chirp> getChirpLikes() {
        return chirpLikes;
    }

    public void setChirpLikes(List<Chirp> chirpLikes) {
        this.chirpLikes = chirpLikes;
    }

    @Transient
    public int getChirpsCounter() {
        return this.chirps.size();
    }

    @Transient
    public void addFollower(User follower) {
        this.followers.add(follower);
    }

    @Transient
    public void removeFollower(User follower) {
        this.followers.remove(follower);
    }

    @Transient
    public void addChirpLike(Chirp chirp) {
        this.chirpLikes.add(chirp);
    }

    @Transient
    public void removeChirpLike(Chirp chirp) {
        this.chirpLikes.remove(chirp);
    }

    @Transient
    public void addFollowing(User following) {
        this.following.add(following);
    }

    @Transient
    public void removeFollowing(User following) {
        this.following.remove(following);
    }

    @Transient
    public int getFollowingCounter() {
        return this.following.size();
    }

    @Transient
    public int getFollowersCounter() {
        return this.followers.size();
    }

    @Transient
    public boolean isUserFollowed(User user) {
        return this.following.contains(user);
    }

    @Transient
    public boolean isChirpLikeExist(Chirp chirp) {
        return this.chirpLikes.contains(chirp);
    }

    @Transient
    public boolean isAuthor(Chirp chirp) {
        return Objects.equals(this.getId(), chirp.getAuthor().getId());
    }
}