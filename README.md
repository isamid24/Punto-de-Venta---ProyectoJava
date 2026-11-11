Sistema de Punto de Venta (POS) - Java, Spring Boot y Vaadin

1. Descripcion General del Sistema

El Sistema de Punto de Venta (POS) es una aplicacion web robusta desarrollada en Java 25 utilizando el framework Spring Boot para el entorno de servidor y Vaadin Flow para la interfaz de usuario web responsive. La aplicacion permite gestionar las operaciones fundamentales de un establecimiento comercial, incluyendo el control de inventario de productos, la administracion de clientes, el procesamiento de ventas en tiempo real con carrito dinámico, la generacion de reportes estadisticos y el arqueo diario de caja con impresion digital de tirilla POS e historial de cierres previos.

Toda la informacion generada por la aplicacion se almacena de forma persistente en archivos de texto plano (.txt) mediante operaciones de entrada y salida en Java (Java I/O), evitando el uso de bases de datos relacionales externas y garantizando el cumplimiento estricto de los requerimientos de persistencia liviana.

2. Modulos Principales y Funcionalidades

2.1 Gestion de Productos (CRUD Completo)

Creacion: Registro de nuevos productos especificando codigo unico, nombre, precio unitario y cantidad en stock.

Lectura: Visualizacion interactiva de todo el inventarios en grillas (Grid) con soporte para desplazamientos.

Actualizacion: Modificacion de precios, nombres o stock existente directamente desde la interfaz.

Eliminacion: Borrado de productos con confirmacion previa mediante cuadros de dialogo flotantes (Dialog).

2.2 Gestion de Clientes (CRUD Completo)

Registro de Clientes: Captura de datos personales incluyendo cedula de ciudadania o NIT, nombre completo, telefono y correo electronico.

Validacion de Documentos: Restriccion de duplicados y campos con formato numérico.

Consultas: Busqueda y actualizacion de datos de contacto de clientes registrados.

2.3 Módulo de Ventas (Caja Registradora)

Seleccion Dinamica: Seleccion de cliente mediante lista desplegable (ComboBox) y seleccion de productos.

Gestion de Carrito en Memoria: Uso de estructuras ArrayList para agregar, modificar o remover items antes de confirmar la compra.

Calculos Automaticos: Calculo en tiempo real de subtotal, IVA, total a pagar y cambio a entregar segun el monto recibido.

Actualizacion Automatica de Inventario: Descuento inmediato de stock en productos.txt al concretar la venta.

2.4 Reportes y Estadisticas

Top 5 de Productos Mas Vendidos: Algoritmo de ordenamiento comparativo que calcula los 5 productos con mayor volumen de unidades vendidas.

Tarjetas de Informacion (Cards): Metricas resumidas como el total de ingresos generados acumulados.

2.5 Arqueo y Cierre de Caja (Modulo de Bonificacion)

Calculo Automatico de Cuadre: Lectura automatica de ventas del dia actual sumadas a la base inicial para calcular el efectivo esperado.

Estados de Caja: Clasificacion dinamica en CUADRADO, SOBRANTE o FALTANTE con indicadores de color en la interfaz.

Generacion de Tirilla POS (Modal): Despliegue de un comprobante digital estilizado tipo recibo termico dentro de un componente Dialog, con tipografia monoespaciada y bordes punteados.

Cierre Rapido con X: Boton explícito de cierre con icono 'X' ubicado en la esquina superior derecha del modal.

Historial de Cierres Anteriores: Tabla interactiva cargada desde cierres.txt con buscador para consultar arqueos realizados en fechas pasadas.

3. Estructura Completa del Proyecto

