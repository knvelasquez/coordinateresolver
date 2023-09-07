
# Microservicio Coordinate Resolver

### Spring Boot, java17,  Cola de Mensajes(Kafka) y PostgreSQl
Proxy inverso que valida las solicitudes, recibe el payload completo con el mensaje y distancia de cada uno de los satelites
Determina cual solicitud se puede procesar desde el propio satelite que recibió en la red interna
El resto de la solicitud es enviado/redirigdo hacia la puerta de enlace del siguiente satellite
El cual se determina mediante una logica y con el payload restante

# Tabla de contenido

- [Análisis y Diseño](Readme2.md)
- [Instalación](#instalación)
- [Construir el contenedor](#construir-el-contenedor)
- [Referencia Microservicio](#referencia-microservicio)
- [Usando curl](#usando-curl)

## Instalación

Clonar el repositorio

```bash
git clone git@github.com:knvelasquez/routersatellite.git
```

## Navegar hasta el directorio del proyecto

```bash
cd routersatellite/
```

## Construir el contenedor

```bash
SATELLITE_NAME=kenobi SPRING_BOOT_PORT=9094 docker compose up
```

**Nota**: Se debe establecer **SATELLITE_NAME** para definir el nombre contextual del satellite

**Nota 2** Se puede definir  **SPRING_BOOT_PORT** para indicar el puerto si no se establce se configura el puerto por default **9093**

### Esperar que finalice la creación del contenedor y la ejecución de pruebas
✔ Container router-satellite  Created

**Nota**: para validar que los contenedores esten iniciados correctamente

```bash
docker ps
```
**Output**
```bash
CONTAINER ID   IMAGE                              COMMAND                  CREATED          STATUS         PORTS                                       NAMES
5b79722ec60f   knvelasquez/routersatellite:v1.1   "/usr/local/bin/mvn-…"   53 seconds ago   Up 4 seconds   0.0.0.0:9094->9094/tcp, :::9094->9094/tcp   router-satellite
```

## Referencia Microservicio

#### description

```http
  POST /router/satellite
```

| Body Parameter                    | Type     | Description   |
|:----------------------------------|:---------|:--------------|
| {`satellite`:[{                   |          |               |
| `name`:"satellitename"            | `String` | **Required**. |
| `distance`:100.0                  | `Float`  | **Required**. |
| `message`:["message","separated"] | `Array`  | **Required**. |
| },                                |          |            |
| {...}                             |          |               |
| ]}                                |          |               |

## Usando **curl**

Se pueden utilizar estos comandos directamente en la terminal de la siguiente manera (*Tener curl instalado*)

### POST /calculador
```bash
curl --location --request POST 'unknow/router/satellite' \
--header 'Authorization: Bearer token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "satellites": [
        {
            "name": "satellite1",
            "distance": 100.0,
            "message": [
                "message",
                 "for",
                "satellite1"
                "separated",
                "by",
                "strings",
                "array"
            ]
        },
        {
            "name": "satellite2",
            "distance": 200.0,
            "message": [
                "message",
                 "for",
                "satellite2"
                "separated",
                "by",
                "strings",
                "array"
            ]
        },
        {
            "name": "satellite3",
            "distance": 300.0,
            "message": [
                "message",
                "for",
                "satellite3"
                "separated",
                "by",
                "strings",
                "array"
            ]
        }
    ]
}'
```