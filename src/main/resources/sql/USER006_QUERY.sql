SELECT
      o.ID            AS "orderId",
      p.NAME          AS "propertyName",
      o.CHECK_IN      AS "checkIn",
      o.CHECK_OUT     AS "checkOut",
      o.STATUS        AS "status",
      o.HOTEL_CHARGES AS "totalPrice",
      pay.NAME        AS "paymentName",

      /* 旅館整體平均分數（四捨五入到 1 位） */
      (
        SELECT ROUND(AVG((rv2.PRICE_SCORE + rv2.ENV_SCORE + rv2.SERVICE_SCORE)/3), 1)
        FROM PETEL_REVIEWS rv2
        WHERE rv2.PROPERTY_ID = o.PROPERTY_ID
      )  AS "propertyAvg",

      /* 旅館首圖（SORT_ORDER 最小) */
      (
            SELECT pi.MEDIA_ID
            FROM PETEL_PROPERTY_IMAGE pi
            WHERE pi.PROPERTY_ID = o.PROPERTY_ID
            ORDER BY pi.SORT_ORDER NULLS LAST, pi.MEDIA_ID
            FETCH FIRST 1 ROW ONLY
      )  AS "mediaId",

      /* 此訂單是否已有評論 */
      CASE WHEN EXISTS (SELECT 1 FROM PETEL_REVIEWS rv WHERE rv.ORDER_ID = o.ID)
           THEN 'true' ELSE 'false'
      END AS "hasReview"

FROM PETEL_ORDERS o
JOIN PETEL_PROPERTY p ON p.ID = o.PROPERTY_ID
JOIN PETEL_PAYMENT  pay ON pay.ID = o.PAYMENT_ID
WHERE o.USER_ID = :userId
[ AND o.STATUS = :status ]
[ AND o.CHECK_OUT >= :from ]
[ AND o.CHECK_IN  <= :to   ]
ORDER BY
      CASE WHEN :sortMode = 'CHECKIN_ASC'  THEN o.CHECK_IN   END ASC,
      CASE WHEN :sortMode = 'CHECKIN_DESC' THEN o.CHECK_IN   END DESC,
      CASE WHEN :sortMode = 'CREATED_ASC'  THEN o.CREATED_AT END ASC,
      CASE WHEN :sortMode = 'CREATED_DESC' THEN o.CREATED_AT END DESC,
      o.ID DESC
[ OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY ]
