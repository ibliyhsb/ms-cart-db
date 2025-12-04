# MS-CART-DB - Servicio de Base de Datos del Carrito

## 📋 Cambios Implementados

### 1. **Entity Cart Actualizada**
- ✅ `id_customer` cambiado de `Long` a `String` (soporta IDs guest como "GUEST_123456_abc")
- ✅ Agregado `product_id` (Long)
- ✅ Agregado `product_name` (String)
- ✅ Agregado `quantity` (int) - cantidad de productos
- ✅ Agregado `size` (String) - tamaño seleccionado
- ✅ Agregado `personalization_message` (TEXT) - mensaje personalizado

### 2. **DTOs Creados/Actualizados**

#### CartItemDTO (NUEVO)
Representa un item individual en el carrito:
```json
{
  "product_id": 123,
  "product_name": "Torta de Chocolate",
  "price": 15000,
  "quantity": 2,
  "size": "mediano",
  "personalization_message": "Feliz Cumpleaños Juan",
  "id_customer": "GUEST_123456_abc"
}
```

#### CartDTO (ACTUALIZADO)
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_123456_abc",
  "items": [
    {
      "product_id": 123,
      "product_name": "Torta de Chocolate",
      "price": 15000,
      "quantity": 2,
      "size": "mediano",
      "personalization_message": "Feliz Cumpleaños"
    },
    {
      "product_id": 456,
      "product_name": "Pastel de Fresa",
      "price": 12000,
      "quantity": 1,
      "size": "grande",
      "personalization_message": null
    }
  ],
  "total": 42000
}
```

### 3. **Repository Mejorado**
- ✅ `findByIdCart()` - Obtener todos los items de un carrito
- ✅ `findByIdCartAndProductIdAndSizeAndPersonalizationMessage()` - Buscar item específico
- ✅ `deleteByIdCart()` - Eliminar todos los items de un carrito
- ✅ `deleteByIdCartAndProductId()` - Eliminar producto específico
- ✅ `findMaxIdCart()` - Obtener último ID de carrito usado

### 4. **Service Reescrito**
Implementa toda la lógica de negocio:

- **createCart(String idCustomer)**: Genera un nuevo `id_cart` único
- **addProduct(Long idCart, CartItemDTO item)**: 
  - Si existe el mismo producto con mismo tamaño y mensaje → incrementa cantidad
  - Si no existe → crea nueva fila
- **updateQuantity(Long idCart, Long productId, int quantity)**: Actualiza cantidad de un producto
- **deleteProduct(Long idCart, Long productId)**: Elimina producto del carrito
- **getCartById(Long idCart)**: Retorna carrito completo con total calculado

### 5. **Controller con Nuevos Endpoints**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/cart/getCartById/{idCart}` | Obtener carrito con todos sus items y total |
| POST | `/api/cart/createCart/{idCustomer}` | Crear nuevo carrito vacío |
| POST | `/api/cart/addProduct/{idCart}` | Agregar producto al carrito |
| PUT | `/api/cart/updateQuantity/{idCart}/{productId}` | Actualizar cantidad de un producto |
| DELETE | `/api/cart/deleteProduct/{idCart}/{productId}` | Eliminar producto del carrito |

---

## 🚀 Instalación y Configuración

### 1. Actualizar la Base de Datos

Ejecuta el script SQL en tu MySQL:

```bash
mysql -u root -p cart_db < update_cart_table.sql
```

O ejecuta manualmente en MySQL Workbench:

```sql
ALTER TABLE cart 
    MODIFY COLUMN id_customer VARCHAR(255) NOT NULL,
    ADD COLUMN IF NOT EXISTS product_id BIGINT,
    ADD COLUMN IF NOT EXISTS product_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS quantity INT NOT NULL DEFAULT 1,
    ADD COLUMN IF NOT EXISTS size VARCHAR(50),
    ADD COLUMN IF NOT EXISTS personalization_message TEXT;
```

### 2. Configuración del Proyecto

Verifica que `application.properties` tenga la configuración correcta:

```properties
spring.application.name=ms-cart-db
server.port=8180

spring.datasource.url=jdbc:mysql://localhost:3306/cart_db?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### 3. Compilar y Ejecutar

```bash
# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

El servicio estará disponible en: **http://localhost:8180**

