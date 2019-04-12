package org.chirper.web.controllers;

import org.chirper.service.ChirpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LikeController extends BaseController {

    private final ChirpService chirpService;

    @Autowired
    public LikeController(ChirpService chirpService) {
        this.chirpService = chirpService;
    }

    @GetMapping("/chirp/like/{chirpId}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView likeAndUnlikeAChirp(@PathVariable(name = "chirpId") String chirpId,
                                   HttpServletRequest request) {

        this.chirpService.likeAndUnlikeAChirp(chirpId);

        return super.redirect(request.getHeader("Referer"));
    }
}
