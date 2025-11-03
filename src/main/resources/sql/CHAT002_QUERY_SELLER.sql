SELECT
  t.id                       AS THREAD_ID,
  t.order_id                 AS ORDER_ID,
  o.status                   AS ORDER_STATUS,
  COALESCE(u.name, CAST(a.email AS NVARCHAR2(255))) AS DISPLAY_NAME,
  u.media_id AS MEDIA_ID
FROM PETEL_CHAT_THREADS t
LEFT JOIN PETEL_ORDERS         o  ON o.id = t.order_id
LEFT JOIN PETEL_USERS          u  ON u.id = o.user_id
LEFT JOIN PETEL_ACCOUNTS       a  ON a.id = t.user_id
WHERE t.SELLER_ID = :accountId
ORDER BY t.id DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
