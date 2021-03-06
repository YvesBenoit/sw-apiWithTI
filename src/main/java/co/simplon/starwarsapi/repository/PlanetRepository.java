package co.simplon.starwarsapi.repository;

import co.simplon.starwarsapi.model.planet.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Integer> {
}
