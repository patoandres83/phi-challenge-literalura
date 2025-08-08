package literalura.Literalura_desafio.repository;

import literalura.Literalura_desafio.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByName(String name); // Ejemplo: buscar autor por nombre

    // Buscar autor por nombre exacto (ignora mayúsculas/minúsculas)
    List<Autor> findByNameIgnoreCase(String name);

    @Query("SELECT a FROM Autor a WHERE a.birthYear <= :anio AND (a.deathYear IS NULL OR a.deathYear > :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") Integer anio);

    // Listar todos los autores ordenados por nombre
    List<Autor> findAllByOrderByNameAsc();
}