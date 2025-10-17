select
    O.USER_ID,
    O.PROPERTY_ID,
    O.PAYMENT_ID,
    O.HOTEL_CHARGES,
    O.CHECK_IN,
    O.CHECK_OUT,
    O.STATUS,
    O.GUEST,
    U.NAME,
    U.PHONE,
    O.GUEST_NAME,
    O.GUEST_PHONE,
    O.NOTE,
    O.CREATED_AT,
    O.UPDATED_AT,
    I.ARRIVAL_DATE,
    I.ROOM_ID,
    I.QUANTITY,
    I.PRICE
from
    PETEL_USERS U
right join
    PETEL_ORDERS O
on
    U.ID = O.USER_ID
left join
    PETEL_ORDER_ITEMS I
on
    O.ID = I.ORDER_ID
where
    O.ID =:orderID