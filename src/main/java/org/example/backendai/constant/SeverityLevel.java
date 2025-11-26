package org.example.backendai.constant;

import lombok.Getter;

@Getter
public enum SeverityLevel {
    NANG("Nặng", "nặng"),
    TRUNG_BINH("Trung bình", "trung bình"),
    NHE("Nhẹ", "nhẹ");

    private final String displayName;
    private final String aiLabel;

    SeverityLevel(String displayName, String aiLabel) {
        this.displayName = displayName;
        this.aiLabel = aiLabel;
    }

    public static SeverityLevel fromAiLabel(String aiLabel) {
        for (SeverityLevel level : values()) {
            if (level.aiLabel.equalsIgnoreCase(aiLabel)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid AI label: " + aiLabel);
    }
}