punto-de-venta/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── examble/
│                   └── puntodeventa/
│                       ├── PuntodeventaApplication.java   # Clase principal de entrada Spring Boot
│                       ├── archivos/                      # Capa de Persistencia (Lectura y Escritura I/O)
│                       │   ├── ArchivoClientes.java
│                       │   ├── ArchivoProductos.java
│                       │   ├── ArchivoVentas.java
│                       │   └── ArchivoCierres.java        # Gestion de persistencia para arqueos de caja
│                       ├── layout/                        # Capa de Interfaz de Usuario (Views de Vaadin)
│                       │   ├── MainLayout.java            # Layout raiz con menu lateral de navegacion
│                       │   ├── ProductosView.java         # Vista de administracion de productos
│                       │   ├── ClientesView.java          # Vista de administracion de clientes
│                       │   ├── VentaView.java             # Vista del punto de venta y carrito
│                       │   ├── ReportesView.java          # Vista de estadisticas y Top 5
│                       │   └── CierreCajaView.java        # Vista de arqueo, tirilla e historial de caja
│                       └── modelos/                       # Capa de Entidades de Negocio (POJOs)
│                           ├── Clientes.java
│                           ├── Productos.java
│                           ├── LineaVenta.java
│                           ├── Venta.java
│                           └── CierreCaja.java            # Modelo estructurado para registros de cuadre
├── productos.txt                                          # Archivo plano de base de datos de productos
├── clientes.txt                                           # Archivo plano de base de datos de clientes
├── ventas.txt                                             # Archivo plano de historial de transacciones
├── cierres.txt                                            # Archivo plano de historial de arqueos de caja
└── pom.xml                                                # Configuracion de dependencias Maven


4. Especificacion de Persistencia y Formato de Archivos Planos

Los datos son almacenados en la raiz del proyecto utilizando la barra vertical (|) como delimitador de campos:

productos.txt

Formato: codigo|nombre|precio|stock

Ejemplo: PROD-01|Leche Entera 1L|4200.0|50

clientes.txt

Formato: cedula|nombre|telefono|email

Ejemplo: 1017123456|Juan Perez|3001234567|juan@example.com

ventas.txt

Formato: idVenta|fechaHora|cedulaCliente|total|detalles

Ejemplo: VEN-84920|2026-11-11 14:30:00|1017123456|12600.0|Leche Entera 1L x3

cierres.txt

Formato: idCierre|fechaHora|baseInicial|totalVentas|totalEsperado|efectivoContado|diferencia|estado

Ejemplo: CIE-1024|2026-11-11 18:30:00|50000.0|125000.0|175000.0|175000.0|0.0|CUADRADO

5. Requisitos del Sistema y Tecnologias Utilizadas

Requisitos de Software

Java Development Kit (JDK): Version 17 o superior.

Gestor de Construccion: Maven 3.8+

Entorno de Desarrollo Recomenado: Visual Studio Code o IntelliJ IDEA.

Stack Tecnologico

Lenguaje: Java 17

Framework Backend: Spring Boot 3.x

Framework Frontend UI: Vaadin Flow 24+

Manejo de Colecciones: ArrayList, HashMap, Java Streams API

Persistencia: Java File I/O (BufferedReader, BufferedWriter, FileReader, FileWriter)

6. Instrucciones de Instalacion y Ejecucion

Abrir la carpeta del proyecto en la terminal o IDE de preferencia.

Verificar la presencia de los archivos de texto .txt en el directorio raiz del proyecto.

Compilar el proyecto ejecutando el comando de Maven:

mvn clean install


Iniciar la aplicacion ejecutando la clase principal PuntodeventaApplication.java o mediante el comando:

mvn spring-boot:run


Abrir un navegador web e ingresar a la direccion local:

http://localhost:8080


7. Modulo de Cierre de Caja y Bonificacion Tecnica (0.5)

El modulo CierreCajaView fue disenado para dar cumplimiento completo a las especificaciones requeridas para la bonificacion:

Diseno en formato Tirilla: La tirilla POS se presenta en una ventana modal (Dialog) con configuracion CSS especifica (Courier New, fondo estilo papel de recibo #fdfbf7, bordes punteados y alineacion estructurada de totales y firmas).

Boton de salida X: La tirilla incluye en su esquina superior derecha un boton con el icono de cierre (CLOSE) que ejecuta dialog.close() al ser presionado, permitiendo salir del comprobante en cualquier momento.

Consulta de Cierres Anteriores: Se implementa un panel de historial con un Grid<CierreCaja> conectado al archivo cierres.txt y un filtro de busqueda en tiempo real que permite auditar arqueos realizados en fechas previas.

8. Arquitectura de Software y Patrones Aplicados

Separacion por Capas: El codigo esta dividido en las capas de modelos (entidades POJO), archivos (persistencia I/O) y layout (vistas e interfaz web).

Herencia en UI: Las clases de vista extienden de contenedores de Vaadin como VerticalLayout.

Programacion Orientada a Objetos (POO): Encapsulamiento de atributos, constructores parametrizados y metodos accesores (getters y setters).

Manejo de Excepciones: Bloques try-catch para capturar errores de lectura y escritura de archivos (IOException), garantizando la estabilidad de la aplicacion.