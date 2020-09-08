package facades;

import dto.MovieDTO;
import entities.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Movie> getMovieByTitle(String title) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Movie.getByName", Movie.class);
            query.setParameter("title", title);
            List<Movie> mv = query.getResultList();
            return mv;
        } catch (javax.persistence.NoResultException e) {
            String[] error = {""};
            List<Movie> mvfail = new ArrayList<>();
            Movie m = new Movie(0, "Title Not Found", error);
            mvfail.add(m);
            return mvfail;
        } finally {
            em.close();
        }
    }

    public Movie getMovieById(Long id) {
        EntityManager em = getEntityManager();
        try {
            Movie mv = em.find(Movie.class, id);
            return mv;
        } finally {
            em.close();
        }
    }

    public List<Movie> getAllMovies() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movie> query = em.createNamedQuery("Movie.getAll", Movie.class);
            List<Movie> mvs = query.getResultList();
            return mvs;

        } finally {
            em.close();
        }
    }

    public Movie addMovie(Movie mv) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(mv);
            em.getTransaction().commit();
            return mv;
        } finally {
            em.close();
        }

    }

    //TODO Remove/Change this before use
    public long getMovieCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(r) FROM Movie r").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }
    }
    
    
    public void insertMoviesToDB(){
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Movie(1994, "Pulp Fiction", new String[]{"Uma Thurman", "John Travolta", "Samuel L. Jackson"}));
            em.persist(new Movie(1990, "Goodfellas", new String[]{"Ray Liotta", "Robert De Niro"}));
            em.persist(new Movie(2018, "Venom", new String[]{"Tom Hardy", "Michelle Williams"}));
            em.persist(new Movie(2019, "Joker", new String[]{"Joaqin Pheonix", "Robert De Niro"}));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        
    }
    

}
