package dev.johnkyaw.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnkyaw.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.johnkyaw.movie.model.Movie;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServices {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieFromDB(String name) throws JsonProcessingException {
        //Search movie json
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.omdbapi.com/?apikey=b79fdda2&t=";

        //Convert it to object
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Movie movie = objectMapper.readValue(restTemplate.getForObject(url + name, String.class), Movie.class);

        //Save movie if not in the database
        Movie existingMovie = movieRepository.findByTitle(movie.getTitle());
        if(existingMovie == null) {
            movieRepository.save(movie);
        }

        return movie;
    }
}
