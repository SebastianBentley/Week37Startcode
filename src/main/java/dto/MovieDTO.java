package dto;

import entities.Movie;


public class MovieDTO {
    private String title;
    private int year;
    private String[] actors;
    private Long id;

    public MovieDTO(Movie mv) {
        this.title = mv.getTitle();
        this.year = mv.getYear();
        this.actors = mv.getActors();
        this.id = mv.getId();
    }

    public MovieDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }
    
    
    
    
    
    
}
