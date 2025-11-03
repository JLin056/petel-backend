SELECT
  t.id        AS THREAD_ID,
  t.order_id  AS ORDER_ID,
  o.status    AS ORDER_STATUS,
  p.name      AS DISPLAY_NAME,
  s.media_id AS MEDIA_ID
FROM PETEL_CHAT_THREADS t
LEFT JOIN PETEL_ORDERS         o  ON o.id = t.order_id
LEFT JOIN PETEL_PROPERTY     p  ON p.id = o.property_id
LEFT JOIN PETEL_SELLERS      s  ON s.id = p.seller_id
WHERE t.USER_ID = :accountId
ORDER BY t.id DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
