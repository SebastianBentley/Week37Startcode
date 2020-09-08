package facades;

import utils.EMF_Creator;
import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.constraints.AssertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = MovieFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(new Movie(2000, "Some txt", new String[]{"Test1", "Test2"}));
            em.persist(new Movie(2001, "aaa", new String[]{"Test1", "Test2"}));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    
    @Test
    public void testMovieCountMethod() {
        assertEquals(2, facade.getMovieCount(), "Expects two rows in the database");
    }
    
    @Test
    public void testGetMovieByTitle(){
        String expectedTitle = "aaa";
        List<Movie> mv = facade.getMovieByTitle("aaa");
        String actualTitle = mv.get(0).getTitle();
        assertEquals(expectedTitle, actualTitle);
    }
    
    @Test
    public void testGetAllMovies(){
        int expectedAmount = 2;
        List<Movie> mvs = facade.getAllMovies();
        int actualAmount = mvs.size();
        assertEquals(expectedAmount, actualAmount);
    }
    
    @Test
    public void testAddMovie(){
        int mvsSizeBefoeAdd = facade.getAllMovies().size();
        String[] actors = {"test"};
        Movie mv = new Movie(2001, "test", actors);
        facade.addMovie(mv);
        int mvSizeAfterAdd = facade.getAllMovies().size();
        assertTrue(mvSizeAfterAdd > mvsSizeBefoeAdd);
        
    }
    
    

}
