CREATE DATABASE indimetra_app;
USE indimetra_app;

-- Tabla de Usuarios
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255) NULL,
    is_author BOOLEAN NULL,
    social_links VARCHAR(255) NULL,
    country VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE users
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;


-- Tabla de Roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL UNIQUE,
    description TEXT NULL  -- Utilizamos text que es mas indicado para descripciones largas
);

ALTER TABLE roles
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Tabla Intermedia entre Usuarios y Roles
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tabla de Categorías
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT NULL -- Utilizamos text que es mas indicado para descripciones largas
);

ALTER TABLE categories
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE categories
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Tabla de Cortometrajes (Películas y Cortometrajes)
CREATE TABLE cortometrajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    technique VARCHAR(50), -- (analógico, blanco y negro...)
    release_year INT NOT NULL,
    duration INT NOT NULL,
    language VARCHAR(50) NOT NULL DEFAULT 'Spanish',
    rating DECIMAL(3,1) DEFAULT 0 CHECK (rating BETWEEN 0 AND 5),
    image_url VARCHAR(255),
    video_url VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

ALTER TABLE cortometrajes
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Tabla de Valoraciones y Comentarios
CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    cortometraje_id INT NOT NULL,
    rating DECIMAL(3,1) NOT NULL CHECK (rating BETWEEN 0 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (cortometraje_id) REFERENCES cortometrajes(id) ON DELETE CASCADE
);

ALTER TABLE reviews
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Tabla de Favoritos
CREATE TABLE favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    cortometraje_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (cortometraje_id) REFERENCES cortometrajes(id) ON DELETE CASCADE,
    UNIQUE (user_id, cortometraje_id)
);

ALTER TABLE favorites
CHANGE COLUMN added_at created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE favorites
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Insert de prueba
INSERT INTO roles (name, description) VALUES
('ROLE_ADMIN', 'El administrador tiene acceso completo a todas las funcionalidades de la plataforma. Puede gestionar usuarios, modificar y eliminar contenido, revisar reportes y mantener la calidad del catálogo de cortometrajes. También es responsable de moderar reseñas, asignar categorías y supervisar el cumplimiento de las normas de la comunidad.'),
('ROLE_USER', 'El usuario estándar puede navegar por la plataforma, visualizar cortometrajes, calificarlos y dejar reseñas. Si el usuario sube contenido, se convierte en autor, lo que le permite compartir sus obras con la comunidad. Además, puede guardar favoritos, gestionar listas de reproducción y participar en la interacción con otros miembros de la plataforma.');

-- Insertar usuarios con contraseñas encriptadas (12345678) y con imagen de perfil opcional
INSERT INTO users (username, email, password, profile_image, is_author, social_links, country) VALUES
('admin', 'admin@email.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://placehold.co/600x400', FALSE, NULL, 'España'),
('usuario1', 'user1@email.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://placehold.co/600x400', TRUE, 'https://twitter.com/usuario1', 'Argentina'),
('usuario2', 'user2@email.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://placehold.co/600x400', TRUE, 'https://instagram.com/usuario2', 'México'),
('usuario3', 'user3@email.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://placehold.co/600x400', FALSE, 'https://facebook.com/usuario3', 'Colombia');

-- Asignar roles a los usuarios
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin es ROLE_ADMIN
(2, 2), -- usuario1 es ROLE_USER
(3, 2), -- usuario2 es ROLE_USER
(4, 2); -- usuario3 es ROLE_USER

-- Insertar categorías
INSERT INTO categories (name, description) VALUES
('Drama', 'Historias intensas con una gran carga emocional.'),
('Documental', 'Películas basadas en hechos reales o investigativos.'),
('Experimental', 'Obras que exploran narrativas y técnicas innovadoras.');

-- Insertar cortometrajes
INSERT INTO cortometrajes (title, description, technique, release_year, duration, language, rating, image_url, video_url, user_id, category_id) VALUES
('Cortometraje Experimental 1', 'Un viaje abstracto a través de luces y sombras.', 'Blanco y Negro', 2023, 12, 'Spanish', 4.5, 'https://placehold.co/600x400', 'https://www.youtube.com/watch?v=m6jfZa00vkY&ab_channel=IsmaelKrall', 2, 3),
('Documental Indie', 'Un vistazo a la vida cotidiana en las montañas.', 'Cine Analógico', 2022, 30, 'English', 4.8, 'https://placehold.co/600x400', 'https://www.youtube.com/watch?v=m6jfZa00vkY&ab_channel=IsmaelKrall', 3, 2),
('Cine de Autor', 'Historia de un hombre en búsqueda de su identidad.', 'Planos Secuencia', 2021, 20, 'French', 4.2, 'https://placehold.co/600x400', 'https://www.youtube.com/watch?v=m6jfZa00vkY&ab_channel=IsmaelKrall', 2, 1);

-- Insertar valoraciones
INSERT INTO reviews (user_id, cortometraje_id, rating, comment) VALUES
(2, 1, 5, 'Increíble propuesta visual y narrativa.'),
(3, 2, 4, 'Muy interesante, pero algo lento en ciertas partes.'),
(3, 3, 5, 'Una obra maestra del cine independiente.');

-- Insertar favoritos
INSERT INTO favorites (user_id, cortometraje_id, added_at) VALUES
(2, 1, NOW()),
(2, 2, NOW()),
(3, 3, NOW());
