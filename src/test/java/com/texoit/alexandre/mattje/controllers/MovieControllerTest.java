package com.texoit.alexandre.mattje.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.texoit.alexandre.mattje.dto.Winner;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Arrays;
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
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "movies_producers", "movies_studios", "producer", "studio", "movie");
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
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        WinnerRange winnerRange = mapper.reader()
                .forType(WinnerRange.class)
                .readValue(response.getBody());
        Assertions.assertEquals(0, winnerRange.getMax().size());
        Assertions.assertEquals(0, winnerRange.getMin().size());
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
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        WinnerRange winnerRange = mapper.reader()
                .forType(WinnerRange.class)
                .readValue(response.getBody());
        Assertions.assertEquals(1, winnerRange.getMax().size());
        Assertions.assertEquals(1, winnerRange.getMin().size());
    }

    @Test
    public void oneMinAndTwoMax() throws JsonProcessingException {
        Set<Producer> producersLoosers = new HashSet<>();
        producersLoosers.add(this.producerService.findOrCreateProducer("Producer looser 1"));
        Set<Studio> studios = new HashSet<>();
        studios.add(this.studioService.findOrCreateStudio("Studio 1"));
        this.movieService.save(Movie.builder()
                .winner(false)
                .title("2000 looser")
                .producers(producersLoosers)
                .studios(studios)
                .year(2000)
                .build());
        Set<Producer> producersMin = new HashSet<>();
        producersMin.add(this.producerService.findOrCreateProducer("Producer min 1"));
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2000 winner")
                .producers(producersMin)
                .studios(studios)
                .year(2000)
                .build());
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2001 winner")
                .producers(producersMin)
                .studios(studios)
                .year(2001)
                .build());
        Set<Producer> producersMax = new HashSet<>();
        producersMax.add(this.producerService.findOrCreateProducer("Producer max 1"));
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2002 winner")
                .producers(producersMax)
                .studios(studios)
                .year(2002)
                .build());
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2005 winner")
                .producers(producersMax)
                .studios(studios)
                .year(2005)
                .build());
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("2008 winner")
                .producers(producersMax)
                .studios(studios)
                .year(2008)
                .build());
        this.movieService.save(Movie.builder()
                .winner(true)
                .title("20118 winner")
                .producers(producersMax)
                .studios(studios)
                .year(2011)
                .build());
        ResponseEntity<String> response =
                this.testRestTemplate.getForEntity(BASE_URL + "/winner_range", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        WinnerRange winnerRange = mapper.reader()
                .forType(WinnerRange.class)
                .readValue(response.getBody());
        Assertions.assertEquals(3, winnerRange.getMax().size());
        Assertions.assertEquals(Arrays.asList(
                Winner.builder().producer("Producer max 1").previousWin(2002).followingWin(2005).interval(3).build(),
                Winner.builder().producer("Producer max 1").previousWin(2005).followingWin(2008).interval(3).build(),
                Winner.builder().producer("Producer max 1").previousWin(2008).followingWin(2011).interval(3).build()
        ), winnerRange.getMax());
        Assertions.assertEquals(1, winnerRange.getMin().size());
        Assertions.assertEquals(Arrays.asList(Winner.builder()
                        .producer("Producer min 1")
                        .previousWin(2000)
                        .followingWin(2001)
                        .interval(1)
                .build()), winnerRange.getMin());
    }

}
