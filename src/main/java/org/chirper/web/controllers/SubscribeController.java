package org.chirper.web.controllers;

import org.chirper.domain.models.view.UserAllFollowersViewModel;
import org.chirper.domain.models.view.UserAllFollowingViewModel;
import org.chirper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SubscribeController extends BaseController {

    private final UserService userService;

    @Autowired
    public SubscribeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/follow/{toFollowId}")
    public ModelAndView followUser(@PathVariable(name = "toFollowId") String toFollowId,
                                   HttpServletRequest request) {

        this.userService.followUser(toFollowId);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/user/unfollow/{followedId}")
    public ModelAndView unfollowUser(@PathVariable(name = "followedId") String followedId,
                                     HttpServletRequest request) {

        this.userService.unfollowUser(followedId);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/user/followersList/{userId}")
    @ResponseBody
    public List<UserAllFollowersViewModel> listFollowers(@PathVariable(name = "userId") String userId) {

        List<UserAllFollowersViewModel> allFollowersUsers
                = this.userService.getUserAllFollowers(userId);

        return allFollowersUsers;
    }


    @GetMapping("/user/followingList/{userId}")
    @ResponseBody
    public List<UserAllFollowingViewModel> listFollowing(@PathVariable(name = "userId") String userId) {

        List<UserAllFollowingViewModel> allFollowingUsers
                = this.userService.getUserAllFollowing(userId);

        return allFollowingUsers;
    }
}
