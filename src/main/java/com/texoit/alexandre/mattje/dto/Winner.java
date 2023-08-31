package com.texoit.alexandre.mattje.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Winner {

    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;

}
