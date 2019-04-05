package org.chirper.service;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.chirper.repository.ChirpRepository;
import org.chirper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChirpServiceImpl implements ChirpService {
    private final UserService userService;

    private final UserRepository userRepository;

    private final ChirpRepository chirpRepository;

    @Autowired
    public ChirpServiceImpl(UserService userService, UserRepository userRepository, ChirpRepository chirpRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.chirpRepository = chirpRepository;
    }

    @Override
    public void likeAndUnlikeAChirp(String chirpId) {
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
    }

    @Override
    public List<User> getChirpLikes(String chirpId) {
        Chirp currentChirp = this.chirpRepository.findById(chirpId).get();

        List<User> likesUsers = currentChirp.getUserLikes();

        return likesUsers;
    }

    @Override
    public List<Chirp> getAllByFollowingUsers(String currentUserId) {
        List<Chirp> allChirps = this.chirpRepository.findAllByFollowedUsers(currentUserId);

        return allChirps;
    }

    @Override
    public void createChirp(ChirpCreateBindingModel chirpCreateBindingModel) {
        User author = this.userService.getCurrentLoggedUser();

        Chirp chirp = new Chirp(
                chirpCreateBindingModel.getContent(),
                LocalDateTime.now(),
                author);

        this.chirpRepository.saveAndFlush(chirp);
        this.userRepository.saveAndFlush(author);
    }
}
