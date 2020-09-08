package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.MovieDTO;
import entities.Movie;
import utils.EMF_Creator;
import facades.MovieFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("Movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    
    private static final MovieFacade FACADE =  MovieFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMovies(){
        List<Movie> mvs = FACADE.getAllMovies();
        List<MovieDTO> dtos = new ArrayList<>();
        for (Movie mv : mvs) {
            MovieDTO dto = new MovieDTO(mv);
            dtos.add(dto);
        }
        return GSON.toJson(dtos);
    }
    
    
    @Path("title/{title}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieByTitle(@PathParam("title") String title){
        List<Movie> mv = FACADE.getMovieByTitle(title);
        List<MovieDTO> movies = new ArrayList<>();
        for (Movie mov : mv) {
            movies.add(new MovieDTO(mov));
        }
        return GSON.toJson(movies);
    }
    
    @Path("id/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieById(@PathParam("id") Long id){
        Movie mv = FACADE.getMovieById(id);
        return GSON.toJson(mv);
    }
    
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMovieCount() {
        long count = FACADE.getMovieCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
    
    @Path("insertMovies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String insertMoviesToDB(){
        FACADE.insertMoviesToDB();
        return "{\"msg\":\"4 rows added\"}";
    }
    
    
}
