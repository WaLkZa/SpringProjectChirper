package org.chirper.service;

import org.chirper.domain.entities.User;
import org.chirper.domain.models.view.UserAllFollowersViewModel;
import org.chirper.domain.models.view.UserAllFollowingViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean createUser(User user);

    User getCurrentLoggedUser();

    List<User> getAll();

    void setUserRole(String id, String role);

    void followUser(String toFollowId);

    void unfollowUser(String followedId);

    List<UserAllFollowersViewModel> getUserAllFollowers(String userId);

    List<UserAllFollowingViewModel> getUserAllFollowing(String userId);
}