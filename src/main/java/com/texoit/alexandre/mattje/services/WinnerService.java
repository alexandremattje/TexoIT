package com.texoit.alexandre.mattje.services;

import com.texoit.alexandre.mattje.dto.Interval;
import com.texoit.alexandre.mattje.dto.ProducerWinner;
import com.texoit.alexandre.mattje.dto.Winner;
import com.texoit.alexandre.mattje.dto.WinnerRange;
import com.texoit.alexandre.mattje.repositories.ProducerRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WinnerService {

    private final ProducerRepository movieService;

    public WinnerService(ProducerRepository movieService) {
        this.movieService = movieService;
    }

    public WinnerRange findWinnerRanger() {

        List<ProducerWinner> allWinners = movieService.getAllProducerWithWinnerMovies();
        Map<String, List<Interval>> producerMapped = allWinners.stream().reduce(
                new HashMap<String, List<Interval>>(),
                (map, winner) -> {
                    List<Interval> intervals = map.get(winner.getProducer());
                    if (intervals == null) {
                        intervals = new ArrayList<>();
                        intervals.add (new Interval(winner.getYear(), 0));
                    } else {
                        Interval interval = intervals.get(intervals.size() - 1);
                        interval.setFollowing(winner.getYear());
                        intervals.add (new Interval(winner.getYear(), 0));
                    }
                    map.put(winner.getProducer(), intervals);
                    return map;
                }, (q, w) -> q)
                    // filter invalid entries need to have at least 2 Interval
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            entry -> entry.getValue().stream()
                                    // this filter is to remove all interval without following year
                                    .filter(it -> it.getFollowing() > 0).collect(Collectors.toList())));

        return WinnerRange.builder()
                .min(extractMinWinner(producerMapped))
                .max(extractMaxWinner(producerMapped))
                .build();
    }

    private List<Winner> extractMaxWinner(Map<String, List<Interval>> producerMapped) {
        int max = producerMapped.values().stream().reduce(0, (minFromMap, yearList) ->
                yearList.stream().reduce(minFromMap, (minFromList, interval) -> {
                    if (interval.difference() > minFromList) {
                        return interval.difference();
                    }
                    return minFromList;
                }, (a, b) -> a), (a, b) -> a);
        return getWinners(producerMapped, max);
    }

    private List<Winner> extractMinWinner(Map<String, List<Interval>> producerMapped) {
        int min = producerMapped.values().stream().reduce(99, (minFromMap, yearList) ->
                yearList.stream().reduce(minFromMap, (minFromList, interval) -> {
                    if (interval.difference() < minFromList) {
                        return interval.difference();
                    }
                    return minFromList;
                }, (a, b) -> a), (a, b) -> a);
        return getWinners(producerMapped, min);
    }

    private List<Winner> getWinners(Map<String, List<Interval>> producerMapped, int differenceToFind) {
        return producerMapped.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(it -> it.difference() == differenceToFind))
                .map(entry -> {
                    List<Winner> winners = new ArrayList<>();
                    entry.getValue().forEach(it -> {
                        winners.add(
                                Winner.builder()
                                        .interval(it.difference())
                                        .producer(entry.getKey())
                                        .previousWin(it.getPrevious())
                                        .followingWin(it.getFollowing())
                                        .build());
                    });
                    return winners;
                }).reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

}
