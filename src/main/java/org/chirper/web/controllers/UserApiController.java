package org.chirper.web.controllers;

import org.chirper.domain.models.view.UserAllFollowersViewModel;
import org.chirper.domain.models.view.UserAllFollowingViewModel;
import org.chirper.domain.models.view.UserAllLikesViewModel;
import org.chirper.service.ChirpService;
import org.chirper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserApiController {

    private final UserService userService;

    private final ChirpService chirpService;

    @Autowired
    public UserApiController(UserService userService, ChirpService chirpService) {
        this.userService = userService;
        this.chirpService = chirpService;
    }

    @GetMapping("/api/followersList/{userId}")
    public List<UserAllFollowersViewModel> listFollowers(@PathVariable(name = "userId") String userId) {

        List<UserAllFollowersViewModel> allFollowersUsers
                = this.userService.getUserAllFollowers(userId);

        return allFollowersUsers;
    }


    @GetMapping("/api/followingList/{userId}")
    public List<UserAllFollowingViewModel> listFollowing(@PathVariable(name = "userId") String userId) {

        List<UserAllFollowingViewModel> allFollowingUsers
                = this.userService.getUserAllFollowing(userId);

        return allFollowingUsers;
    }

    @GetMapping("/api/listLikes/{chirpId}")
    public List<UserAllLikesViewModel> listLikes(@PathVariable(name = "chirpId") String chirpId) {

        List<UserAllLikesViewModel> likesUsers
                = this.chirpService.getChirpLikes(chirpId);

        return likesUsers;
    }
}
