package literalura.Literalura_desafio.model;

import jakarta.persistence.*; // Importa las anotaciones de JPA

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "libro") // Nombre de la tabla en la base de datos
public class Libro {

    @Id
    // Aquí podrías usar GenerationType.IDENTITY para IDs de base de datos,
    // o si el 'id' de Gutendex es numérico y único, podrías mapearlo directamente
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // El ID de Gutendex podría usarse directamente si es numérico y único

    @Column(name = "titulo", nullable = false)
    private String titulo; // 'title' de la API de Gutendex

    // Relación Many-to-Many con Autor
    // @JoinTable define la tabla intermedia para la relación
    // 'name': nombre de la tabla intermedia (ej. libro_autor)
    // 'joinColumns': columna que referencia a esta entidad (Libro) en la tabla intermedia
    // 'inverseJoinColumns': columna que referencia a la otra entidad (Autor)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Cascade para persistir/unir autores
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>(); // 'authors' de la API de Gutendex

    // Mapeo para 'languajes' que es un array de Strings.
    // Esto se puede guardar como un String separado por comas, o JSONB en PostgreSQL.
    // Para simplificar, usaremos String separado por comas aquí.
    @Column(name = "idiomas")
    private String languages; // 'languages' de la API (originalmente un array)

    @Column(name = "conteo_descargas")
    private Long downloadCount; // 'download_count' de la API de Gutendex (Long para números grandes)

    @Column(name = "resumen", columnDefinition = "TEXT") // Mapea a un tipo TEXT en PostgreSQL
    private String resumen; // Campo para el resumen, si lo obtienes de otra fuente o lo generas

    // Constructor vacío (necesario para JPA)
    public Libro() {}

    public Libro(Long id, String title, Set<Autor> autores, String languages, Long downloadCount, String summaries) {
        this.id = id;
        this.titulo = title;
        this.autores = autores;
        this.languages = languages;
        this.downloadCount = downloadCount;
        this.resumen = summaries;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return titulo;
    }

    public void setTitle(String title) {
        this.titulo = title;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    //Método para añadir un autor
    public void addAutor(Autor autor) {
        this.autores.add(autor);
        autor.getLibros().add(this); // Asegura la bidireccionalidad
    }

    public void removeAutor(Autor autor) {
        this.autores.remove(autor);
        autor.getLibros().remove(this); // Asegura la bidireccionalidad
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getSummaries() {
        return resumen;
    }

    public void setSummaries(String summary) {
        this.resumen = summary;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", title='" + titulo + '\'' +
                // Evitar recursión infinita con autores en toString() si no es estrictamente necesario
                //", autores=" + autores +
                ", languages='" + languages + '\'' +
                ", downloadCount=" + downloadCount +
                ", summaries='" + resumen + '\'' +
                '}';
    }
}