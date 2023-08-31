package com.texoit.alexandre.mattje.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WinnerRange {

    private List<Winner> min;
    private List<Winner> max;

}
