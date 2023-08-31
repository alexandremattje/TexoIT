package com.texoit.alexandre.mattje.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Interval {

    private Integer previous;

    @Setter
    private Integer following;

    public Integer difference() {
        return following - previous;
    }

}