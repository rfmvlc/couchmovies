package com.couchbase.demo.couchmovies.shell;

import com.couchbase.demo.couchmovies.service.MoviesService;
import com.couchbase.demo.couchmovies.service.RatingsService;
import com.couchbase.demo.couchmovies.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@Lazy
@ShellComponent
public class AdminCommands {

    @Autowired
    MoviesService moviesService;

    @Autowired
    RatingsService ratingsService;

    @Autowired
    TagsService tagsService;

// TODO: not finished yet!
//    @Autowired
//    DownloadService moviesDownloadService;

    @ShellMethod(value = "Load movies")
    public void loadMovies(@ShellOption(defaultValue = "0") int limit) {

        moviesService.load(limit);

    }

    @ShellMethod(value = "Load ratings")
    public void loadRatings(@ShellOption(defaultValue = "0") int limit) {

        ratingsService.load(limit);

    }

    @ShellMethod(value = "rate a movie")
    public void rate(@ShellOption(defaultValue = "1") long userId, @ShellOption(defaultValue = "1") long movieId, @ShellOption(defaultValue = "1") long rating) {

        ratingsService.rate(userId, movieId, rating);

    }
// TODO: not finished yet!
//
//    @ShellMethod(value = "Load tags")
//    public void addTags(@ShellOption(defaultValue = "0") int limit) {
//
//        tagsService.addTags(limit);
//
//    }
//
//    @ShellMethod(value = "Remove tags")
//    public void removeTags() {
//
//        tagsService.removeTags();
//
//    }

//    @ShellMethod("Download couchmovies dataset")
//    public void downloadMovies() {
//
//        moviesDownloadService.download();
//        moviesDownloadService.unzip();
//
//    }

}
