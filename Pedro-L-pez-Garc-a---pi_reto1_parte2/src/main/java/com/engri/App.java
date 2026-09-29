package com.engri;

import java.time.LocalDate;

/**
 * Programa principal para demostrar el funcionamiento de las clases del reto.
 * Las pruebas automatizadas se encuentran en src/test/java.
 */
public class App {

    public static void main(String[] args) {
        probarNoticia();
        probarPeriodico();
        probarLimite();

        System.out.println("=== TODAS LAS PRUEBAS MANUALES FINALIZADAS CORRECTAMENTE ===");
    }

    /**
     * Comprueba la creación de una noticia y los métodos básicos de Noticia.
     */
    private static void probarNoticia() {
        System.out.println("=== PRUEBA 1: CREAR NOTICIAS ===");

        Noticia noticia = new Noticia(20260929, 900, "Nueva versión del sistema publicada.");

        comprobar(noticia.getFecha() == 20260929,
                "La fecha de la noticia no es correcta.");
        comprobar(noticia.getLecturas() == 0,
                "Las lecturas iniciales deben ser 0.");
        comprobar(noticia.igualFecha(29, 9, 2026),
                "igualFecha() no reconoce correctamente la fecha.");
        comprobar(!noticia.igualFecha(28, 9, 2026),
                "igualFecha() ha dado un positivo incorrecto.");

        noticia.incLecturas();
        comprobar(noticia.getLecturas() == 1,
                "incLecturas() no ha incrementado correctamente las lecturas.");

        System.out.println(noticia);
    }

    /**
     * Comprueba las operaciones principales de Periodico.
     */
    private static void probarPeriodico() {
        System.out.println("=== PRUEBA 2: INSERTAR ===");

        Noticia noticia1 = new Noticia(20260929, 900, "Nueva versión del sistema publicada.");
        Noticia noticia2 = new Noticia(20260928, 1200, "El equipo presenta el nuevo proyecto.");
        Noticia noticia3 = new Noticia(20260929, 1100, "Se anuncia una nueva funcionalidad.");
        Noticia noticia4 = new Noticia(20260930, 800, "Comienza la fase final del proyecto.");

        Periodico periodico = new Periodico();
        periodico.insertar(noticia1);
        periodico.insertar(noticia2);
        periodico.insertar(noticia3);
        periodico.insertar(noticia4);
        periodico.insertar(null); // Debe ignorarse.

        System.out.println("Noticias insertadas:");
        periodico.mostrar();

        System.out.println("=== PRUEBA 3: PRIMERA NOTICIA ===");

        Noticia primera = periodico.primeraNoticia(29, 9, 2026);
        comprobar(primera == noticia1,
                "primeraNoticia() no devuelve la primera coincidencia.");
        System.out.println("Primera noticia del 29/9/2026:");
        System.out.println(primera);

        comprobar(periodico.primeraNoticia(1, 1, 2000) == null,
                "primeraNoticia() debería devolver null si no hay coincidencias.");
        System.out.println("Búsqueda de una fecha inexistente: null");

        System.out.println("=== PRUEBA 4: MÁS POPULARES ===");

        // Se fuerza un empate para comprobar que se muestran todas las noticias más leídas.
        noticia1.incLecturas();
        noticia1.incLecturas();
        noticia2.incLecturas();
        noticia2.incLecturas();
        noticia3.incLecturas();
        noticia3.incLecturas();
        noticia4.incLecturas();

        periodico.masPopulares();

        System.out.println("=== PRUEBA 5: BORRAR ANTERIORES ===");
        periodico.borrarAnteriores(29, 9, 2026);

        comprobar(periodico.primeraNoticia(28, 9, 2026) == null,
                "borrarAnteriores() no ha eliminado la noticia anterior a la fecha límite.");
        comprobar(periodico.primeraNoticia(29, 9, 2026) != null,
                "borrarAnteriores() ha eliminado una noticia de la propia fecha límite.");
        comprobar(periodico.primeraNoticia(30, 9, 2026) != null,
                "borrarAnteriores() ha eliminado una noticia posterior a la fecha límite.");

        System.out.println("Noticias después de borrar las anteriores al 29/9/2026:");
        periodico.mostrar();

        System.out.println("=== PRUEBA ADICIONAL: PERIÓDICO VACÍO ===");
        Periodico periodicoVacio = new Periodico();
        comprobar(periodicoVacio.primeraNoticia(29, 9, 2026) == null,
                "Un periódico vacío debe devolver null en primeraNoticia().");
        periodicoVacio.masPopulares();
    }

    /**
     * Comprueba que el periódico respeta el máximo de 1000 noticias.
     */
    private static void probarLimite() {
        System.out.println("=== PRUEBA ADICIONAL: LÍMITE DE 1000 NOTICIAS ===");

        Periodico periodico = new Periodico();
        LocalDate fechaBase = LocalDate.of(2026, 1, 1);

        for (int i = 0; i < 1000; i++) {
            LocalDate fecha = fechaBase.plusDays(i);
            int fechaEntera = fecha.getYear() * 10000
                    + fecha.getMonthValue() * 100
                    + fecha.getDayOfMonth();

            periodico.insertar(new Noticia(fechaEntera, 1200, "Noticia de prueba " + i));
        }

        // La noticia número 1001 no debe insertarse.
        periodico.insertar(new Noticia(20991231, 2359, "Noticia fuera de capacidad"));

        comprobar(periodico.primeraNoticia(31, 12, 2099) == null,
                "El periódico no debe superar las 1000 noticias.");
    }

    /**
     * Lanza un error claro si una condición de la prueba no se cumple.
     */
    private static void comprobar(boolean condicion, String mensajeError) {
        if (!condicion) {
            throw new AssertionError(mensajeError);
        }
    }
}
