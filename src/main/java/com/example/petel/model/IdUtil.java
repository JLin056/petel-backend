package com.example.petel.model;

public class IdUtil {

    /**
     * 生成表格的 ID (Primary Key)
     *
     * @param letter 英文字母
     * @param lastId 表格既有的最大 ID
     * @return 表格的 ID，例如："A000000001"
     */
    public static String generateTableId(String letter, String lastId) {

        if (lastId == null || lastId.isEmpty()) {
            return letter + "000000001";
        }
        return letter + String.format("%09d", Integer.parseInt(lastId.substring(1)) + 1);

    }

    /**
     * 表格的 ID 數值部分加一，適用於多筆資料要新增至資料庫
     *
     * @param id 表格的 ID，例如："A000000001"
     * @return 表格的 ID 數值部分加一，呈上輸入，則輸出："A000000002"
     */
    public static String tableIdIncrement(String id) {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID 不可為空");
        }

        return id.charAt(0) + String.format("%09d", Integer.parseInt(id.substring(1)) + 1);

    }
}
