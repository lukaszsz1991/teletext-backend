INSERT INTO page_stats (page_id)
SELECT id FROM pages WHERE page_number = 101
UNION ALL
SELECT id FROM pages WHERE page_number = 101;

INSERT INTO page_templates (name, category, source, config_json)
VALUES ('Szablon Sportowy 1', 'SPORTS', 'SPORT_TABLE', '{"league": "bundesliga"}'),
       ('Szablon Finansowy 1', 'FINANCE', 'EXCHANGE_RATE', '{"currencyCode": "USD"}'),
       ('Szablon Pogodowy 1', 'WEATHER', 'WEATHER', '{"city": "Warszawa"}');

INSERT INTO pages (page_number, category, template_id)
SELECT 299, 'SPORTS',   id FROM page_templates WHERE name = 'Szablon Sportowy 1'
UNION ALL
SELECT 899, 'FINANCE',  id FROM page_templates WHERE name = 'Szablon Finansowy 1'
UNION ALL
SELECT 599, 'WEATHER',  id FROM page_templates WHERE name = 'Szablon Pogodowy 1';
