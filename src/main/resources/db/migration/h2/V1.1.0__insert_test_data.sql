INSERT INTO page_stats (page_id)
SELECT id FROM pages WHERE page_number = 101
UNION ALL
SELECT id FROM pages WHERE page_number = 101;

INSERT INTO pages (page_number, category)
VALUES
  (801, 'sports'),
  (802, 'finance'),
  (803, 'weather');
