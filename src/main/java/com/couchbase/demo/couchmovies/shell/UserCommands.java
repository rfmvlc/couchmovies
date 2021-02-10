package com.couchbase.demo.couchmovies.shell;

import com.couchbase.demo.couchmovies.service.MoviesService;
import com.couchbase.demo.couchmovies.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.commands.Quit;

@Lazy
@ShellComponent
public class UserCommands {

    @Autowired
    RatingsService ratingsService;

    @Autowired
    MoviesService moviesService;

    @ShellMethod(value = "list my last ratings")
    public void myRatings(@ShellOption(defaultValue = "1") long userId) {
        ratingsService.myRatings(userId);
    }

    @ShellMethod("Search for a movie")
    public void searchMovie(@ShellOption String searchString) {
        moviesService.search(searchString);
    }

    @ShellMethod("Analyse top movies")
    public void topMovies(@ShellOption(defaultValue = "10") int numMovies) {
        moviesService.showTopMovies(numMovies);
    }

}
