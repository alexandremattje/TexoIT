package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.model.Movie;
import com.texoit.alexandre.mattje.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void save (Movie movie) {
        this.movieRepository.save(movie);
    }

    public List<Movie> findAllWinners() {
        return this.movieRepository.findByWinnerTrueOrderByYear();
    }
}
