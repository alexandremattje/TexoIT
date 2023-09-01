package com.texoit.alexandre.mattje.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name="producer")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(targetEntity = Movie.class, mappedBy = "producers")
    private Set<Movie> movies;

}
