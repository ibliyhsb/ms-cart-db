-- Script SQL para actualizar la tabla cart con las nuevas columnas

-- Opción 1: Modificar tabla existente (RECOMENDADO)
-- Usar este script si ya tienes datos en la tabla y quieres conservarlos

ALTER TABLE cart 
    MODIFY COLUMN id_customer VARCHAR(255) NOT NULL,
    ADD COLUMN IF NOT EXISTS product_id BIGINT,
    ADD COLUMN IF NOT EXISTS product_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS quantity INT NOT NULL DEFAULT 1,
    ADD COLUMN IF NOT EXISTS size VARCHAR(50),
    ADD COLUMN IF NOT EXISTS personalization_message TEXT;

-- Actualizar datos existentes si la columna 'product' existía
-- UPDATE cart SET product_name = product WHERE product_name IS NULL;

-- Opcional: Eliminar la columna 'product' antigua si ya no la necesitas
-- ALTER TABLE cart DROP COLUMN product;

-- -------------------------------------------------------------------

-- Opción 2: Recrear tabla desde cero (ATENCIÓN: ELIMINA TODOS LOS DATOS)
-- Usar este script solo si quieres empezar de cero

/*
DROP TABLE IF EXISTS cart;

CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cart BIGINT NOT NULL,
    id_customer VARCHAR(255) NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    size VARCHAR(50),
    personalization_message TEXT,
    INDEX idx_cart (id_cart),
    INDEX idx_customer (id_customer)
);
*/
