package org.chirper.web.controllers;

import org.chirper.domain.entities.User;
import org.chirper.service.ChirpService;
import org.chirper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LikeController extends BaseController {

    private final UserService userService;

    private final ChirpService chirpService;

    @Autowired
    public LikeController(UserService userService, ChirpService chirpService) {
        this.userService = userService;
        this.chirpService = chirpService;
    }

    @GetMapping("/chirp/like/{chirpId}")
    public ModelAndView likeAndUnlikeAChirp(@PathVariable(name = "chirpId") String chirpId,
                                   HttpServletRequest request) {

        this.chirpService.likeAndUnlikeAChirp(chirpId);

        return super.redirect(request.getHeader("Referer"));
    }

    @GetMapping("/chirp/listLikes/{chirpId}")
    public ModelAndView listLikes(@PathVariable(name = "chirpId") String chirpId,
                                      ModelAndView modelAndView) {

        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        List<User> likesUsers = this.chirpService.getChirpLikes(chirpId);

        modelAndView.addObject("likesUsers", likesUsers);
        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        return super.view("chirp/likes", modelAndView);
    }
}
