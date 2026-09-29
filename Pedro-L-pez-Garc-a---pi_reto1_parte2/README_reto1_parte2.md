# Proyecto Intermodular - Reto 1: Periódico Digital

## 1. Herramienta de IA utilizada

Para la elaboración de este trabajo se ha utilizado **ChatGPT (OpenAI), modelo GPT-5.6 Luna**, como herramienta de apoyo para:

- Analizar el enunciado del reto.
- Proponer una implementación alternativa del proyecto.
- Revisar aspectos de diseño, eficiencia, mantenibilidad y uso de Java.
- Comparar la solución desarrollada manualmente con la solución propuesta mediante IA.
- Detectar errores y posibles mejoras.

La solución original del proyecto fue desarrollada sin utilizar IA. La versión alternativa generada mediante IA se ha utilizado únicamente como referencia para realizar la comparación solicitada.

---

## 2. Prompts principales utilizados

### Prompt 1 - Desarrollo de una solución alternativa

> Quiero que resuelvas este ejercicio desde cero. Crea las clases que pide el enunciado. Realiza la actividad como un programador profesional. Intenta buscar, en diferentes webs como Stack Overflow (o sucedáneos), la forma más óptima de hacerlo; es decir, quiero que el código tenga un acabado serio, profesional, óptimo para el rendimiento del ordenador y que, además, cumpla con los requisitos del enunciado del ejercicio.

Este prompt se utilizó para obtener una segunda implementación del reto y disponer así de una base de comparación.

### Prompt 2 - Comparación de las dos soluciones

> Te voy a pasar la solución que hice yo sin IA. Te la adjunto en un zip.
>
> Debes comparar lo que he hecho yo sin usar IA con la solución que tú (La IA) ha proporcionado. Debes de comparar las diferencias de estructura, calidad del código, escalabilidad, claridad, mantenibilidad, eficiencia, uso de Java y cumplimiento del enunciado. Indica ventajas (si las hay) e inconvenientes (si los hay) de cada una, pero no des por hecho que una solución es mejor que la otra.

La comparación se realizó sobre los archivos reales de ambas soluciones, no únicamente sobre descripciones generales.

---

## 3. Comparación entre las dos soluciones

### 3.1. Estructura del código

**Solución propia**

La solución propia utiliza una estructura Maven sencilla:

```text
src/
├── main/
│   └── java/
│       ├── App.java
│       ├── Noticia.java
│       └── Periodico.java
```

Las clases no están dentro de un paquete Java.

**Solución mediante IA**

La solución alternativa utiliza:

```text
src/
├── main/
│   └── java/
│       └── com/
│           └── engri/
│               ├── App.java
│               ├── Noticia.java
│               └── Periodico.java
└── test/
    └── java/
        └── com/
            └── engri/
                ├── NoticiaTest.java
                └── PeriodicoTest.java
```

Esta estructura incorpora un paquete (`com.engri`) y una separación explícita entre código de producción y pruebas.

**Valoración**

La solución propia es más sencilla y tiene menos elementos que configurar. La solución mediante IA presenta una estructura más próxima a un proyecto Java organizado para crecer y separar posteriormente distintas partes del código.

El enunciado muestra una estructura con `com.engri` y especifica el uso de Maven e IntelliJ, por lo que la segunda solución se ajusta mejor a ese ejemplo concreto. No obstante, la ausencia de paquetes en la solución propia no impide que las clases funcionen correctamente dentro del ejercicio.

---

### 3.2. Facilidad de comprensión

**Solución propia**

La implementación es directa y permite relacionar fácilmente cada requisito con su código. Por ejemplo, `igualFecha()` convierte directamente día, mes y año al formato `aaaammdd`, y `primeraNoticia()` recorre el `ArrayList` hasta encontrar la primera coincidencia.

Esto facilita su lectura, especialmente en un ejercicio introductorio de programación orientada a objetos.

**Solución mediante IA**

Utiliza más métodos auxiliares, constantes y validaciones. Entre ellos aparecen funciones para validar fechas, horas y componentes de fecha, además de una función específica para convertir una fecha.

La lógica queda más separada, pero también aumenta la cantidad de código que hay que comprender.

**Valoración**

La solución propia destaca por simplicidad y proximidad al enunciado. La solución mediante IA destaca por separar mejor determinadas responsabilidades. Ninguna ventaja implica que la otra sea inadecuada.

---

### 3.3. Complejidad del código

**Solución propia**

Es pequeña y contiene únicamente la lógica necesaria para las funcionalidades principales.

La complejidad algorítmica de las operaciones principales es apropiada para el tamaño del ejercicio:

- `primeraNoticia()`: O(n)
- `masPopulares()`: O(n)
- `mostrar()`: O(n)
- `borrarAnteriores()`: recorrido lineal, aunque las eliminaciones repetidas de un `ArrayList` pueden provocar desplazamientos adicionales.

**Solución mediante IA**

Mantiene la misma complejidad asintótica para las operaciones principales, pero añade:

