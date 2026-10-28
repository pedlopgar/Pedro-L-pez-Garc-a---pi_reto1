import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App2 {

    public static final String url =
            "https://store.steampowered.com/charts/mostplayed/?cc=es&l=spanish";

    /**
     * Compruebo el codigo de estado de la respuesta HTTP.
     *
     * @param url Direccion de la pagina
     * @return Codigo de estado HTTP
     */
    public static int getStatusConnectionCode(String url) {

        Connection.Response response = null;

        try {
            response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Language", "es-ES,es;q=0.9")
                    .timeout(30000)
                    .ignoreHttpErrors(true)
                    .execute();

        } catch (IOException ex) {
            System.out.println("Error al obtener el Status Code: "
                    + ex.getMessage());
        }

        if (response != null) {
            return response.statusCode();
        }

        return -1;
    }

    /**
     * Obtengo el HTML de una pagina en un objeto Document.
     *
     * @param url Direccion de la pagina
     * @return Documento HTML o null si falla la peticion
     */
    public static Document getHtmlDocument(String url) {

        Document doc = null;

        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Language", "es-ES,es;q=0.9")
                    .timeout(30000)
                    .ignoreHttpErrors(true)
                    .execute();

            if (response.statusCode() == 200) {
                doc = response.parse();
            } else {
                System.out.println("Error HTTP "
                        + response.statusCode() + ": " + url);
            }

        } catch (IOException ex) {
            System.out.println("Error al obtener el HTML: "
                    + ex.getMessage());
        }

        return doc;
    }

    /**
     * Obtengo los textos de varios elementos sin repetirlos.
     *
     * @param elementos Elementos HTML encontrados
     * @return Textos separados por comas
     */
    public static String obtenerTextosUnicos(Elements elementos) {

        ArrayList<String> textos = new ArrayList<>();

        for (Element elem : elementos) {

            String texto = elem.text().trim();

            if (!texto.isEmpty() && !textos.contains(texto)) {
                textos.add(texto);
            }
        }

        return String.join(", ", textos);
    }

    public static void main(String[] args) {

        // Compruebo si la peticion devuelve un 200
        int codigo = getStatusConnectionCode(url);

        if (codigo == 200) {

            // Obtengo el HTML de la clasificacion
            Document document = getHtmlDocument(url);

            if (document == null) {
                System.out.println(
                        "No se ha podido obtener la clasificacion.");
                return;
            }

            // Busco los enlaces a las fichas de los juegos
            Elements entradas = document.select("a[href*='/app/']");

            // Guardo los identificadores y nombres de los juegos
            ArrayList<String> juegos = new ArrayList<>();
            ArrayList<String> nombresJuegos = new ArrayList<>();

            Pattern patronId = Pattern.compile("/app/(\\d+)");

            for (Element elem : entradas) {

                String enlace = elem.attr("href");

                Matcher coincidencia = patronId.matcher(enlace);

                if (coincidencia.find()) {

                    String idJuego = coincidencia.group(1);

                    if (!juegos.contains(idJuego)) {

                        String nombre = elem.text().trim();

                        if (nombre.isEmpty()) {
                            nombre = elem.attr("title").trim();
                        }

                        if (nombre.isEmpty()) {
                            nombre = elem.select("img").attr("alt").trim();
                        }

                        juegos.add(idJuego);
                        nombresJuegos.add(nombre);
                    }
                }

                // Me quedo con los primeros 100 identificadores
                if (juegos.size() == 100) {
                    break;
                }
            }

            System.out.println("Juegos encontrados: " + juegos.size());

            int juegosProcesados = 0;

            // Creo el fichero donde guardare los datos
            try (BufferedWriter escritor =
                         new BufferedWriter(
                                 new FileWriter("masJugadosSteam.txt"))) {

                // Recorro los identificadores de los juegos
                for (int i = 0; i < juegos.size(); i++) {

                    // Dejo una pausa entre peticiones
                    if (i > 0) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            System.out.println("Proceso interrumpido.");
                            break;
                        }
                    }

                    String idJuego = juegos.get(i);
                    String nombreClasificacion = nombresJuegos.get(i);

                    String urlJuego =
                            "https://store.steampowered.com/app/"
                                    + idJuego + "/?l=spanish";

                    // Obtengo el HTML de la ficha del juego
                    Document ficha = getHtmlDocument(urlJuego);

                    if (ficha == null) {
                        System.out.println(
                                "No se ha podido descargar la ficha de "
                                        + idJuego + ". Se detiene el proceso.");
                        break;
                    }

                    // Extraigo el titulo sin duplicarlo
                    Element elementoTitulo =
                            ficha.select(".apphub_AppName").first();

                    String titulo = nombreClasificacion;

                    if (elementoTitulo != null
                            && !elementoTitulo.text().trim().isEmpty()) {
                        titulo = elementoTitulo.text().trim();
                    }

                    // Extraigo el año de lanzamiento
                    String fecha =
                            ficha.select(".release_date .date").text();

                    String ano = "No disponible";

                    Matcher coincidenciaAno =
                            Pattern.compile("(19|20)\\d{2}")
                                    .matcher(fecha);

                    if (coincidenciaAno.find()) {
                        ano = coincidenciaAno.group();
                    }

                    // Extraigo los generos
                    Elements generosEncontrados =
                            ficha.select("a[href*='/genre/']");

                    String genero =
                            obtenerTextosUnicos(generosEncontrados);

                    if (genero.isEmpty()) {
                        genero = "No disponible";
                    }

                    // Extraigo el desarrollador
                    String desarrollador =
                            obtenerTextosUnicos(
                                    ficha.select("#developers_list a"));

                    if (desarrollador.isEmpty()) {
                        desarrollador = "No disponible";
                    }

                    // Extraigo el publisher sin repetir nombres
                    String publisher =
                            obtenerTextosUnicos(
                                    ficha.select("a[href*='/publisher/']"));

                    if (publisher.isEmpty()) {
                        publisher = "No disponible";
                    }

                    // Extraigo la descripcion
                    String descripcion =
                            ficha.select("#game_area_description").text();

                    // Quito el encabezado de la descripcion
                    descripcion = descripcion
                            .replaceFirst(
                                    "(?i)^Acerca de este juego\\s*", "")
                            .trim();

                    // Si esta vacia, pruebo con la descripcion breve
                    if (descripcion.isEmpty()) {
                        descripcion =
                                ficha.select(".game_description_snippet")
                                        .text().trim();
                    }

                    // Como ultima alternativa, consulto los metadatos
                    if (descripcion.isEmpty()) {
                        descripcion = ficha
                                .select("meta[name=description]")
                                .attr("content").trim();
                    }

                    if (descripcion.isEmpty()) {
                        descripcion = "No disponible";
                    }

                    // Compruebo que existe un titulo
                    if (titulo.isEmpty()) {
                        System.out.println(
                                "No se ha encontrado el titulo del juego: "
                                        + idJuego);
                        continue;
                    }

                    // Escribo los datos en el fichero
                    escritor.write("Titulo: " + titulo);
                    escritor.newLine();

                    escritor.write("Año: " + ano);
                    escritor.newLine();

                    escritor.write("Genero: " + genero);
                    escritor.newLine();

                    escritor.write("Desarrollador: " + desarrollador);
                    escritor.newLine();

                    escritor.write("Publisher: " + publisher);
                    escritor.newLine();

                    escritor.write("Descripcion: " + descripcion);
                    escritor.newLine();

                    escritor.write("----------------------------------------");
                    escritor.newLine();

                    juegosProcesados++;

                    System.out.println("Procesado: " + titulo);
                }

            } catch (IOException ex) {
                System.out.println("Error al escribir el fichero: "
                        + ex.getMessage());
            }

            System.out.println("\nTotal de juegos procesados: "
                    + juegosProcesados);

            // Leo el fichero y muestro su contenido por consola
            System.out.println("\nContenido de masJugadosSteam.txt:\n");

            try (BufferedReader lector =
                         new BufferedReader(
                                 new FileReader("masJugadosSteam.txt"))) {

                String linea;

                while ((linea = lector.readLine()) != null) {
                    System.out.println(linea);
                }

            } catch (IOException ex) {
                System.out.println("Error al leer el fichero: "
                        + ex.getMessage());
            }

        } else {
            System.out.println(
                    "La conexion no ha devuelto un 200. Codigo: " + codigo);
        }
    }
}