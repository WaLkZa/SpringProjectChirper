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
//    @Query(value = "SELECT * FROM chirps AS c WHERE c.author_id = ?1 ORDER BY c.date_added DESC", nativeQuery = true)
//    List<Chirp> findAllByAuthorId(String userId);

    List<Chirp> findAllByAuthorOrderByDateAddedDesc(User user);

    List<Chirp> findAllByOrderByDateAddedDesc();

//    @Query(value = "SELECT * FROM chirps AS c ORDER BY c.date_added DESC", nativeQuery = true)
//    List<Chirp> findAllChirps();

//    @Query("SELECT c FROM Chirp c WHERE c.author = :authorId ORDER BY c.dateAdded DESC")
//    List<Chirp> findAllByAuthorId(@Param("authorId") String authorId);

//    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author ORDER BY p.date DESC")
//    List<Post> findLatest5Posts(Pageable pageable);
}