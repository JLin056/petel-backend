select
    O.USER_ID,
    O.PROPERTY_ID,
    O.PAYMENT_ID,
    O.HOTEL_CHARGES,
    O.CHECK_IN,
    O.CHECK_OUT,
    O.STATUS,
    O.NOTE,
    O.CREATED_AT,
    O.UPDATED_AT,
    I.ARRIVAL_DATE,
    I.ROOM_ID,
    I.QUANTITY,
    I.PRICE
from
    PETEL_ORDERS O
left join
    PETEL_ORDER_ITEMS I
on
    O.ID = I.ORDER_ID
where
    O.ID =:orderID