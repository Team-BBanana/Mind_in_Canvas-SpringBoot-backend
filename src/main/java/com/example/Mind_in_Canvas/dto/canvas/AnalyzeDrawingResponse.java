package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyzeDrawingResponse {
    private int status; // 응답 상태 코드
    private Data data;  // 응답 데이터

    @Getter
    @Setter
    public static class Data {
        private String drawingId; // UUID 형식의 그림 ID
        private AiFeedback aiFeedback; // AI 피드백

        @Getter
        @Setter
        public static class AiFeedback {
            private String text; // AI 텍스트 답변
            private String audio; // TTS로 변환된 음성 데이터
        }
    }
}
