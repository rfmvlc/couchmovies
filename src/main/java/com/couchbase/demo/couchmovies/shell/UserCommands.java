package com.couchbase.demo.couchmovies.shell;

import com.couchbase.demo.couchmovies.service.MoviesService;
import com.couchbase.demo.couchmovies.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Lazy
@ShellComponent
public class UserCommands {

    @Autowired
    RatingsService ratingsService;

    @Autowired
    MoviesService moviesService;

    @ShellMethod(value = "list my last ratings")
    public void rate(@ShellOption(defaultValue = "1") long userId, @ShellOption(defaultValue = "1") long movieId, @ShellOption(defaultValue = "3") int rating) {
        ratingsService.rate(userId, movieId, rating);
    }

    @ShellMethod(value = "list my last ratings")
    public void myRatings(@ShellOption(defaultValue = "1") long userId) {
        ratingsService.myRatings(userId);
    }

    @ShellMethod("Search for a movie")
    public void searchMovie(@ShellOption String searchString) {
        moviesService.search(searchString);
    }

    @ShellMethod("Analyse top movies")
    public void top10Movies() {
        moviesService.showTopMovies(10);
    }

}
