package literalura.Literalura_desafio.service;// Ajusta el paquete

// Importaciones de Jackson
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


import literalura.Literalura_desafio.dto.GutendexAuthor;
import literalura.Literalura_desafio.dto.GutendexBookResponse;
import literalura.Literalura_desafio.dto.GutendexBookResult;
import literalura.Literalura_desafio.model.Autor;
import literalura.Literalura_desafio.model.Libro;
import literalura.Literalura_desafio.repository.AutorRepository;
import literalura.Literalura_desafio.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibroService {

    private static final String API_BASE_URL = "https://gutendex.com/books/";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // Declarar ObjectMapper de Jackson

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    List<String> librosBuscados;

    // Inyección de dependencias a través del constructor
    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.httpClient = HttpClient.newBuilder().build();

        // Inicializar ObjectMapper
        this.objectMapper = new ObjectMapper();
        // Opcional: para imprimir JSON en la consola
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.librosBuscados = new ArrayList<>();
    }

    public List<String> getLibrosBuscados() {
        return librosBuscados;
    }

    @Transactional
    public Libro consultarYGuardarLibro(String nombreLibro) {
        String encodedNombreLibro = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);
        String url = API_BASE_URL + "?search=" + encodedNombreLibro;

        System.out.println("Consultando URL de Gutendex: " + url);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            System.out.println("Código de estado HTTP: " + statusCode);

            String jsonResponse = response.body();

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                System.out.println("\n--- Respuesta JSON de Gutendex ---");
                // Usar ObjectMapper para pretty print el JSON
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(jsonResponse)));
                System.out.println("----------------------------------\n");

                // Deserializar el JSON a nuestros DTOs de Gutendex usando ObjectMapper
                GutendexBookResponse gutendexResponse = objectMapper.
                        readValue(jsonResponse, GutendexBookResponse.class);

                if (gutendexResponse != null && gutendexResponse.getResults()
                        != null && !gutendexResponse.getResults().isEmpty()) {

                    //aquí guarda el primero de los resultados del json entregados
                    //gutendex
                    GutendexBookResult result = gutendexResponse.getResults().get(0);

                    /*verifica si el libro buscado no está dentro del arreglo
                     * de libros buscados*/
                    String nombreDecodificado = "";
                    String nombreCodificado = result.getTitle();
                    try {
                        // Decodifica el String. "UTF-8" es la codificación estándar para URLs.
                        nombreDecodificado = URLDecoder.decode(nombreCodificado, "UTF-8");
                        //System.out.println("Nombre decodificado: " + nombreDecodificado);
                        if (librosBuscados.contains(nombreDecodificado)) {
                            System.out.println("'" + nombreDecodificado +
                                    "' ya existe en la lista de libros buscados. No se agregará.");
                        } else {
                            librosBuscados.add(nombreDecodificado);
                            System.out.println("'" + nombreDecodificado +
                                    "' no existía en la lista de libros buscados. Se agregó a la lista.");
                        }

                    } catch (UnsupportedEncodingException e) {
                        System.err.println("Error de codificación: " + e.getMessage());
                        // Manejar la excepción, quizás loggear o usar el nombre codificado como fallback
                        nombreDecodificado = nombreCodificado; // Opcional: si falla, usa el original
                    }


                    Optional<Libro> libroExistente = libroRepository.findById(result.getId());

                    if (libroExistente.isPresent()) {
                        System.out.println("Libro '" + result.getTitle()
                                + "' (ID: " + result.getId() + ") ya existe en la base de datos.");
                        return libroExistente.get();
                    } else {
                        Libro nuevoLibro = new Libro();
                        nuevoLibro.setId(result.getId());
                        nuevoLibro.setTitle(result.getTitle());
                        nuevoLibro.setDownloadCount((long) result.getDownloadCount());

                        if (result.getLanguages() != null && !result.getLanguages().isEmpty()) {
                            nuevoLibro.setLanguages(String.join(",", result.getLanguages()));
                        } else {
                            nuevoLibro.setLanguages("");
                        }

                        if (result.getSummaries() != null && !result.getSummaries().isEmpty()) {
                            // Une la lista de strings de 'summaries' en un solo String.
                            // Uso "\n\n" como delimitador para que cada "resumen" mantenga su línea.
                            nuevoLibro.setSummaries(String.join("\n\n", result.getSummaries()));
                        } else {
                            // Si no hay summaries o la lista está vacía, guarda null o un string vacío.
                            nuevoLibro.setSummaries(null);
                        }

                        Set<Autor> autoresPersistidos = new HashSet<>();
                        if (result.getAuthors() != null) {
                            for (GutendexAuthor gutendexAuthor : result.getAuthors()) {
                                Optional<Autor> autorExistente = autorRepository.findByName(gutendexAuthor.getName());
                                Autor autor;
                                if (autorExistente.isPresent()) {
                                    autor = autorExistente.get();
                                    System.out.println("Autor '" + autor.getName() + "' ya existe en la base de datos.");
                                } else {
                                    autor = new Autor();
                                    autor.setName(gutendexAuthor.getName());
                                    autor.setBirthYear(gutendexAuthor.getBirth_year());
                                    autor.setDeathYear(gutendexAuthor.getDeath_year());
                                    autor = autorRepository.save(autor);
                                    System.out.println("Nuevo autor '" + autor.getName() + "' guardado.");
                                }
                                nuevoLibro.addAutor(autor);
                                autoresPersistidos.add(autor);
                            }
                        }

                        Libro libroGuardado = libroRepository.save(nuevoLibro);
                        System.out.println("Libro '" + libroGuardado.getTitle() + "' (ID: "
                                + libroGuardado.getId() + ") guardado exitosamente.");
                        return libroGuardado;
                    }
                } else {
                    System.out.println("No se encontraron resultados para '" + nombreLibro + "'.");
                    return null;
                }
            } else {
                System.out.println("La API no devolvió contenido para '" + nombreLibro + "'.");
                return null;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al consultar o procesar la API de Gutendex para '" + nombreLibro + "': " + e.getMessage());
            // Considera usar un logger como SLF4J/Logback en lugar de e.printStackTrace() en producción
            return null;
        }
    }

    @Transactional
    public List<Libro> listarTodosLosLibrosOrdenadosPorTituloDesc() {
        return libroRepository.findAllByOrderByTituloDesc();
    }

    // Contar y mostrar libros en español
    public void mostrarLibrosEnEspanol() {
        List<Libro> libros = libroRepository.findByLanguages("es");

        System.out.println("Cantidad de libros en español: " + libros.size());
        libros.forEach(libro -> System.out.println(libro));
    }

    // Contar y mostrar libros en inglés usando Streams
    public void mostrarLibrosEnInglesStream() {
        List<Libro> libros = libroRepository.findAll().stream()
                .filter(libro -> "en".equalsIgnoreCase(libro.getLanguages()))
                .toList();

        System.out.println("Cantidad de libros en inglés: " + libros.size());
        libros.forEach(System.out::println);
    }

}