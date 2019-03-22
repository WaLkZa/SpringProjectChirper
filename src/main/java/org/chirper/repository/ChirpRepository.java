package org.chirper.repository;

import org.chirper.domain.entities.Chirp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, String> {
}