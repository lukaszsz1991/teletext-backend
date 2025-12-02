ALTER TABLE page_contents DROP COLUMN content;
ALTER TABLE pages DROP COLUMN title;

DELETE FROM page_contents WHERE id IN (2, 4);

ALTER TABLE page_contents ADD COLUMN title VARCHAR(255) NOT NULL DEFAULT 'Test title';
ALTER TABLE page_contents ADD COLUMN description TEXT NOT NULL DEFAULT 'Test description of page. It will be much longer ' ||
                                                                       'usually.';

ALTER TABLE page_contents ADD CONSTRAINT uq_page_contents_page_id UNIQUE (page_id);
