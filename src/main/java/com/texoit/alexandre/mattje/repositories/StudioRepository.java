package com.texoit.alexandre.mattje.repositories;

import com.texoit.alexandre.mattje.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    Studio findByName(String name);

}
