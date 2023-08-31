package com.texoit.alexandre.mattje.controllers;

import com.texoit.alexandre.mattje.dto.WinnerRange;
import com.texoit.alexandre.mattje.services.MovieService;
import com.texoit.alexandre.mattje.services.WinnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("movies")
public class MovieController {

    private final WinnerService winnerService;

    public MovieController(WinnerService winnerService) {
        this.winnerService = winnerService;
    }
    
    @GetMapping("winner_range")
    public WinnerRange winnerRange() {
        return this.winnerService.findWinnerRanger();
    }

}
