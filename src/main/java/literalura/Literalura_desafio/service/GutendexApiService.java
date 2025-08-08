package literalura.Literalura_desafio.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexApiService {
    //url base para consultar a la API
    private static final String API_BASE_URL = "https://gutendex.com/books/";
    //se crea el cliente http para interactuar con la API
    private final HttpClient httpClient; // Declarar un cliente HTTP

    // Constructor para inicializar el HttpClient
    public GutendexApiService() {
        this.httpClient = HttpClient.newBuilder().build(); // Inicializar el HttpClient
    }
    //método para consumir API
    public void consultarLibroPorTitulo(String nombreLibro) {
        // Codificar el nombre del libro para usarlo en la URL
        String encodedNombreLibro = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);
        String url = API_BASE_URL + "?search=" + encodedNombreLibro;

        System.out.println("Consultando URL de Gutendex: " + url);

        try {
            // Construir la solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)) // Define la URI de la solicitud
                    .GET() // Especifica que es una solicitud GET
                    .build(); // Construye el objeto HttpRequest

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode(); // Obtiene el código de estado de la respuesta
            System.out.println("Código de estado HTTP: " + statusCode);

            String jsonResponse = response.body(); // Obtiene el cuerpo de la respuesta como String

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                // Usa Gson para parsear y luego "embellecer" el JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Object jsonObject = gson.fromJson(jsonResponse, Object.class); // Parsea el JSON a un objeto genérico
                String prettyJson = gson.toJson(jsonObject); // Convierte el objeto de nuevo a JSON con formato

                System.out.println("\n--- Respuesta JSON de Gutendex ---");
                System.out.println(prettyJson);
                System.out.println("----------------------------------\n");
            } else {
                System.out.println("La API no devolvió contenido.");
            }

        } catch (IOException | InterruptedException e) {
            // IOException: Errores de red o de I/O
            // InterruptedException: Si el hilo que envía la solicitud es interrumpido
            System.err.println("Error al consultar la API de Gutendex: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace para depuración
        }
    }


}
