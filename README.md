# phi-challenge-literalura
Proyecto JAVA Spring Boot para conectar la API [gutendex.com](https://gutendex.com/) de libros a una base de datos local Postgresql

El esquema general de la funciones de la aplicación en consola

<img width="528" height="331" alt="image" src="https://github.com/user-attachments/assets/67843cdf-9ae5-4bce-ab3e-002d8f759782" />

Para ello se utilizó una estructura básica de proyecto Spring Boot en Intellij

<img width="341" height="571" alt="image" src="https://github.com/user-attachments/assets/a1dd82fc-6e21-4ec6-b650-058e5fc2b448" />


Los objetos DTO:
- GutendexAutor, se encarga de manejar la información relativa a autores.
- GutendexBookResponse, se encarga de manejar los resultados de cantidad de libros de la API.
- GutendexBookResult, se encarga de manejar la información de un libro específico. Notar que se usa la anotación @JsonIgnoreProperties(ignoreUnknown = true), para ingnorar todos los campos desconocidos del resultado del libro. Además se especifica que el atributo JSON de respuesta de la API download_count se manejará dentro de la varible downloadCount dentro de la clase mediante la anotación @JsonProperty("download_count")

Los objetos Model:
- Autor: Define la tabla autor de la base de datos (mediante la anotación @Entity y @Table(name="autor")) con los atributos (id,year_nacimiento,year_muerte,nombre) con su contructor getters y setters
- Libro: Define la tabla libro con los atributos (id,conteo_descargas,idiomas,resumen,titulo). En esta clase además se define la realación entre las tablas autor y libro, con las anotaciones @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) y @JoinTable, de esta forma en la base de datos Postgresql se crea la entidad libro_autor, la cual contiene los relacionamientos.

Los objetos Repository:
- AutorRepository: Clase encargada de la comunicación con la base de datos, utilizando JPA y derived queries, tiene los métodos para:
  * Encontrar un autor por nombre de autor.
  * Encontrar un autor por parte de su nombre.
  * Encontrar autores vivos dado un año por el usuario.
  * Listar todos los autores de la base ordenados de forma ascendente por nombre.
-LibroRepository: Clase encargada de la comunicación con la tabla libro de la base de datos, tiene los métodos para:
  * Encontrar un libro por título.
  * Encontrar todos los libros ordenados por título de forma descendente.
  * Contar todos los libros de un idioma determinado.
  * Listar todos los libros de un idioma determinado.

Los objetos Service:
- AutorService: utiliza el objeto AutorRepository, para entregar resultados al ejecutable principal de la aplicación.
- GutendexApiService: clase que se usa para una comunicación de prueba con la API Gutendex y presentar los resultados JSON mapeados con Gson.
- LibroService: clase que se encarga de la consulta de libros a la API (utilizando las clases Java HttpClient,HttpRequest, HttpResponse entregando el código de estado HTTP al usuario y mostrando la URL consultada a la API) de Gutendex y guardar los resultados en la base de datos, a través del método consultarYGuardarLibro que recibe como parámetro un libro ingresado por el usuario en consola. Tiene la anotación @Transactional que permite realizar operaciones de inserción de libros y autores a la base de datos si es que el libro buscado en la API no se encuentra. De los libros encontrados en la API Gutendex sólo obtiene el primero encontrado y lo guarda. Usa Streams para mostrar todos los libros del idioma inglés.
- MenuAplicación: este objeto contiene todos los menús y submenús de opciones del sistema.

Principales dificultades encontradas para realizar el desafío.
1. Puerto de la aplicación ocupado, si hay instalado más de un motor de base de datos en el equipo, el puerto de ejecución 8080, suele estar ocupado y no se ejecuta el proyecto. Para ello se configuró el archivo application.prperties con la anotación "server.port=0", esto hace que spring busque un puerto desocupado y ejecute la aplicación en dicho puerto.
2. Describir los métodos repositories con los mismos nombres definidos en su clase padre, por ejemplo, en la clase Autor se define la variable "nombre" y en el repository se define "FindByName" lo que generaba que la aplicación no se ejecutara.
3. Total desconocimiento del entorno Spring Boot, por lo que requiere de muchas horas de estudio para poder entender la mecánica de los procesos. En este proceso de entendimiento ayudaron la IAs Chat GPT y Gemini.
 


