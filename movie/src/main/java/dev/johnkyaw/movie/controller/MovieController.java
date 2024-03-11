package dev.johnkyaw.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.johnkyaw.movie.model.Movie;
import dev.johnkyaw.movie.model.Ratings;
import dev.johnkyaw.movie.service.MovieServices;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {
    @Autowired
    private MovieServices movieServices;
    @GetMapping("/")
    public String showSearchPage() {
        return "index";
    }

    @PostMapping("/result")
    public String showSavedMovies(@RequestParam("movieName") String movieName, Model model) throws JsonProcessingException {
        Movie movie = movieServices.getMovieFromAPI(movieName);

        //get rotten tomato rating
        Ratings rtRating = null;
        List<Ratings> ratingsList = movie.getRatings();
        if(!ratingsList.isEmpty()) {
            for(Ratings rating : movie.getRatings()) {
                if(rating.getSource().equals("Rotten Tomatoes")) {
                    rtRating = rating;
                    break;
                }
            }
        }

//        //convert blob to image
//        String base64encodedImage = Base64.encodeBase64String(movie.getImage());

        model.addAttribute("rating", rtRating);
        model.addAttribute("movie", movie);
//        model.addAttribute("image", base64encodedImage);

        return "resultMovie";
    }

    @GetMapping("/savedMovies")
    public String showSavedMovies(Model model) throws JsonProcessingException {
        List<Movie> movieList = movieServices.getAllMovies();
        model.addAttribute("movies", movieList);
        return "savedMovies";
    }
}
