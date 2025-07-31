CREATE TABLE topics (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    slug TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    view_count INT DEFAULT 0,
    author_id UUID NOT NULL,

    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
  );

CREATE TABLE answers (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    slug TEXT NOT NULL UNIQUE,
    author_id UUID NOT NULL,
    topic_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);
