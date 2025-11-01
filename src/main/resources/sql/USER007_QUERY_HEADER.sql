SELECT
    o.ID              AS "orderId",
    o.STATUS          AS "status",
    o.CHECK_IN        AS "checkIn",
    o.CHECK_OUT       AS "checkOut",
    o.HOTEL_CHARGES   AS "hotelCharges",

    o.PAYMENT_ID      AS "paymentId",
    pay.NAME          AS "paymentName",

    p.ID              AS "propertyId",
    p.NAME            AS "propertyName",
    p.TEL             AS "propertyTel",
    p.ADDRESS         AS "propertyAddress",

    /* 旅館首圖（SORT_ORDER 最小） */
    (
        SELECT pi.MEDIA_ID
        FROM PETEL_PROPERTY_IMAGE pi
        WHERE pi.PROPERTY_ID = p.ID
        ORDER BY pi.SORT_ORDER NULLS LAST, pi.MEDIA_ID
        FETCH FIRST 1 ROW ONLY
    )                 AS "propertyMediaId",

    o.GUEST           AS "guest",
    o.GUEST_NAME      AS "guestName",
    o.GUEST_PHONE     AS "guestPhone",

    o.NOTE            AS "note"

FROM PETEL_ORDERS o
JOIN PETEL_PROPERTY p ON p.ID = o.PROPERTY_ID
LEFT JOIN PETEL_PAYMENT pay ON pay.ID = o.PAYMENT_ID
WHERE o.ID = :orderId
