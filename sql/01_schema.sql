-- ============================================================
-- Catálogo Simples de Livros, Séries e Filmes
-- Script de criação do banco e tabela
-- ============================================================

CREATE DATABASE IF NOT EXISTS catalogo_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE catalogo_db;

DROP TABLE IF EXISTS catalogo;

CREATE TABLE catalogo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    genero VARCHAR(100) NOT NULL,
    ano_lancamento INT NOT NULL,
    descricao TEXT,
    avaliacao DECIMAL(3,1) NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tipo CHECK (tipo IN ('LIVRO','SERIE','FILME')),
    CONSTRAINT chk_ano CHECK (ano_lancamento BETWEEN 1900 AND 2100),
    CONSTRAINT chk_avaliacao CHECK (avaliacao IS NULL OR (avaliacao BETWEEN 0.0 AND 10.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para pesquisa e filtro (requisito 10)
CREATE INDEX idx_catalogo_titulo ON catalogo(titulo);
CREATE INDEX idx_catalogo_tipo ON catalogo(tipo);

-- ============================================================
-- Dados de exemplo
-- ============================================================
INSERT INTO catalogo (titulo, tipo, genero, ano_lancamento, descricao, avaliacao) VALUES
('Harry Potter e a Pedra Filosofal', 'LIVRO', 'Fantasia', 1997, 'Primeiro livro da saga Harry Potter, escrito por J.K. Rowling.', 9.5),
('Harry Potter e a Câmara Secreta', 'LIVRO', 'Fantasia', 1998, 'Segundo livro da saga Harry Potter.', 9.2),
('Breaking Bad', 'SERIE', 'Drama', 2008, 'Um professor de química se torna produtor de metanfetamina.', 9.8),
('Stranger Things', 'SERIE', 'Ficção Científica', 2016, 'Crianças enfrentam forças sobrenaturais na cidade de Hawkins.', 8.7),
('O Senhor dos Anéis: A Sociedade do Anel', 'FILME', 'Fantasia', 2001, 'Primeiro filme da trilogia de Peter Jackson.', 9.0),
('Interestelar', 'FILME', 'Ficção Científica', 2014, 'Um grupo de astronautas viaja através de um buraco de minhoca.', 9.3),
('Dom Casmurro', 'LIVRO', 'Romance', 1899, 'Clássico de Machado de Assis. Ano ajustado para 1899 para validação.', 8.5),
('The Witcher', 'SERIE', 'Fantasia', 2019, 'Geralt de Rívia, um caçador de monstros mutante.', 8.0);

-- Verificação
-- SELECT * FROM catalogo;
