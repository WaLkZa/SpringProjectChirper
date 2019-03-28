package org.chirper.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private String username;

    private String password;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private Set<UserRole> authorities;

    private Set<Chirp> chirps;

    private Set<User> followers;

    private Set<User> following;

    public User() {
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
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
            , targetEntity = UserRole.class
            , fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public Set<UserRole> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
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
    public Set<Chirp> getChirps() {
        return chirps;
    }

    public void setChirps(Set<Chirp> chirps) {
        this.chirps = chirps;
    }

    @Transient
    public int getChirpsCounter() {
        return this.chirps.size();
    }

    @ManyToMany(mappedBy = "following")
    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    @Transient
    public void setFollower(User follower) {
        this.followers.add(follower);
    }

    @Transient
    public void removeFollower(User follower) {
        this.followers.remove(follower);
    }

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "followers",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id", referencedColumnName = "id"))
    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    @Transient
    public void setFollowing(User following) {
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
    public boolean isAuthor(Chirp chirp) {
        return Objects.equals(this.getId(), chirp.getAuthor().getId());
    }
}