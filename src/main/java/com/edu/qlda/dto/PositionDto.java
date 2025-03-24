package com.edu.qlda.dto;

import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {
    @JoinColumn(name = "position_id")
    Integer positionId;
    String name;
}
