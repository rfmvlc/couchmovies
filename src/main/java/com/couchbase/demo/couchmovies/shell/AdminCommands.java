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
public class AdminCommands {

    @Autowired
    MoviesService moviesService;

    @Autowired
    RatingsService ratingsService;

    @ShellMethod(value = "Load movies")
    public void loadMovies(@ShellOption(defaultValue = "0") int limit) {
        moviesService.load(limit);
    }

    @ShellMethod(value = "Load ratings")
    public void loadRatings(@ShellOption(defaultValue = "0") int limit, @ShellOption(defaultValue = "false") boolean randomRatings) {
        ratingsService.load(limit, randomRatings);
    }

}
