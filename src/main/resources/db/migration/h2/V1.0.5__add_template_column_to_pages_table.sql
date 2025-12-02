ALTER TABLE page_contents DROP COLUMN additional_data;

ALTER TABLE pages ADD COLUMN template_id BIGINT;
ALTER TABLE pages ADD CONSTRAINT fk_page_template FOREIGN KEY (template_id) REFERENCES page_templates(id);
