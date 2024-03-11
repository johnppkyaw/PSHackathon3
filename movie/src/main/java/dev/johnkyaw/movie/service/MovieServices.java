package dev.johnkyaw.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnkyaw.movie.repository.MovieRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.johnkyaw.movie.model.Movie;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Service
public class MovieServices {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieFromAPI(String name) throws JsonProcessingException {
        //Search movie json
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.omdbapi.com/?apikey=b79fdda2&t=";

        //Convert it to object
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.disable (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            //alternatively, you can use annotation @JsonIgnoreProperties(ignoreUnknown = true)
            //at Class level in Movie class.
        Movie movie = objectMapper.readValue(restTemplate.getForObject(url + name, String.class), Movie.class);

        //convert image to blob and save it as base64 string
        try {
            URL imageUrl = new URL(movie.getPoster());
            InputStream inputStream = imageUrl.openStream();
            //convert it to bytes and then to base64 string
            //use @Lob and @Column(name = "image", columnDefinition = "LONGBLOB") in the image field if data is too long to store in mysql.
            movie.setImage(Base64.encodeBase64String(inputStream.readAllBytes()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Save movie if not in the database
        Movie existingMovie = movieRepository.findByTitle(movie.getTitle());
        if(existingMovie == null) {
            movieRepository.save(movie);
        }

        return movie;
    }
}
