SELECT
    rm.ID as ROOM_ID,
    rm.NAME as ROOM_NAME,
    rm.INFO as ROOM_INFO,
    rm.ROOM_SIZE as ROOM_SIZE,
    rm.PET_TYPE_ID as PET_TYPE_ID,
    -- 如果有 inventory 記錄用 AVAILABLE_QTY，否則用 TOTAL_UNITS
    CASE
        WHEN COUNT(ri.ROOM_ID) > 0 THEN MIN(ri.AVAILABLE_QTY)
        ELSE rm.TOTAL_UNITS
    END as AVAILABLE_QTY,
    -- 如果有 inventory 記錄用 PRICE，否則用 BASE_PRICE
    CASE
        WHEN COUNT(ri.ROOM_ID) > 0 THEN MIN(ri.PRICE)
        ELSE rm.BASE_PRICE
    END as PRICE
FROM PETEL_ROOMS rm
LEFT JOIN PETEL_ROOM_INVENTORIES ri
    ON rm.ID = ri.ROOM_ID
    [AND ri.STAY_DATE >= :checkIn]
    [AND ri.STAY_DATE < :checkOut]
WHERE rm.PROPERTY_ID = :propertyId
  AND rm.PET_TYPE_ID IN (:petTypeList)
GROUP BY rm.ID, rm.NAME, rm.INFO, rm.ROOM_SIZE, rm.PET_TYPE_ID, rm.TOTAL_UNITS, rm.BASE_PRICE
ORDER BY rm.ID
