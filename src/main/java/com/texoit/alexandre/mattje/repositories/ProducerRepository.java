package com.texoit.alexandre.mattje.repositories;

import com.texoit.alexandre.mattje.dto.ProducerWinner;
import com.texoit.alexandre.mattje.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Producer findByName(String name);

    @Query("SELECT new com.texoit.alexandre.mattje.dto.ProducerWinner(p.name, m.year) FROM producer p JOIN p.movies m WHERE m.winner ORDER BY p.name")
    List<ProducerWinner> getAllProducerWithWinnerMovies();
}
