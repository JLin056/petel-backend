SELECT
    s.ID AS SELLER_ID,
    a.ID AS ACCOUNT_ID,
    a.EMAIL,
    s.NAME,
    s.BUSINESS_CODE,
    a.ROLE,
    a.STATUS
 FROM
    PETEL_SELLERS s
 LEFT JOIN
    PETEL_ACCOUNTS a ON s.ACCOUNT_ID = a.ID
 WHERE
    a.ROLE = 'SELLER'
    [AND s.ID = :sellerId]
    [AND a.ID = :accountId]
    [AND a.EMAIL = :email]
    [AND s.NAME LIKE :name]
    [AND s.BUSINESS_CODE = :businessCode]
 ORDER BY s.ID
 [OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY]
