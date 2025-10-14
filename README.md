Funcionalidades Implementadas

La aplicación cuenta con un ciclo de vida completo para la gestión de órdenes de servicio (CRUD):

* **Crear Órdenes (Create)**: Un formulario validado permite a los usuarios registrar nuevas órdenes de servicio, detallando el cliente, el equipo y el problema.
* **Leer Órdenes (Read)**:
    * Una pantalla principal muestra una lista de todas las órdenes de servicio existentes.
    * Al seleccionar una orden, se navega a una pantalla de detalles que muestra toda la información registrada.
* **Actualizar Órdenes (Update)**: Desde la pantalla de detalles, es posible cambiar el estado de la orden (`Pendiente`, `En Proceso`, `Finalizado`). El cambio se refleja tanto en los detalles como en la lista principal.
* **Eliminar Órdenes (Delete)**: Se puede eliminar una orden de servicio de forma permanente desde la pantalla de detalles.

### Mejoras de UI/UX ✨

* **Feedback Visual por Estado**: En la lista principal, las tarjetas de cada orden cambian de color según su estado, permitiendo una identificación visual rápida.
* **Mensajes de Confirmación**: La aplicación utiliza Snackbars para notificar al usuario cuando una orden ha sido guardada o eliminada con éxito.

### Integración de Recursos Nativos 📱

Para cumplir con los requisitos del proyecto, se integraron dos funcionalidades nativas clave:

1.  **Cámara**: En la pantalla de detalles, el usuario puede tomar una foto del equipo utilizando la cámara del dispositivo. La foto capturada se muestra en la interfaz. Se manejan los permisos de cámara y el almacenamiento temporal de la imagen.
2.  **Geolocalización (GPS)**: Al crear una nueva orden, la aplicación permite registrar la ubicación geográfica actual del dispositivo. Estas coordenadas (latitud y longitud) se guardan en la base de datos y se muestran en los detalles de la orden.

---

## 🏗️ Arquitectura y Tecnologías Utilizadas

El proyecto fue desarrollado siguiendo las mejores prácticas recomendadas por Google para el desarrollo de Android.

* **Lenguaje**: **Kotlin**
* **Interfaz de Usuario (UI)**: **Jetpack Compose**, el framework declarativo moderno para construir interfaces de usuario nativas.
* **Arquitectura**: **MVVM (Model-View-ViewModel)**, separando la lógica de la interfaz para un código más limpio, mantenible y fácil de probar.
* **Persistencia de Datos**: **Room Persistence Library**, una capa de abstracción sobre SQLite que facilita la creación y gestión de la base de datos local.
* **Navegación**: **Jetpack Navigation for Compose** para gestionar el flujo entre las diferentes pantallas de la aplicación.
* **Operaciones Asíncronas**: **Kotlin Coroutines** para manejar operaciones en segundo plano (como el acceso a la base de datos) sin bloquear el hilo principal.
* **Dependencias Adicionales**:
    * **Coil**: Para cargar y mostrar imágenes de manera eficiente.
    * **Google Play Services Location**: Para obtener la ubicación del dispositivo de forma precisa.

---

## 📂 Estructura del Proyecto

El código fuente está organizado en los siguientes paquetes para una clara separación de responsabilidades:

* `database`: Contiene la definición de la base de datos (AppDatabase), los DAOs y los TypeConverters.
* `model`: Define las clases de datos (Entities) que representan las tablas de la base de datos, como `ServiceOrder`.
* `screens`: Contiene todos los componentes de la interfaz de usuario (`@Composable`) para cada pantalla de la aplicación.
* `viewmodel`: Contiene las clases `ViewModel` que exponen los datos y la lógica de negocio a las pantallas.

---

## 🚀 Cómo Ejecutar el Proyecto

Para clonar y ejecutar este proyecto en tu propia máquina, sigue estos pasos:

1.  **Clona el repositorio** desde GitHub:
    ```bash
    git clone [URL-DE-TU-REPOSITORIO]
    ```
2.  **Abre el proyecto** en la última versión estable de Android Studio.
3.  **Sincroniza Gradle**: Android Studio detectará el archivo `build.gradle.kts` y descargará automáticamente todas las dependencias necesarias.
4.  **Ejecuta la aplicación**: Selecciona un emulador o un dispositivo físico y presiona el botón "Run" (▶).

---

## 👥 Autor

* [Paulo Loyola Ortiz]

- Documento Readme revisado y complementado con ayuda de Gemini 2.5
