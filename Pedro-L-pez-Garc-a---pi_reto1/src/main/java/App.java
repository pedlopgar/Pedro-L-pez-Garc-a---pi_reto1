public class App {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA 1: CREAR NOTICIAS ===");

        Periodico periodico = new Periodico();

        Noticia n1 = new Noticia(20240101,1130,"Pepito ha descubierto una estrella de neutrinos en la galaxia Pepinetix");
        Noticia n2 = new Noticia(20260921,2025,"Última hora: Votar a gente mala tiene consecuencias peores.");
        Noticia n3 = new Noticia(20250516,845,"Chaval aprueba curso.");
        Noticia n4 = new Noticia(20250516,845,"Me he comido un bocata de lomo-queso, ñam.");

        if (n1 != null && n2 != null & n3 != null& n4 != null){
            System.out.println("Noticias creadas con éxito");
        }

        System.out.println("=== PRUEBA 2: INSERTAR ===");

        periodico.insertar(n1);
        periodico.insertar(n2);
        periodico.insertar(n3);
        periodico.insertar(n4);

        periodico.mostrar();

//......................................................................................................................
        System.out.println("=== PRUEBA 3: PRIMERA NOTICIA ===");

        Noticia noticiaObjetivo = periodico.primeraNoticia(21,9,2026);
        if (noticiaObjetivo != null){
            System.out.println("Primera noticia:" + noticiaObjetivo.toString());
        } else {
            System.out.println("ERROR: No hay noticias para mostrar.");
        }

//......................................................................................................................
        System.out.println("=== PRUEBA 4: MÁS POPULARES ===");

        n1.incLecturas();
        n1.incLecturas();
        n2.incLecturas();
        n4.incLecturas();
        n3.incLecturas();
        n3.incLecturas();
        n2.incLecturas();
        n2.incLecturas();
        periodico.masPopulares();

//......................................................................................................................
        System.out.println("=== PRUEBA 5: BORRAR ANTERIORES ===");

        periodico.borrarAnteriores(01,01,2024);
        periodico.mostrar();

//......................................................................................................................
    }
}
