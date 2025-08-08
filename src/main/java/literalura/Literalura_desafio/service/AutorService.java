package literalura.Literalura_desafio.service;
import java.net.http.HttpClient;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import literalura.Literalura_desafio.model.Autor;
import literalura.Literalura_desafio.repository.AutorRepository;
import org.springframework.stereotype.Service;

@Service
public class AutorService {
    private static final String API_BASE_URL = "https://gutendex.com/books/";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;// Declarar ObjectMapper de Jackson
    private final AutorRepository autorRepository;
    //List<String> librosBuscados;

    public AutorService(AutorRepository autorRepository){
        this.httpClient = HttpClient.newBuilder().build();
        // Inicializar ObjectMapper
        this.objectMapper = new ObjectMapper();
        // Opcional: para imprimir JSON bonito en la consola
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.autorRepository = autorRepository;
    }

    public List<Autor> buscarPorNombre(String nombre) {
        return autorRepository.findByNameIgnoreCase(nombre);
    }

    public List<Autor> findByNombreContainingIgnoreCase(String nombre) {
        return autorRepository.findByNameIgnoreCase(nombre);
    }

    public List<Autor> buscarAutoresVivosEn(int anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }

    public List<Autor> verTodosLosAutores(){
        return autorRepository.findAllByOrderByNameAsc();
    }
}
