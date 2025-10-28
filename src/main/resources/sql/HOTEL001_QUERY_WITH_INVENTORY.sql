WITH RoomAvailability AS (
    SELECT
        rm.ID as ROOM_Id,
        rm.PROPERTY_ID as PROPERTY_ID,
        -- 如果有 inventory 記錄用 AVAILABLE_QTY，否則用 TOTAL_UNITS
        CASE
            WHEN COUNT(ri.ROOM_ID) > 0 THEN MIN(ri.AVAILABLE_QTY)
            ELSE rm.TOTAL_UNITS
        END as AVAILABLE_QTY,
        -- 如果有 inventory 記錄用 PRICE，否則用 BASE_PRICE
        CASE
            WHEN COUNT(ri.ROOM_ID) > 0 THEN MIN(ri.PRICE)
            ELSE rm.BASE_PRICE
        END as MIN_PRICE
    FROM PETEL_ROOMS rm
    LEFT JOIN PETEL_ROOM_INVENTORIES ri
        ON rm.ID = ri.ROOM_ID
        AND ri.STAY_DATE >= :checkIn
        AND ri.STAY_DATE < :checkOut
    WHERE rm.PET_TYPE_ID IN (:petTypeList)
    GROUP BY rm.ID, rm.PROPERTY_ID, rm.TOTAL_UNITS, rm.BASE_PRICE
    HAVING CASE
            WHEN COUNT(ri.ROOM_ID) > 0 THEN MIN(ri.AVAILABLE_QTY)
            ELSE rm.TOTAL_UNITS
        END >= :petCount
),
FilteredProperties AS (
    SELECT
        p.ID as PROPERTY_ID,
        p.NAME as NAME,
        p.ADDRESS as ADDRESS,
        p.INFO as INFO,
        p.POSTAL_CODE as POSTAL_CODE,
        po.CITY as CITY,
        MIN(ra.MIN_PRICE) as MIN_PRICE,
        MIN(ra.AVAILABLE_QTY) as AVAILABLE_QTY
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
    GROUP BY p.ID, p.NAME, p.ADDRESS, p.INFO, p.POSTAL_CODE, po.CITY
)
SELECT
    fp.PROPERTY_ID as PROPERTY_ID,
    fp.NAME as NAME,
    fp.ADDRESS as ADDRESS,
    fp.INFO as INFO,
    fp.POSTAL_CODE as POSTAL_CODE,
    fp.CITY as CITY,
    fp.MIN_PRICE as MIN_PRICE,
    fp.AVAILABLE_QTY as AVAILABLE_QTY,
    COALESCE(AVG((r.PRICE_SCORE + r.ENV_SCORE + r.SERVICE_SCORE) / 3.0), 0) as AVG_RATING
FROM FilteredProperties fp
LEFT JOIN PETEL_REVIEWS r ON fp.PROPERTY_ID = r.PROPERTY_ID
GROUP BY fp.PROPERTY_ID, fp.NAME, fp.ADDRESS, fp.INFO, fp.POSTAL_CODE, fp.CITY, fp.MIN_PRICE, fp.AVAILABLE_QTY
[HAVING AVG((r.PRICE_SCORE + r.ENV_SCORE + r.SERVICE_SCORE) / 3.0) >= :minRating]
ORDER BY AVG_RATING DESC, MIN_PRICE ASC
OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY
