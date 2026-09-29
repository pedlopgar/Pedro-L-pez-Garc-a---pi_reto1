package com.engri;

import java.util.ArrayList;

/**
 * Gestiona el conjunto de noticias de un periódico.
 *
 * <p>El enunciado fija un máximo de 1000 noticias. Se utiliza un ArrayList
 * con esa capacidad inicial y se mantiene numNoticias porque ambos atributos
 * forman parte de los requisitos del reto.</p>
 */
public class Periodico {
    private static final int CAPACIDAD_MAXIMA = 1000;

    private final ArrayList<Noticia> noticias;
    private int numNoticias;

    /**
     * Crea un periódico vacío con capacidad inicial para 1000 noticias.
     */
    public Periodico() {
        noticias = new ArrayList<>(CAPACIDAD_MAXIMA);
        numNoticias = 0;
    }

    /**
     * Inserta una noticia si no se ha alcanzado el límite de capacidad.
     * Las referencias nulas se ignoran porque no representan una noticia válida.
     *
     * @param n noticia a insertar
     */
    public void insertar(Noticia n) {
        if (n == null || numNoticias >= CAPACIDAD_MAXIMA) {
            return;
        }

        noticias.add(n);
        numNoticias++;
    }

    /**
     * Devuelve la primera noticia cuya fecha coincide con la indicada.
     * La búsqueda respeta el orden actual del ArrayList.
     *
     * @param d día
     * @param m mes
     * @param a año
     * @return primera coincidencia o null si no existe
     * @throws IllegalArgumentException si la fecha indicada no es válida
     */
    public Noticia primeraNoticia(int d, int m, int a) {
        int fechaBuscada = Noticia.convertirFecha(d, m, a);

        for (int i = 0; i < numNoticias; i++) {
            Noticia noticia = noticias.get(i);
            if (noticia.getFecha() == fechaBuscada) {
                return noticia;
            }
        }

        return null;
    }

    /**
     * Muestra todas las noticias que tienen el mayor número de lecturas.
     * Si varias empatan, se muestran todas ellas y se mantiene su orden.
     */
    public void masPopulares() {
        if (numNoticias == 0) {
            System.out.println("No hay noticias.");
            return;
        }

        int maxLecturas = 0;

        // Primera pasada: localizar el máximo número de lecturas.
        for (int i = 0; i < numNoticias; i++) {
            int lecturas = noticias.get(i).getLecturas();
            if (lecturas > maxLecturas) {
                maxLecturas = lecturas;
            }
        }

        // Segunda pasada: mostrar todas las noticias que empatan con el máximo.
        for (int i = 0; i < numNoticias; i++) {
            Noticia noticia = noticias.get(i);
            if (noticia.getLecturas() == maxLecturas) {
                System.out.println(noticia);
            }
        }
    }

    /**
     * Elimina todas las noticias anteriores a la fecha indicada.
     * La noticia cuya fecha coincide exactamente con el límite se conserva.
     *
     * @param d día
     * @param m mes
     * @param a año
     * @throws IllegalArgumentException si la fecha indicada no es válida
     */
    public void borrarAnteriores(int d, int m, int a) {
        int fechaLimite = Noticia.convertirFecha(d, m, a);

        // removeIf permite eliminar todos los elementos que cumplen la condición
        // de una sola operación lógica y mantiene el orden de los restantes.
        noticias.removeIf(noticia -> noticia.getFecha() < fechaLimite);
        numNoticias = noticias.size();
    }

    /**
     * Muestra todas las noticias almacenadas en el orden del ArrayList.
     */
    public void mostrar() {
        for (int i = 0; i < numNoticias; i++) {
            System.out.println(noticias.get(i));
        }
    }
}
