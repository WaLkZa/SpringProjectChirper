package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.service.ChirpService;
import org.chirper.service.UserService;
import org.chirper.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView index() {
        return super.redirect("login");
    }

    @GetMapping("/feed")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Feed")
    public ModelAndView home(ModelAndView modelAndView) {
        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        List<Chirp> allChirps = this.chirpService.getAllByFollowingUsers(currentLoggedUser.getId());

        modelAndView.addObject("allChirps", allChirps);
        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        return super.view("feed", modelAndView);
    }
}
