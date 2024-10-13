package com.helmo.greenThumb.infrastructures;


import com.helmo.greenThumb.model.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarietyRepository extends JpaRepository<Variety, Long> {
    // rien a mettre dedans, géré automatiquement(il me semble)
}

