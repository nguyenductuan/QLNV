package com.edu.qlda.service;



import com.edu.qlda.entity.Position;
import com.edu.qlda.repository.PostionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    private final PostionRepository postionRepository;

    public PositionService(PostionRepository postionRepository) {

        this.postionRepository = postionRepository;
    }
    public List<Position> listPosition(){
        return postionRepository.findAllPostion();
    }
}
