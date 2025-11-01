SELECT
    i.ID            AS "itemId",
    i.ROOM_ID       AS "roomId",
    r.NAME          AS "roomName",
    i.ARRIVAL_DATE  AS "arrivalDate",
    i.QUANTITY      AS "quantity",
    i.PRICE         AS "price"
FROM PETEL_ORDER_ITEMS i
JOIN PETEL_ROOMS r ON r.ID = i.ROOM_ID
WHERE i.ORDER_ID = :orderId
ORDER BY i.ARRIVAL_DATE, r.NAME, i.ID
