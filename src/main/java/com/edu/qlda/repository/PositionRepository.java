package com.edu.qlda.repository;
import com.edu.qlda.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position,Integer> {
    @Query(value = "select position_id, name  from position", nativeQuery = true)
    List<Position> findAllPositions();
}
