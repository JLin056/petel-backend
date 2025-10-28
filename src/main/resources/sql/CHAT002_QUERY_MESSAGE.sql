SELECT thread_id,
       id            AS last_msg_id,
       message_type  AS last_msg_type,
       content       AS last_msg_content,
       created_at    AS last_msg_time
FROM (
  SELECT
    m.thread_id, m.id, m.message_type, m.content, m.created_at,
    ROW_NUMBER() OVER (PARTITION BY m.thread_id ORDER BY m.created_at DESC) AS rn
  FROM PETEL_CHAT_MESSAGES m
  WHERE m.thread_id IN (:ids)
)
WHERE rn = 1
