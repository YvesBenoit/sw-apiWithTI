package co.simplon.starwarsapi.controller;

import co.simplon.starwarsapi.model.planet.Planet;
import co.simplon.starwarsapi.repository.PlanetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private PlanetRepository planetRepository;

    public PlanetController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @GetMapping
    public List<Planet> getPlanetList() {
        return planetRepository.findAll();
    }

    @GetMapping("/{planetId}")
    public ResponseEntity<Planet> getPlanet(@PathVariable Integer planetId) {
        Optional<Planet> planet = planetRepository.findById(planetId);

        if (planet.isPresent()) {
            return ResponseEntity.ok(planet.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Planet> createPlanet(@RequestBody Planet planet) {
        return ResponseEntity.ok(planetRepository.save(planet));
    }

    @PutMapping("/{planetId}")
    public ResponseEntity<Planet> updatePlanet(@RequestBody Planet planet, @PathVariable Integer planetId) {
        if (planetRepository.existsById( planetId) && planet.getId().equals(planetId)) {
            return ResponseEntity.ok(planetRepository.save(planet));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{planetId}")
    public ResponseEntity<Planet> deletePlanet(@PathVariable Integer planetId) {
        if (planetRepository.existsById(planetId)) {
            planetRepository.deleteById(planetId);
        }
        return ResponseEntity.noContent().build();
    }
}