- Validación de entradas.
- Constantes para valores como el límite máximo.
- Métodos auxiliares.
- Pruebas automatizadas.
- Configuración Maven para JUnit.

En `borrarAnteriores()` utiliza `removeIf()`, lo que expresa directamente que deben eliminarse los elementos que cumplen la condición.

**Valoración**

La solución propia tiene menor complejidad accidental y resulta suficiente para un máximo de 1000 noticias. La solución mediante IA tiene una estructura más preparada para ampliar el proyecto, aunque introduce complejidad adicional que el enunciado no exige.

---

### 3.4. Tratamiento de errores

**Solución propia**

No realiza una validación exhaustiva de los datos introducidos en `Noticia`. Por ejemplo, el constructor acepta valores de fecha, hora o texto que podrían ser inválidos.

Además, `Periodico.insertar()` no evita la inserción de una referencia `null`.

El enunciado no exige explícitamente estas validaciones, por lo que se consideran limitaciones de robustez y no incumplimientos directos del ejercicio.

**Solución mediante IA**

Incluye validaciones para:

- Fechas imposibles.
- Años bisiestos.
- Horas inválidas.
- Textos nulos o vacíos.
- Referencias `null`.
- Desbordamiento del contador de lecturas.

La solución mediante IA también valida las fechas recibidas en las operaciones del periódico.

**Valoración**

La solución mediante IA ofrece un tratamiento de errores más completo. La solución propia mantiene un comportamiento más sencillo y ajustado estrictamente al nivel de requisitos del reto.

---

### 3.5. Mantenibilidad

**Solución propia**

Su principal ventaja es su tamaño reducido. Al existir pocas clases y pocos métodos auxiliares, resulta fácil localizar cada funcionalidad.

Como contrapartida, no dispone de pruebas unitarias independientes y mantiene algunos valores directamente en el código, como el límite de 1000 noticias.

**Solución mediante IA**

Separa:

- Código de producción.
- Programa de demostración (`App`).
- Pruebas automatizadas.
- Configuración Maven.

También utiliza una constante para la capacidad máxima y mantiene ciertas responsabilidades en métodos auxiliares.

Esto facilita comprobar los cambios cuando el proyecto evoluciona.

**Valoración**

Para un ejercicio pequeño, la solución propia es fácil de mantener precisamente por ser pequeña. Para una ampliación posterior, la separación y las pruebas de la solución mediante IA proporcionarían una base más preparada.

---

### 3.6. Pruebas realizadas

**Solución propia**

Las comprobaciones se realizan principalmente desde `App.java`, mediante una serie de pruebas manuales:

- Creación de noticias.
- Inserción.
- Búsqueda de la primera noticia.
- Noticias populares.
- Borrado de noticias anteriores.

El propio enunciado solicita una clase `App` que permita probar el funcionamiento de la solución.

**Solución mediante IA**

Además de las pruebas manuales de `App`, se añadieron pruebas unitarias con JUnit:

- `NoticiaTest`
- `PeriodicoTest`

En total se prepararon **18 pruebas automatizadas**, cubriendo construcción, lecturas, fechas, `toString()`, inserción, límite de 1000 noticias, borrado, empates de popularidad, periódico vacío y otros casos límite.

**Valoración**

La solución propia cumple la función de demostración solicitada por el reto. La solución mediante IA añade una capa de verificación automática que permite detectar regresiones después de modificar el código.

---

### 3.7. Cumplimiento del enunciado

Las dos soluciones implementan las clases y métodos principales solicitados:

- `Noticia`
- `Periodico`
- `App`
- `getFecha()`
- `getLecturas()`
- `incLecturas()`
- `igualFecha()`
- `toString()`
- `insertar()`
- `primeraNoticia()`
- `masPopulares()`
- `borrarAnteriores()`
- `mostrar()`

También utilizan `ArrayList<Noticia>` y contemplan el límite de 1000 noticias.

Existe, sin embargo, una diferencia relevante:

En la solución propia, `borrarAnteriores()` utiliza:

```java
noticias.get(i).getFecha() <= fechaBorrado
```

El enunciado solicita eliminar las noticias **anteriores** a la fecha indicada. Por tanto, la comparación coherente es:

```java
noticias.get(i).getFecha() < fechaBorrado
```

Con `<=`, también se elimina la noticia correspondiente exactamente a la fecha límite.

En la solución mediante IA esta condición se implementó correctamente con `<`.

---

## 4. Errores o problemas detectados

### 4.1. Problemas de la solución propia

**1. Condición incorrecta en `borrarAnteriores()`**

Se utiliza `<=` en lugar de `<`.

Esto hace que se eliminen también las noticias cuya fecha coincide exactamente con la fecha indicada.

**2. Inserción de referencias `null`**

`Periodico.insertar()` permite almacenar `null`. Posteriormente, métodos como `primeraNoticia()` podrían intentar utilizar la referencia y provocar una excepción.

**3. Uso de `&` en lugar de `&&`**

En `App` aparece una condición similar a:

```java
if (n1 != null && n2 != null & n3 != null & n4 != null)
```

