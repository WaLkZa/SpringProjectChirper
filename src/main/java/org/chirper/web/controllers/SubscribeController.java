package org.chirper.web.controllers;

import org.chirper.domain.entities.User;
import org.chirper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/follow/{toFollowId}")
    public ModelAndView followUser(@PathVariable(name = "toFollowId") String toFollowId,
                                   HttpServletRequest request) {

        this.userService.followUser(toFollowId);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/unfollow/{followedId}")
    public ModelAndView unfollowUser(@PathVariable(name = "followedId") String followedId,
                                     HttpServletRequest request) {

        this.userService.unfollowUser(followedId);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/followersList/{userId}")
    public ModelAndView listFollowers(@PathVariable(name = "userId") String userId,
                                      Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        List<User> allUsers = this.userService.getUserAllFollowers(userId);

        modelAndView.addObject("allUsers", allUsers);

        return super.view("discover", modelAndView);
    }


    @GetMapping("/followingList/{userId}")
    public ModelAndView listFollowing(@PathVariable(name = "userId") String userId,
                                      Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        List<User> allUsers = this.userService.getUserAllFollowing(userId);

        modelAndView.addObject("allUsers", allUsers);

        return super.view("discover", modelAndView);
    }
}
