package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.service.ChirpService;
import org.chirper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;

    private final ChirpService chirpService;

    @Autowired
    public HomeController(UserService userService, ChirpService chirpService) {
        this.userService = userService;
        this.chirpService = chirpService;
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession sessionArg) {
        HttpSession session = (HttpSession) sessionArg.getAttribute("username");

        if (session != null) {
            return super.redirect("feed");
        } else {
            return super.redirect("login");
        }
    }

    @GetMapping("/feed")
    public ModelAndView home(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        User user = this.userService.getCurrentLoggedUser();

        List<Chirp> allChirps = this.chirpService.getAllByFollowingUsers(user.getId());

        modelAndView.addObject("allChirps", allChirps);
        modelAndView.addObject("user", user);

//        if (this.getPrincipalAuthority(authentication) != null
//                && this.getPrincipalAuthority(authentication).equals("ADMIN")){
//            return this.view("admin-home", modelAndView);
//        }

        return super.view("feed", modelAndView);
    }

    @GetMapping("/discover")
    public ModelAndView discover(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        List<User> allUsers = this.userService.getAll();

        modelAndView.addObject("allUsers", allUsers);

        return super.view("discover", modelAndView);
    }

    @GetMapping("/profile")
    public ModelAndView profile(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        User user = this.userService.getCurrentLoggedUser();

        modelAndView.addObject("user", user);

        return super.view("profile", modelAndView);
    }

    @GetMapping("/profile/{usernameArg}")
    public ModelAndView profile(@PathVariable("usernameArg") String usernameArg,
                                Authentication authentication,
                                ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        User user = (User) this.userService.loadUserByUsername(usernameArg);
        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        modelAndView.addObject("user", user);

        if (currentLoggedUser.getId().equals(user.getId())) {
            return super.view("profile", modelAndView);
        }

        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        return super.view("foreign_profile", modelAndView);
    }
}
