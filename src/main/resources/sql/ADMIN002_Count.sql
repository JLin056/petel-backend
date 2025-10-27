SELECT COUNT(*) AS TOTAL_COUNT
FROM
    PETEL_SELLERS s
LEFT JOIN
    PETEL_ACCOUNTS a ON s.ACCOUNT_ID = a.ID
WHERE
    (a.ROLE = 'SELLER' or a.ROLE = 'seller')
    [AND s.ID LIKE :sellerId]
    [AND a.ID LIKE :accountId]
    [AND a.EMAIL LIKE :email]
    [AND s.NAME LIKE :name]
    [AND s.PHONE LIKE :phone]
