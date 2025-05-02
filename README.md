# Carrito de Compras - Microservicios con Spring Boot

Es importante mencionar antes de la descripcion del proyecto que por ser una prueba tecnica, los 4 microservicios estan en un multirepo sin embargo las buenas practicas mandan que por razones de CI/CD cada uno de los microservicios deben estar en un monorepo.

Este proyecto implementa una solución de microservicios para un carrito de compras utilizando Spring Boot y siguiendo las mejores prácticas de diseño y arquitectura.

## Visión General

La aplicación está dividida en cuatro microservicios principales:

1. **Product Service**: Actúa como un proxy para la API externa FakeStoreAPI
2. **Order Service**: Gestiona órdenes y sus detalles
3. **Payment Service**: Simula el proceso de pago
4. **Security Service**: Proporciona autenticación y autorización a través de JWT

## Requisitos

- Java 17
- Maven
- Git

## Estructura del Proyecto

```
carrito-compras/
├── product-service/
├── order-service/
├── payment-service/
└── security-service/
```

## Tecnologías Utilizadas

- **Spring Boot 3.2.0**: Framework base para los microservicios
- **Spring Data JPA**: Para la persistencia de datos
- **Spring Security**: Para la seguridad y autenticación
- **H2 Database**: Base de datos en memoria para desarrollo y pruebas
- **MapStruct**: Para el mapeo entre entidades y DTOs
- **Lombok**: Para reducir el código repetitivo
- **JWT**: Para la autenticación basada en tokens

## Configuración y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/rguevarasv/banco-cuscatlan
cd carrito-compras
```

### 2. Compilar los microservicios

```bash
cd product-service
mvn clean install

cd ../order-service
mvn clean install

cd ../payment-service
mvn clean install

cd ../security-service
mvn clean install
```

### 3. Ejecutar los microservicios

Abre 4 terminales diferentes, una para cada microservicio:

**Terminal 1 - Product Service**

```bash
cd product-service
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8081`

**Terminal 2 - Order Service**

```bash
cd order-service
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8082`

**Terminal 3 - Payment Service**

```bash
cd payment-service
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8083`

**Terminal 4 - Security Service**

```bash
cd security-service
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8084`

## API Endpoints

### Product Service

- `GET /api/products`: Obtener todos los productos
- `GET /api/products/{id}`: Obtener un producto por ID
- `GET /api/products/category/{category}`: Obtener productos por categoría
- `GET /api/products/categories`: Obtener todas las categorías

### Order Service

- `GET /api/orders`: Obtener todas las órdenes
- `GET /api/orders/{id}`: Obtener una orden por ID
- `GET /api/orders/customer/{customerId}`: Obtener órdenes por cliente
- `POST /api/orders`: Crear una orden
- `PUT /api/orders/{id}`: Actualizar una orden
- `DELETE /api/orders/{id}`: Eliminar una orden
- `PATCH /api/orders/{id}/status`: Actualizar el estado de una orden

### Payment Service

- `GET /api/payments`: Obtener todos los pagos
- `GET /api/payments/{id}`: Obtener un pago por ID
- `GET /api/payments/order/{orderId}`: Obtener pagos por orden
- `POST /api/payments/process`: Procesar un pago
- `POST /api/payments/{id}/refund`: Reembolsar un pago

### Security Service

- `POST /api/auth/register`: Registrar un nuevo usuario
- `POST /api/auth/authenticate`: Autenticar un usuario y obtener un token JWT

## Pruebas con Postman

Se incluye una colección de Postman en el archivo `Shopping Cart Microservices API.postman_collection.json`. Importa esta colección en Postman para probar todos los endpoints.

## Características Principales

1. **Arquitectura de Microservicios**: La aplicación está divida en microservicios independientes
2. **Patrones de Diseño**:
   - Repository Pattern
   - DTO Pattern
   - Service Layer Pattern
   - Dependency Injection
3. **Principios SOLID**:
   - Single Responsibility
   - Open/Closed
   - Liskov Substitution
   - Interface Segregation
   - Dependency Inversion
4. **Manejo de Errores**:
   - GlobalExceptionHandler en cada microservicio
   - DTOs de respuesta personalizados para errores API
   - Validación de entradas
5. **Seguridad**:
   - Autenticación basada en JWT
   - Control de acceso basado en roles
   - Encriptación de contraseñas

## Esquema de la Base de Datos

### Customer

```
id: Long (PK)
firstName: String
lastName: String
email: String (unique)
address: String
phone: String
```

### Order

```
id: Long (PK)
customer_id: Long (FK)
orderDate: LocalDateTime
status: Enum (CREATED, PROCESSING, COMPLETED, CANCELLED)
totalAmount: BigDecimal
shippingAddress: String
```

En la consola del H2 bajo la url http://localhost:8082/h2-console
se debe ingrsar con los siguientes datos:

driver class: org.h2.Driver
jdbc url: jdbc:h2:mem:orderdb
usuario: sa
password: 

y procedemos a ingresar la siguiente query

```sql
INSERT INTO customers (first_name, last_name, email, address, phone) 
VALUES ('Rafael', 'Guevara', 'rguevara@correo.com', 'Chalchuapa, Santa Ana', '1234-5678');
```

### OrderDetail

```
id: Long (PK)
order_id: Long (FK)
productId: Long
productName: String
quantity: Integer
unitPrice: BigDecimal
subtotal: BigDecimal
```

### Payment

```
id: Long (PK)
orderId: Long
amount: BigDecimal
paymentDate: LocalDateTime
status: Enum (PENDING, COMPLETED, FAILED, REFUNDED)
paymentMethod: String
transactionId: String
```

### User

```
id: Long (PK)
firstName: String
lastName: String
email: String (unique)
password: String (encoded)
role: Enum (USER, ADMIN)
```