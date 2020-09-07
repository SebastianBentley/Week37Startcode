package dto;

import entities.Movie;


public class MovieDTO {
    private String Title;
    private int year;
    private String[] actors;

    public MovieDTO(Movie mv) {
        this.Title = mv.getTitle();
        this.year = mv.getYear();
        this.actors = mv.getActors();
    }

    public MovieDTO() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
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
