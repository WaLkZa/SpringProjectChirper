package org.chirper.domain.models.service;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.UserRole;

import java.util.Set;

public class UserServiceModel {

    private String id;

    private String username;

    private String password;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private Set<UserRole> authorities;

    private Set<Chirp> chirps;

    //private Integer chirpsCounter;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Set<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }

    public Set<Chirp> getChirps() {
        return chirps;
    }

    public void setChirps(Set<Chirp> chirps) {
        this.chirps = chirps;
    }

//    public Integer getChirpsCounter() {
//        return chirpsCounter;
//    }
//
//    public void setChirpsCounter(Integer chirpsCounter) {
//        this.chirpsCounter = chirpsCounter;
//    }

    public String extractAuthority() {
        return this.getAuthorities()
                .stream()
                .findFirst()
                .orElse(null)
                .getAuthority();
    }
}
