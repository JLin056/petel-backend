SELECT
    u.ID as USER_ID,
    a.ID as ACCOUNT_ID,
    a.EMAIL,
    u.NAME,
    u.PHONE,
    a.ROLE,
    a.STATUS
 FROM
    PETEL_USERS u
 LEFT JOIN
    PETEL_ACCOUNTS a ON u.ACCOUNT_ID = a.ID
 WHERE
    a.ROLE = 'USER'
    [AND a.ID LIKE :accountId]
    [AND a.EMAIL LIKE :email]
    [AND u.NAME LIKE :name]
    [AND u.PHONE LIKE :phone]
 ORDER BY a.ID
 [OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY]
