package com.helmo.greenThumb.infrastructures;


import com.helmo.greenThumb.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    // rien a mettre dedans, géré automatiquement(il me semble)
}

