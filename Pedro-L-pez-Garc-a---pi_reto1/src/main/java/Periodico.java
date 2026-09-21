import java.util.ArrayList;

public class Periodico {
    private ArrayList<Noticia> noticias;
    private int numNoticias;

    public Periodico() {
        noticias = new ArrayList<>(1000);
        numNoticias = 0;
    }

    public void insertar(Noticia n){
        if (numNoticias < 1000) {
            noticias.add(n);
            numNoticias++;
        }
    }

    public Noticia primeraNoticia(int d, int m, int a){
        for (Noticia noticia : noticias) {
            if(noticia.igualFecha (d,m,a)){
                return noticia;
            }
        }
        return null;
    }

    public void masPopulares(){
        if (numNoticias == 0) {
            System.out.println("ERROR: No existen noticias todavía.");
            return;
        }

        int maxLecturas = 0;

        for (Noticia noticia : noticias) {
            if (noticia.getLecturas() > maxLecturas) {
                maxLecturas = noticia.getLecturas();
            }
        }

        for (Noticia noticia : noticias) {
            if (noticia.getLecturas() == maxLecturas) {
                System.out.println(noticia);
            }
        }
    }

    public void borrarAnteriores(int d, int m, int a){
        int fechaBorrado = (a*10000)+(m*100)+d;
        for (int i = noticias.size()-1; i >= 0; i--) {
           if (noticias.get(i).getFecha() <= fechaBorrado) {
               noticias.remove(i);
               numNoticias--;
           }
        }
    }

    public void mostrar(){
        for (Noticia noticia : noticias) {
            System.out.println(noticia.toString());
        }
    }

}
