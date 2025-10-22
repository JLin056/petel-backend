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
  )               AS "propertyAvg",

  /* 旅館首圖（SORT_ORDER 最小) */
  (
    SELECT :s3Base || s3.BUCKET || '/' || s3.OBJECT_KEY
    FROM PETEL_PROPERTY_IMAGE pi
    JOIN PETEL_MEDIA_S3 s3 ON s3.MEDIA_ID = pi.MEDIA_ID
    WHERE pi.PROPERTY_ID = o.PROPERTY_ID
      AND pi.SORT_ORDER = (
        SELECT MIN(pi2.SORT_ORDER)
        FROM PETEL_PROPERTY_IMAGE pi2
        WHERE pi2.PROPERTY_ID = o.PROPERTY_ID
      )
  )               AS "imageUrl",

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
ORDER BY o.CREATED_AT DESC
[ OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY ]
