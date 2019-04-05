package org.chirper.service;

import org.chirper.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean createUser(User user);

    User getCurrentLoggedUser();

    List<User> getAll();

    boolean promoteUser(String id);

    boolean demoteUser(String id);

    void followUser(String toFollowId);

    void unfollowUser(String followedId);

    List<User> getUserAllFollowers(String userId);

    List<User> getUserAllFollowing(String userId);
}