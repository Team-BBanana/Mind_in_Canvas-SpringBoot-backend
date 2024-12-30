package com.example.Mind_in_Canvas.domain.report;

import com.example.Mind_in_Canvas.domain.gallery.drawing.Drawing;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "robot_report")
@Getter
public class RobotReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID robotReportId;    // UUID 형식의 고유 식별자

    @ManyToOne(fetch = FetchType.LAZY)  // 드로잉과 다대일 관계, 지연 로딩 사용
    @JoinColumn(name = "drawing_id", columnDefinition = "BINARY(16)")    // 외래키로 drawing_id 사용
    private Drawing drawing;            // 관련된 드로잉 엔티티 참조

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;             // 요약 내용을 저장하는 TEXT 타입 필드

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;            // 상세 내용을 저장하는 TEXT 타입 필드

    @Column(name = "created_at")
    private LocalDateTime createdAt;    // 생성 시간을 저장하는 timestamp 필드

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}