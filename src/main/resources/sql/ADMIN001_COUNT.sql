SELECT COUNT(*) AS TOTAL_COUNT
FROM
    PETEL_PROPERTY p
LEFT JOIN
    PETEL_SELLERS s ON p.SELLER_ID = s.ID
WHERE
    1=1
    [AND p.NAME LIKE :propertyName]
    [AND p.TEL LIKE :tel]
    [AND p.POSTAL_CODE LIKE :postalCode]
    [AND p.ADDRESS LIKE :address]
    [AND s.NAME LIKE :sellerName]
