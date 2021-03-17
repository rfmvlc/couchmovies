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

    @ShellMethod(value = "Rate a movie")
    public void rateMovie(@ShellOption(defaultValue = "1") long userId, @ShellOption(defaultValue = "1") long movieId, @ShellOption(defaultValue = "3") float rating, boolean fail, boolean subDoc) {
        if (!subDoc)
            ratingsService.rate(userId, movieId, rating);
        else
            ratingsService.rateSubDoc(userId, movieId, rating, fail);

    }

    @ShellMethod(value = "Get movie info")
    public void getMovie(@ShellOption(defaultValue = "1") long movieId, boolean sdk) {
        if (!sdk)
            moviesService.getMovie(movieId);
        else
            moviesService.getMovieSDK(movieId);

    }


    @ShellMethod(value = "list my last ratings")
    public void findMyRatings(@ShellOption(defaultValue = "1") long userId) {
        ratingsService.findMyRatings(userId);
    }

    @ShellMethod(value = "list my last ratings")
    public void findAllMovies(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int pageSize, @ShellOption(defaultValue = "0") long movieId) {

        moviesService.findAll(page, pageSize, movieId);
    }

    @ShellMethod("Search for a movie")
    public void searchMovie(@ShellOption String searchString) {
        moviesService.search(searchString);
    }

    @ShellMethod("Analyse top movies")
    public void findTopTenMovies() {
        moviesService.findTopTenMovies(10);
    }

}
