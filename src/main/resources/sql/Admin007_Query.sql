SELECT
    a.ACCOUNTS_ID,
    a.EMAIL,
    u.USERS_NAME,
    u.USERS_PHONE,
    a.ROLE,
    a.STATUS
FROM
    PETEL_ACCOUNTS a
LEFT JOIN
    PETEL_USERS u ON a.ACCOUNTS_ID = u.ACCOUNTS_ID
WHERE
    a.ROLE = 'USER'
    [AND a.ACCOUNTS_ID = :accountsId]
    [AND a.EMAIL = :email]
    [AND u.USERS_NAME LIKE :name]
    [AND u.USERS_PHONE = :phone]
