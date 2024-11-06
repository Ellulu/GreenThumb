package com.helmo.greenThumb.infrastructures;


import com.helmo.greenThumb.model.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VarietyRepository extends JpaRepository<Variety, Long> {
    Optional<Variety> findByVarietyName(String name);
}
