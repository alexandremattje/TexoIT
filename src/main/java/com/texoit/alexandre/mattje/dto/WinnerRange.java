package com.texoit.alexandre.mattje.dto;

import jakarta.persistence.NamedAttributeNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WinnerRange {

    private List<Winner> min;
    private List<Winner> max;

}
