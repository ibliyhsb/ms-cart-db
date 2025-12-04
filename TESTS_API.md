# 🧪 Tests de API - MS-CART-DB

Este archivo contiene ejemplos de pruebas para todos los endpoints del servicio.

## 📋 Prerequisitos

1. El servicio debe estar ejecutándose en `http://localhost:8180`
2. La base de datos MySQL debe estar actualizada con las nuevas columnas
3. Usar Postman, Insomnia, curl o cualquier cliente REST

---

## 🔧 Ejecutar el Servicio

```bash
cd c:\Users\catar\OneDrive\Desktop\ms-cart-db
.\gradlew.bat bootRun
```

---

## 🧪 Tests con cURL

### 1️⃣ Crear Carrito Nuevo

**Usuario Guest:**
```bash
curl -X POST "http://localhost:8180/api/cart/createCart/GUEST_1234567_abc123"
```

**Usuario Registrado:**
```bash
curl -X POST "http://localhost:8180/api/cart/createCart/12345"
```

**Respuesta Esperada:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_1234567_abc123",
  "items": [],
  "total": 0
}
```

---

### 2️⃣ Agregar Producto al Carrito

**Producto Simple (sin personalización):**
```bash
curl -X POST "http://localhost:8180/api/cart/addProduct/1" ^
  -H "Content-Type: application/json" ^
  -d "{\"product_id\":123,\"product_name\":\"Torta de Chocolate\",\"price\":15000,\"quantity\":2,\"size\":\"mediano\",\"personalization_message\":null,\"id_customer\":\"GUEST_1234567_abc123\"}"
```

**Producto con Personalización:**
```bash
curl -X POST "http://localhost:8180/api/cart/addProduct/1" ^
  -H "Content-Type: application/json" ^
  -d "{\"product_id\":456,\"product_name\":\"Torta Personalizada\",\"price\":20000,\"quantity\":1,\"size\":\"grande\",\"personalization_message\":\"Feliz Cumpleaños Juan\",\"id_customer\":\"GUEST_1234567_abc123\"}"
```

**Respuesta Esperada:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_1234567_abc123",
  "items": [
    {
      "product_id": 123,
      "product_name": "Torta de Chocolate",
      "price": 15000,
      "quantity": 2,
      "size": "mediano",
      "personalization_message": null
    },
    {
      "product_id": 456,
      "product_name": "Torta Personalizada",
      "price": 20000,
      "quantity": 1,
      "size": "grande",
      "personalization_message": "Feliz Cumpleaños Juan"
    }
  ],
  "total": 50000
}
```

---

### 3️⃣ Obtener Carrito por ID

```bash
curl -X GET "http://localhost:8180/api/cart/getCartById/1"
```

**Respuesta Esperada:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_1234567_abc123",
  "items": [...],
  "total": 50000
}
```

---

### 4️⃣ Actualizar Cantidad de un Producto

```bash
curl -X PUT "http://localhost:8180/api/cart/updateQuantity/1/123" ^
  -H "Content-Type: application/json" ^
  -d "{\"quantity\":5}"
```

**Respuesta Esperada:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_1234567_abc123",
  "items": [
    {
      "product_id": 123,
      "product_name": "Torta de Chocolate",
      "price": 15000,
      "quantity": 5,
      "size": "mediano",
      "personalization_message": null
    },
    {
      "product_id": 456,
      "product_name": "Torta Personalizada",
      "price": 20000,
      "quantity": 1,
      "size": "grande",
      "personalization_message": "Feliz Cumpleaños Juan"
    }
  ],
  "total": 95000
}
```

---

### 5️⃣ Eliminar Producto del Carrito

```bash
curl -X DELETE "http://localhost:8180/api/cart/deleteProduct/1/456"
```

**Respuesta Esperada:**
```json
{
  "id_cart": 1,
  "id_customer": "GUEST_1234567_abc123",
  "items": [
    {
      "product_id": 123,
      "product_name": "Torta de Chocolate",
      "price": 15000,
      "quantity": 5,
      "size": "mediano",
      "personalization_message": null
    }
  ],
  "total": 75000
}
```

---

## 🧪 Tests con PowerShell (Invoke-RestMethod)

### 1️⃣ Crear Carrito
```powershell
Invoke-RestMethod -Uri "http://localhost:8180/api/cart/createCart/GUEST_1234567_abc123" -Method POST
```

### 2️⃣ Agregar Producto
```powershell
$body = @{
    product_id = 123
    product_name = "Torta de Chocolate"
    price = 15000
    quantity = 2
    size = "mediano"
    personalization_message = $null
    id_customer = "GUEST_1234567_abc123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8180/api/cart/addProduct/1" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"
```

