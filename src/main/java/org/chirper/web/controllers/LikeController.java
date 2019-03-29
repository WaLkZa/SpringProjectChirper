package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.repository.ChirpRepository;
import org.chirper.repository.UserRepository;
import org.chirper.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
public class LikeController extends BaseController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final ChirpRepository chirpRepository;

    public LikeController(UserService userService, UserRepository userRepository, ChirpRepository chirpRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.chirpRepository = chirpRepository;
    }

    @GetMapping("/chirp/like/{chirpId}")
    public ModelAndView likeAndUnlikeAChirp(@PathVariable(name = "chirpId") String chirpId,
                                   HttpServletRequest request) {

        Chirp currentChirp = this.chirpRepository.findById(chirpId).get();
        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        boolean isChirpLikeExist = currentLoggedUser.isChirpLikeExist(currentChirp);

        if (isChirpLikeExist) {
            currentLoggedUser.removeChirpLike(currentChirp);
        } else {
            currentLoggedUser.addChirpLike(currentChirp);
        }

        this.userRepository.saveAndFlush(currentLoggedUser);
        this.chirpRepository.saveAndFlush(currentChirp);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/chirp/listLikes/{chirpId}")
    public ModelAndView listLikes(@PathVariable(name = "chirpId") String chirpId,
                                      Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        Chirp currentChirp = this.chirpRepository.findById(chirpId).get();

        List<User> likesUsers = currentChirp.getUserLikes();
        modelAndView.addObject("likesUsers", likesUsers);

        return super.view("chirp/likes", modelAndView);
    }
}
