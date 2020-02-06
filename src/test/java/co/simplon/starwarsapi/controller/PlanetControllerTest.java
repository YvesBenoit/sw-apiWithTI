package co.simplon.starwarsapi.controller;

import co.simplon.starwarsapi.repository.PlanetRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import co.simplon.starwarsapi.model.planet.Planet;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest

public class PlanetControllerTest {

    @Autowired
    MockMvc mockMvc;

//    @Autowired
//    PlanetController planetController;

    @MockBean
    PlanetRepository planetRepository;

    @Test
    public void getPlanetsFromRepository() throws Exception {
        when(this.planetRepository.findAll()).thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/api/planets")).andExpect(status().isOk());
    }

    @Test
    public void getPlanetById() throws Exception {
        when(this.planetRepository.findById(anyInt())).thenReturn(java.util.Optional.of(new Planet(1, "AlderaanSimulé")));

        this.mockMvc.perform(get("/api/planets/1")).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("AlderaanSimulé"));
    }

    @Test
    public void getPlanetByIdNotFound() throws Exception {
        when(this.planetRepository.findById(any())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/api/planets/2000")).andExpect(status().isNotFound());
    }

    @Test
    public void createCity() throws Exception {
        when(this.planetRepository.save(any())).thenReturn(new Planet(1,"planetBouchon"));
        this.mockMvc.perform(post("/api/planets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Terre\"}"))  /* content obligatoire avec à minima "{}"  */
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("planetBouchon"))
                .andExpect(jsonPath("id").value(1));
    }

}
