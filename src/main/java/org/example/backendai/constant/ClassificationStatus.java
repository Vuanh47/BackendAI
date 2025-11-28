package org.example.backendai.constant;

public enum ClassificationStatus {
    ACTIVE("Đang hiển thị"),
    REVIEWED("Đã xem xét");

    private final String displayName;

    ClassificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}