INSERT INTO pages(page_number, category, template_id)
SELECT 501, 'WEATHER', id FROM page_templates WHERE name = 'Pogoda Wrocław';

INSERT INTO pages(page_number, category, template_id)
SELECT 201, 'SPORTS', id FROM page_templates WHERE name = 'Tabela Ekstraklasa';