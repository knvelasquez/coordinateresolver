
# Microservicio Coordinate Resolver

### Spring Boot, java17,  Cola de Mensajes(Kafka) y PostgreSQl
Recibe un payload desmenuzado con el mensaje recibido en el satelite
Calcula las coordenadas mediante la triangulación de la posición de los demás satelites
Procesa el mensaje
Encrypta el resultado del mensaje y las coordenadas en un objeto y agrega la clave privada que fué recibida como un secreto
La idea es que el mensaje pueda ser desencryptado por otra entidad que pueda aportar el mismo secreto que se indico para encryptar por primera vez
Esto agrega una capa de seguridad adicional para que el caso de hackeo o perdida de datos ya que todo lo almancenado en esa zona desmilitarizada se encuentra encryptado

# Tabla de contenido

- [Análisis y Diseño](Readme2.md)
- [Instalación](#instalación)
- [Construir el contenedor](#construir-el-contenedor)
- [Referencia Microservicio](#referencia-microservicio)
- [Usando curl](#usando-curl)

## Instalación

Clonar el repositorio

```bash
git git@github.com:knvelasquez/coordinateresolver.git
```

## Navegar hasta el directorio del proyecto

```bash
cd coordinateresolver/
```

## Construir el contenedor

```bash
SPRING_BOOT_PORT=9093 docker compose up
```

**Nota 2** Se puede definir  **SPRING_BOOT_PORT** para indicar el puerto si no se establce se configura el puerto por default **9093**

### Esperar que finalice la creación del contenedor y la ejecución de pruebas
✔ Container zookeeper            Created                                                                                                                                                                                  0.0s
✔ Container kafka                Created                                                                                                                                                                                  0.0s
✔ Container coordinate-resolver  Created

**Nota**: para validar que los contenedores esten iniciados correctamente

```bash
docker ps
```
**Output**
```bash
CONTAINER ID   IMAGE                              COMMAND                  CREATED          STATUS         PORTS                                       NAMES
CONTAINER ID   IMAGE                                 COMMAND                  CREATED              STATUS          PORTS                                       NAMES
bb511851ecb0   bitnami/kafka:latest                  "/opt/bitnami/script…"   About a minute ago   Up 7 seconds    0.0.0.0:9092->9092/tcp, :::9092->9092/tcp   kafka
c83a4bdc0303   knvelasquez/coordinateresolver:v1.1   "/usr/local/bin/mvn-…"   About a minute ago   Up 7 seconds    0.0.0.0:9093->9093/tcp, :::9093->9093/tcp   coordinate-resolver
44dbf8a9d1d9   bitnami/zookeeper:latest              "/opt/bitnami/script…"   2 minutes ago        Up 7 seconds    2181/tcp, 2888/tcp, 3888/tcp, 8080/tcp      zookeeper
```

## Referencia Microservicio

#### description

```http
  POST /coordinate/resolver
```

| Body Parameter                    | Type     | Description   |
|:----------------------------------|:---------|:--------------|
| {`satellite`:{                    |          |               |
| `name`:"satellitename"            | `String` | **Required**. |
| `distance`:100.0                  | `Float`  | **Required**. |
| `message`:["message","separated"] | `Array`  | **Required**. |
| }                                 |          |               |

## Usando **curl**

description

### POST /coordinate/resolver
```bash
curl --location --request POST 'http://api.gateway/coordinate/resolver' \
--header 'Authorization: Bearer token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "satellite-name",
    "distance": 100.0,
    "message": [
        "message",
        "",
        "",
        "test",
        ""
    ]
}'
```

**Nota**: este es un servicio privado por lo que no está expuesto públicamente su uso se realiza mediante la puerta de **enlace/apigateway** y **[RouterSatellite](https://github.com/knvelasquez/routersatellite)**
el cual se encarga de derivar las solicitudes a este microservicio
