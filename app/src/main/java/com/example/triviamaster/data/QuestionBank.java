package com.example.triviamaster.data;

import android.graphics.Color;
import com.example.triviamaster.R;
import com.example.triviamaster.models.Level;
import com.example.triviamaster.models.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class QuestionBank {

    public static final QuestionBank INSTANCE = new QuestionBank();

    private static final List<Question> world1Level1Questions = Arrays.asList(
            new Question(101, "¿Qué proceso realizan las plantas para convertir luz solar en alimento?", "Naturaleza", Color.parseColor("#10B981"), Arrays.asList("Respiración", "Fotosíntesis", "Fermentación", "Transpiración"), 1, "La fotosíntesis utiliza clorofila y luz solar para sintetizar glucosa."),
            new Question(102, "¿Cuál es el polinizador más importante para los ecosistemas agrícolas?", "Naturaleza", Color.parseColor("#10B981"), Arrays.asList("Abeja", "Mariposa", "Colibrí", "Escarabajo"), 0, "Las abejas polinizan cerca del 75% de las plantas con flores que alimentan al mundo."),
            new Question(103, "¡Ordena todas las letras para formar la palabra!", "Naturaleza", Color.parseColor("#10B981"), Collections.emptyList(), 0, "La FLOR es la estructura reproductiva característica de las plantas fanerógamas.", true, "FLOR", 0, R.drawable.ic_clue_flower),
            new Question(104, "¿Cuántas patas tienen los insectos adultos en su anatomía?", "Biología", Color.parseColor("#10B981"), Arrays.asList("4 patas", "6 patas", "8 patas", "10 patas"), 1, "Todos los insectos adultos tienen 3 pares de patas articuladas (6 patas)."),
            new Question(105, "¿Cuál es el animal terrestre más rápido del planeta?", "Naturaleza", Color.parseColor("#10B981"), Arrays.asList("León", "Guepardo", "Gacela", "Caballo"), 1, "El guepardo puede alcanzar velocidades de hasta 120 km/h.")
    );

    private static final List<Question> world1Level2Questions = Arrays.asList(
            new Question(106, "¿En qué continente floreció la civilización del Antiguo Egipto?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("Asia", "África", "Europa", "Oceanía"), 1, "Egipto se ubica en el noreste del continente africano a lo largo del río Nilo."),
            new Question(107, "¿Qué estructura monumental egipcia es una de las 7 Maravillas del Mundo Antiguo?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("El Coliseo", "La Gran Pirámide de Guiza", "El Faro de Alejandría", "Petra"), 1, "La Gran Pirámide de Keops en Guiza sigue en pie hoy en día."),
            new Question(108, "¡Ordena todas las letras para formar la palabra!", "Historia", Color.parseColor("#F59E0B"), Collections.emptyList(), 0, "La CASA y monumentos egipcios reflejaban su avanzada arquitectura.", true, "CASA", 0, R.drawable.ic_clue_house),
            new Question(109, "¿Cómo se llamaba la escritura sagrada mediante símbolos de los egipcios?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("Cuneiforme", "Jeroglíficos", "Alfabeto Fenicio", "Cirílico"), 1, "Los jeroglíficos fueron descifrados gracias a la Piedra de Rosetta."),
            new Question(110, "¿Quién fue la última reina famosa de la dinastía ptolemaica de Egipto?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("Nefertiti", "Cleopatra VII", "Hatshepsut", "Arsinoe"), 1, "Cleopatra VII gobernó hasta la conquista romana en el año 30 a.C.")
    );

    private static final List<Question> world1Level3Questions = Arrays.asList(
            new Question(111, "¿Qué elemento químico compone principalmente el aire que respiramos?", "Ciencia", Color.parseColor("#3B82F6"), Arrays.asList("Oxígeno", "Nitrógeno", "Dióxido de Carbono", "Hidrógeno"), 1, "El nitrógeno representa aproximadamente el 78% del aire atmosférico."),
            new Question(112, "¿A qué temperatura hierve el agua pura al nivel del mar?", "Ciencia", Color.parseColor("#3B82F6"), Arrays.asList("80 °C", "90 °C", "100 °C", "120 °C"), 2, "A 1 atmósfera de presión, el punto de ebullición del agua es exactamente 100 °C."),
            new Question(113, "¡Ordena todas las letras para formar la palabra!", "Ciencia", Color.parseColor("#3B82F6"), Collections.emptyList(), 0, "El HELADO cambia de estado sólido a líquido por absorción de calor.", true, "HELADO", 0, R.drawable.ic_clue_icecream),
            new Question(114, "¿Cuál es el planeta más cercano al Sol?", "Astronomía", Color.parseColor("#8B5CF6"), Arrays.asList("Venus", "Mercurio", "Tierra", "Marte"), 1, "Mercurio orbita el Sol a tan solo unos 58 millones de kilómetros."),
            new Question(115, "¿Cuál es el metal más liviano de la tabla periódica?", "Química", Color.parseColor("#3B82F6"), Arrays.asList("Aluminio", "Sodio", "Litio", "Magnesio"), 2, "El litio (Li, número atómico 3) es el elemento metálico menos denso.")
    );

    private static final List<Question> world1Level4Questions = Arrays.asList(
            new Question(116, "¿Cuál es el océano más grande y profundo de la Tierra?", "Geografía", Color.parseColor("#10B981"), Arrays.asList("Océano Atlántico", "Océano Índico", "Océano Pacífico", "Océano Ártico"), 2, "El océano Pacífico cubre más de 165 millones de km²."),
            new Question(117, "¿Qué país tiene forma alargada y costera en el suroeste de Sudamérica?", "Geografía", Color.parseColor("#10B981"), Arrays.asList("Chile", "Chile", "Argentina", "Ecuador"), 1, "Chile tiene más de 4.300 km de longitud y un ancho promedio de 175 km."),
            new Question(118, "¡Ordena todas las letras para formar la palabra!", "Geografía", Color.parseColor("#10B981"), Collections.emptyList(), 0, "El MONTE o montaña es una eminencia topográfica natural del relieve terrestre.", true, "MONTE", 0, R.drawable.ic_clue_mountain),
            new Question(119, "¿Cuál es el desierto cálido más grande del planeta?", "Geografía", Color.parseColor("#10B981"), Arrays.asList("Atacama", "Kalahari", "Sáhara", "Gobi"), 2, "El desierto del Sáhara abarca más de 9 millones de km² en el norte de África."),
            new Question(120, "¿Qué ciudad es conocida como 'La Ciudad Eterna'?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("Atenas", "Roma", "París", "El Cairo"), 1, "Roma fue apodada Urbs Aeterna por poetas romanos debido a su gloria perpetua.")
    );

    private static final List<Question> world2Level1Questions = Arrays.asList(
            new Question(201, "¿Qué lenguaje oficial adoptó Google para desarrollo de Android nativo?", "Tecnología", Color.parseColor("#8B5CF6"), Arrays.asList("Kotlin", "Swift", "C#", "PHP"), 0, "Google declaró Kotlin lenguaje oficial preferente para Android en Google I/O 2017 y 2019."),
            new Question(202, "¿Qué significan las siglas 'HTML' en desarrollo web?", "Tecnología", Color.parseColor("#8B5CF6"), Arrays.asList("High Text Machine Language", "HyperText Markup Language", "Hyper Tabular Multi Language", "Home Tool Markup Logic"), 1, "HyperText Markup Language es el estándar para crear la estructura de páginas web."),
            new Question(203, "¡Ordena todas las letras para formar la palabra!", "Tecnología", Color.parseColor("#8B5CF6"), Collections.emptyList(), 0, "El ROBOT ejecuta tareas mecánicas y lógicas programadas.", true, "ROBOT", 0, R.drawable.ic_clue_robot),
            new Question(204, "¿Qué puerto físico universal reemplazó gradualmente al micro-USB?", "Tecnología", Color.parseColor("#8B5CF6"), Arrays.asList("USB Tipo C", "Lightning", "Thunderbolt 1", "FireWire"), 0, "USB-C es reversible y capaz de transmitir datos a alta velocidad y energía."),
            new Question(205, "¿Cuál es el sistema operativo móvil con mayor cuota de mercado mundial?", "Tecnología", Color.parseColor("#8B5CF6"), Arrays.asList("iOS", "Android", "Windows Phone", "HarmonyOS"), 1, "Android domina más del 70% del mercado móvil mundial de dispositivos inteligentes.")
    );

    private static final List<Question> world2Level2Questions = Arrays.asList(
            new Question(206, "¿Qué saga cinematográfica incluye a Luke Skywalker y Darth Vader?", "Cine", Color.parseColor("#EC4899"), Arrays.asList("Star Trek", "Star Wars", "Dune", "Matrix"), 1, "Star Wars fue creada por George Lucas en 1977."),
            new Question(207, "¿Qué director dirigió 'Titanic' y 'Avatar'?", "Cine", Color.parseColor("#EC4899"), Arrays.asList("Steven Spielberg", "James Cameron", "Christopher Nolan", "Martin Scorsese"), 1, "James Cameron dirigió ambas películas, las cuales rompieron récords históricos de taquilla."),
            new Question(208, "¡Ordena todas las letras para formar la palabra!", "Cine", Color.parseColor("#EC4899"), Collections.emptyList(), 0, "El CINE nació a fines del siglo XIX con el cinematógrafo de los hermanos Lumière.", true, "CINE", 0, R.drawable.ic_ribbon_star),
            new Question(209, "¿Qué película animada de Pixar tiene como protagonista al pez payaso Nemo?", "Cine", Color.parseColor("#EC4899"), Arrays.asList("Toy Story", "Buscando a Nemo", "Cars", "Up"), 1, "Buscando a Nemo (2003) relata la búsqueda de Marlin a través del océano."),
            new Question(210, "¿Cómo se llama el reino ficticio de 'Frozen' gobernado por Elsa y Anna?", "Cine", Color.parseColor("#EC4899"), Arrays.asList("Arendelle", "Genovia", "Wakanda", "Narnia"), 0, "Arendelle está inspirado en los fiordos y arquitectura típica de Noruega.")
    );

    private static final List<Question> world2Level3Questions = Arrays.asList(
            new Question(211, "¿Qué país fue el anfitrión de la Copa del Mundo de fútbol de 2022?", "Deportes", Color.parseColor("#F97316"), Arrays.asList("Rusia", "Catar", "Brasil", "Alemania"), 1, "Catar albergó la Copa Mundial FIFA 2022 ganada por Argentina."),
            new Question(212, "¿Cuántos anillos entrelazados conforman el símbolo olímpico oficial?", "Deportes", Color.parseColor("#F97316"), Arrays.asList("3 anillos", "4 anillos", "5 anillos", "6 anillos"), 2, "Los 5 anillos representan los 5 continentes unidos por el olimpismo."),
            new Question(213, "¡Ordena todas las letras para formar la palabra!", "Deportes", Color.parseColor("#F97316"), Collections.emptyList(), 0, "El BALÓN o pelota es el elemento central de la mayoría de deportes en equipo.", true, "BALON", 0, R.drawable.ic_clue_ball),
            new Question(214, "¿Cuál es la distancia aproximada de una carrera de Maratón clásica?", "Deportes", Color.parseColor("#F97316"), Arrays.asList("21,09 km", "42,195 km", "50 km", "35 km"), 1, "La distancia de 42,195 km quedó oficializada en los Juegos de Londres 1908."),
            new Question(215, "¿En qué deporte se utiliza una canasta suspendida a 3,05 metros de altura?", "Deportes", Color.parseColor("#F97316"), Arrays.asList("Vóley", "Básquetbol", "Handball", "Polo"), 1, "El baloncesto fue inventado por James Naismith en 1891.")
    );

    private static final List<Question> world2Level4Questions = Arrays.asList(
            new Question(216, "¿Qué famosa consola de videojuegos de 1989 popularizó el juego Tetris?", "Videojuegos", Color.parseColor("#8B5CF6"), Arrays.asList("Game Boy", "Sega Genesis", "Atari 2600", "PlayStation 1"), 0, "La Game Boy de Nintendo venía empaquetada con Tetris y fue un éxito arrollador."),
            new Question(217, "¿Quién es el fontanero más famoso de los videojuegos de Nintendo?", "Videojuegos", Color.parseColor("#8B5CF6"), Arrays.asList("Sonic", "Mario", "Crash Bandicoot", "Pac-Man"), 1, "Mario debutó originalmente en Donkey Kong (1981) como 'Jumpman'."),
            new Question(218, "¡Ordena todas las letras para formar la palabra!", "Videojuegos", Color.parseColor("#8B5CF6"), Collections.emptyList(), 0, "El JUEGO estimula habilidades cognitivas, reflejos y entretenimiento.", true, "JUEGO", 0, R.drawable.ic_bolt),
            new Question(219, "¿En qué juego de rol recolectas criaturas dentro de pequeñas Pokébolas?", "Videojuegos", Color.parseColor("#8B5CF6"), Arrays.asList("Digimon", "Pokémon", "Yu-Gi-Oh!", "Monster Hunter"), 1, "Pokémon fue creado por Satoshi Tajiri para Game Boy en 1996."),
            new Question(220, "¿Cómo se llama el protagonista con espada de 'The Legend of Zelda'?", "Videojuegos", Color.parseColor("#8B5CF6"), Arrays.asList("Zelda", "Link", "Ganon", "Sheik"), 1, "El héroe se llama Link; Zelda es la princesa del reino de Hyrule.")
    );

    private static final List<Question> world3Level1Questions = Arrays.asList(
            new Question(301, "¿Quién pintó el famoso fresco en el techo de la Capilla Sixtina?", "Arte", Color.parseColor("#F59E0B"), Arrays.asList("Leonardo da Vinci", "Miguel Ángel", "Rafael", "Donatello"), 1, "Miguel Ángel Buonarroti pintó la bóveda entre 1508 y 1512."),
            new Question(302, "¿En qué museo de París se exhibe la 'Mona Lisa' de Leonardo da Vinci?", "Arte", Color.parseColor("#F59E0B"), Arrays.asList("Museo del Prado", "Museo del Louvre", "Museo Británico", "Metropolitan"), 1, "El Museo del Louvre en París recibe millones de visitantes para admirar la Gioconda."),
            new Question(303, "¡Ordena todas las letras para formar la palabra!", "Arte", Color.parseColor("#F59E0B"), Collections.emptyList(), 0, "El ARTE es la manifestación estética de la creatividad y sensibilidad humana.", true, "ARTE", 0, R.drawable.ic_clue_brush),
            new Question(304, "¿Qué obra cumbre de la literatura española relata las andanzas de un caballero andante?", "Literatura", Color.parseColor("#EC4899"), Arrays.asList("Don Quijote de la Mancha", "El Cantar de mio Cid", "La Celestina", "Cien años de soledad"), 0, "Don Quijote de la Mancha de Miguel de Cervantes fue publicado en 1605."),
            new Question(305, "¿Qué corriente artística representaron Salvador Dalí y René Magritte?", "Arte", Color.parseColor("#F59E0B"), Arrays.asList("Impresionismo", "Surrealismo", "Cubismo", "Barroco"), 1, "El surrealismo exploró el mundo de los sueños y el subconsciente.")
    );

    private static final List<Question> world3Level2Questions = Arrays.asList(
            new Question(306, "¿En qué año cayó el Muro de Berlín, símbolo de la Guerra Fría?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("1980", "1989", "1991", "1975"), 1, "La caída pacífica del muro de Berlín ocurrió el 9 de noviembre de 1989."),
            new Question(307, "¿Qué emperador romano fue apuñalado en los idus de marzo del 44 a.C.?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("Nerón", "Julio César", "Augusto", "Calígula"), 1, "Julio César fue asesinado en el Senado por una conspiración de senadores."),
            new Question(308, "¡Ordena todas las letras para formar la palabra!", "Historia", Color.parseColor("#F59E0B"), Collections.emptyList(), 0, "El REY ejercía el mando soberano sobre los reinos del mundo clásico.", true, "REY", 0, R.drawable.ic_crown),
            new Question(309, "¿En qué año Cristóbal Colón llegó por primera vez a las costas de América?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("1492", "1500", "1485", "1512"), 0, "La expedición desembarcó el 12 de octubre de 1492 en la isla Guanahani."),
            new Question(310, "¿Quién fue el célebre pacifista que lideró la independencia de la India?", "Historia", Color.parseColor("#F59E0B"), Arrays.asList("Jawaharlal Nehru", "Mahatma Gandhi", "Nelson Mandela", "Martin Luther King"), 1, "Gandhi promovió la desobediencia civil no violenta para lograr la emancipación india.")
    );

    private static final List<Question> world3Level3Questions = Arrays.asList(
            new Question(311, "¿Qué dios de la mitología griega es el soberano del rayo y el Olimpo?", "Mitología", Color.parseColor("#8B5CF6"), Arrays.asList("Poseidón", "Zeus", "Hades", "Apolo"), 1, "Zeus gobernaba sobre los dioses del monte Olimpo armado con rayos celestiales."),
            new Question(312, "¿Cómo se llamaba el martillo mágico del dios nórdico Thor?", "Mitología", Color.parseColor("#8B5CF6"), Arrays.asList("Excalibur", "Mjolnir", "Gungnir", "Aegis"), 1, "Mjolnir fue forjado por enanos y sólo podía ser levantado por los dignos."),
            new Question(313, "¡Ordena todas las letras para formar la palabra!", "Mitología", Color.parseColor("#8B5CF6"), Collections.emptyList(), 0, "Los DIOSES del Olimpo regían las fuerzas naturales en los mitos griegos.", true, "DIOS", 0, R.drawable.ic_sparkle_gold),
            new Question(314, "¿Qué criatura mitológica renace de sus propias cenizas?", "Mitología", Color.parseColor("#8B5CF6"), Arrays.asList("Grifo", "Ave Fénix", "Minotauro", "Pegaso"), 1, "El Fénix es un símbolo eterno de renacimiento y renovación en varias culturas."),
            new Question(315, "¿Cómo se llamaba el caballo alado de la mitología griega?", "Mitología", Color.parseColor("#8B5CF6"), Arrays.asList("Pegaso", "Centauro", "Quimera", "Cancerbero"), 0, "Pegaso nació de la sangre de Medusa tras ser derrotada por Perseo.")
    );

    private static final List<Question> world3Level4Questions = Arrays.asList(
            new Question(316, "¿Qué instrumento musical de cuerdas frotadas posee 4 cuerdas y un arco?", "Música", Color.parseColor("#EC4899"), Arrays.asList("Guitarra", "Violín", "Arpa", "Flauta"), 1, "El violín es el miembro más pequeño y agudo de la familia de las cuerdas clásicas."),
            new Question(317, "¿Qué compositor clásico compuso la 9ª Sinfonía a pesar de padecer sordera?", "Música", Color.parseColor("#EC4899"), Arrays.asList("Mozart", "Beethoven", "Bach", "Chopin"), 1, "Ludwig van Beethoven completó su 9ª Sinfonía y el Himno a la Alegría en 1824."),
            new Question(318, "¡Ordena todas las letras para formar la palabra!", "Música", Color.parseColor("#EC4899"), Collections.emptyList(), 0, "La NOTA musical representa la duración y la altura del sonido.", true, "NOTA", 0, R.drawable.ic_clue_music_note),
            new Question(319, "¿Cuántas notas musicales naturales existen en la escala diatónica occidental?", "Música", Color.parseColor("#EC4899"), Arrays.asList("5 notas", "7 notas", "10 notas", "12 notas"), 1, "Las 7 notas naturales son: Do, Re, Mi, Fa, Sol, La, Si."),
            new Question(320, "¿Qué cantante fue coronado mundialmente como el 'Rey del Pop'?", "Música", Color.parseColor("#EC4899"), Arrays.asList("Elvis Presley", "Michael Jackson", "Freddie Mercury", "Prince"), 1, "Michael Jackson vendió cientos de millones de álbumes con discos como Thriller.")
    );

    private static final List<Question> world4Level1Questions = Arrays.asList(
            new Question(401, "¿Qué físico propuso la Teoría de la Relatividad con la fórmula E=mc²?", "Ciencia", Color.parseColor("#3B82F6"), Arrays.asList("Isaac Newton", "Albert Einstein", "Niels Bohr", "Galileo Galilei"), 1, "Einstein formuló la relatividad especial en 1905 y la general en 1915."),
            new Question(402, "¿Cuál es la velocidad aproximada de la luz en el vacío?", "Física", Color.parseColor("#3B82F6"), Arrays.asList("150.000 km/s", "300.000 km/s", "500.000 km/s", "1.000.000 km/s"), 1, "La luz viaja a exactamente 299.792 kilómetros por segundo en el vacío."),
            new Question(403, "¡Ordena todas las letras para formar la palabra!", "Física", Color.parseColor("#3B82F6"), Collections.emptyList(), 0, "El ÁTOMO es la unidad constituyente fundamental de la materia ordinaria.", true, "ATOMO", 0, R.drawable.ic_clue_atom),
            new Question(404, "¿Qué fuerza universal descubierta por Newton atrae los cuerpos celestes?", "Física", Color.parseColor("#3B82F6"), Arrays.asList("Electromagnetismo", "Gravedad", "Fuerza Nuclear", "Inercia"), 1, "La gravitación universal describe la atracción entre masas en el universo."),
            new Question(405, "¿Qué instrumento espacial reemplazó y complementa al telescopio Hubble en 2022?", "Astronomía", Color.parseColor("#8B5CF6"), Arrays.asList("Voyager", "James Webb Space Telescope", "Kepler", "Spitzer"), 1, "El telescopio James Webb observa el cosmos profundo mediante radiación infrarroja.")
    );

    private static final List<Question> world4Level2Questions = Arrays.asList(
            new Question(406, "¿En qué país se encuentra la zona más fría habitada permanentemente (Oimiakón)?", "Geografía", Color.parseColor("#10B981"), Arrays.asList("Canadá", "Rusia (Siberia)", "Noruega", "Groenlandia"), 1, "Oimiakón ha registrado temperaturas récord por debajo de los -70 °C."),
            new Question(407, "¿Qué continente concentra más del 70% del agua dulce congelada de la Tierra?", "Geografía", Color.parseColor("#10B981"), Arrays.asList("Ártico", "Antártida", "América del Norte", "Asia"), 1, "El casquete de hielo de la Antártida almacena la inmensa mayoría del agua dulce."),
            new Question(408, "¡Ordena todas las letras para formar la palabra!", "Geografía", Color.parseColor("#10B981"), Collections.emptyList(), 0, "La NIEVE se forma cuando el vapor de agua precipita a temperaturas bajo cero.", true, "NIEVE", 0, R.drawable.ic_clue_snowflake),
            new Question(409, "¿Cuál es el fenómeno lumínico nocturno en cielos polares causado por partículas solares?", "Ciencia", Color.parseColor("#3B82F6"), Arrays.asList("Arcoíris lunar", "Aurora boreal / austral", "Fata Morgana", "Centella"), 1, "Las auroras ocurren cuando el viento solar colisiona con el campo magnético terrestre."),
            new Question(410, "¿Viven pingüinos autóctonos en el Polo Norte geográfico?", "Naturaleza", Color.parseColor("#10B981"), Arrays.asList("Sí, abundan", "No, viven sólo en el hemisferio sur", "Sólo en invierno", "En islas árticas"), 1, "Los pingüinos habitan exclusivamente en el hemisferio sur del planeta.")
    );

    private static final List<Question> world4Level3Questions = Arrays.asList(
            new Question(411, "¿Quién fue el filósofo griego que proclamó: 'Sólo sé que no sé nada'?", "Filosofía", Color.parseColor("#F59E0B"), Arrays.asList("Platón", "Sócrates", "Aristóteles", "Pitágoras"), 1, "Sócrates utilizó el método de la mayéutica para cuestionar certezas aparentes."),
            new Question(412, "¿Qué filósofo francés acuñó la frase 'Pienso, luego existo' (Cogito, ergo sum)?", "Filosofía", Color.parseColor("#F59E0B"), Arrays.asList("Jean-Jacques Rousseau", "René Descartes", "Voltaire", "Michel Foucault"), 1, "Descartes estableció la duda metódica como pilar del racionalismo."),
            new Question(413, "¡Ordena todas las letras para formar la palabra!", "Filosofía", Color.parseColor("#F59E0B"), Collections.emptyList(), 0, "La IDEA en la filosofía platónica representa el modelo inteligible supremo.", true, "IDEA", 0, R.drawable.ic_clue_book),
            new Question(414, "¿Qué rama de la filosofía se encarga de estudiar lo que es bueno y lo que es moralmente correcto?", "Filosofía", Color.parseColor("#F59E0B"), Arrays.asList("Epistemología", "Ética", "Estética", "Ontología"), 1, "La ética examina los principios y deberes que rigen el comportamiento humano."),
            new Question(415, "¿Quién escribió el influyente tratado político renacentista 'El Príncipe'?", "Filosofía", Color.parseColor("#F59E0B"), Arrays.asList("Tomás Moro", "Nicolás Maquiavelo", "Erasmo de Róterdam", "Hobbes"), 1, "Maquiavelo dedicó El Príncipe en 1513 a los Médici en Florencia.")
    );

    private static final List<Question> world4Level4Questions = Arrays.asList(
            new Question(416, "¿Cómo se denominan las regiones del espacio de gravedad tan colosal que ni la luz escapa?", "Astronomía", Color.parseColor("#8B5CF6"), Arrays.asList("Supernovas", "Agujeros Negros", "Púlsares", "Nebulosas"), 1, "Los agujeros negros poseen un horizonte de sucesos donde la velocidad de escape supera la luz."),
            new Question(417, "¿Qué teoría cosmológica describe el origen expansivo del universo hace 13.800 millones de años?", "Astronomía", Color.parseColor("#8B5CF6"), Arrays.asList("Teoría del Estado Estacionario", "Teoría del Big Bang", "Multiverso Cuántico", "Universo Pulsante"), 1, "El Big Bang fue propuesto inicialmente por Georges Lemaître y comprobado por la radiación cósmica."),
            new Question(418, "¡Ordena todas las letras para formar la palabra!", "Astronomía", Color.parseColor("#8B5CF6"), Collections.emptyList(), 0, "El COHETE permite a la humanidad explorar los límites exteriores del cosmos.", true, "COHETE", 0, R.drawable.ic_rocket),
            new Question(419, "¿Qué galaxia espiral colisionará con nuestra Vía Láctea dentro de 4.500 millones de años?", "Astronomía", Color.parseColor("#8B5CF6"), Arrays.asList("Galaxia del Sombrero", "Andrómeda (M31)", "Triángulo", "Ojo Negro"), 1, "Andrómeda se aproxima a la Vía Láctea a unos 110 kilómetros por segundo."),
            new Question(420, "¿Cuál es la partícula mediadora fundamental de la interacción electromagnética y la luz?", "Física", Color.parseColor("#3B82F6"), Arrays.asList("Bosón de Higgs", "Fotón", "Gluón", "Gravitón"), 1, "El fotón es el cuanto elemental de radiación electromagnética y luz.")
    );

    private static final List<Level> allLevels = Arrays.asList(
            new Level(1, 1, 1, "El Despertar de la Flora", "Fotosíntesis y Secretos de la Naturaleza", "Naturaleza", Color.parseColor("#10B981"), world1Level1Questions),
            new Level(2, 1, 2, "Tesoros de los Faraones", "Misterios del Antiguo Egipto y el Nilo", "Historia", Color.parseColor("#F59E0B"), world1Level2Questions),
            new Level(3, 1, 3, "Alquimia & Elementos", "Fundamentos de la Química y el Aire", "Ciencia", Color.parseColor("#3B82F6"), world1Level3Questions),
            new Level(4, 1, 4, "Rutas de la Tierra", "Cordilleras, Desiertos y Océanos", "Geografía", Color.parseColor("#10B981"), world1Level4Questions),
            new Level(5, 2, 1, "Reino de los Algoritmos", "Kotlin, Bits y Creadores del Software", "Tecnología", Color.parseColor("#8B5CF6"), world2Level1Questions),
            new Level(6, 2, 2, "Noches de Hollywood", "Clásicos del Cine y Películas Animadas", "Cine", Color.parseColor("#EC4899"), world2Level2Questions),
            new Level(7, 2, 3, "Espíritu de Campeones", "Hazañas Olímpicas y Récords Mundiales", "Deportes", Color.parseColor("#F97316"), world2Level3Questions),
            new Level(8, 2, 4, "Mundos Pixelados", "Consolas Retro y Leyendas del Gaming", "Videojuegos", Color.parseColor("#8B5CF6"), world2Level4Questions),
            new Level(9, 3, 1, "Obras del Renacimiento", "Pintores Geniales y Letras Universales", "Arte", Color.parseColor("#F59E0B"), world3Level1Questions),
            new Level(10, 3, 2, "Ecos del Pasado", "Ciudadelas Sagradas y Murallas Caídas", "Historia", Color.parseColor("#F59E0B"), world3Level2Questions),
            new Level(11, 3, 3, "Dioses y Mitos", "Rayos en el Olimpo y el Martillo de Thor", "Mitología", Color.parseColor("#8B5CF6"), world3Level3Questions),
            new Level(12, 3, 4, "Sinfonías Eternas", "Notas Mágicas y Estrellas del Rock", "Música", Color.parseColor("#EC4899"), world3Level4Questions),
            new Level(13, 4, 1, "Física de lo Invisible", "Relatividad, Átomos y la Luz Cósmica", "Física", Color.parseColor("#3B82F6"), world4Level1Questions),
            new Level(14, 4, 2, "Tierras Heladas", "Glaciares, Auroras y Desiertos Blancos", "Geografía", Color.parseColor("#10B981"), world4Level2Questions),
            new Level(15, 4, 3, "El Dilema de la Mente", "Sócrates, Descartes y la Búsqueda Ética", "Filosofía", Color.parseColor("#F59E0B"), world4Level3Questions),
            new Level(16, 4, 4, "Fronteras del Cosmos", "Agujeros Negros y el Vuelo de las Voyagers", "Astronomía", Color.parseColor("#8B5CF6"), world4Level4Questions)
    );

    private QuestionBank() {
    }

    public List<Level> getAllLevels() {
        return allLevels;
    }

    public List<Level> getLevelsForWorld(int worldId) {
        List<Level> destination = new ArrayList<>();
        for (Level level : allLevels) {
            if (level.getWorldId() == worldId) {
                destination.add(level);
            }
        }
        return destination;
    }

    public Level getLevelById(int id) {
        for (Level level : allLevels) {
            if (level.getId() == id) {
                return level;
            }
        }
        return null;
    }

    public List<Question> getRandomQuiz(int count) {
        List<Question> pool = new ArrayList<>();
        for (Level level : allLevels) {
            pool.addAll(level.getQuestions());
        }
        Collections.shuffle(pool);
        return new ArrayList<>(pool.subList(0, Math.min(count, pool.size())));
    }
}