---

## 📡 Ejemplos de Uso

### 1. Crear un Carrito Nuevo

```bash
POST http://localhost:8180/api/cart/createCart/GUEST_123456_abc
```

**Respuesta:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_123456_abc",
  "items": [],
  "total": 0
}
```

### 2. Agregar Producto al Carrito

```bash
POST http://localhost:8180/api/cart/addProduct/1
Content-Type: application/json

{
  "product_id": 123,
  "product_name": "Torta de Chocolate",
  "price": 15000,
  "quantity": 2,
  "size": "mediano",
  "personalization_message": "Feliz Cumpleaños Juan",
  "id_customer": "GUEST_123456_abc"
}
```

**Respuesta:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_123456_abc",
  "items": [
    {
      "product_id": 123,
      "product_name": "Torta de Chocolate",
      "price": 15000,
      "quantity": 2,
      "size": "mediano",
      "personalization_message": "Feliz Cumpleaños Juan"
    }
  ],
  "total": 30000
}
```

### 3. Obtener Carrito por ID

```bash
GET http://localhost:8180/api/cart/getCartById/1
```

### 4. Actualizar Cantidad de un Producto

```bash
PUT http://localhost:8180/api/cart/updateQuantity/1/123
Content-Type: application/json

{
  "quantity": 5
}
```

### 5. Eliminar Producto del Carrito

```bash
DELETE http://localhost:8180/api/cart/deleteProduct/1/123
```

---

## 🔑 Puntos Clave

### Lógica de Duplicados
El sistema identifica productos duplicados por:
- `product_id`
- `size`
- `personalization_message`

Si agregas un producto con estos 3 valores idénticos, **incrementa la cantidad**.
Si alguno es diferente, **crea una nueva fila**.

### Ejemplo:
```
Item 1: Torta Chocolate, tamaño "mediano", mensaje "Feliz Cumpleaños" → quantity: 2
Item 2: Torta Chocolate, tamaño "grande", mensaje "Feliz Cumpleaños"  → quantity: 1 (NUEVA FILA)
Item 3: Torta Chocolate, tamaño "mediano", mensaje "Aniversario"      → quantity: 1 (NUEVA FILA)
```

### Cálculo del Total
El total se calcula automáticamente en `getCartById()`:

```
Total = Σ(precio × cantidad) de todos los items
```

### Soporte para IDs Guest
- Usuario registrado: `id_customer = "12345"`
- Usuario guest: `id_customer = "GUEST_1234567_abc123"`

---

## 🗄️ Estructura de la Tabla

```sql
CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cart BIGINT NOT NULL,
    id_customer VARCHAR(255) NOT NULL,
    product_id BIGINT,
    product_name VARCHAR(255),
    price INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    size VARCHAR(50),
    personalization_message TEXT,
    INDEX idx_cart (id_cart),
    INDEX idx_customer (id_customer)
);
```

**Nota:** Un carrito puede tener múltiples filas (una por cada producto diferente) con el mismo `id_cart`.

---

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL 8.x**
- **Lombok**
- **Gradle**

---

## 📝 Notas Importantes

1. El campo `id_customer` ahora es `String` para soportar usuarios guest
2. Cada producto en el carrito es una fila separada en la tabla
3. El total se calcula automáticamente al obtener el carrito
4. Los métodos de eliminación usan `@Transactional` para consistencia
5. El endpoint cambió de `/api/Cart` a `/api/cart` (minúscula)

---

## ✅ Checklist de Implementación

- [x] Entity Cart actualizada
- [x] CartItemDTO creado
- [x] CartDTO actualizado
- [x] Repository con nuevos métodos
- [x] Service con lógica completa
- [x] Controller con nuevos endpoints
- [x] Script SQL de actualización
- [ ] Ejecutar script SQL en MySQL
- [ ] Probar endpoints con Postman/Insomnia
- [ ] Integrar con Business Service (ms-cart-bs)

---

## 🔗 Integración con otros Servicios

Este microservicio (ms-cart-db) se comunica con:

- **Business Service (ms-cart-bs)**: Expone la lógica de negocio
- **BFF**: Expone los endpoints al frontend

El flujo completo es:
```
Frontend → BFF → Business Service → Database Service (este) → MySQL
```
