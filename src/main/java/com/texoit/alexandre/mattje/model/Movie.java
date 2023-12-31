package com.texoit.alexandre.mattje.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Entity(name="movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="year_")
    private Integer year;

    private String title;

    @ManyToMany(targetEntity = Studio.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "movies_studios",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "studios_id")
    )
    private Set<Studio> studios;

    @ManyToMany(targetEntity = Producer.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "movies_producers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id")
    )
    private Set<Producer> producers;

    private Boolean winner;

}
