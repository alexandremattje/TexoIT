package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.dto.ProducerWinner;
import com.texoit.alexandre.mattje.dto.WinnerRange;
import com.texoit.alexandre.mattje.model.Movie;
import com.texoit.alexandre.mattje.model.Producer;
import com.texoit.alexandre.mattje.repositories.ProducerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WinnerService {

    private final ProducerRepository movieService;

    public WinnerService(ProducerRepository movieService) {
        this.movieService = movieService;
    }

    public WinnerRange findWinnerRanger() {
        List<ProducerWinner> allWinners = movieService.getAllProducerWithWinnerMovies();
        allWinners.forEach(it -> System.out.println(it.toString()));
        return WinnerRange.builder().build();
    }
    
}
