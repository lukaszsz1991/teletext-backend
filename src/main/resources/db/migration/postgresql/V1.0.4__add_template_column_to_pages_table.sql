ALTER TABLE pages ADD COLUMN template_id BIGINT;
ALTER TABLE pages ADD CONSTRAINT fk_page_template FOREIGN KEY (template_id) REFERENCES page_templates(id);

INSERT INTO pages(page_number, category, template_id)
SELECT 501, 'WEATHER', id FROM page_templates WHERE name = 'Pogoda Wrocław';

INSERT INTO pages(page_number, category, template_id)
SELECT 201, 'SPORTS', id FROM page_templates WHERE name = 'Tabela Ekstraklasa';