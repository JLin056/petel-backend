package com.example.petel.model;

public enum ReturnCodeAndDescEnum {
    SUCCESS("0000", "交易成功"),
    ERROR_INPUT("E001", "必填欄位不完整"),
    UPDATE_FAIL("E002", "更新失敗"),
    INSERT_FAIL("E003", "新增失敗"),
    DELETE_FAIL("E004", "刪除失敗"),
    DATA_NOT_FOUND("E702", "查無資料"),
    S9999("9999", "其他系統異常"),
    // 驗證與權限相關
    UNAUTHORIZED("E401", "未登入或 JWT 無效"),
    FORBIDDEN("E403", "權限不足");



    private String code;

    private String desc;

    private ReturnCodeAndDescEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}