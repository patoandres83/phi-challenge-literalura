package literalura.Literalura_desafio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexBookResult {
    private Long id; // El ID de Gutendex, lo usaremos como PK de nuestro Libro
    private String title;
    private List<GutendexAuthor> authors; // Lista de autores
    private List<String> subjects;
    private List<String> bookshelves;
    private List<String> languages;
    private boolean copyright;
    private String media_type;

    @JsonProperty("download_count")
    private int downloadCount;

    private List<String> summaries = new ArrayList<>();
    // ... otros campos que Gutendex pueda devolver y no necesitemos mapear

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<GutendexAuthor> getAuthors() { return authors; }
    public void setAuthors(List<GutendexAuthor> authors) { this.authors = authors; }

    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }

    public List<String> getBookshelves() { return bookshelves; }
    public void setBookshelves(List<String> bookshelves) { this.bookshelves = bookshelves; }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public boolean isCopyright() { return copyright; }
    public void setCopyright(boolean copyright) { this.copyright = copyright; }

    public String getMedia_type() { return media_type; }
    public void setMedia_type(String media_type) { this.media_type = media_type; }

    public int getDownloadCount() { return downloadCount; }
    public void setDownloadCount(int download_count) { this.downloadCount = download_count; }

    public List<String> getSummaries() { return summaries; }
    public void setSummaries(List<String> summaries) { this.summaries = summaries; }
}
