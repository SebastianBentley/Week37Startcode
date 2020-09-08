package rest;

import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Movie r1,r2,r3;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        r1 = new Movie(1994, "Pulp Fiction", new String[]{"Uma Thurman", "John Travolta", "Samuel L. Jackson"});
        r2 = new Movie(1990, "Goodfellas", new String[]{"Ray Liotta", "Robert De Niro"});
        r3 = new Movie(2018, "Venom", new String[]{"Tom Hardy", "Michelle Williams"});
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
        .when()
        .get("/Movie").then()
        .statusCode(200);
    }
    
    @Test
    public void testServerIsUpTwo() {
        System.out.println("Testing is server UP");
        given()
        .when()
        .get("/Movie").then()
        .statusCode(HttpStatus.OK_200.getStatusCode());
    }
    
    
    
   
    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/Movie/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Hello World"));   
    }
    
    @Test
    public void testCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/Movie/count").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(3));   
    }
    
    
    @Test
    public void testGetAllMovies() throws Exception {
        given()
        .contentType("application/json")
        .get("/Movie/all").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("size()", is(3))
        .and()
        .body("title",hasItems("Pulp Fiction", "Goodfellas", "Venom"));
    }
    
    @Test
    public void testFindByTitle(){
        given()
        .contentType("application/json")
        .get("/Movie/title/Goodfellas").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("[0].id", equalTo(r2.getId().intValue()));
        
    }
    
    @Test
    public void testFindByTitleNotFound(){
        given()
        .contentType("application/json")
        .get("/Movie/title/Spiderman").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("[0].id", is(not(equalTo(r2.getId().intValue()))));
        
    }
    
    
    @Test
    public void testFindById(){
        given()
        .contentType("application/json")
        .get("/Movie/id/" + r2.getId()).then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("title", equalTo(r2.getTitle()));
    }
    
    
    
}
