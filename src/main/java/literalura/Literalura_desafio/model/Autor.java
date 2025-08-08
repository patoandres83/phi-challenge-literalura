package literalura.Literalura_desafio.model;
import jakarta.persistence.*; // Importa las anotaciones de JPA
import java.util.HashSet;
import java.util.Set;

    @Entity // Indica que esta clase es una entidad JPA y se mapea a una tabla en la BD
    @Table(name = "autor") // Nombre de la tabla en la base de datos
    public class Autor {

        @Id // Marca 'id' como la clave primaria
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de IDs (autoincremental en PostgreSQL)
        private Long id;

        @Column(name = "nombre", nullable = false) // Mapea a la columna 'nombre', no puede ser nulo
        private String name; // 'name' de la API de Gutendex

        @Column(name = "year_nacimiento") // Mapea a la columna 'year_nacimiento'
        private Integer birthYear; // 'birth_year' de la API de Gutendex (Integer para permitir nulos si no hay dato)

        @Column(name = "year_muerte") // Mapea a la columna 'year_muerte'
        private Integer deathYear; // 'death_year' de la API de Gutendex (Integer para permitir nulos)

        // Relación Many-to-Many con Libro. 'mappedBy' indica que la relación es
        // propiedad de la entidad Libro, específicamente por el campo 'autores'.
        // Esto evita la creación de una tabla intermedia redundante para la relación.
        @ManyToMany(mappedBy = "autores")
        private Set<Libro> libros = new HashSet<>(); // Usa Set para evitar duplicados y optimizar búsquedas

        // Constructor vacío (necesario para JPA)
        public Autor() {}

        public Autor(String name, Integer birthYear, Integer deathYear) {
            this.name = name;
            this.birthYear = birthYear;
            this.deathYear = deathYear;
        }

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(Integer birthYear) {
            this.birthYear = birthYear;
        }

        public Integer getDeathYear() {
            return deathYear;
        }

        public void setDeathYear(Integer deathYear) {
            this.deathYear = deathYear;
        }

        public Set<Libro> getLibros() {
            return libros;
        }

        public void setLibros(Set<Libro> libros) {
            this.libros = libros;
        }

        @Override
        public String toString() {
            return "Autor{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", birthYear=" + birthYear +
                    ", deathYear=" + deathYear +
                    '}';
        }
    }

