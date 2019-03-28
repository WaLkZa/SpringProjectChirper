package org.chirper.web.controllers;

import org.chirper.domain.entities.User;
import org.chirper.repository.UserRepository;
import org.chirper.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
public class SubscribeController extends BaseController {

    private final UserService userService;

    private final UserRepository userRepository;

    public SubscribeController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/follow/{toFollowId}")
    public ModelAndView followUser(@PathVariable(name = "toFollowId") String toFollowId,
                                   HttpServletRequest request) {

        User toFollowUser = this.userRepository.findById(toFollowId).get();
        User currentLoggedUser = this.userService.getCurrentLoggedUser();
        currentLoggedUser.setFollowing(toFollowUser);
        toFollowUser.setFollower(currentLoggedUser);

        this.userRepository.saveAndFlush(currentLoggedUser);
        this.userRepository.saveAndFlush(toFollowUser);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/unfollow/{followedId}")
    public ModelAndView unfollowUser(@PathVariable(name = "followedId") String followedId,
                                     HttpServletRequest request) {

        User followedUser = this.userRepository.findById(followedId).get();
        User currentLoggedUser = this.userService.getCurrentLoggedUser();
        currentLoggedUser.removeFollowing(followedUser);
        followedUser.removeFollower(currentLoggedUser);

        this.userRepository.saveAndFlush(currentLoggedUser);
        this.userRepository.saveAndFlush(followedUser);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/followersList/{userId}")
    public ModelAndView listFollowers(@PathVariable(name = "userId") String userId,
                                      Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        User currentUser = this.userRepository.findById(userId).get();
        Set<User> allUsers =  currentUser.getFollowers();

        modelAndView.addObject("allUsers", allUsers);

        return super.view("discover", modelAndView);
    }


    @GetMapping("/followingList/{userId}")
    public ModelAndView listFollowing(@PathVariable(name = "userId") String userId,
                                      Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        User currentUser = this.userRepository.findById(userId).get();
        Set<User> allUsers =  currentUser.getFollowing();

        modelAndView.addObject("allUsers", allUsers);

        return super.view("discover", modelAndView);
    }
}
