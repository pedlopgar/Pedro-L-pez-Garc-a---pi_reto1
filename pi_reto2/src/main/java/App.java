import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import java.io.IOException;

public class App {
    /**
     * Con esta método compruebo el Status code de la respuesta que recibo al hacer la petición
     * EJM:
     * 		200 OK			300 Multiple Choices
     * 		301 Moved Permanently	305 Use Proxy
     * 		400 Bad Request		403 Forbidden
     * 		404 Not Found		500 Internal Server Error
     * 		502 Bad Gateway		503 Service Unavailable
     * @param url
     * @return Status Code
     */
    public static int getStatusConnectionCode(String url) {

        Connection.Response response = null;

        try {
            response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36")
                    .header("Accept-Language", "es-ES,es;q=0.9")
                    .timeout(100000)
                    .ignoreHttpErrors(true)
                    .execute();

        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: "
                    + ex.getMessage());
        }

        if (response != null) {
            return response.statusCode();
        }

        return -1;
    }

    /**
     * Con este método devuelvo un objeto de la clase Document con el contenido del
     * HTML de la web que me permitirá parsearlo con los métodos de la librelia JSoup
     * @param url
     * @return Documento con el HTML
     */
    public static Document getHtmlDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/156.0.0.0 Safari/537.36")
                    .header("Accept-Language", "es-ES,es;q=0.9")
                    .timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }
        return doc;
    }
    public static final String url =
            "https://www.filmaffinity.com/es/cat_new_th_es.html";

    public static void main(String[] args) {

        // Compruebo si la peticion devuelve un 200
        int codigo = getStatusConnectionCode(url);

        if (codigo == 200) {

            // Obtengo el HTML de la cartelera
            Document document = getHtmlDocument(url);

            if (document == null) {
                System.out.println("No se ha podido obtener la cartelera.");
                return;
            }

            // Busco los enlaces que contienen la direccion de una ficha de pelicula
            Elements entradas = document.select("a[href*='/es/film']");

            System.out.println("Enlaces encontrados: " + entradas.size());

            // Guardo las URL que ya he procesado
            ArrayList<String> peliculasVisitadas = new ArrayList<>();

            // Creo el fichero y escribo los datos de las peliculas
            try (BufferedWriter escritor =
                         new BufferedWriter(new FileWriter("cartelera.txt"))) {

                // Recorro los enlaces encontrados
                for (Element elem : entradas) {

                    String urlPelicula = elem.absUrl("href");

                    // Evito procesar dos veces la misma pelicula
                    if (urlPelicula.isEmpty()
                            || peliculasVisitadas.contains(urlPelicula)) {
                        continue;
                    }

                    peliculasVisitadas.add(urlPelicula);

                    // Obtengo el HTML de la ficha de la pelicula
                    Document ficha = getHtmlDocument(urlPelicula);

                    if (ficha == null) {
                        continue;
                    }

                    // Extraigo los datos de la pelicula
                    String titulo = ficha.select("h1#main-title").text();
                    String ano = ficha.select("dt:contains(Año) + dd").text();
                    String pais = ficha.select("dt:contains(País) + dd").text();
                    String director = ficha.select("dt:contains(Dirección) + dd").text();
                    String sinopsis = ficha.select("dt:contains(Sinopsis) + dd").text();

                    // Compruebo que he encontrado el titulo
                    if (titulo.isEmpty()) {
                        System.out.println("No se ha encontrado el titulo: "
                                + urlPelicula);
                        continue;
                    }

                    // Escribo los datos en el fichero
                    escritor.write("Titulo: " + titulo);
                    escritor.newLine();

                    escritor.write("Año: " + ano);
                    escritor.newLine();

                    escritor.write("Pais: " + pais);
                    escritor.newLine();

                    escritor.write("Director: " + director);
                    escritor.newLine();

                    escritor.write("Sinopsis: " + sinopsis);
                    escritor.newLine();

                    escritor.write("----------------------------------------");
                    escritor.newLine();

                    System.out.println("Procesada: " + titulo);
                }

            } catch (IOException ex) {
                System.out.println("Error al escribir el fichero: "
                        + ex.getMessage());
            }

            // Leo el fichero y muestro su contenido por consola
            System.out.println("\nContenido de cartelera.txt:\n");

            try (BufferedReader lector =
                         new BufferedReader(new FileReader("cartelera.txt"))) {

                String linea;

                while ((linea = lector.readLine()) != null) {
                    System.out.println(linea);
                }

            } catch (IOException ex) {
                System.out.println("Error al leer el fichero: "
                        + ex.getMessage());
            }

        } else {
            System.out.println("La conexion no ha devuelto un 200. Codigo: "
                    + getStatusConnectionCode(url));
        }
    }

}
