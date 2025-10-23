SELECT COUNT(*) AS TOTAL_COUNT
FROM PETEL_ORDERS o
WHERE o.USER_ID = :userId
[ AND o.STATUS    = :status ]
[ AND o.CHECK_OUT >= :from ]
[ AND o.CHECK_IN  <= :to   ]