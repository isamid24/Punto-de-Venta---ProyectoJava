# Punto de Venta (POS) - Barrio

Sistema web de gestión comercial, control de inventario y punto de venta para negocios locales desarrollado en Java 21, Spring Boot y Vaadin 25.

---

## Índice

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Estado del Proyecto](#estado-del-proyecto)
3. [Demostración de Funciones y Módulos](#demostración-de-funciones-y-módulos)
4. [Componentes Investigados (Proyecto 8)](#componentes-investigados-proyecto-8)
5. [Bonificación: Cierre de Caja Diario](#bonificación-cierre-de-caja-diario)
6. [Acceso al Proyecto e Instalación](#acceso-al-proyecto-e-instalación)
7. [Tecnologías Utilizadas](#tecnologías-utilizadas)
8. [Estructura del Proyecto](#estructura-del-proyecto)
9. [Personas Desarrolladoras del Proyecto](#personas-desarrolladoras-del-proyecto)
10. [Licencia](#licencia)

---

## Descripción del Proyecto

El Punto de Venta (POS) - Barrio es una aplicación web diseñada para digitalizar la gestión comercial de negocios pequeños y medianos. Reemplaza los registros manuales por una interfaz centralizada donde se gestionan inventarios, información de clientes, ventas en carrito, cálculo de cambio en tiempo real, histórico de transacciones y analítica de negocio.

El sistema está construido bajo una arquitectura orientada a componentes UI con Vaadin 25 y un esquema de persistencia en archivos planos de texto (`.txt`) utilizando campos delimitados por el carácter tubo (`|`). Esto garantiza la recuperación automática y durabilidad de los datos ante reinicios de la aplicación sin depender de motores de bases de datos relacionales externos.

---

## Estado del Proyecto

El proyecto se encuentra finalizado, validado y en estado de producción para su correspondiente sustentación académica. Cumple con la totalidad de los requerimientos del Proyecto 8, los lineamientos del Núcleo Común y la implementación de la Bonificación (+0.5) correspondiente al Cierre de Caja Diario.

---

## Demostración de Funciones y Módulos

### Módulo de Venta (Caja) (`/`)
* Selección interactiva de productos del catálogo cargado en memoria.
* Control estricto de disponibilidad de inventario (evita registrar ventas superiores al stock existente).
* Modificación dinámica del carrito de compras con cálculo automático de subtotales y total general.
* Columna de acción rápida en la grilla para eliminar renglones individuales del carrito.
* Menú desplegable para vinculación opcional de cliente registrado o asignación automática a "Consumidor Final".
* Validación del monto recibido frente al total a pagar con cálculo inmediato del cambio a entregar.

### Módulo de Catálogo de Productos (`/productos`)
* Gestión integral CRUD (Crear, Leer, Modificar y Eliminar productos).
* Búsqueda en tiempo real mediante campo de texto (`txtBuscar`) que filtra por código o nombre.
* Validaciones de entrada mediante patrones (código en formato `P` seguido de tres dígitos, precios y existencias numéricas positivas).

### Módulo de Gestión de Clientes (`/clientes`)
* Formulario para registro y actualización de datos de clientes (Cédula, Nombre Completo y Teléfono).
* Disposición simétrica a dos columnas alineada con el estándar visual del catálogo.
* Búsqueda interactiva por número de documento o nombre.
* Diálogo modal de confirmación (`Dialog`) previo a cualquier operación de borrado.

### Módulo de Historial de Ventas (`/historial`)
* Consulta estructurada en grilla de todas las transacciones procesadas.
* Exposición de ID de venta, fecha/hora, cédula del cliente asociado, total facturado, monto pagado y cambio generado.

### Módulo de Cierre de Caja Diario (`/cierre-caja`)
* Ingreso de datos de Base Inicial y Efectivo Físico Contado mediante componentes numéricos.
* Sumatoria dinámica de las transacciones registradas durante la fecha en curso leyendo `ventas.txt`.
* Cálculo del resultado del arqueo: `Total Esperado = Base Inicial + Ventas del Día`.
* Clasificación condicional por color según el balance:
  * Verde: Caja cuadrada (Diferencia igual a $0.00).
  * Azul: Sobrante de dinero detectado.
  * Rojo: Faltante de dinero detectado.

### Módulo de Reportes y Estadísticas (`/reportes`)
* Indicadores ejecutivos en tarjetas visuales (`Card`) para Ventas del Día, Ventas del Mes y Producto Estrella.
* Grilla de clasificación del Top 5 de productos más vendidos, ordenada descendentemente por rotación comercial.

---

## Componentes Investigados (Proyecto 8)

| Componente | Descripción de Uso | Enlace a Documentación |
| --- | --- | --- |
| **`NumberField`** | Utilizado para la captura de cantidades en el carrito, ingreso del monto recibido en caja y montos de efectivo en el arqueo diario, restringiendo la entrada a valores estrictamente numéricos. | [Documentación de NumberField](https://vaadin.com/docs/latest/components/number-field) |
| **`SplitLayout`** | Utilizado en la pantalla principal de caja para dividir la interfaz en dos páneles con barra deslizable (65% catálogo/carrito y 35% proceso de cobro). | [Documentación de SplitLayout](https://vaadin.com/docs/latest/components/split-layout) |
| **`Card`** | Estructuración visual de contenedores en el módulo de reportes para presentar métricas clave de forma ejecutiva. | [Documentación de Layouts / Cards](https://vaadin.com/docs/latest/components/card) |

---

## Bonificación: Cierre de Caja Diario (+0.5)

El sistema incorpora la funcionalidad completa de arqueo diario en la clase `CierreCajaView.java`. Esta vista procesa en tiempo real los registros guardados en `ventas.txt`, filtra las transacciones que coinciden con la fecha actual del sistema (`yyyy-MM-dd`), añade la base inicial declarada por el usuario y contrasta el total esperado frente al efectivo contado, notificando inmediatamente las diferencias en pantalla.

---

## Acceso al Proyecto e Instalación

### Requisitos Previos
* Java Development Kit (JDK) versión 21 o superior.
* Apache Maven versión 3.9 o superior (o uso del ejecutable `mvnw` incluido).
* Entorno de desarrollo compatible (Visual Studio Code, IntelliJ IDEA o Eclipse).

### Pasos para la Ejecución

1. Clonar el repositorio remoto:
   ```bash
   git clone https://github.com/tu-usuario/proyecto-pos.git
   cd proyecto-pos
   ```

2. Compilar la aplicación y ejecutar el servidor embebido:
   * En sistemas Microsoft Windows:
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```
   * En sistemas Linux / macOS:
     ```bash
     ./mvnw spring-boot:run
     ```

3. Acceder a la aplicación web:
   Abrir un navegador e ingresar a la URL `http://localhost:8080`.

---

## Tecnologías Utilizadas

* **Lenguaje de Programación:** Java 21
* **Framework Backend:** Spring Boot 3.x
* **Framework Frontend / UI:** Vaadin 25 (Componentes de acceso gratuito)
* **Persistencia de Datos:** Archivos de texto plano (`.txt`) mediante paquetes `java.io` (`BufferedReader`, `BufferedWriter`)
* **Gestor de Dependencias:** Apache Maven

---

## Estructura del Proyecto

```text
puntodeventa/
├── src/
│   └── main/
│       └── java/
│           └── com/examble/puntodeventa/
│               ├── archivos/
│               │   ├── ArchivoClientes.java
│               │   ├── ArchivoProductos.java
│               │   └── ArchivoVentas.java
│               ├── layout/
│               │   ├── CierreCajaView.java
│               │   ├── ClientesView.java
│               │   ├── HistorialView.java
│               │   ├── MainLayout.java
│               │   ├── ProductosView.java
│               │   ├── ReportesView.java
│               │   └── VentaView.java
│               └── modelos/
│                   ├── CierreCaja.java
│                   ├── Clientes.java
│                   ├── LineaVenta.java
│                   ├── Productos.java
│                   └── Venta.java
├── clientes.txt
├── productos.txt
├── ventas.txt
└── pom.xml
```

---

## Personas Desarrolladoras del Proyecto

| Nombre | Rol | Carácter |
| --- | --- | --- |
| **Isabella Sánchez Posada** | Desarrolladora Única | Trabajo Individual |

---

## Licencia

Este proyecto se distribuye bajo los términos de la Licencia MIT. Desarrollado como entrega académica individual para la asignatura de Lógica de Programación / Aplicaciones Web con Vaadin.