CREATE TABLE IF NOT EXISTS pages (
    id BIGSERIAL PRIMARY KEY,
    page_number INT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS page_contents (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    page_id BIGINT NOT NULL REFERENCES pages(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

INSERT INTO pages (page_number, title, category)
VALUES
    (100, 'Wiadomości lokalne', 'NEWS'),
    (200, 'Prognoza pogody', 'WEATHER');

INSERT INTO page_contents (content, page_id)
VALUES
    ('Dziś w mieście odbędzie się festiwal muzyki.', 1),
    ('Nowy projekt rewitalizacji parku miejskiego.', 1),
    ('Temperatura w Warszawie: 18°C, słonecznie.', 2),
    ('Prognoza na jutro: przelotne opady deszczu.', 2);