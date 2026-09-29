package com.engri;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodicoTest {
    private final PrintStream salidaOriginal = System.out;

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    void insertarYPrimeraNoticiaDebenRespetarElOrden() {
        Periodico periodico = new Periodico();
        Noticia primera = new Noticia(20260929, 900, "Primera");
        Noticia segunda = new Noticia(20260929, 1100, "Segunda");

        periodico.insertar(primera);
        periodico.insertar(segunda);

        assertSame(primera, periodico.primeraNoticia(29, 9, 2026));
    }

    @Test
    void primeraNoticiaDebeDevolverNullSiNoExisteONoHayNoticias() {
        Periodico vacio = new Periodico();
        assertNull(vacio.primeraNoticia(29, 9, 2026));

        Periodico periodico = new Periodico();
        periodico.insertar(new Noticia(20260929, 900, "Noticia"));

        assertNull(periodico.primeraNoticia(30, 9, 2026));
    }

    @Test
    void insertarDebeIgnorarReferenciasNulas() {
        Periodico periodico = new Periodico();

        periodico.insertar(null);

        assertNull(periodico.primeraNoticia(29, 9, 2026));
    }

    @Test
    void noDebeSuperarLasMilNoticias() {
        Periodico periodico = new Periodico();
        LocalDate fechaBase = LocalDate.of(2026, 1, 1);

        for (int i = 0; i < 1000; i++) {
            periodico.insertar(new Noticia(toFecha(fechaBase.plusDays(i)), 1200, "Noticia " + i));
        }

        periodico.insertar(new Noticia(20991231, 1200, "Fuera de limite"));

        assertNull(periodico.primeraNoticia(31, 12, 2099));
    }

    @Test
    void borrarAnterioresDebeEliminarSoloLasFechasMenores() {
        Periodico periodico = new Periodico();
        Noticia anterior = new Noticia(20260928, 900, "Anterior");
        Noticia limite = new Noticia(20260929, 900, "Limite");
        Noticia posterior = new Noticia(20260930, 900, "Posterior");

        periodico.insertar(anterior);
        periodico.insertar(limite);
        periodico.insertar(posterior);

        periodico.borrarAnteriores(29, 9, 2026);

        assertNull(periodico.primeraNoticia(28, 9, 2026));
        assertSame(limite, periodico.primeraNoticia(29, 9, 2026));
        assertSame(posterior, periodico.primeraNoticia(30, 9, 2026));
    }

    @Test
    void borrarAnterioresDebeMantenerElOrden() {
        Periodico periodico = new Periodico();
        Noticia primera = new Noticia(20260929, 800, "Primera");
        Noticia segunda = new Noticia(20260930, 800, "Segunda");

        periodico.insertar(new Noticia(20260928, 800, "Eliminar"));
        periodico.insertar(primera);
        periodico.insertar(segunda);

        periodico.borrarAnteriores(29, 9, 2026);

        ByteArrayOutputStream salida = capturarSalida(periodico::mostrar);
        String resultado = salida.toString(StandardCharsets.UTF_8);

        assertTrue(resultado.indexOf("Primera") < resultado.indexOf("Segunda"));
    }

    @Test
    void masPopularesDebeMostrarTodasLasNoticiasEmpatadas() {
        Periodico periodico = new Periodico();
        Noticia primera = new Noticia(20260929, 800, "Primera");
        Noticia segunda = new Noticia(20260929, 900, "Segunda");
        Noticia tercera = new Noticia(20260929, 1000, "Tercera");

        periodico.insertar(primera);
        periodico.insertar(segunda);
        periodico.insertar(tercera);

        primera.incLecturas();
        primera.incLecturas();
        segunda.incLecturas();
        segunda.incLecturas();
        tercera.incLecturas();

        ByteArrayOutputStream salida = capturarSalida(periodico::masPopulares);
        String resultado = salida.toString(StandardCharsets.UTF_8);

        assertTrue(resultado.contains("Primera"));
        assertTrue(resultado.contains("Segunda"));
        assertFalse(resultado.contains("Tercera"));
    }

    @Test
    void masPopularesDebeInformarSiElPeriodicoEstaVacio() {
        Periodico periodico = new Periodico();

        ByteArrayOutputStream salida = capturarSalida(periodico::masPopulares);
        String resultado = salida.toString(StandardCharsets.UTF_8).trim();

        assertEquals("No hay noticias.", resultado);
    }

    @Test
    void despuesDeBorrarSeDebePoderInsertarSinPerderLaSincronizacion() {
        Periodico periodico = new Periodico();
        periodico.insertar(new Noticia(20260928, 800, "Eliminar"));
        Noticia conservar = new Noticia(20260929, 800, "Conservar");
        Noticia nueva = new Noticia(20261001, 800, "Nueva");

        periodico.insertar(conservar);
        periodico.borrarAnteriores(29, 9, 2026);
        periodico.insertar(nueva);

        assertSame(conservar, periodico.primeraNoticia(29, 9, 2026));
        assertSame(nueva, periodico.primeraNoticia(1, 10, 2026));
    }

    @Test
    void losLectoresDeLasNoticiasDebenMantenerseAlBorrarOtrasNoticias() {
        Periodico periodico = new Periodico();
        Noticia eliminar = new Noticia(20260928, 800, "Eliminar");
        Noticia conservar = new Noticia(20260929, 800, "Conservar");

        conservar.incLecturas();
        periodico.insertar(eliminar);
        periodico.insertar(conservar);

        periodico.borrarAnteriores(29, 9, 2026);

        assertEquals(1, conservar.getLecturas());
    }

    private static int toFecha(LocalDate fecha) {
        return fecha.getYear() * 10000 + fecha.getMonthValue() * 100 + fecha.getDayOfMonth();
    }

    private static ByteArrayOutputStream capturarSalida(Runnable accion) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
        accion.run();
        return salida;
    }
}
