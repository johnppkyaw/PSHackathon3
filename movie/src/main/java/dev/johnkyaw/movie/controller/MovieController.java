package dev.johnkyaw.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.johnkyaw.movie.model.Movie;
import dev.johnkyaw.movie.model.Ratings;
import dev.johnkyaw.movie.service.MovieServices;
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

        //get rotten tomato rating
        Movie movie = movieServices.getMovieFromDB(movieName);
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

        model.addAttribute("rating", rtRating);
        model.addAttribute("movie", movie);
        return "resultMovie";
    }

    @GetMapping("/savedMovies")
    public String showSavedMovies(Model model) throws JsonProcessingException {
        List<Movie> movieList = movieServices.getAllMovies();
        model.addAttribute("movies", movieList);
        return "savedMovies";
    }
}
