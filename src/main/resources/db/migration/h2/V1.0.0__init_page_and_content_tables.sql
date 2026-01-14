CREATE TABLE IF NOT EXISTS pages (
    id BIGSERIAL PRIMARY KEY,
    page_number INT NOT NULL UNIQUE,
    category VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS page_contents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    page_id BIGINT NOT NULL REFERENCES pages(id) ON DELETE CASCADE,
    source VARCHAR(255),
    additional_data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE page_contents ADD CONSTRAINT uq_page_contents_page_id UNIQUE (page_id);

INSERT INTO pages (page_number, category)
VALUES
    (101, 'NEWS'),
    (901, 'MISC');

INSERT INTO page_contents (title, description, page_id)
VALUES
    ('Wiadomość lokalna','Dziś w mieście odbędzie się festiwal muzyki.', 1),
    ('Dodatkowa informacja wprowadzona przez administratora','Temperatura w Warszawie: 18°C, słonecznie.', 2);