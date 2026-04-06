-- This runs only if no admin exists yet
-- Password is: admin (BCrypt encoded)
INSERT IGNORE INTO users (username, email, password, role, status, created_at, updated_at)
VALUES (
    'admin',
    'admin@finance.com',
    '$2a$12$OmV6gd29.Imt9ozOC0Y1DO7HDjv68CgQhuNOC4gtvQ7GsE6VyIVam',
    'ADMIN',
    'ACTIVE',
    NOW(),
    NOW()
);
