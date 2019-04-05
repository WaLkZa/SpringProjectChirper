package org.chirper.web.controllers;

import org.chirper.domain.entities.User;
import org.chirper.service.ChirpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LikeController extends BaseController {

    private final ChirpService chirpService;

    @Autowired
    public LikeController(ChirpService chirpService) {
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
                                      Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("loggedUsername", authentication.getName());

        List<User> likesUsers = this.chirpService.getChirpLikes(chirpId);

        modelAndView.addObject("likesUsers", likesUsers);

        return super.view("chirp/likes", modelAndView);
    }
}
