package org.chirper.service;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface ChirpService {
    void likeAndUnlikeAChirp(String chirpId);

    List<User> getChirpLikes(String chirpId);

    List<Chirp> getAllByFollowingUsers(String currentUserId);

    void createChirp(@ModelAttribute ChirpCreateBindingModel chirpCreateBindingModel);
}
