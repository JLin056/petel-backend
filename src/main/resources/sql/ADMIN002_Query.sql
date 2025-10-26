SELECT
    s.ID AS SELLER_ID,
    a.ID AS ACCOUNT_ID,
    a.EMAIL,
    s.NAME,
    s.PHONE,
    a.ROLE,
    a.STATUS
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
 ORDER BY s.ID
 [OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY]
