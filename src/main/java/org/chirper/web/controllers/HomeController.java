package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.view.AllUsersViewModel;
import org.chirper.domain.models.view.ForeignUserProfileViewModel;
import org.chirper.repository.ChirpRepository;
import org.chirper.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;

    private final ChirpRepository chirpRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, ChirpRepository chirpRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.chirpRepository = chirpRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession sessionArg) {
        HttpSession session = (HttpSession) sessionArg.getAttribute("username");

        if (session != null) {
            return this.redirect("feed");
        } else {
            return this.redirect("login");
        }
    }

    @GetMapping("/feed")
    public ModelAndView home(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        List<Chirp> allChirps = this.chirpRepository.findAll();

        modelAndView.addObject("allChirps", allChirps);

//        if (this.getPrincipalAuthority(authentication) != null
//                && this.getPrincipalAuthority(authentication).equals("ADMIN")){
//            return this.view("admin-home", modelAndView);
//        }

        return this.view("feed", modelAndView);
    }

    @GetMapping("/discover")
    public ModelAndView discover(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        Set<AllUsersViewModel> allUsersViewModel = this
                .userService
                .getAll()
                .stream()
                .map(x -> this.modelMapper.map(x, AllUsersViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        modelAndView.addObject("allUsers", allUsersViewModel);

        return this.view("discover", modelAndView);
    }

    @GetMapping("/profile")
    public ModelAndView profile(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User author = (User) this.userService.loadUserByUsername(userDetails.getUsername());

        List<Chirp> myChirps = this.chirpRepository.findAllByAuthorId(author.getId());

        modelAndView.addObject("myChirps", myChirps);
        modelAndView.addObject("author", author);

        return this.view("profile", modelAndView);
    }

    @GetMapping("/profile/{usernameArg}")
    public ModelAndView profile(@PathVariable("usernameArg") String usernameArg,
                                Authentication authentication,
                                ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());


        UserDetails user = this.userService.loadUserByUsername(usernameArg);

        ForeignUserProfileViewModel foreignUserProfileViewModel =
                this.modelMapper.map(user, ForeignUserProfileViewModel.class);

        modelAndView.addObject("user", foreignUserProfileViewModel);


        return this.view("foreign_profile", modelAndView);
    }
}
