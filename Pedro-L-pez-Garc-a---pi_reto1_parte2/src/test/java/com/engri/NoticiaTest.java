package com.engri;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoticiaTest {

    @Test
    void constructorDebeInicializarLosDatosYLasLecturas() {
        Noticia noticia = new Noticia(20260929, 1005, "Texto de prueba");

        assertEquals(20260929, noticia.getFecha());
        assertEquals(0, noticia.getLecturas());
    }

    @Test
    void incLecturasDebeIncrementarUnaUnidad() {
        Noticia noticia = new Noticia(20260929, 1005, "Texto de prueba");

        noticia.incLecturas();
        noticia.incLecturas();

        assertEquals(2, noticia.getLecturas());
    }

    @Test
    void igualFechaDebeReconocerCoincidencias() {
        Noticia noticia = new Noticia(20260929, 1005, "Texto de prueba");

        assertTrue(noticia.igualFecha(29, 9, 2026));
        assertFalse(noticia.igualFecha(28, 9, 2026));
    }

    @Test
    void toStringDebeContenerLaInformacionDeLaNoticia() {
        Noticia noticia = new Noticia(20260929, 1005, "Texto de prueba");
        noticia.incLecturas();

        String resultado = noticia.toString();

        assertTrue(resultado.contains("29/9/2026 - 10:5"));
        assertTrue(resultado.contains("Texto de prueba"));
        assertTrue(resultado.contains("Leída 1 veces"));
    }

    @Test
    void debeRechazarFechasInvalidas() {
        assertThrows(IllegalArgumentException.class,
                () -> new Noticia(20260230, 1005, "Texto de prueba"));
    }

    @Test
    void debeRechazarHorasInvalidas() {
        assertThrows(IllegalArgumentException.class,
                () -> new Noticia(20260929, 1260, "Texto de prueba"));
    }

    @Test
    void debeRechazarTextoNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Noticia(20260929, 1005, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Noticia(20260929, 1005, "   "));
    }

    @Test
    void igualFechaDebeRechazarUnaFechaInvalida() {
        Noticia noticia = new Noticia(20260929, 1005, "Texto de prueba");

        assertThrows(IllegalArgumentException.class,
                () -> noticia.igualFecha(31, 2, 2026));
    }
}
