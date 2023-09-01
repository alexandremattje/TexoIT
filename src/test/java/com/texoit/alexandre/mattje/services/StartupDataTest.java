package com.texoit.alexandre.mattje.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StartupDataTest {

    @Autowired
    private StartupDataService startupDataService;

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
    }

}
