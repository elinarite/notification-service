INSERT INTO users (id, user_name, first_name, last_name, created_at)
VALUES (1, 'telegram_user1', 'John', 'Doe', CURRENT_TIMESTAMP),
         (1361169404, 'elinka1234', 'Elina', 'Arite', CURRENT_TIMESTAMP);

INSERT INTO price_alerts (user_id, currency_name, min_threshold, max_threshold, created_at, update_at)
VALUES (1, 'BTC', 75.00, 85000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1361169404, 'BTC', 0.50, 1.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);