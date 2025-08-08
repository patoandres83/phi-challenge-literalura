package literalura.Literalura_desafio.repository;

import literalura.Literalura_desafio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // <- ¡Importante!

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Método personalizado: buscar libro por título
    Optional<Libro> findByTitulo(String title);
    List<Libro> findAllByOrderByTituloDesc();

    // Derived queries para buscar los libros por idiomas
    long countByLanguages(String idioma);
    List<Libro> findByLanguages(String idioma);
}

