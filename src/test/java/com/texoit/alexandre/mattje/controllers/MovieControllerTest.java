package com.texoit.alexandre.mattje.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.texoit.alexandre.mattje.dto.WinnerRange;
import com.texoit.alexandre.mattje.model.Movie;
import com.texoit.alexandre.mattje.model.Producer;
import com.texoit.alexandre.mattje.model.Studio;
import com.texoit.alexandre.mattje.services.MovieService;
import com.texoit.alexandre.mattje.services.ProducerService;
import com.texoit.alexandre.mattje.services.StudioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {

    private final String BASE_URL = "/movies";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private StudioService studioService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach () {
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "movies_producers", "movie_studios", "producer", "studio", "movie");
    }

    @Test
    public void noMinOrMax() throws JsonProcessingException {
        Set<Producer> producers = new HashSet<>();
        producers.add(this.producerService.findOrCreateProducer("Producer 1"));
        Set<Studio> studios = new HashSet<>();
        studios.add(this.studioService.findOrCreateStudio("Studio 1"));
        this.movieService.save(Movie.builder()
                        .winner(true)
                        .title("2000 winner")
                        .producers(producers)
                        .studios(studios)
                        .year(2000)
                .build());
        ResponseEntity<String> response =
                this.testRestTemplate.getForEntity(BASE_URL + "/winner_range", String.class);

        ObjectMapper mapper = new ObjectMapper();
        WinnerRange winnerRange = mapper.reader()
                .forType(WinnerRange.class)
                .readValue(response.getBody());
        Assertions.assertEquals(0, winnerRange.getMax().size());
        Assertions.assertEquals(0, winnerRange.getMin().size());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void sameMinAndMaxWithOneYear() throws JsonProcessingException {
        Set<Producer> producers = new HashSet<>();
        producers.add(this.producerService.findOrCreateProducer("Producer 1"));
        Set<Producer> producersLoosers = new HashSet<>();
        producersLoosers.add(this.producerService.findOrCreateProducer("Producer looser 1"));
        Set<Studio> studios = new HashSet<>();
        studios.add(this.studioService.findOrCreateStudio("Studio 1"));
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2000 winner")
                .producers(producers)
                .studios(studios)
                .year(2000)
                .build());
        this.movieService.save(Movie.builder()
                .winner(false)
                .title("2000 looser")
                .producers(producersLoosers)
                .studios(studios)
                .year(2000)
                .build());
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2001 winner")
                .producers(producers)
                .studios(studios)
                .year(2001)
                .build());
        ResponseEntity<String> response =
                this.testRestTemplate.getForEntity(BASE_URL + "/winner_range", String.class);

        ObjectMapper mapper = new ObjectMapper();
        WinnerRange winnerRange = mapper.reader()
                .forType(WinnerRange.class)
                .readValue(response.getBody());
        Assertions.assertEquals(1, winnerRange.getMax().size());
        Assertions.assertEquals(1, winnerRange.getMin().size());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
