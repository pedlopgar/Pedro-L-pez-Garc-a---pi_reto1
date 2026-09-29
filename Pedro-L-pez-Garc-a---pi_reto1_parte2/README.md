# pi_reto1 - Reto 1: Periódico Digital

Solución del Reto 1 del Proyecto Intermodular.

## Estructura

```text
pi_reto1/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/engri/
    │           ├── App.java
    │           ├── Noticia.java
    │           └── Periodico.java
    └── test/
        └── java/
            └── com/engri/
                ├── NoticiaTest.java
                └── PeriodicoTest.java
```

## Clases principales

### `Noticia`

Representa una noticia y almacena cuatro datos:

- `fecha`: fecha en formato `aaaammdd`.
- `hora`: hora en formato `hhmm`.
- `texto`: contenido de la noticia.
- `lecturas`: número de lecturas realizadas.

`fecha`, `hora` y `texto` son inmutables después de construir el objeto. El único dato que cambia es `lecturas`, mediante `incLecturas()`.

Antes de crear una noticia se comprueban la fecha, la hora y el texto. Las fechas se validan como fechas reales de calendario, incluyendo años bisiestos. Las horas admiten desde `0000` hasta `2359` y exigen que los minutos estén entre `00` y `59`.

### `Periodico`

Gestiona las noticias mediante `ArrayList<Noticia>` y mantiene `numNoticias`, tal y como exige el enunciado.

- `insertar()` ignora referencias nulas y no permite superar las 1000 noticias.
- `primeraNoticia()` recorre la lista desde el principio y devuelve la primera coincidencia.
- `masPopulares()` realiza dos recorridos lineales: primero obtiene el máximo número de lecturas y después muestra todas las noticias que empatan en ese máximo.
- `borrarAnteriores()` elimina las noticias cuya fecha sea menor que la indicada y conserva las que coincidan exactamente con la fecha límite.
- `mostrar()` recorre las noticias respetando el orden del `ArrayList`.

Para `borrarAnteriores()` se utiliza `removeIf()`. En `ArrayList`, este enfoque permite realizar la eliminación según un predicado sin repetir llamadas individuales a `remove()` y evita el patrón de desplazamientos repetidos que puede aparecer al eliminar elementos uno a uno. La documentación y ejemplos de Java/Stack Overflow describen este uso como apropiado para eliminar todos los elementos que cumplen una condición. 

## Pruebas

Las pruebas automáticas están en `src/test/java` y utilizan JUnit 6.

`NoticiaTest` comprueba:

- construcción y valores iniciales;
- incremento de lecturas;
- comparación de fechas;
- representación textual;
- rechazo de fechas inválidas;
- rechazo de horas inválidas;
- rechazo de textos nulos o vacíos.

`PeriodicoTest` comprueba:

- inserción y orden de búsqueda;
- periódico vacío;
- búsquedas sin coincidencia;
- referencias nulas;
- límite de 1000 noticias;
- borrado de noticias anteriores;
- conservación de las noticias de la fecha límite y posteriores;
- conservación del orden después de borrar;
- noticias más populares con empate;
- comportamiento de `masPopulares()` en un periódico vacío.

## Ejecución con Maven

Desde la raíz del proyecto:

```bash
mvn clean test
```

Para compilar y empaquetar:

```bash
mvn clean package
```

Para ejecutar el JAR generado:

```bash
java -jar target/pi_reto1-1.0-SNAPSHOT.jar
```

También puede ejecutarse `com.engri.App` directamente desde IntelliJ IDEA.

## Relación con los requisitos del enunciado

| Requisito | Implementación |
|---|---|
| Proyecto `pi_reto1` | `artifactId` = `pi_reto1` |
| Clase `Noticia` | `src/main/java/com/engri/Noticia.java` |
| Atributos privados | Todos los atributos son `private` |
| Constructor fecha/hora/texto | `Noticia(int fecha, int hora, String texto)` |
| `getFecha()` | Implementado |
| `getLecturas()` | Implementado |
| `incLecturas()` | Implementado |
| `igualFecha(d,m,a)` | Implementado |
| `toString()` | Implementado con el formato indicado |
| Clase `Periodico` | `src/main/java/com/engri/Periodico.java` |
| `ArrayList<Noticia>` de capacidad 1000 | `new ArrayList<>(1000)` |
| `numNoticias` | Implementado y sincronizado |
| `insertar(Noticia)` | Implementado |
| `primeraNoticia(d,m,a)` | Implementado |
| `masPopulares()` | Implementado, incluyendo empates |
| `borrarAnteriores(d,m,a)` | Implementado |
| `mostrar()` | Implementado |
| Clase `App` | Implementada con demostraciones manuales |
| Pruebas automatizadas | JUnit en `src/test/java` |
| Maven | `pom.xml` preparado para compilar, probar y empaquetar |

## Referencias técnicas consultadas

- JUnit: documentación oficial sobre JUnit 6 y su integración con Maven.
  https://docs.junit.org/6.0.2/user-guide/
- Apache Maven Surefire: ejecución de pruebas sobre JUnit Platform.
  https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html
- Oracle Java API: `ArrayList`.
  https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html
- Stack Overflow: discusión sobre eliminación eficiente de elementos de `ArrayList` y el uso de `removeIf()`.
  https://stackoverflow.com/questions/55933497/most-efficient-way-to-delete-elements-from-array-lists
