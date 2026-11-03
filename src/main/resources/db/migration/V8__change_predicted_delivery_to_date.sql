ALTER TABLE orders
    ALTER COLUMN predicted_date_of_delivery TYPE date
        USING predicted_date_of_delivery::date;