En este contexto debe utilizarse `&&` de forma consistente. Además, comprobar que objetos recién creados mediante `new` no sean `null` resulta innecesario.

**4. Ausencia de validación de entradas**

No se comprueba que fechas, horas y textos sean válidos.

Esto no contradice una exigencia explícita del enunciado, pero reduce la robustez.

**5. Ausencia de pruebas unitarias**

Las pruebas se concentran en `App`. No existe una batería de tests JUnit independiente.

**6. `artifactId` diferente del nombre indicado para el proyecto**

En el `pom.xml` de la solución propia se utiliza:

```xml
<artifactId>pi_pedlopgar</artifactId>
```

mientras que el enunciado indica que el proyecto y el repositorio deben llamarse `pi_reto1`.

El código puede ejecutarse pese a esta diferencia, pero conviene revisar el nombre antes de la entrega.

**7. Archivos generados y configuración de IntelliJ**

La solución propia contiene `target/` y parte de la configuración de `.idea/`. Para un repositorio Git limpio, normalmente interesa excluir los artefactos generados y los archivos de configuración personal del IDE que no sean necesarios.

El `.gitignore` de la solución propia ya contempla `target/`, pero en el ZIP analizado esos archivos estaban presentes, por lo que conviene revisar qué se está incluyendo realmente en el repositorio.

---

### 4.2. Posibles inconvenientes de la solución mediante IA

La solución mediante IA no presenta, en la comparación realizada, el error funcional detectado en `borrarAnteriores()`. Sin embargo, presenta algunas características que pueden considerarse inconvenientes según el objetivo del ejercicio:

**1. Mayor complejidad**

Introduce validaciones y métodos auxiliares que no eran necesarios para cumplir los requisitos básicos.

**2. Dependencia adicional para JUnit**

El `pom.xml` añade JUnit y plugins Maven para ejecutar las pruebas. Esto mejora la automatización, pero también aumenta la configuración y las dependencias del proyecto.

**3. Mayor distancia respecto a una solución introductoria**

La versión mediante IA está más orientada a buenas prácticas de ingeniería de software que a una implementación mínima de los conceptos que plantea el reto.

Por tanto, algunas de sus mejoras aportan valor principalmente si el proyecto se amplía o si se desea trabajar con prácticas de desarrollo más completas.

---

## 5. Conclusión de la comparación

Las dos soluciones son funcionales y parten de enfoques diferentes.

La solución propia prioriza la **simplicidad, la comprensión y la correspondencia directa con el enunciado**. Esto facilita entender el funcionamiento de las clases y seguir el flujo de la práctica.

La solución mediante IA prioriza la **robustez, la validación, las pruebas automatizadas y una estructura preparada para crecer**. Como consecuencia, también contiene más código y más elementos que deben mantenerse.

Por tanto, no se considera que una de las dos soluciones sea globalmente superior en todos los aspectos. La valoración depende del objetivo:

- Para una solución pequeña y directamente relacionada con el contenido del reto, la simplicidad de la versión propia resulta adecuada.
- Para una versión orientada a mantenimiento, ampliación y pruebas automatizadas, las decisiones de la solución mediante IA aportan ventajas.

La comparación también ha permitido detectar mejoras concretas en la solución propia, especialmente la corrección de `borrarAnteriores()` y el tratamiento de referencias `null`.

---

## 6. Reflexión personal sobre el uso de la IA

La IA presenta ventajas significativas respecto al trabajo que se pueda hacer sin ella. No sólo en cuanto a facilidad de uso e inmediatez de la producción, también desde un punto de vista didáctico.

Uno de los principales inconvenientes ha resultado ser la falta de contexto que tiene esta sobre el proyecto, así como una toma de decisiones efectuada por la IA que el usuario desconoce y es, en ocasiones, difícil de controlar.

Pese a las ventajas de la IA, el código hecho por uno mismo sigue siendo más comprensible, dado que la estructura de este viene dada por nuestro propio razonamiento, haciendo que entendamos e interioricemos el flujo interno del código.

Los errores que se han encontrado en la IA han sido, en esencia, por falta de contexto o de órdenes precisas. Sin embargo, se han solucionado mediante el uso de esta misma.

Los cambios, a futuro, que se podrían efectuar sobre el código propio se basarán en un uso complementario de la IA. El objetivo no será crear una estructura de cero, sino corregir, reforzar y mejorar la existente.

---

## 7. Reflexión personal final

La IA es una herramienta que, nos guste más o menos, ha venido para quedarse. Puede resultar muy útil a la hora de agilizar nuestro trabajo, así como facilitarlo. No obstante, una cosa que jamás podrá hacer la IA es tener la imaginación y el ingenio humano. Esta, por supuesto, puede aplicar propuestas o ideas ya existentes, pero jamás crear nuevas. Es nuestro trabajo idear, diseñar y aplicar soluciones nuevas, que desafíen cualquier paradigma anterior. No sólo en el campo de la programación, sino en cualquier campo donde la creatividad del ser humano es el factor diferencial, único e intransferible.