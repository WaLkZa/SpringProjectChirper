package org.chirper.repository;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, String> {
    List<Chirp> findAllByAuthorOrderByDateAddedDesc(User user);

    List<Chirp> findAllByOrderByDateAddedDesc();

    @Query(value = "SELECT c.*, u2.username FROM users AS u " +
            "INNER JOIN followers AS f ON f.follower_id = u.id " +
            "INNER JOIN users as u2 ON u2.id = f.followed_id " +
            "INNER JOIN chirps AS c ON c.author_id = u2.id " +
            "WHERE u.id = ?1 " +
            "ORDER BY c.date_added DESC", nativeQuery = true)
    List<Chirp> findAllByFollowedUsers(String currentUserId);
}