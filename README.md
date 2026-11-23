# API de órdenes de transporte

API REST para manejar órdenes de transporte, conductores y la asignación de un conductor a cada orden (con su PDF y su imagen de evidencia). 
- Stack: Java 17 y Spring Boot 3.
- Persistencia: Base de datos H2 en memoria

## Requerimientos

- JDK 17 o superior
- Maven 3.9 o superior

## Como ejecutar el API

```bash
  mvn spring-boot:run
```
## Cómo correr las pruebas

```bash
  mvn test
```
Servidor: `http://localhost:8080`.

Swagger: `http://localhost:8080/swagger-ui.html`

## Endpoints

| Método | Ruta | Para qué sirve |
|--------|------|----------------|
| POST | `/api/auth/register` | Crear usuario y recibir token |
| POST | `/api/auth/login` | Iniciar sesión y recibir token |
| POST | `/api/orders` | Crear una orden (nace en `CREATED`) |
| GET | `/api/orders/{id}` | Consultar una orden |
| GET | `/api/orders` | Listar órdenes con filtros |
| PATCH | `/api/orders/{id}/status` | Cambiar el estado de la orden |
| POST | `/api/drivers` | Crear un conductor |
| GET | `/api/drivers/active` | Listar los conductores activos |
| POST | `/api/orders/{id}/assignment` | Asignar un conductor a la orden |
| GET | `/api/orders/{id}/assignment` | Ver la asignación de la orden |
| POST | `/api/orders/{id}/assignment/pdf` | Adjuntar un PDF |
| POST | `/api/orders/{id}/assignment/image` | Adjuntar una imagen (.png, .jpg) |

Filtros de `GET /api/orders` (todos opcionales y combinables):

- `status`: `CREATED`, `IN_TRANSIT`, `DELIVERED` o `CANCELLED`
- `createdFrom` y `createdTo`: fechas `yyyy-MM-dd`, incluyen el día completo
- `origin` y `destination`: texto que debe estar contenido en el origen o destino (sin importar mayúsculas)

## Configuración

Está en `src/main/resources/application.properties`. Lo que probablemente quieras cambiar en un entorno real se puede pasar por variable de entorno:

| Variable | Qué hace | Valor por defecto |
|----------|----------|-------------------|
| `JWT_SECRET` | Secreto para firmar los tokens (mínimo 32 caracteres) | uno de desarrollo, cámbialo |
| `STORAGE_DIR` | Carpeta donde se guardan los archivos | `uploads` |

Los logs salen en JSON, una línea por evento. Cada petición lleva un `requestId` (también va en el header `X-Request-Id`) para poder seguirla en los logs.

## Cómo está organizado

```
src/main/java/com/transportes/api
├── auth         registro y login
├── security     JWT, filtro y configuración de Spring Security
├── user         usuarios de la API
├── order        órdenes, estados y filtros
├── driver       conductores
├── assignment   asignación conductor-orden y sus archivos
├── storage      guardado de PDF e imágenes en disco
├── config       configuración de Swagger
└── common       manejo de errores y logging
```

Cada módulo sigue la misma estructura: controlador, servicio, repositorio, entidad, DTOs y un mapper escrito a mano.

## Notas

- Como la base es en memoria, al reiniciar se pierden usuarios, conductores y órdenes (los archivos de `uploads/` sí se quedan en disco).

## Ejecución con Docker

Este proyecto incluye un `Dockerfile` multi-stage build para empaquetar y ejecutar la aplicación de forma contenerizada.

### Construir la imagen de Docker
Ubicado en la raíz del proyecto (donde se encuentra el `Dockerfile`), ejecutar:

```bash
docker build -t transport-orders-api .
```


### Levantar el contenedor
La aplicación requiere una clave secreta para la validación de seguridad (JWT). Debes pasarla mediante la variable de entorno JWT_SECRET al momento de levantar el contenedor:

```bash
docker run --name transport-api -p 8080:8080 -e JWT_SECRET=TU_TOKEN_JWT_VALIDO transport-orders-api
```


