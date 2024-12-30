package com.example.Mind_in_Canvas.domain.gallery.drawing;

import com.example.Mind_in_Canvas.dto.gallery.DrawingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, UUID> {
    List<Drawing> findAllByImageImageId(UUID imageId);
}