### 3️⃣ Obtener Carrito
```powershell
Invoke-RestMethod -Uri "http://localhost:8180/api/cart/getCartById/1" -Method GET
```

### 4️⃣ Actualizar Cantidad
```powershell
$body = @{ quantity = 5 } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8180/api/cart/updateQuantity/1/123" `
    -Method PUT `
    -Body $body `
    -ContentType "application/json"
```

### 5️⃣ Eliminar Producto
```powershell
Invoke-RestMethod -Uri "http://localhost:8180/api/cart/deleteProduct/1/123" -Method DELETE
```

---

## 📝 Casos de Prueba Importantes

### ✅ Test 1: Agregar el Mismo Producto (debe incrementar cantidad)

**Paso 1:** Agregar producto
```json
POST /api/cart/addProduct/1
{
  "product_id": 123,
  "product_name": "Torta",
  "price": 10000,
  "quantity": 2,
  "size": "mediano",
  "personalization_message": "Mensaje 1",
  "id_customer": "GUEST_123"
}
```

**Paso 2:** Agregar el mismo producto con mismo tamaño y mensaje
```json
POST /api/cart/addProduct/1
{
  "product_id": 123,
  "product_name": "Torta",
  "price": 10000,
  "quantity": 3,
  "size": "mediano",
  "personalization_message": "Mensaje 1",
  "id_customer": "GUEST_123"
}
```

**Resultado Esperado:** Solo debe haber 1 item con `quantity: 5`

---

### ✅ Test 2: Agregar Producto Similar Pero Diferente (debe crear nueva fila)

**Paso 1:** Agregar producto
```json
POST /api/cart/addProduct/1
{
  "product_id": 123,
  "product_name": "Torta",
  "price": 10000,
  "quantity": 2,
  "size": "mediano",
  "personalization_message": "Mensaje 1",
  "id_customer": "GUEST_123"
}
```

**Paso 2:** Agregar mismo producto pero con diferente tamaño
```json
POST /api/cart/addProduct/1
{
  "product_id": 123,
  "product_name": "Torta",
  "price": 10000,
  "quantity": 1,
  "size": "grande",
  "personalization_message": "Mensaje 1",
  "id_customer": "GUEST_123"
}
```

**Resultado Esperado:** Debe haber 2 items diferentes (uno mediano, uno grande)

---

### ✅ Test 3: Calcular Total Correctamente

**Agregar varios productos:**
- Producto A: precio=10000, cantidad=2 → 20000
- Producto B: precio=15000, cantidad=3 → 45000
- Producto C: precio=5000, cantidad=1 → 5000

**Total Esperado:** 70000

---

### ✅ Test 4: Usuario Guest

```json
POST /api/cart/createCart/GUEST_1701234567_a8f3bc
```

El sistema debe aceptar IDs con formato `GUEST_[timestamp]_[hash]`

---

## 🐛 Errores Comunes y Soluciones

### Error: "Cannot resolve table 'cart'"
**Solución:** Ejecutar el script SQL de actualización

### Error: "Column 'quantity' not found"
**Solución:** Verificar que las columnas nuevas existan en la tabla

### Error: 404 Not Found
**Solución:** Verificar que el endpoint sea `/api/cart` (minúscula)

### Error: Connection refused
**Solución:** Verificar que MySQL esté corriendo y que las credenciales sean correctas

---

## 📊 Verificar Datos en MySQL

```sql
-- Ver todos los carritos
SELECT * FROM cart;

-- Ver items de un carrito específico
SELECT * FROM cart WHERE id_cart = 1;

-- Ver total calculado de un carrito
SELECT 
    id_cart,
    SUM(price * quantity) as total
FROM cart 
WHERE id_cart = 1
GROUP BY id_cart;

-- Ver carritos de usuarios guest
SELECT * FROM cart WHERE id_customer LIKE 'GUEST_%';
```

---

## ✅ Checklist de Pruebas

- [ ] Crear carrito con usuario guest
- [ ] Crear carrito con usuario registrado
- [ ] Agregar producto simple
- [ ] Agregar producto con personalización
- [ ] Agregar producto duplicado (verificar incremento de cantidad)
- [ ] Agregar producto similar con diferente tamaño
- [ ] Obtener carrito por ID
- [ ] Verificar cálculo correcto del total
- [ ] Actualizar cantidad de un producto
- [ ] Eliminar producto del carrito
- [ ] Verificar que el total se actualiza correctamente después de cada operación

---

## 🔗 Links Útiles

- Swagger UI (si está habilitado): http://localhost:8180/swagger-ui.html
- Base de datos: MySQL en puerto 3306
- Aplicación: http://localhost:8180
