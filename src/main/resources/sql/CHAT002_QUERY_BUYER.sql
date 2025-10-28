SELECT
  t.id        AS thread_id,
  t.order_id  AS order_id,
  p.name      AS display_name
FROM PETEL_CHAT_THREADS t
LEFT JOIN PETEL_ORDERS   o ON o.id = t.order_id
LEFT JOIN PETEL_PROPERTY p ON p.id = o.property_id
WHERE t.USER_ID = :accountId
ORDER BY t.id DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
