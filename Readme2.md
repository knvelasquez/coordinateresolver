# Análisis y Diseño

Un breve resumen de como imaginé el diseño de esta solución.

# Tabla de contenido

- [Volver al inicio](README.md)
- [Introducción](#introducción)
- [Arquitectura general](#arquitectura-general)
  - [Red-interna-satelite](#red-interna-satelite)
  - [Torre_de_control](#torre-de-control)
- [Arquitectura_completa](#arquitectura-completa)

## Introducción
Me enfoqué principalemente en descomponer las responsabilidades de una manera distribuida y usando microservicios, cada uno de los cuáles tendrá una función específica del negocio y que tiene la posibilidad de implementarse 
y escalarse de forma independiente, lo cual facilita la flexibilidad durante el despliegue continuo.

# Arquitectura general
Tomando el espacio como escenario y cada satelite como un servidor me incliné por la idea de crear una red interna dentro
de cada **satelite** y una **torre de control**

![Space_satellites](https://i.imgur.com/UBuH893.png)

## Red interna satelite

Cada satelite dispone de su propia red interna aislada e independiente


![Nework_satellites](https://i.imgur.com/GreEVR9.png)

Servicios:
- **Apigateway**:
  - El punto único de acceso en cada satelite 
    - Puerta de enlace/apigateway (**nginx**)
  - Mantiene las reglas que redirigen las correspondientes solicitudes hacia los servicios privados 
    - **jwtAuth** y **RouterSatellite**
  - La nomenclatura es {SATELLITE_NAME}.apigateway
    - Ejemplo **kenobi.apigateway**-**skywalker.apigateway**-**sato.apigateway**

- **[jwtauth](https://github.com/knvelasquez/jwtauth)**: 
  - Expone una interfaz/adaptador rest 
    - En **springboot** y por medio del **apigateway** 
  - Autentica cada request con el id de la nave(**idShip**) y el secreto/clave privada(**secret**)
  - Responde un jwt el cual deberá indicarse dentro del header en cada request 
    - Como mecanismo de seguridad

- **[RouterSatellite](https://github.com/knvelasquez/routersatellite)**:
  - Es el proxy inverso que valida las solicitudes
  - Recibe el payload completo con el mensaje y distancia de cada uno de los satelites
  - Determina cual solicitud se puede procesar desde el propio satelite que recibió en la red interna
  - El resto de la solicitud es enviado/redirigdo hacia la puerta de enlace del siguiente satellite
    - El cual se determina mediante una logica y con el payload restante
 

- **[CoordinatorResolver](https://github.com/knvelasquez/coordinateresolver)**:
  - Recibe un payload desmenuzado con el mensaje recibido en el satelite
  - Calcula las coordenadas mediante la triangulación de la posición de los demás satelites
  - Procesa el mensaje
  - Encrypta el resultado del mensaje y las coordenadas en un objeto y agrega la clave privada que fué recibida como un secreto
  - La idea es que el mensaje pueda ser desencryptado por otra entidad que pueda aportar el mismo secreto que se indico para encryptar por primera vez
    - Esto agrega una capa de seguridad adicional para que el caso de hackeo o perdida de datos ya que todo lo almancenado en esa zona desmilitarizada se encuentra encryptado
    

- **Servicio de cola de mesajes**:
  - Almacena los mensaje encriptados en un **topico de satellite**
  - Dispone este mecanismo para devolver el mensaje encryptado a la torre de control ya que se encuentra suscrito a este topico

![Nework_control_tower](https://i.imgur.com/j6ctz5E.png)

Por otro lado dispuse un lugar denominado **torre de control** el cual funciona como un servidor maestro y en donde se obtendrán los mensajes
**cifrados/encryptados** por cada uno de los satelites

## Torre de Control
Expone una interfaz/adaptador rest en **spring** que permite a los operadores de la torre de control conocer la totalidad de mensajes
obtenidos desde los diversos puntos de cada satelite tambien se podra obtener el detalle de 
mensaje por medio de un mecanismo de desencriptacion.

Servicios:

**[jwtauth](https://github.com/knvelasquez/jwtauth)**:
- Expone una interfaz/adaptador rest
    - En **springboot** y por medio del **apigateway**
- Autentica cada request con el id de la nave(**idShip**) y el secreto/clave privada(**secret**)
- Responde un jwt el cual deberá indicarse dentro del header en cada request
    - Como mecanismo de seguridad
  

**[MessageConsumer](https://github.com/knvelasquez/messageconsumer) Listener**: 
- Se suscribe al topico de cada uno de los satellites y obtiene los mensajes que se encuentran almacenados
- Persiste el mensaje obtenido hacia una base de datos en **postgresql**

**[MessageConsumer](https://github.com/knvelasquez/messageconsumer) Rest**:
- Expone un servicio rest/adaptador en spring que permite a los operadores de la torre de control conocer el total de mensajes registrados en la base de datos
- La información es obtenida mediante un json paginado
  - Para permitir mejor indexación de los resultados obtenidos y no sea lenta la respuesta
  - Para poder utilizar este servicio el operador debe proveer un jwt en el encabezado de la solicitud
    - Permite añadir una capa de seguridad en cada request

- Expone un servicio rest/adaptador en spring que permite obtener el detalle de un mensaje mediante su id
- Para poder utilizar este servicio el operador debe proveer un jwt en el encabezado de la solicitud
  - Permite añadir una capa de seguridad en cada request
- Desencrypta el mensaje mediante el secret que se encuentra en los claims del jwt
  - Si concuerda el secret devuelve el mensaje desencryptado
  - En caso contrario devolvera un mensaje indicando que no ha sido posible desencriptar

**DataBase**:
- Almacena tods los mensaje que se recibieron por cada uno de los satelites
  - De manera encryptada
- Unifica todos los mensajes obtenidos en cada uno de los satellites
- Permanece en una red segura y no desmilitarizada como los satelites

![Control_tower](https://i.imgur.com/k7EchoM.png)

# Arquitectura completa
![Control_tower](https://i.imgur.com/6h3LVIq.png)
