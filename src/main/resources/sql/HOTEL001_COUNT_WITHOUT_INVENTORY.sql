WITH RoomAvailability AS (
    SELECT
        rm.PROPERTY_ID as PROPERTY_ID,
        rm.BASE_PRICE as MIN_PRICE
    FROM PETEL_ROOMS rm
    WHERE rm.PET_TYPE_ID = :petType
      AND rm.TOTAL_UNITS >= :petCount
),
FilteredProperties AS (
    SELECT
        p.ID as PROPERTY_ID,
        MIN(ra.MIN_PRICE) as MIN_PRICE
    FROM PETEL_PROPERTY p
    INNER JOIN PETEL_POSTALS po ON p.POSTAL_CODE = po.ID
    INNER JOIN RoomAvailability ra ON p.ID = ra.PROPERTY_ID
    WHERE 1=1
    [AND po.CITY = :city]
    [AND ra.MIN_PRICE >= :priceMin]
    [AND ra.MIN_PRICE <= :priceMax]
    [AND EXISTS (
        SELECT 1 FROM PETEL_PROPERTY_FACILITIES pf
        WHERE pf.PROPERTY_ID = p.ID
        AND pf.FACILITY_ID IN ( :facilities )
        GROUP BY pf.PROPERTY_ID
        HAVING COUNT(DISTINCT pf.FACILITY_ID) >= :facilityCount
    )]
    GROUP BY p.ID
)
SELECT COUNT(*) AS TOTAL_COUNT
FROM (
    SELECT fp.PROPERTY_ID
    FROM FilteredProperties fp
    LEFT JOIN PETEL_REVIEWS r ON fp.PROPERTY_ID = r.PROPERTY_ID
    GROUP BY fp.PROPERTY_ID
    [HAVING AVG((r.PRICE_SCORE + r.ENV_SCORE + r.SERVICE_SCORE) / 3.0) >= :minRating]
)
