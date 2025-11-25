package org.example.backendai.constant;

public enum Department {
    NOI("Nội khoa"),
    NGOAI("Ngoại khoa"),
    SAN_PHUKHOA("Sản phụ khoa"),
    NHI("Nhi khoa"),
    RANHIEU("Răng hàm mặt"),
    MAT("Mắt"),
    TAI_MUI_HONG("Tai mũi họng"),
    TAMTHAN("Tâm thần"),
    UNG_BUOU("Ung bướu"),
    DA_LIEU("Da liễu"),
    YHCT("Y học cổ truyền"),
    CAPCUU("Cấp cứu");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}