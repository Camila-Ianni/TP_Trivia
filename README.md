# TriviaMaster - TP 2 Aplicaciones Moviles

Trabajo Practico N° 2 (Sistema de Gestion sobre el TP1) para la materia **Aplicaciones Moviles** de la carrera **Analista de Sistemas**.

---

## Descripcion del Proyecto

**TriviaMaster** es una aplicacion movil nativa desarrollada en **Java** para Android con estetica arcade pixel art retro y tematica de monstruos. Evoluciona la base del TP1 integrando persistencia completa en **SQLite** mediante `SQLiteOpenHelper`, acceso asincrono en hilos secundarios con `ExecutorService`, sistema de **Login y Registro**, **CRUD de Usuarios**, **CRUD de Preguntas de Trivia**, **Historial de Partidas**, **Guia de Juego interactiva**, **Mapa interactivo** (16 niveles, Castillo Final, Cueva de Retos y Guardianes), **Dificultad seleccionable**, **Comodines con energia**, **Medallas de Honor** y **Coleccion de Guardianes**.

---

## Caracteristicas Principales

1. **Autenticacion y CRUD de Usuarios (SQLite):**
   - Login con validacion de credenciales en SQLite.
   - Registro de nuevas cuentas con validacion de correo, contraseña y usuario unico (`UNIQUE COLLATE NOCASE`).
   - Edicion de perfil, cierre de sesion, eliminacion de cuenta y pantalla dedicada de administracion de usuarios (`UserManagerActivity`).
2. **Gestor de Preguntas de Trivia (CRUD en SQLite):**
   - Creacion, busqueda, filtrado por nivel (1 al 16), edicion (con `RadioGroup` para elegir la respuesta correcta) y eliminacion de preguntas (`QuestionManagerActivity`).
   - Integracion directa con las partidas en `GameActivity`.
3. **Historial de Partidas (SQLite):**
   - Registro automatico de cada partida jugada (modo/nivel, dificultad, aciertos, puntaje, estrellas y fecha) en la tabla `match_history`.
   - Visualizacion y eliminacion individual o total desde `MatchHistoryActivity`.
4. **Guia de Juego y Mapa Interactivo:**
   - Guia interactiva de 4 pasos al primer ingreso y accesible desde el boton `?` del mapa.
   - **Cueva de Retos** interactiva para jugar Desafios Rapidos de 10 preguntas aleatorias.
   - **Guardianes del Mapa** interactivos con dialogos de pistas y progreso por zona.
5. **Jugabilidad Avanzada:**
   - 3 niveles de dificultad (*Principiante*, *Veterano*, *Leyenda*) con variacion real de tiempo, vidas y multiplicador de puntaje.
   - Comodines en partida (*50/50* o *Letra* en puzzles de palabras, y *+5s*) consumiendo energia.
   - Explicaciones educativas al validar cada respuesta y feedback sensorial retro opcional (sonido y vibracion).

---

## Arquitectura y Pantallas (Activities)

- `MainActivity`: Login de usuarios y acceso a Registro y Salon de la Fama.
- `RegisterActivity`: Registro de nuevos usuarios con selector de avatar.
- `MapActivity`: Roadmap interactivo de 16 niveles, Castillo Final, Cueva de Retos, Guardianes y Guia de Juego.
- `GameActivity`: Trivia de opcion multiple, puzzles de letras, temporizador, dificultad, comodines y explicaciones.
- `ResultActivity`: Calculo de estrellas, medallas, cofres y guardado en SQLite (`users` y `match_history`).
- `RankingActivity`: Salon de la Fama ordenado desde SQLite.
- `ProfileActivity`: Edicion de cuenta, preferencias arcade (sonido/vibracion), Medallas, Bestiario de Guardianes y accesos a CRUDs/Historial.
- `UserManagerActivity`: CRUD completo de usuarios en SQLite.
- `QuestionManagerActivity`: CRUD completo de preguntas de trivia en SQLite.
- `MatchHistoryActivity`: Historial de partidas del usuario en SQLite.

---

## Tecnologias

- **Lenguaje:** Java 17
- **Base de datos:** SQLite (`TriviaDbHelper`, version 2: tablas `users`, `questions`, `match_history`)
- **Concurrencia:** `ExecutorService` + `Handler(Looper.getMainLooper())` (`DbExecutor`)
- **UI:** ViewBinding, Material Components, RecyclerView, recursos centralizados en `strings.xml` y `android:hint`
- **SDK Minimo:** Android 7.0 (API 24) | **SDK Target:** Android 14 (API 34)

---

## Autora

- **Alumna:** Camila Ianni
- **Materia:** Aplicaciones Moviles
- **Carrera:** Analista de Sistemas
