-- 刪除訂單相關資料
-- 注意：由於有外鍵關聯，需要先刪除 ORDER_ITEMS，再刪除 ORDERS

-- 刪除訂單明細
DELETE FROM PETEL_ORDER_ITEMS;

-- 刪除訂單
DELETE FROM PETEL_ORDERS;

-- 確認刪除
COMMIT;
