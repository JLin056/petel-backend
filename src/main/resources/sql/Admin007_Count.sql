SELECT COUNT(*) AS TOTAL_COUNT
FROM
    PETEL_USERS u
LEFT JOIN
    PETEL_ACCOUNTS a ON u.ACCOUNT_ID = a.ID
WHERE
    a.ROLE = 'USER'
    [AND a.ID = :accountId]
    [AND a.EMAIL = :email]
    [AND u.NAME LIKE :name]
    [AND u.PHONE = :phone]
