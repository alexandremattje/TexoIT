package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.dto.Winner;
import com.texoit.alexandre.mattje.dto.WinnerRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StartupDataTest {

    @Autowired
    private StartupDataService startupDataService;

    @Autowired
    private WinnerService winnerService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach () {
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "movies_producers", "movies_studios", "producer", "studio", "movie");
    }

    @Test
    public void loadInvalidFile() {
        try {
            this.startupDataService.importFile("src/test/resources/movielist-wrong-header.csv");
        } catch (Exception e) {
            Assertions.assertEquals("Header of src/test/resources/movielist-wrong-header.csv is invalid. Must be \"year;title;studios;producers;winner\"", e.getMessage());
        }
    }

    @Test
    public void loadValidFile() throws Exception {
        this.startupDataService.importFile("src/test/resources/movielist.csv");
        Assertions.assertEquals(206, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "movie"));

        WinnerRange winnerRange = this.winnerService.findWinnerRanger();
        Assertions.assertEquals(1, winnerRange.getMax().size());
        Assertions.assertEquals(Arrays.asList(
                Winner.builder().producer("Matthew Vaughn").previousWin(2002).followingWin(2015).interval(13).build()
        ), winnerRange.getMax());
        Assertions.assertEquals(1, winnerRange.getMin().size());
        Assertions.assertEquals(Arrays.asList(Winner.builder()
                .producer("Joel Silver").previousWin(1990).followingWin(1991).interval(1).build()
        ), winnerRange.getMin());
    }

}
