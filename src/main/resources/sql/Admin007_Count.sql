SELECT COUNT(*) AS TOTAL_COUNT
FROM
    PETEL_USERS u
LEFT JOIN
    PETEL_ACCOUNTS a ON u.ACCOUNT_ID = a.ID
WHERE
    a.ROLE = 'USER' or a.ROLE = 'user'
    [AND a.ID LIKE :accountId]
    [AND a.EMAIL LIKE :email]
    [AND u.NAME LIKE :name]
    [AND u.PHONE LIKE :phone]
