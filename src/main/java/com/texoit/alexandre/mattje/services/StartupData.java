package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.model.Movie;
import com.texoit.alexandre.mattje.model.Producer;
import com.texoit.alexandre.mattje.model.Studio;
import com.texoit.alexandre.mattje.repositories.MovieRepository;
import com.texoit.alexandre.mattje.repositories.StudioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StartupData {

    private final String FILE_HEADER = "year;title;studios;producers;winner";
    private final String SPLIT_PARAM = ",| and ";
    private final MovieService movieService;
    private final StudioService studioService;
    private final ProducerService producerService;

    @Autowired
    public StartupData (MovieService movieService, StudioService studioService, ProducerService producerService) {
        this.movieService = movieService;
        this.studioService = studioService;
        this.producerService = producerService;
    }

    public void importFile(String fileName) {
        try {
            File file = ResourceUtils.getFile(String.format("%s", fileName));
            List<String> dataLines = Files.readAllLines(file.toPath());
            if (fileIsValid(dataLines.get(0))) {
                dataLines.remove(0);
                dataLines.forEach(this::importLine);
            } else {
                log.error("Header of {} is invalid. Must be \"{}\"", fileName, FILE_HEADER);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void importLine(String line) {
        String[] columns = line.split(";");
        Movie movie = Movie.builder()
                .year(Integer.valueOf(columns[0].trim()))
                .title(columns[1])
                .studios(extractStudios(columns[2]))
                .producers(extractProducers(columns[3]))
                .winner(columns.length == 5)
                .build();
        this.movieService.save(movie);
    }

    private Set<Producer> extractProducers(String producers) {
        List<String> list = Arrays.asList(producers.split(SPLIT_PARAM));
        return list.stream().filter(it -> !it.trim().isEmpty()).map(this.producerService::findOrCreateProducer).collect(Collectors.toSet());
    }

    private Set<Studio> extractStudios(String studios) {
        List<String> list = Arrays.asList(studios.split(SPLIT_PARAM));
        return list.stream().filter(it -> !it.trim().isEmpty()).map(this.studioService::findOrCreateStudio).collect(Collectors.toSet());
    }

    private boolean fileIsValid(String firstLine) {
        return FILE_HEADER.equals(firstLine);
    }

}
