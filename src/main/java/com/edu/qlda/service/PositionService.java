package com.edu.qlda.service;

import com.edu.qlda.entity.Position;
import com.edu.qlda.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<Position> listPositions() {
        return positionRepository.findAllPositions();
    }
}
