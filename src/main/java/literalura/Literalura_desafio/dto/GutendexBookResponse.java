package literalura.Literalura_desafio.dto;

import java.util.List;

public class GutendexBookResponse {
    private int count;
    private String next;
    private String previous;
    private List<GutendexBookResult> results; // La lista de libros encontrados

    // Getters y Setters
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }
    public String getPrevious() { return previous; }
    public void setPrevious(String previous) { this.previous = previous; }
    public List<GutendexBookResult> getResults() { return results; }
    public void setResults(List<GutendexBookResult> results) { this.results = results; }
}
