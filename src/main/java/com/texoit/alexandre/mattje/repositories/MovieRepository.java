package com.texoit.alexandre.mattje.repositories;

import com.texoit.alexandre.mattje.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findByWinnerTrueOrderByYear();
}
