UPDATE page_contents SET source = 'MANUAL' WHERE source IS NULL;
ALTER TABLE page_contents ALTER COLUMN source SET NOT NULL;