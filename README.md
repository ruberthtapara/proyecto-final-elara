# StyleGen AI - Marketplace de Moda Inteligente

## Descripción
StyleGen AI es una aplicación de e-commerce revolucionaria que utiliza Inteligencia Artificial Multimodal (Gemini) para actuar como un asesor de moda personal. Los usuarios pueden explorar un catálogo de productos reales y consultar a la IA si una prenda de la tienda combina con su propia ropa mediante la cámara de su dispositivo.

## Características Principales
- **Catálogo en Tiempo Real:** Consumo de productos desde FakeStoreAPI usando Retrofit.
- **Asesoría IA (Gemini):** Análisis multimodal que combina la imagen del producto de la tienda con una foto tomada por el usuario.
- **Closet Virtual:** Historial de consejos y prendas guardadas localmente usando Room.
- **UI Moderna:** Desarrollada íntegramente con Jetpack Compose y Material 3.
- **Arquitectura Robusta:** Sigue el patrón MVVM con Repository Pattern y Clean Architecture.

## Tecnologías Utilizadas
- **Kotlin & Coroutines/Flow**
- **Jetpack Compose & Material 3**
- **Retrofit & kotlinx.serialization**
- **Room Persistence Library**
- **Google AI SDK (Gemini 1.5 Flash)**
- **Firebase Auth & Storage**
- **Coil 3 (Carga de imágenes)**
- **CameraX**

## Configuración del Proyecto

### 1. Clave de API de Gemini
Para que el Asesor de IA funcione, debes añadir tu clave de API al archivo `local.properties`:
1. Ve a [Google AI Studio](https://aistudio.google.com/) y genera una clave.
2. En la raíz del proyecto, abre `local.properties` y añade:
   ```properties
   GEMINI_API_KEY=tu_clave_aqui
   ```

### 2. Configuración de Firebase
La app está preparada para Firebase Auth y Storage. Para activarlo:
1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Añade una app de Android con el paquete `com.example.proyect_final`.
3. Descarga el archivo `google-services.json` y colócalo en la carpeta `app/`.
4. Habilita **Authentication** (Email/Google) y **Cloud Storage**.

## Estructura del Proyecto
- `data/`: Implementación de APIs, Base de Datos y Repositorios.
- `domain/`: Modelos de negocio e interfaces de repositorios.
- `ui/`: Pantallas de Compose, ViewModels y Componentes reutilizables.
- `util/`: Clases de utilidad y helpers.

## Evaluación (Rúbrica)
Este proyecto cumple con los criterios de la rúbrica de Tecsup:
- **Git:** Mensajes descriptivos y estructura limpia.
- **IA:** Integración central de Gemini 1.5 Flash.
- **Persistencia:** Uso de Room y Retrofit.
- **Servicios:** Preparado para Firebase.
- **UI/UX:** Diseño premium con Material 3.

---
Desarrollado para el Proyecto Final de Programación en Móviles 2026.
