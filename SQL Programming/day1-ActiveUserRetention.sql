SELECT 
    7 AS month,
    COUNT(DISTINCT july.user_id) AS monthly_active_users
FROM (
    SELECT DISTINCT user_id
    FROM user_actions
    WHERE EXTRACT(MONTH FROM event_date) = 7
      AND EXTRACT(YEAR FROM event_date) = 2022
) july
JOIN (
    SELECT DISTINCT user_id
    FROM user_actions
    WHERE EXTRACT(MONTH FROM event_date) = 6
      AND EXTRACT(YEAR FROM event_date) = 2022
) june
ON july.user_id = june.user_id;