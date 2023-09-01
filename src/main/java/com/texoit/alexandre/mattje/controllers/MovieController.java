package com.texoit.alexandre.mattje.controllers;

import com.texoit.alexandre.mattje.dto.WinnerRange;
import com.texoit.alexandre.mattje.services.WinnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
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
