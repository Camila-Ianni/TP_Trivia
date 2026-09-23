# TriviaMaster - TP 1 Aplicaciones Moviles

Trabajo Practico N° 1 para la materia **Aplicaciones Moviles** de la carrera **Analista de Sistemas**.

---

## Descripcion del Proyecto

**TriviaMaster** es una aplicacion movil nativa desarrollada en **Kotlin** para Android con estetica arcade pixel art retro y tematica de monstruos amigables. Integra un **Roadmap interactivo vertical de 16 niveles y Castillo Final**, sistema de **4 Avatares Pixel Art**, sistema de **Medallas de Honor desbloqueables**, minijuego de ordenar letras, sistema de vidas, multiplicador de racha, temporizador interactivo y persistencia 100% local sin base de datos.

---

## Caracteristicas Principales

1. **Roadmap de Niveles Pixel Art (16 Niveles + Gran Castillo Final):**
   - Mapa con scroll continuo y 17 nodos interactivos.
   - Niveles tematicos (Ciencia, Historia, Cine, Musica, Deportes, Videojuegos, etc.).
   - Conquista final del Gran Castillo de la Sabiduria al completar el nivel 16.
2. **Sistema de Avatares Exclusivos (4 Fotos de Perfil Pixel):**
   - Perfil 1: Wincky
   - Perfil 2: Bloop
   - Perfil 3: Chomp
   - Perfil 4: Zook
3. **Sistema de Medallas de Honor:**
   - **Medalla 1 (Iniciado de Trivia):** Superar el Nivel 5 del mapa.
   - **Medalla 2 (Maestro de Sabiduria):** Superar el Nivel 10 del mapa.
   - **Medalla 3 (Conquistador del Castillo):** Superar los 16 niveles y conquistar el Gran Castillo.
   - Visualizacion en escala de grises mientras estan bloqueadas y a todo color al desbloquearse.
   - Dialogo emergente de celebracion al obtener cada medalla.
4. **Minijuego de Ordenar Palabras:**
   - Letras desordenadas para ubicar en casillas interactivas, con escena natural pixel art y feedback inmediato.
5. **Cofre Sorpresa:**
   - Recompensas con animaciones y regalos interactivos de vidas y bonificaciones.

---

## Cumplimiento de Consignas Academicas del TP1

- [x] **Minimo 3 Pantallas (Activities):** La aplicacion cuenta con **6 Activities**:
  1. `MainActivity`: Pantalla de bienvenida, solicitud obligatoria de nombre y lanzamiento del juego.
  2. `MapActivity`: Roadmap interactivo con los 16 niveles y el Castillo Final.
  3. `GameActivity`: Mecanica de trivia con preguntas de opcion multiple, minijuego de palabra desordenada, temporizador y vidas.
  4. `ResultActivity`: Pantalla de resultados con calculo de estrellas, felicitaciones personalizadas y desbloqueo de medallas.
  5. `RankingActivity`: Tabla de posiciones con record personal y podio.
  6. `ProfileActivity`: Gestion del nombre, selector entre los 4 avatares pixel y galeria interactiva de medallas de honor.
- [x] **Navegacion hacia atras permitida en todas las pantallas:** Control del BackStack con botones de retorno dedicados y `android:parentActivityName` en `AndroidManifest.xml`.
- [x] **Solicitud del nombre de usuario utilizado en los mensajes:**
  - Solicitado al ingresar con validacion.
  - Utilizado en el saludo del mapa (*"¡Hola, [Nombre]!"*), en mensajes de victoria, en dialogos de recompensa y en el perfil.
- [x] **Uso de recursos graficos:**
  - Ilustraciones pixel art de monstruos, roadmap ilustrado, 4 avatares exclusivos, 3 medallas de honor, iconos tematicos y botones con relieve retro.
- [x] **SIN USO DE BASES DE DATOS:**
  - Cumplimiento estricto: No se utiliza SQLite, ni Room, ni Firebase, ni bases de datos remotas.
  - La persistencia es 100% local mediante la API nativa de `SharedPreferences` de Android y listas en memoria.

---

## Tecnologias

- **Lenguaje:** Kotlin 2.0+
- **Arquitectura:** Android Nativo con ViewBinding
- **Persistencia:** Android SharedPreferences (Sin bases de datos)
- **SDK Minimo:** Android 7.0 (API 24)
- **SDK Target:** Android 14 (API 34)

---

## Autora / Integrantes

- **Alumna:** Camila Ianni
- **Materia:** Aplicaciones Moviles
- **Carrera:** Analista de Sistemas
