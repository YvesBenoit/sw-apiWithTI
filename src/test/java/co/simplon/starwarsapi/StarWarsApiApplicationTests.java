package co.simplon.starwarsapi;

import static org.assertj.core.api.Assertions.assertThat;

import co.simplon.starwarsapi.model.planet.Planet;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StarWarsApiApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    public  Integer lastCreatedPlanetId=-1;

    @Test
    @Order(1)
    public void getPlanets() {
        // When retrieving planets from /api/planets
//		List<?> planets = this.restTemplate.getForObject("/api/planets",List.class);
        ResponseEntity<List> responseEntity = this.restTemplate.exchange("/api/planets", HttpMethod.GET, null, List.class);
        List<?> planets = responseEntity.getBody();

        // Then OK status code should be sent back and
        // the list of planets should be returned and should filled.
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planets).isNotNull();
    }

    @Test
    @Order(2)
    public void getExistingPlanetById() {
        // When retrieving an existing planet by its Id (16)
        ResponseEntity<Planet> responseEntity = this.restTemplate.exchange("/api/planets/{planetId}", HttpMethod.GET, null, Planet.class, 16);
        Planet felucia = responseEntity.getBody();

        // Then OK status code should be sent back and
        // the planet should be returned and should be filled with its attributes
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(felucia).isNotNull();
        assertThat(felucia.getName()).isEqualTo("Felucia");
        assertThat(felucia.getRotationPeriod()).isEqualTo(34);
        assertThat(felucia.getOrbitalPeriod()).isEqualTo(231);
    }

    // Get non existing planet
    @Test
    @Order(3)
    public void getNonExistingPlanet() {
        // When getting on /api/planets/2000
        ResponseEntity<Planet> responseEntity = this.restTemplate.exchange("/api/planets/{planetId}", HttpMethod.GET, null, Planet.class, 2000);

        // Then NOT_FOUND status code should be sent back
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(4)
    public void createPlanet() {
        //Given new planet
        String newPlanetName = "newPlanet";
        Planet newPlanet = new Planet(newPlanetName);
        HttpEntity<Planet> planetHttpEntity = new HttpEntity<>(newPlanet, null);

        // When creating a new planet by POSTing
        ResponseEntity<Planet> responseEntity = this.restTemplate.postForEntity("/api/planets", planetHttpEntity, Planet.class);
        Planet createdPlanet = responseEntity.getBody();

        // Then OK status code shoud be send back and
        // the created planet should be returned and should have an Id and should be set
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createdPlanet.getId()).isPositive();
        assertThat(createdPlanet.getName()).isEqualTo(newPlanetName);
        lastCreatedPlanetId = createdPlanet.getId();
		System.out.println(" Create ==> lastCreatedPlanetId : "+lastCreatedPlanetId);
    }

    // Update existing planet
    @Test
    @Order(5)
    public void updateExistingPlanet() {
        // Given an existing valid Planet (with name modified)
        String newName = "Alderaaaaaaaaaan";
        Planet existingPlanet = new Planet(1, newName);

        HttpEntity<Planet> planetHttpEntity = new HttpEntity<>(existingPlanet, null);

        // When putting this existing planet to /api/planet/1
        ResponseEntity<Planet> responseEntity = this.restTemplate.exchange("/api/planets/{planetId}", HttpMethod.PUT, planetHttpEntity, Planet.class, 1);
        Planet updatedPlanet = responseEntity.getBody();

        // Then OK status code should be sent back and
        // the updated planet should be returned and should have the same ID and an updated name.
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedPlanet.getId()).isEqualTo(1);
        assertThat(updatedPlanet.getName()).isEqualTo(newName);
    }

    // Update non existing planet
    @Test
    @Order(6)
    public void updateNonExistingPlanet() {
        // Given a non existing Planet
        String planetName = "NonExistingPlanet";
        Planet nonExistingPlanet = new Planet(2000, planetName);

        HttpEntity<Planet> planetHttpEntity = new HttpEntity<>(nonExistingPlanet, null);

        // When putting this planet to /api/planet/2000
        ResponseEntity<Planet> responseEntity = this.restTemplate.exchange("/api/planets/{planetId}", HttpMethod.PUT, planetHttpEntity, Planet.class, 2000L);

        // Then NOT_FOUND status code should be sent back.
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Delete planet
    @Test
    @Order(7)
    public void deletePlanet() {
        // When deleting to /api/planet/62
		System.out.println("lastCreatedPlanetId : "+lastCreatedPlanetId);
        ResponseEntity<Planet> responseEntity = this.restTemplate.exchange("/api/planets/{planetId}", HttpMethod.DELETE, null, Planet.class, lastCreatedPlanetId);

        // Then NO_CONTENT status code should be sent back.
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @Test
    void contextLoads() {
    }


}
