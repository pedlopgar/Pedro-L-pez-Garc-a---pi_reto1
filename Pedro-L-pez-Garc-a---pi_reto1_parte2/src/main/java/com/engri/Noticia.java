package com.engri;

/**
 * Representa una noticia del periódico.
 *
 * <p>La fecha se almacena como un entero en formato aaaammdd y la hora
 * como un entero en formato hhmm, tal y como establece el enunciado.</p>
 */
public class Noticia {
    private final int fecha;
    private final int hora;
    private final String texto;
    private int lecturas;

    /**
     * Crea una noticia sin lecturas iniciales.
     *
     * @param fecha fecha en formato aaaammdd
     * @param hora hora en formato hhmm
     * @param texto texto de la noticia
     * @throws IllegalArgumentException si la fecha, la hora o el texto no son válidos
     */
    public Noticia(int fecha, int hora, String texto) {
        validarFecha(fecha);
        validarHora(hora);

        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto de la noticia no puede estar vacío.");
        }

        this.fecha = fecha;
        this.hora = hora;
        this.texto = texto;
        this.lecturas = 0;
    }

    /**
     * Devuelve la fecha en formato aaaammdd.
     *
     * @return fecha de la noticia
     */
    public int getFecha() {
        return fecha;
    }

    /**
     * Devuelve el número de lecturas de la noticia.
     *
     * @return número de lecturas
     */
    public int getLecturas() {
        return lecturas;
    }

    /**
     * Incrementa en una unidad el número de lecturas.
     *
     * @throws ArithmeticException si se alcanza el máximo de un entero
     */
    public void incLecturas() {
        lecturas = Math.incrementExact(lecturas);
    }

    /**
     * Comprueba si la fecha indicada coincide con la fecha de esta noticia.
     *
     * @param d día
     * @param m mes
     * @param a año
     * @return true si ambas fechas coinciden
     * @throws IllegalArgumentException si la fecha indicada no es válida
     */
    public boolean igualFecha(int d, int m, int a) {
        return fecha == convertirFecha(d, m, a);
    }

    /**
     * Convierte una fecha formada por día, mes y año en el formato aaaammdd.
     * Es un método de paquete para que Periodico pueda reutilizar la misma
     * validación sin duplicar lógica.
     */
    static int convertirFecha(int d, int m, int a) {
        validarComponentesFecha(d, m, a);
        return a * 10000 + m * 100 + d;
    }

    /**
     * Devuelve toda la información de la noticia en el formato solicitado.
     */
    @Override
    public String toString() {
        return fecha % 100 + "/" + (fecha / 100) % 100 + "/" + (fecha / 10000) + " - "
                + hora / 100 + ":" + hora % 100 + "\n"
                + texto + "\n"
                + "Leída " + lecturas + " veces\n";
    }

    private static void validarFecha(int fecha) {
        int año = fecha / 10000;
        int mes = (fecha / 100) % 100;
        int dia = fecha % 100;
        validarComponentesFecha(dia, mes, año);
    }

    private static void validarComponentesFecha(int dia, int mes, int año) {
        if (año < 1000 || año > 9999) {
            throw new IllegalArgumentException("El año debe tener cuatro cifras.");
        }

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }

        int diasDelMes = switch (mes) {
            case 2 -> esBisiesto(año) ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };

        if (dia < 1 || dia > diasDelMes) {
            throw new IllegalArgumentException("El día no es válido para el mes indicado.");
        }
    }

    private static boolean esBisiesto(int año) {
        return año % 4 == 0 && (año % 100 != 0 || año % 400 == 0);
    }

    private static void validarHora(int hora) {
        int horas = hora / 100;
        int minutos = hora % 100;

        if (hora < 0 || horas > 23 || minutos > 59) {
            throw new IllegalArgumentException("La hora debe estar en formato hhmm y ser válida.");
        }
    }
}
