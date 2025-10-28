SELECT
  t.id                       AS thread_id,
  t.order_id                 AS order_id,
  COALESCE(u.name, CAST(a.email AS NVARCHAR2(255))) AS display_name
FROM PETEL_CHAT_THREADS t
JOIN PETEL_USERS   u ON u.account_id = t.USER_ID
LEFT JOIN PETEL_ACCOUNTS a ON a.id = u.account_id
WHERE t.SELLER_ID = :accountId
ORDER BY t.id DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
