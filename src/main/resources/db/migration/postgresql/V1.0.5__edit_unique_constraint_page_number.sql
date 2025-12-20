-- NOTE: this migrations doesn't work with h2. More info in ADR-005
ALTER TABLE pages DROP CONSTRAINT pages_page_number_key;
CREATE UNIQUE INDEX ux_pages_active_page_number ON pages (page_number) WHERE deleted_at IS NULL;