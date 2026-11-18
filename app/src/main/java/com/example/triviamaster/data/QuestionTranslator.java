package com.example.triviamaster.data;

import androidx.appcompat.app.AppCompatDelegate;
import com.example.triviamaster.models.Level;
import com.example.triviamaster.models.Question;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class QuestionTranslator {

    private static final class TranslatedQ {
        final String questionEn;
        final String categoryEn;
        final List<String> optionsEn;
        final String explanationEn;
        final String targetWordEn;

        final String questionIt;
        final String categoryIt;
        final List<String> optionsIt;
        final String explanationIt;
        final String targetWordIt;

        TranslatedQ(String qEn, String cEn, List<String> oEn, String eEn, String twEn,
                    String qIt, String cIt, List<String> oIt, String eIt, String twIt) {
            this.questionEn = qEn;
            this.categoryEn = cEn;
            this.optionsEn = oEn;
            this.explanationEn = eEn;
            this.targetWordEn = twEn;
            this.questionIt = qIt;
            this.categoryIt = cIt;
            this.optionsIt = oIt;
            this.explanationIt = eIt;
            this.targetWordIt = twIt;
        }
    }

    private static final class TranslatedLevel {
        final String titleEn;
        final String subtitleEn;
        final String categoryEn;
        final String titleIt;
        final String subtitleIt;
        final String categoryIt;

        TranslatedLevel(String tEn, String sEn, String cEn, String tIt, String sIt, String cIt) {
            this.titleEn = tEn;
            this.subtitleEn = sEn;
            this.categoryEn = cEn;
            this.titleIt = tIt;
            this.subtitleIt = sIt;
            this.categoryIt = cIt;
        }
    }

    private static final Map<String, TranslatedQ> BY_SPANISH_KEY = new HashMap<>();
    private static final Map<Integer, TranslatedLevel> LEVELS = new HashMap<>();

    static {
        add(101, "¿Qué proceso realizan las plantas para convertir luz solar en alimento?",
                "What process do plants use to convert sunlight into food?", "Nature",
                Arrays.asList("Respiration", "Photosynthesis", "Fermentation", "Transpiration"),
                "Photosynthesis uses chlorophyll and sunlight to synthesize glucose.", "",
                "Quale processo usano le piante per convertire la luce solare in cibo?", "Natura",
                Arrays.asList("Respirazione", "Fotosintesi", "Fermentazione", "Traspirazione"),
                "La fotosintesi usa la clorofilla e la luce solare per sintetizzare il glucosio.", "");

        add(102, "¿Cuál es el polinizador más importante para los ecosistemas agrícolas?",
                "What is the most important pollinator for agricultural ecosystems?", "Nature",
                Arrays.asList("Bee", "Butterfly", "Hummingbird", "Beetle"),
                "Bees pollinate nearly 75% of the flowering plants that feed the world.", "",
                "Qual è l'impollinatore più importante per gli ecosistemi agricoli?", "Natura",
                Arrays.asList("Ape", "Farfalla", "Colibrì", "Coleottero"),
                "Le api impollinano circa il 75% delle piante da fiore che nutrono il mondo.", "");

        add(103, "FLOR",
                "Unscramble all the letters to form the word!", "Nature",
                Collections.emptyList(),
                "A FLOWER is the reproductive structure characteristic of flowering plants.", "FLOWER",
                "Riordina tutte le lettere per formare la parola!", "Natura",
                Collections.emptyList(),
                "Il FIORE è la struttura riproduttiva caratteristica delle piante da fiore.", "FIORE");

        add(104, "¿Cuántas patas tienen los insectos adultos en su anatomía?",
                "How many legs do adult insects have in their anatomy?", "Biology",
                Arrays.asList("4 legs", "6 legs", "8 legs", "10 legs"),
                "All adult insects have 3 pairs of jointed legs (6 legs).", "",
                "Quante zampe hanno gli insetti adulti nella loro anatomia?", "Biologia",
                Arrays.asList("4 zampe", "6 zampe", "8 zampe", "10 zampe"),
                "Tutti gli insetti adulti hanno 3 paia di zampe articolate (6 zampe).", "");

        add(105, "¿Cuál es el animal terrestre más rápido del planeta?",
                "What is the fastest land animal on the planet?", "Nature",
                Arrays.asList("Lion", "Cheetah", "Gazelle", "Horse"),
                "The cheetah can reach speeds of up to 120 km/h.", "",
                "Qual è l'animale terrestre più veloce del pianeta?", "Natura",
                Arrays.asList("Leone", "Ghepardo", "Gazzella", "Cavallo"),
                "Il ghepardo può raggiungere velocità fino a 120 km/h.", "");

        add(106, "¿En qué continente floreció la civilización del Antiguo Egipto?",
                "On which continent did the Ancient Egyptian civilization flourish?", "History",
                Arrays.asList("Asia", "Africa", "Europe", "Oceania"),
                "Egypt is located in northeastern Africa along the Nile River.", "",
                "In quale continente fiorì la civiltà dell'Antico Egitto?", "Storia",
                Arrays.asList("Asia", "Africa", "Europa", "Oceania"),
                "L'Egitto si trova nel nord-est dell'Africa lungo il fiume Nilo.", "");

        add(107, "¿Qué estructura monumental egipcia es una de las 7 Maravillas del Mundo Antiguo?",
                "Which monumental Egyptian structure is one of the 7 Wonders of the Ancient World?", "History",
                Arrays.asList("The Colosseum", "The Great Pyramid of Giza", "The Lighthouse of Alexandria", "Petra"),
                "The Great Pyramid of Khufu at Giza is still standing today.", "",
                "Quale struttura monumentale egizia è una delle 7 Meraviglie del Mondo Antico?", "Storia",
                Arrays.asList("Il Colosseo", "La Grande Piramide di Giza", "Il Faro di Alessandria", "Petra"),
                "La Grande Piramide di Cheope a Giza è ancora in piedi oggi.", "");

        add(108, "CASA",
                "Unscramble all the letters to form the word!", "History",
                Collections.emptyList(),
                "Egyptian HOMES and monuments reflected their advanced architecture.", "HOME",
                "Riordina tutte le lettere per formare la parola!", "Storia",
                Collections.emptyList(),
                "Le CASE e i monumenti egizi riflettevano la loro architettura avanzata.", "CASA");

        add(109, "¿Cómo se llamaba la escritura sagrada mediante símbolos de los egipcios?",
                "What was the sacred symbol writing of the ancient Egyptians called?", "History",
                Arrays.asList("Cuneiform", "Hieroglyphics", "Phoenician Alphabet", "Cyrillic"),
                "Hieroglyphics were deciphered thanks to the Rosetta Stone.", "",
                "Come si chiamava la scrittura sacra a simboli degli antichi egizi?", "Storia",
                Arrays.asList("Cuneiforme", "Geroglifici", "Alfabeto Fenicio", "Cirillico"),
                "I geroglifici furono decifrati grazie alla Stele di Rosetta.", "");

        add(110, "¿Quién fue la última reina famosa de la dinastía ptolemaica de Egipto?",
                "Who was the last famous queen of Egypt's Ptolemaic dynasty?", "History",
                Arrays.asList("Nefertiti", "Cleopatra VII", "Hatshepsut", "Arsinoe"),
                "Cleopatra VII ruled until the Roman conquest in 30 BC.", "",
                "Chi fu l'ultima famosa regina della dinastia tolemaica d'Egitto?", "Storia",
                Arrays.asList("Nefertiti", "Cleopatra VII", "Hatshepsut", "Arsinoe"),
                "Cleopatra VII regnò fino alla conquista romana nel 30 a.C.", "");

        add(111, "¿Qué elemento químico compone principalmente el aire que respiramos?",
                "Which chemical element makes up most of the air we breathe?", "Science",
                Arrays.asList("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"),
                "Nitrogen makes up about 78% of Earth's atmospheric air.", "",
                "Quale elemento chimico compone principalmente l'aria che respiriamo?", "Scienza",
                Arrays.asList("Ossigeno", "Azoto", "Anidride Carbonica", "Idrogeno"),
                "L'azoto rappresenta circa il 78% dell'aria atmosferica.", "");

        add(112, "¿A qué temperatura hierve el agua pura al nivel del mar?",
                "At what temperature does pure water boil at sea level?", "Science",
                Arrays.asList("80 °C", "90 °C", "100 °C", "120 °C"),
                "At 1 atmosphere of pressure, water boils at exactly 100 °C.", "",
                "A quale temperatura bolle l'acqua pura al livello del mare?", "Scienza",
                Arrays.asList("80 °C", "90 °C", "100 °C", "120 °C"),
                "A 1 atmosfera di pressione, il punto di ebollizione dell'acqua è 100 °C.", "");

        add(113, "HELADO",
                "Unscramble all the letters to form the word!", "Science",
                Collections.emptyList(),
                "ICE changes from solid to liquid by absorbing heat.", "ICE",
                "Riordina tutte le lettere per formare la parola!", "Scienza",
                Collections.emptyList(),
                "Il GELATO passa dallo stato solido a quello liquido assorbendo calore.", "GELATO");

        add(114, "¿Cuál es el planeta más cercano al Sol?",
                "Which planet is closest to the Sun?", "Astronomy",
                Arrays.asList("Venus", "Mercury", "Earth", "Mars"),
                "Mercury orbits the Sun at only about 58 million kilometers.", "",
                "Qual è il pianeta più vicino al Sole?", "Astronomia",
                Arrays.asList("Venere", "Mercurio", "Terra", "Marte"),
                "Mercurio orbita intorno al Sole a soli 58 milioni di chilometri.", "");

        add(115, "¿Cuál es el metal más liviano de la tabla periódica?",
                "What is the lightest metal on the periodic table?", "Chemistry",
                Arrays.asList("Aluminum", "Sodium", "Lithium", "Magnesium"),
                "Lithium (Li, atomic number 3) is the least dense metallic element.", "",
                "Qual è il metallo più leggero della tavola periodica?", "Chimica",
                Arrays.asList("Alluminio", "Sodio", "Litio", "Magnesio"),
                "Il litio (Li, numero atomico 3) è l'elemento metallico meno denso.", "");

        add(116, "¿Cuál es el océano más grande y profundo de la Tierra?",
                "What is the largest and deepest ocean on Earth?", "Geography",
                Arrays.asList("Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"),
                "The Pacific Ocean covers more than 165 million km².", "",
                "Qual è l'oceano più grande e profondo della Terra?", "Geografia",
                Arrays.asList("Oceano Atlantico", "Oceano Indiano", "Oceano Pacifico", "Oceano Artico"),
                "L'Oceano Pacifico copre oltre 165 milioni di km².", "");

        add(117, "¿Qué país tiene forma alargada y costera en el suroeste de Sudamérica?",
                "Which country has a long, coastal shape in southwestern South America?", "Geography",
                Arrays.asList("Peru", "Chile", "Argentina", "Ecuador"),
                "Chile is over 4,300 km long with an average width of 175 km.", "",
                "Quale paese ha una forma allungata e costiera nel sud-ovest del Sud America?", "Geografia",
                Arrays.asList("Perù", "Cile", "Argentina", "Ecuador"),
                "Il Cile è lungo oltre 4.300 km con una larghezza media di 175 km.", "");

        add(118, "MONTE",
                "Unscramble all the letters to form the word!", "Geography",
                Collections.emptyList(),
                "A MOUNT or mountain is a natural elevation of the Earth's surface.", "MOUNT",
                "Riordina tutte le lettere per formare la parola!", "Geografia",
                Collections.emptyList(),
                "Il MONTE è un'elevazione naturale del rilievo terrestre.", "MONTE");

        add(119, "¿Cuál es el desierto cálido más grande del planeta?",
                "What is the largest hot desert on the planet?", "Geography",
                Arrays.asList("Atacama", "Kalahari", "Sahara", "Gobi"),
                "The Sahara Desert spans over 9 million km² in North Africa.", "",
                "Qual è il deserto caldo più grande del pianeta?", "Geografia",
                Arrays.asList("Atacama", "Kalahari", "Sahara", "Gobi"),
                "Il deserto del Sahara copre oltre 9 milioni di km² nel Nord Africa.", "");

        add(120, "¿Qué ciudad es conocida como 'La Ciudad Eterna'?",
                "Which city is known as 'The Eternal City'?", "History",
                Arrays.asList("Athens", "Rome", "Paris", "Cairo"),
                "Rome was nicknamed Urbs Aeterna by Roman poets for its enduring glory.", "",
                "Quale città è conosciuta come 'La Città Eterna'?", "Storia",
                Arrays.asList("Atene", "Roma", "Parigi", "Il Cairo"),
                "Roma fu soprannominata Urbs Aeterna dai poeti romani.", "");

        add(201, "¿Qué lenguaje oficial adoptó Google para desarrollo de Android nativo?",
                "Which official language did Google adopt for native Android development?", "Technology",
                Arrays.asList("Kotlin", "Swift", "C#", "PHP"),
                "Google announced Kotlin as the preferred official Android language at Google I/O.", "",
                "Quale linguaggio ufficiale ha adottato Google per lo sviluppo Android nativo?", "Tecnologia",
                Arrays.asList("Kotlin", "Swift", "C#", "PHP"),
                "Google ha dichiarato Kotlin linguaggio ufficiale preferito per Android.", "");

        add(202, "¿Qué significan las siglas 'HTML' en desarrollo web?",
                "What does 'HTML' stand for in web development?", "Technology",
                Arrays.asList("High Text Machine Language", "HyperText Markup Language", "Hyper Tabular Multi Language", "Home Tool Markup Logic"),
                "HyperText Markup Language is the standard for structuring web pages.", "",
                "Cosa significa l'acronimo 'HTML' nello sviluppo web?", "Tecnologia",
                Arrays.asList("High Text Machine Language", "HyperText Markup Language", "Hyper Tabular Multi Language", "Home Tool Markup Logic"),
                "HyperText Markup Language è lo standard per creare la struttura delle pagine web.", "");

        add(203, "ROBOT",
                "Unscramble all the letters to form the word!", "Technology",
                Collections.emptyList(),
                "A ROBOT executes programmed mechanical and logical tasks.", "ROBOT",
                "Riordina tutte le lettere per formare la parola!", "Tecnologia",
                Collections.emptyList(),
                "Il ROBOT esegue compiti meccanici e logici programmati.", "ROBOT");

        add(204, "¿Qué puerto físico universal reemplazó gradualmente al micro-USB?",
                "Which universal physical port gradually replaced micro-USB?", "Technology",
                Arrays.asList("USB Type-C", "Lightning", "Thunderbolt 1", "FireWire"),
                "USB-C is reversible and capable of high-speed data and power delivery.", "",
                "Quale porta fisica universale ha sostituito gradualmente la micro-USB?", "Tecnologia",
                Arrays.asList("USB Tipo-C", "Lightning", "Thunderbolt 1", "FireWire"),
                "USB-C è reversibile e trasmette dati ad alta velocità ed energia.", "");

        add(205, "¿Cuál es el sistema operativo móvil con mayor cuota de mercado mundial?",
                "Which mobile operating system has the largest global market share?", "Technology",
                Arrays.asList("iOS", "Android", "Windows Phone", "HarmonyOS"),
                "Android powers over 70% of the global smartphone market.", "",
                "Qual è il sistema operativo mobile con la maggiore quota di mercato mondiale?", "Tecnologia",
                Arrays.asList("iOS", "Android", "Windows Phone", "HarmonyOS"),
                "Android domina oltre il 70% del mercato globale degli smartphone.", "");

        add(206, "¿Qué saga cinematográfica incluye a Luke Skywalker y Darth Vader?",
                "Which film saga features Luke Skywalker and Darth Vader?", "Cinema",
                Arrays.asList("Star Trek", "Star Wars", "Dune", "Matrix"),
                "Star Wars was created by George Lucas in 1977.", "",
                "Quale saga cinematografica include Luke Skywalker e Darth Vader?", "Cinema",
                Arrays.asList("Star Trek", "Star Wars", "Dune", "Matrix"),
                "Star Wars è stata creata da George Lucas nel 1977.", "");

        add(207, "¿Qué director dirigió 'Titanic' y 'Avatar'?",
                "Which director directed 'Titanic' and 'Avatar'?", "Cinema",
                Arrays.asList("Steven Spielberg", "James Cameron", "Christopher Nolan", "Martin Scorsese"),
                "James Cameron directed both record-breaking box office hits.", "",
                "Quale regista ha diretto 'Titanic' e 'Avatar'?", "Cinema",
                Arrays.asList("Steven Spielberg", "James Cameron", "Christopher Nolan", "Martin Scorsese"),
                "James Cameron ha diretto entrambi i film da record al botteghino.", "");

        add(208, "CINE",
                "Unscramble all the letters to form the word!", "Cinema",
                Collections.emptyList(),
                "FILM or cinema was born in the late 19th century with the Lumière brothers.", "FILM",
                "Riordina tutte le lettere per formare la parola!", "Cinema",
                Collections.emptyList(),
                "Il CINE o cinema è nato alla fine del XIX secolo con i fratelli Lumière.", "CINE");

        add(209, "¿Qué película animada de Pixar tiene como protagonista al pez payaso Nemo?",
                "Which Pixar animated film stars the clownfish Nemo?", "Cinema",
                Arrays.asList("Toy Story", "Finding Nemo", "Cars", "Up"),
                "Finding Nemo (2003) tells the story of Marlin's search across the ocean.", "",
                "Quale film d'animazione Pixar ha come protagonista il pesce pagliaccio Nemo?", "Cinema",
                Arrays.asList("Toy Story", "Alla ricerca di Nemo", "Cars", "Up"),
                "Alla ricerca di Nemo (2003) racconta la ricerca di Marlin nell'oceano.", "");

        add(210, "¿Cómo se llama el reino ficticio de 'Frozen' gobernado por Elsa y Anna?",
                "What is the fictional kingdom in 'Frozen' ruled by Elsa and Anna?", "Cinema",
                Arrays.asList("Arendelle", "Genovia", "Wakanda", "Narnia"),
                "Arendelle is inspired by Norwegian fjords and architecture.", "",
                "Come si chiama il regno immaginario di 'Frozen' governato da Elsa e Anna?", "Cinema",
                Arrays.asList("Arendelle", "Genovia", "Wakanda", "Narnia"),
                "Arendelle è ispirato ai fiordi e all'architettura della Norvegia.", "");

        add(211, "¿Qué país fue el anfitrión de la Copa del Mundo de fútbol de 2022?",
                "Which country hosted the 2022 FIFA World Cup?", "Sports",
                Arrays.asList("Russia", "Qatar", "Brazil", "Germany"),
                "Qatar hosted the 2022 FIFA World Cup won by Argentina.", "",
                "Quale paese ha ospitato i Mondiali di calcio del 2022?", "Sport",
                Arrays.asList("Russia", "Qatar", "Brasile", "Germania"),
                "Il Qatar ha ospitato i Mondiali FIFA 2022 vinti dall'Argentina.", "");

        add(212, "¿Cuántos anillos entrelazados conforman el símbolo olímpico oficial?",
                "How many interlocking rings make up the official Olympic symbol?", "Sports",
                Arrays.asList("3 rings", "4 rings", "5 rings", "6 rings"),
                "The 5 rings represent the 5 continents united by Olympism.", "",
                "Quanti anelli intrecciati formano il simbolo olimpico ufficiale?", "Sport",
                Arrays.asList("3 anelli", "4 anelli", "5 anelli", "6 anelli"),
                "I 5 anelli rappresentano i 5 continenti uniti dall'olimpismo.", "");

        add(213, "BALON",
                "Unscramble all the letters to form the word!", "Sports",
                Collections.emptyList(),
                "The BALL is the central element in most team sports.", "BALL",
                "Riordina tutte le lettere per formare la parola!", "Sport",
                Collections.emptyList(),
                "La PALLA è l'elemento centrale nella maggior parte degli sport di squadra.", "PALLA");

        add(214, "¿Cuál es la distancia aproximada de una carrera de Maratón clásica?",
                "What is the approximate distance of a classic Marathon race?", "Sports",
                Arrays.asList("21.09 km", "42.195 km", "50 km", "35 km"),
                "The 42.195 km distance was standardized at the 1908 London Olympics.", "",
                "Qual è la distanza approssimativa di una maratona classica?", "Sport",
                Arrays.asList("21,09 km", "42,195 km", "50 km", "35 km"),
                "La distanza di 42,195 km fu ufficializzata alle Olimpiadi di Londra 1908.", "");

        add(215, "¿En qué deporte se utiliza una canasta suspendida a 3,05 metros de altura?",
                "In which sport is a hoop suspended 3.05 meters high used?", "Sports",
                Arrays.asList("Volleyball", "Basketball", "Handball", "Polo"),
                "Basketball was invented by James Naismith in 1891.", "",
                "In quale sport si usa un canestro sospeso a 3,05 metri di altezza?", "Sport",
                Arrays.asList("Pallavolo", "Pallacanestro", "Pallamano", "Polo"),
                "La pallacanestro fu inventata da James Naismith nel 1891.", "");

        add(216, "¿Qué famosa consola de videojuegos de 1989 popularizó el juego Tetris?",
                "Which famous 1989 handheld console popularized the game Tetris?", "Video Games",
                Arrays.asList("Game Boy", "Sega Genesis", "Atari 2600", "PlayStation 1"),
                "Nintendo's Game Boy came bundled with Tetris and became a massive hit.", "",
                "Quale famosa console portatile del 1989 ha reso popolare Tetris?", "Videogiochi",
                Arrays.asList("Game Boy", "Sega Genesis", "Atari 2600", "PlayStation 1"),
                "Il Game Boy di Nintendo era venduto con Tetris ed ebbe un enorme successo.", "");

        add(217, "¿Quién es el fontanero más famoso de los videojuegos de Nintendo?",
                "Who is Nintendo's most famous video game plumber?", "Video Games",
                Arrays.asList("Sonic", "Mario", "Crash Bandicoot", "Pac-Man"),
                "Mario originally debuted in Donkey Kong (1981) as 'Jumpman'.", "",
                "Chi è l'idraulico più famoso dei videogiochi Nintendo?", "Videogiochi",
                Arrays.asList("Sonic", "Mario", "Crash Bandicoot", "Pac-Man"),
                "Mario debuttò originariamente in Donkey Kong (1981) come 'Jumpman'.", "");

        add(218, "JUEGO",
                "Unscramble all the letters to form the word!", "Video Games",
                Collections.emptyList(),
                "A GAME stimulates cognitive skills, reflexes, and fun.", "GAME",
                "Riordina tutte le lettere per formare la parola!", "Videogiochi",
                Collections.emptyList(),
                "Il GIOCO stimola abilità cognitive, riflessi e divertimento.", "GIOCO");

        add(219, "¿En qué juego de rol recolectas criaturas dentro de pequeñas Pokébolas?",
                "In which RPG do you collect creatures inside small Poké Balls?", "Video Games",
                Arrays.asList("Digimon", "Pokémon", "Yu-Gi-Oh!", "Monster Hunter"),
                "Pokémon was created by Satoshi Tajiri for Game Boy in 1996.", "",
                "In quale gioco di ruolo raccogli creature dentro piccole Poké Ball?", "Videogiochi",
                Arrays.asList("Digimon", "Pokémon", "Yu-Gi-Oh!", "Monster Hunter"),
                "Pokémon fu creato da Satoshi Tajiri per Game Boy nel 1996.", "");

        add(220, "¿Cómo se llama el protagonista con espada de 'The Legend of Zelda'?",
                "What is the name of the sword-wielding hero in 'The Legend of Zelda'?", "Video Games",
                Arrays.asList("Zelda", "Link", "Ganon", "Sheik"),
                "The hero is named Link; Zelda is the princess of Hyrule.", "",
                "Come si chiama il protagonista con spada di 'The Legend of Zelda'?", "Videogiochi",
                Arrays.asList("Zelda", "Link", "Ganon", "Sheik"),
                "L'eroe si chiama Link; Zelda è la principessa di Hyrule.", "");

        add(301, "¿Quién pintó el famoso fresco en el techo de la Capilla Sixtina?",
                "Who painted the famous fresco on the ceiling of the Sistine Chapel?", "Art",
                Arrays.asList("Leonardo da Vinci", "Michelangelo", "Raphael", "Donatello"),
                "Michelangelo Buonarroti painted the vault between 1508 and 1512.", "",
                "Chi dipinse il famoso affresco sul soffitto della Cappella Sistina?", "Arte",
                Arrays.asList("Leonardo da Vinci", "Michelangelo", "Raffaello", "Donatello"),
                "Michelangelo Buonarroti dipinse la volta tra il 1508 e il 1512.", "");

        add(302, "¿En qué museo de París se exhibe la 'Mona Lisa' de Leonardo da Vinci?",
                "In which Paris museum is Leonardo da Vinci's 'Mona Lisa' displayed?", "Art",
                Arrays.asList("Prado Museum", "Louvre Museum", "British Museum", "Metropolitan"),
                "The Louvre Museum in Paris welcomes millions of visitors to see La Gioconda.", "",
                "In quale museo di Parigi è esposta la 'Gioconda' di Leonardo da Vinci?", "Arte",
                Arrays.asList("Museo del Prado", "Museo del Louvre", "British Museum", "Metropolitan"),
                "Il Museo del Louvre a Parigi accoglie milioni di visitatori per la Gioconda.", "");

        add(303, "ARTE",
                "Unscramble all the letters to form the word!", "Art",
                Collections.emptyList(),
                "ART is the aesthetic expression of human creativity and sensitivity.", "ART",
                "Riordina tutte le lettere per formare la parola!", "Arte",
                Collections.emptyList(),
                "L'ARTE è l'espressione estetica della creatività umana.", "ARTE");

        add(304, "¿Qué obra cumbre de la literatura española relata las andanzas de un caballero andante?",
                "Which masterpiece of Spanish literature tells the adventures of a wandering knight?", "Literature",
                Arrays.asList("Don Quixote", "El Cantar de mio Cid", "La Celestina", "One Hundred Years of Solitude"),
                "Don Quixote by Miguel de Cervantes was published in 1605.", "",
                "Quale capolavoro della letteratura spagnola narra le avventure di un cavaliere errante?", "Letteratura",
                Arrays.asList("Don Chisciotte della Mancia", "El Cantar de mio Cid", "La Celestina", "Cent'anni di solitudine"),
                "Don Chisciotte di Miguel de Cervantes fu pubblicato nel 1605.", "");

        add(305, "¿Qué corriente artística representaron Salvador Dalí y René Magritte?",
                "Which artistic movement did Salvador Dalí and René Magritte represent?", "Art",
                Arrays.asList("Impressionism", "Surrealism", "Cubism", "Baroque"),
                "Surrealism explored the world of dreams and the subconscious.", "",
                "Quale corrente artistica rappresentarono Salvador Dalí e René Magritte?", "Arte",
                Arrays.asList("Impressionismo", "Surrealismo", "Cubismo", "Barocco"),
                "Il surrealismo esplorava il mondo dei sogni e dell'inconscio.", "");

        add(306, "¿En qué año cayó el Muro de Berlín, símbolo de la Guerra Fría?",
                "In what year did the Berlin Wall fall, a symbol of the Cold War?", "History",
                Arrays.asList("1980", "1989", "1991", "1975"),
                "The peaceful fall of the Berlin Wall occurred on November 9, 1989.", "",
                "In che anno cadde il Muro di Berlino, simbolo della Guerra Fredda?", "Storia",
                Arrays.asList("1980", "1989", "1991", "1975"),
                "La caduta del muro di Berlino avvenne il 9 novembre 1989.", "");

        add(307, "¿Qué emperador romano fue apuñalado en los idus de marzo del 44 a.C.?",
                "Which Roman leader was stabbed on the Ides of March in 44 BC?", "History",
                Arrays.asList("Nero", "Julius Caesar", "Augustus", "Caligula"),
                "Julius Caesar was assassinated in the Senate by a conspiracy of senators.", "",
                "Quale condottiero romano fu pugnalato alle Idi di Marzo del 44 a.C.?", "Storia",
                Arrays.asList("Nerone", "Giulio Cesare", "Augusto", "Caligola"),
                "Giulio Cesare fu assassinato in Senato da una congiura di senatori.", "");

        add(308, "REY",
                "Unscramble all the letters to form the word!", "History",
                Collections.emptyList(),
                "A KING held sovereign rule over the kingdoms of the ancient world.", "KING",
                "Riordina tutte le lettere per formare la parola!", "Storia",
                Collections.emptyList(),
                "Il RE esercitava il potere sovrano sui regni del mondo antico.", "RE");

        add(309, "¿En qué año Cristóbal Colón llegó por primera vez a las costas de América?",
                "In what year did Christopher Columbus first reach the Americas?", "History",
                Arrays.asList("1492", "1500", "1485", "1512"),
                "The expedition landed on October 12, 1492 on Guanahani Island.", "",
                "In che anno Cristoforo Colombo raggiunse per la prima volta le coste dell'America?", "Storia",
                Arrays.asList("1492", "1500", "1485", "1512"),
                "La spedizione sbarcò il 12 ottobre 1492 sull'isola di Guanahani.", "");

        add(310, "¿Quién fue el célebre pacifista que lideró la independencia de la India?",
                "Who was the famous pacifist who led India's independence?", "History",
                Arrays.asList("Jawaharlal Nehru", "Mahatma Gandhi", "Nelson Mandela", "Martin Luther King"),
                "Gandhi promoted nonviolent civil disobedience to achieve Indian independence.", "",
                "Chi fu il celebre pacifista che guidò l'indipendenza dell'India?", "Storia",
                Arrays.asList("Jawaharlal Nehru", "Mahatma Gandhi", "Nelson Mandela", "Martin Luther King"),
                "Gandhi promosse la disobbedienza civile non violenta per l'indipendenza indiana.", "");

        add(311, "¿Qué dios de la mitología griega es el soberano del rayo y el Olimpo?",
                "Which Greek god is the ruler of thunderbolts and Mount Olympus?", "Mythology",
                Arrays.asList("Poseidon", "Zeus", "Hades", "Apollo"),
                "Zeus ruled over the gods of Mount Olympus armed with celestial lightning.", "",
                "Quale dio della mitologia greca è il sovrano del fulmine e dell'Olimpo?", "Mitologia",
                Arrays.asList("Poseidone", "Zeus", "Ade", "Apollo"),
                "Zeus regnava sugli dei del monte Olimpo armato di fulmini celesti.", "");

        add(312, "¿Cómo se llamaba el martillo mágico del dios nórdico Thor?",
                "What was the name of the Norse god Thor's magical hammer?", "Mythology",
                Arrays.asList("Excalibur", "Mjolnir", "Gungnir", "Aegis"),
                "Mjolnir was forged by dwarves and could only be lifted by the worthy.", "",
                "Come si chiamava il martello magico del dio norreno Thor?", "Mitologia",
                Arrays.asList("Excalibur", "Mjolnir", "Gungnir", "Egida"),
                "Mjolnir fu forgiato dai nani e poteva essere sollevato solo dai degni.", "");

        add(313, "DIOS",
                "Unscramble all the letters to form the word!", "Mythology",
                Collections.emptyList(),
                "Each GOD of Olympus governed natural forces in Greek mythology.", "GOD",
                "Riordina tutte le lettere per formare la parola!", "Mitologia",
                Collections.emptyList(),
                "Gli DEI dell'Olimpo governavano le forze naturali nei miti greci.", "DIO");

        add(314, "¿Qué criatura mitológica renace de sus propias cenizas?",
                "Which mythological creature is reborn from its own ashes?", "Mythology",
                Arrays.asList("Griffin", "Phoenix", "Minotaur", "Pegasus"),
                "The Phoenix is an eternal symbol of rebirth and renewal across cultures.", "",
                "Quale creatura mitologica rinasce dalle proprie ceneri?", "Mitologia",
                Arrays.asList("Grifone", "Fenice", "Minotauro", "Pegaso"),
                "La Fenice è un simbolo eterno di rinascita e rinnovamento.", "");

        add(315, "¿Cómo se llamaba el caballo alado de la mitología griega?",
                "What was the winged horse in Greek mythology called?", "Mythology",
                Arrays.asList("Pegasus", "Centaur", "Chimera", "Cerberus"),
                "Pegasus was born from the blood of Medusa after Perseus defeated her.", "",
                "Come si chiamava il cavallo alato della mitologia greca?", "Mitologia",
                Arrays.asList("Pegaso", "Centauro", "Chimera", "Cerbero"),
                "Pegaso nacque dal sangue di Medusa sconfitta da Perseo.", "");

        add(316, "¿Qué instrumento musical de cuerdas frotadas posee 4 cuerdas y un arco?",
                "Which bowed string instrument has 4 strings and a bow?", "Music",
                Arrays.asList("Guitar", "Violin", "Harp", "Flute"),
                "The violin is the smallest and highest-pitched member of the classical strings.", "",
                "Quale strumento musicale ad arco possiede 4 corde e un archetto?", "Musica",
                Arrays.asList("Chitarra", "Violino", "Arpa", "Flauto"),
                "Il violino è lo strumento più piccolo e acuto della famiglia degli archi.", "");

        add(317, "¿Qué compositor clásico compuso la 9ª Sinfonía a pesar de padecer sordera?",
                "Which classical composer wrote the 9th Symphony despite being deaf?", "Music",
                Arrays.asList("Mozart", "Beethoven", "Bach", "Chopin"),
                "Ludwig van Beethoven completed his 9th Symphony and Ode to Joy in 1824.", "",
                "Quale compositore classico scrisse la 9ª Sinfonia nonostante la sordità?", "Musica",
                Arrays.asList("Mozart", "Beethoven", "Bach", "Chopin"),
                "Ludwig van Beethoven completò la sua 9ª Sinfonia nel 1824.", "");

        add(318, "NOTA",
                "Unscramble all the letters to form the word!", "Music",
                Collections.emptyList(),
                "A musical NOTE represents the pitch and duration of a sound.", "NOTE",
                "Riordina tutte le lettere per formare la parola!", "Musica",
                Collections.emptyList(),
                "La NOTA musicale rappresenta la durata e l'altezza del suono.", "NOTA");

        add(319, "¿Cuántas notas musicales naturales existen en la escala diatónica occidental?",
                "How many natural musical notes exist in the Western diatonic scale?", "Music",
                Arrays.asList("5 notes", "7 notes", "10 notes", "12 notes"),
                "The 7 natural notes are: C, D, E, F, G, A, B.", "",
                "Quante note musicali naturali esistono nella scala diatonica occidentale?", "Musica",
                Arrays.asList("5 note", "7 note", "10 note", "12 note"),
                "Le 7 note naturali sono: Do, Re, Mi, Fa, Sol, La, Si.", "");

        add(320, "¿Qué cantante fue coronado mundialmente como el 'Rey del Pop'?",
                "Which singer was crowned worldwide as the 'King of Pop'?", "Music",
                Arrays.asList("Elvis Presley", "Michael Jackson", "Freddie Mercury", "Prince"),
                "Michael Jackson sold hundreds of millions of albums with hits like Thriller.", "",
                "Quale cantante fu incoronato nel mondo come il 'Re del Pop'?", "Musica",
                Arrays.asList("Elvis Presley", "Michael Jackson", "Freddie Mercury", "Prince"),
                "Michael Jackson vendette centinaia di milioni di dischi come Thriller.", "");

        add(401, "¿Qué físico propuso la Teoría de la Relatividad con la fórmula E=mc²?",
                "Which physicist proposed the Theory of Relativity with the formula E=mc²?", "Science",
                Arrays.asList("Isaac Newton", "Albert Einstein", "Niels Bohr", "Galileo Galilei"),
                "Einstein formulated special relativity in 1905 and general relativity in 1915.", "",
                "Quale fisico propose la Teoria della Relatività con la formula E=mc²?", "Scienza",
                Arrays.asList("Isaac Newton", "Albert Einstein", "Niels Bohr", "Galileo Galilei"),
                "Einstein formulò la relatività ristretta nel 1905 e quella generale nel 1915.", "");

        add(402, "¿Cuál es la velocidad aproximada de la luz en el vacío?",
                "What is the approximate speed of light in a vacuum?", "Physics",
                Arrays.asList("150,000 km/s", "300,000 km/s", "500,000 km/s", "1,000,000 km/s"),
                "Light travels at exactly 299,792 kilometers per second in a vacuum.", "",
                "Qual è la velocità approssimativa della luce nel vuoto?", "Fisica",
                Arrays.asList("150.000 km/s", "300.000 km/s", "500.000 km/s", "1.000.000 km/s"),
                "La luce viaggia esattamente a 299.792 chilometri al secondo nel vuoto.", "");

        add(403, "ATOMO",
                "Unscramble all the letters to form the word!", "Physics",
                Collections.emptyList(),
                "The ATOM is the fundamental building block of ordinary matter.", "ATOM",
                "Riordina tutte le lettere per formare la parola!", "Fisica",
                Collections.emptyList(),
                "L'ATOMO è l'unità fondamentale della materia ordinaria.", "ATOMO");

        add(404, "¿Qué fuerza universal descubierta por Newton atrae los cuerpos celestes?",
                "Which universal force discovered by Newton attracts celestial bodies?", "Physics",
                Arrays.asList("Electromagnetism", "Gravity", "Nuclear Force", "Inertia"),
                "Universal gravitation describes the attraction between masses in the universe.", "",
                "Quale forza universale scoperta da Newton attrae i corpi celesti?", "Fisica",
                Arrays.asList("Elettromagnetismo", "Gravità", "Forza Nucleare", "Inerzia"),
                "La gravitazione universale descrive l'attrazione tra le masse nell'universo.", "");

        add(405, "¿Qué instrumento espacial reemplazó y complementa al telescopio Hubble en 2022?",
                "Which space telescope launched to succeed and complement Hubble?", "Astronomy",
                Arrays.asList("Voyager", "James Webb Space Telescope", "Kepler", "Spitzer"),
                "The James Webb Telescope observes the deep cosmos using infrared radiation.", "",
                "Quale strumento spaziale ha affiancato e superato il telescopio Hubble nel 2022?", "Astronomia",
                Arrays.asList("Voyager", "James Webb Space Telescope", "Kepler", "Spitzer"),
                "Il telescopio James Webb osserva il cosmo profondo tramite raggi infrarossi.", "");

        add(406, "¿En qué país se encuentra la zona más fría habitada permanentemente (Oimiakón)?",
                "In which country is the coldest permanently inhabited place (Oymyakon)?", "Geography",
                Arrays.asList("Canada", "Russia (Siberia)", "Norway", "Greenland"),
                "Oymyakon has recorded extreme temperatures below -70 °C.", "",
                "In quale paese si trova la zona più fredda abitata stabilmente (Ojmjakon)?", "Geografia",
                Arrays.asList("Canada", "Russia (Siberia)", "Norvegia", "Groenlandia"),
                "Ojmjakon ha registrato temperature record sotto i -70 °C.", "");

        add(407, "¿Qué continente concentra más del 70% del agua dulce congelada de la Tierra?",
                "Which continent holds over 70% of Earth's frozen fresh water?", "Geography",
                Arrays.asList("Arctic", "Antarctica", "North America", "Asia"),
                "The Antarctic ice sheet stores the vast majority of Earth's fresh water.", "",
                "Quale continente concentra oltre il 70% dell'acqua dolce ghiacciata della Terra?", "Geografia",
                Arrays.asList("Artico", "Antartide", "Nord America", "Asia"),
                "La calotta glaciale dell'Antartide conserva la maggior parte dell'acqua dolce.", "");

        add(408, "NIEVE",
                "Unscramble all the letters to form the word!", "Geography",
                Collections.emptyList(),
                "SNOW forms when water vapor freezes and precipitates below zero.", "SNOW",
                "Riordina tutte le lettere per formare la parola!", "Geografia",
                Collections.emptyList(),
                "La NEVE si forma quando il vapore acqueo precipita sotto zero.", "NEVE");

        add(409, "¿Cuál es el fenómeno lumínico nocturno en cielos polares causado por partículas solares?",
                "What is the polar night light phenomenon caused by solar particles?", "Science",
                Arrays.asList("Moonbow", "Aurora borealis / australis", "Fata Morgana", "Ball lightning"),
                "Auroras occur when solar wind collides with Earth's magnetic field.", "",
                "Qual è il fenomeno luminoso notturno nei cieli polari causato da particelle solari?", "Scienza",
                Arrays.asList("Arcobaleno lunare", "Aurora boreale / australe", "Fata Morgana", "Fulmine globulare"),
                "Le aurore avvengono quando il vento solare collide con il campo magnetico terrestre.", "");

        add(410, "¿Viven pingüinos autóctonos en el Polo Norte geográfico?",
                "Do native penguins live at the geographic North Pole?", "Nature",
                Arrays.asList("Yes, many", "No, they only live in the Southern Hemisphere", "Only in winter", "On Arctic islands"),
                "Penguins live exclusively in the Southern Hemisphere of our planet.", "",
                "Vivono pinguini autoctoni al Polo Nord geografico?", "Natura",
                Arrays.asList("Sì, moltissimi", "No, vivono solo nell'emisfero sud", "Solo in inverno", "Sulle isole artiche"),
                "I pinguini abitano esclusivamente nell'emisfero australe del pianeta.", "");

        add(411, "¿Quién fue el filósofo griego que proclamó: 'Sólo sé que no sé nada'?",
                "Which Greek philosopher proclaimed: 'I know that I know nothing'?", "Philosophy",
                Arrays.asList("Plato", "Socrates", "Aristotle", "Pythagoras"),
                "Socrates used the maieutic method to question apparent certainties.", "",
                "Chi fu il filosofo greco che proclamò: 'So di non sapere'?", "Filosofia",
                Arrays.asList("Platone", "Socrate", "Aristotele", "Pitagora"),
                "Socrate utilizzò il metodo maieutico per mettere in dubbio le certezze.", "");

        add(412, "¿Qué filósofo francés acuñó la frase 'Pienso, luego existo' (Cogito, ergo sum)?",
                "Which French philosopher coined 'I think, therefore I am' (Cogito, ergo sum)?", "Philosophy",
                Arrays.asList("Jean-Jacques Rousseau", "René Descartes", "Voltaire", "Michel Foucault"),
                "Descartes established methodical doubt as the pillar of rationalism.", "",
                "Quale filosofo francese coniò la frase 'Penso, dunque sono' (Cogito, ergo sum)?", "Filosofia",
                Arrays.asList("Jean-Jacques Rousseau", "René Descartes", "Voltaire", "Michel Foucault"),
                "Cartesio pose il dubbio metodico come pilastro del razionalismo.", "");

        add(413, "IDEA",
                "Unscramble all the letters to form the word!", "Philosophy",
                Collections.emptyList(),
                "An IDEA in Platonic philosophy represents the supreme intelligible form.", "IDEA",
                "Riordina tutte le lettere per formare la parola!", "Filosofia",
                Collections.emptyList(),
                "L'IDEA nella filosofia platonica rappresenta il modello intelligibile supremo.", "IDEA");

        add(414, "¿Qué rama de la filosofía se encarga de estudiar lo que es bueno y lo que es moralmente correcto?",
                "Which branch of philosophy studies what is good and morally right?", "Philosophy",
                Arrays.asList("Epistemology", "Ethics", "Aesthetics", "Ontology"),
                "Ethics examines the principles and duties governing human behavior.", "",
                "Quale ramo della filosofia studia ciò che è buono e moralmente corretto?", "Filosofia",
                Arrays.asList("Epistemologia", "Etica", "Estetica", "Ontologia"),
                "L'etica esamina i principi e i doveri che regolano il comportamento umano.", "");

        add(415, "¿Quién escribió el influyente tratado político renacentista 'El Príncipe'?",
                "Who wrote the influential Renaissance political treatise 'The Prince'?", "Philosophy",
                Arrays.asList("Thomas More", "Niccolò Machiavelli", "Erasmus", "Hobbes"),
                "Machiavelli dedicated The Prince in 1513 to the Medici in Florence.", "",
                "Chi scrisse l'influente trattato politico rinascimentale 'Il Principe'?", "Filosofia",
                Arrays.asList("Tommaso Moro", "Niccolò Machiavelli", "Erasmo da Rotterdam", "Hobbes"),
                "Machiavelli dedicò Il Principe nel 1513 ai Medici a Firenze.", "");

        add(416, "¿Cómo se denominan las regiones del espacio de gravedad tan colosal que ni la luz escapa?",
                "What are regions of space with gravity so immense that not even light escapes?", "Astronomy",
                Arrays.asList("Supernovas", "Black Holes", "Pulsars", "Nebulae"),
                "Black holes have an event horizon where escape velocity exceeds light speed.", "",
                "Come si chiamano le regioni dello spazio con gravità tale che nemmeno la luce sfugge?", "Astronomia",
                Arrays.asList("Supernove", "Buchi Neri", "Pulsar", "Nebulose"),
                "I buchi neri hanno un orizzonte degli eventi da cui nulla può uscire.", "");

        add(417, "¿Qué teoría cosmológica describe el origen expansivo del universo hace 13.800 millones de años?",
                "Which cosmological theory describes the universe's expansion 13.8 billion years ago?", "Astronomy",
                Arrays.asList("Steady State Theory", "Big Bang Theory", "Quantum Multiverse", "Oscillating Universe"),
                "The Big Bang was first proposed by Georges Lemaître and confirmed by cosmic radiation.", "",
                "Quale teoria cosmologica descrive l'origine espansiva dell'universo?", "Astronomia",
                Arrays.asList("Teoria dello Stato Stazionario", "Teoria del Big Bang", "Multiverso Quantistico", "Universo Pulsante"),
                "Il Big Bang fu proposto da Georges Lemaître e confermato dalla radiazione cosmica.", "");

        add(418, "COHETE",
                "Unscramble all the letters to form the word!", "Astronomy",
                Collections.emptyList(),
                "A ROCKET enables humanity to explore the outer reaches of the cosmos.", "ROCKET",
                "Riordina tutte le lettere per formare la parola!", "Astronomia",
                Collections.emptyList(),
                "Il RAZZO permette all'umanità di esplorare i confini del cosmo.", "RAZZO");

        add(419, "¿Qué galaxia espiral colisionará con nuestra Vía Láctea dentro de 4.500 millones de años?",
                "Which spiral galaxy will collide with our Milky Way in 4.5 billion years?", "Astronomy",
                Arrays.asList("Sombrero Galaxy", "Andromeda (M31)", "Triangulum", "Black Eye"),
                "Andromeda is approaching the Milky Way at about 110 kilometers per second.", "",
                "Quale galassia a spirale colliderà con la Via Lattea tra 4,5 miliardi di anni?", "Astronomia",
                Arrays.asList("Galassia Sombrero", "Andromeda (M31)", "Triangolo", "Occhio Nero"),
                "Andromeda si avvicina alla Via Lattea a circa 110 chilometri al secondo.", "");

        add(420, "¿Cuál es la partícula mediadora fundamental de la interacción electromagnética y la luz?",
                "What is the fundamental force-carrying particle of electromagnetism and light?", "Physics",
                Arrays.asList("Higgs Boson", "Photon", "Gluon", "Graviton"),
                "The photon is the elementary quantum of electromagnetic radiation and light.", "",
                "Qual è la particella mediatrice fondamentale dell'elettromagnetismo e della luce?", "Fisica",
                Arrays.asList("Bosone di Higgs", "Fotone", "Gluone", "Gravitone"),
                "Il fotone è il quanto elementare della radiazione elettromagnetica e della luce.", "");

        LEVELS.put(1, new TranslatedLevel("Awakening of Flora", "Photosynthesis & Secrets of Nature", "Nature",
                "Risveglio della Flora", "Fotosintesi e Segreti della Natura", "Natura"));
        LEVELS.put(2, new TranslatedLevel("Treasures of Pharaohs", "Mysteries of Ancient Egypt & the Nile", "History",
                "Tesori dei Faraoni", "Misteri dell'Antico Egitto e del Nilo", "Storia"));
        LEVELS.put(3, new TranslatedLevel("Alchemy & Elements", "Fundamentals of Chemistry & Air", "Science",
                "Alchimia ed Elementi", "Fondamenti di Chimica e Aria", "Scienza"));
        LEVELS.put(4, new TranslatedLevel("Routes of the Earth", "Mountain Ranges, Deserts & Oceans", "Geography",
                "Rotte della Terra", "Catene Montuose, Deserti e Oceani", "Geografia"));
        LEVELS.put(5, new TranslatedLevel("Realm of Algorithms", "Kotlin, Bits & Software Creators", "Technology",
                "Regno degli Algoritmi", "Kotlin, Bit e Creatori del Software", "Tecnologia"));
        LEVELS.put(6, new TranslatedLevel("Hollywood Nights", "Cinema Classics & Animated Films", "Cinema",
                "Notti di Hollywood", "Classici del Cinema e Film d'Animazione", "Cinema"));
        LEVELS.put(7, new TranslatedLevel("Spirit of Champions", "Olympic Feats & World Records", "Sports",
                "Spirito dei Campioni", "Imprese Olimpiche e Record Mondiali", "Sport"));
        LEVELS.put(8, new TranslatedLevel("Pixel Worlds", "Retro Consoles & Gaming Legends", "Video Games",
                "Mondi Pixelati", "Console Retro e Leggende del Gaming", "Videogiochi"));
        LEVELS.put(9, new TranslatedLevel("Renaissance Works", "Master Painters & World Literature", "Art",
                "Opere del Rinascimento", "Grandi Pittori e Letteratura", "Arte"));
        LEVELS.put(10, new TranslatedLevel("Echoes of the Past", "Sacred Citadels & Fallen Walls", "History",
                "Echi del Passato", "Cittadelle Sacre e Mura Cadute", "Storia"));
        LEVELS.put(11, new TranslatedLevel("Gods & Myths", "Thunder on Olympus & Thor's Hammer", "Mythology",
                "Dei e Miti", "Fulmini sull'Olimpo e Martello di Thor", "Mitologia"));
        LEVELS.put(12, new TranslatedLevel("Eternal Symphonies", "Magic Notes & Rock Legends", "Music",
                "Sinfonie Eterne", "Note Magiche e Stelle del Rock", "Musica"));
        LEVELS.put(13, new TranslatedLevel("Physics of the Unseen", "Relativity, Atoms & Cosmic Light", "Physics",
                "Fisica dell'Invisibile", "Relatività, Atomi e Luce Cosmica", "Fisica"));
        LEVELS.put(14, new TranslatedLevel("Frozen Lands", "Glaciers, Auroras & White Deserts", "Geography",
                "Terre Ghiacciate", "Ghiacciai, Aurore e Deserti Bianchi", "Geografia"));
        LEVELS.put(15, new TranslatedLevel("Dilemma of the Mind", "Socrates, Descartes & Ethics", "Philosophy",
                "Dilemma della Mente", "Socrate, Cartesio ed Etica", "Filosofia"));
        LEVELS.put(16, new TranslatedLevel("Frontiers of Cosmos", "Black Holes & Voyager's Flight", "Astronomy",
                "Confini del Cosmo", "Buchi Neri e Viaggio delle Voyager", "Astronomia"));
    }

    private QuestionTranslator() {
    }

    private static void add(int id, String keyEs,
                            String qEn, String cEn, List<String> oEn, String eEn, String twEn,
                            String qIt, String cIt, List<String> oIt, String eIt, String twIt) {
        TranslatedQ entry = new TranslatedQ(qEn, cEn, oEn, eEn, twEn, qIt, cIt, oIt, eIt, twIt);
        BY_SPANISH_KEY.put(keyEs, entry);
        BY_SPANISH_KEY.put(String.valueOf(id), entry);
    }

    public static String getCurrentLanguage() {
        String tags = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        if (tags != null && !tags.isEmpty()) {
            String lower = tags.toLowerCase(Locale.ROOT);
            if (lower.startsWith("en")) return "en";
            if (lower.startsWith("it")) return "it";
            if (lower.startsWith("es")) return "es";
        }
        String sys = Locale.getDefault().getLanguage().toLowerCase(Locale.ROOT);
        if ("en".equals(sys) || "it".equals(sys)) {
            return sys;
        }
        return "es";
    }

    public static Question localize(Question q) {
        if (q == null || q.isCustom()) {
            return q;
        }
        String lang = getCurrentLanguage();
        if ("es".equals(lang)) {
            return q;
        }
        TranslatedQ t = q.isWordPuzzle()
                ? BY_SPANISH_KEY.get(q.getTargetWord())
                : BY_SPANISH_KEY.get(q.getQuestionText());
        if (t == null) {
            t = BY_SPANISH_KEY.get(String.valueOf(q.getId()));
        }
        if (t == null) {
            return q;
        }
        String qText = "en".equals(lang) ? t.questionEn : t.questionIt;
        String cat = "en".equals(lang) ? t.categoryEn : t.categoryIt;
        List<String> opts = "en".equals(lang) ? t.optionsEn : t.optionsIt;
        String exp = "en".equals(lang) ? t.explanationEn : t.explanationIt;
        String tw = q.isWordPuzzle() ? ("en".equals(lang) ? t.targetWordEn : t.targetWordIt) : q.getTargetWord();
        return new Question(
                q.getId(),
                q.getLevelId(),
                qText,
                cat,
                q.getCategoryColor(),
                opts,
                q.getCorrectAnswerIndex(),
                exp,
                q.isWordPuzzle(),
                tw,
                q.getMissingCharIndex(),
                q.getClueIconRes(),
                q.isCustom()
        );
    }

    public static Level localizeLevel(Level level) {
        if (level == null) {
            return null;
        }
        String lang = getCurrentLanguage();
        if ("es".equals(lang)) {
            return level;
        }
        TranslatedLevel tl = LEVELS.get(level.getId());
        if (tl == null) {
            return level;
        }
        String title = "en".equals(lang) ? tl.titleEn : tl.titleIt;
        String subtitle = "en".equals(lang) ? tl.subtitleEn : tl.subtitleIt;
        String cat = "en".equals(lang) ? tl.categoryEn : tl.categoryIt;
        List<Question> localizedQuestions = new java.util.ArrayList<>();
        if (level.getQuestions() != null) {
            for (Question q : level.getQuestions()) {
                localizedQuestions.add(localize(q));
            }
        }
        return new Level(
                level.getId(),
                level.getWorldId(),
                level.getLevelNumber(),
                title,
                subtitle,
                cat,
                level.getCategoryColor(),
                localizedQuestions
        );
    }
}
