# Pre-requisites

- Couchbase Server 6.6 (default collection enabled)
- Java 1.8.x
- Maven 3.x
- Spring Data 4.x

> Setup your environment as described on Java SDK docs [here](https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html)

# Dataset

## Download zip file

```
$ wget -c http://files.grouplens.org/datasets/movielens/ml-latest.zip -O movies.zip
```

## Extract the zip file and copy content to movie's folder

> Example

```
$  unzip -j "movies.zip" -d ~
``` 

# Create movies bucket and collections

> Create "movies" bucket as described on Rest APIs docs [here](https://docs.couchbase.com/server/current/rest-api/rest-bucket-create.html)


# Configure settings

> File: `resources/application.properties`

Adjust to your environment settings:

```
# Cluster nodes
## List of nodes comma separated
com.couchbase.demo.couchmovies.connection-string=*****
# Bucket + Credentials
com.couchbase.demo.couchmovies.bucket-name=*****
com.couchbase.demo.couchmovies.username=*****
com.couchbase.demo.couchmovies.password=******
# Movies dataset
com.couchbase.demo.couchmovies.home=*****
```

# Run the application

```
$ mvn spring-boot:run
  
  [INFO] Scanning for projects...
  [INFO]
  [INFO] -------------------< com.couchbase.demo:couchmovies >-------------------
  [INFO] Building couchmovies 1.3.0
  [INFO] --------------------------------[ jar ]---------------------------------
  [INFO]
  [INFO] >>> spring-boot-maven-plugin:2.1.7.RELEASE:run (default-cli) > test-compile @ couchmovies >>>
  [INFO]
  [INFO] --- maven-resources-plugin:3.1.0:resources (default-resources) @ couchmovies ---
  [INFO] Using 'UTF-8' encoding to copy filtered resources.
  [INFO] Copying 1 resource
  [INFO] Copying 1 resource
  [INFO]
  [INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ couchmovies ---
  [INFO] Changes detected - recompiling the module!
  [INFO] Compiling 19 source files to /Users/raymundoflores/Code/couchmovies/target/classes
  [INFO] /Users/raymundoflores/Code/couchmovies/src/main/java/com/couchbase/demo/couchmovies/data/SDKAsyncRepo.java: Some input files use unchecked or unsafe operations.
  [INFO] /Users/raymundoflores/Code/couchmovies/src/main/java/com/couchbase/demo/couchmovies/data/SDKAsyncRepo.java: Recompile with -Xlint:unchecked for details.
  [INFO]
  [INFO] --- maven-resources-plugin:3.1.0:testResources (default-testResources) @ couchmovies ---
  [INFO] Using 'UTF-8' encoding to copy filtered resources.
  [INFO] skip non existing resourceDirectory /Users/raymundoflores/Code/couchmovies/src/test/resources
  [INFO]
  [INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ couchmovies ---
  [INFO] No sources to compile
  [INFO]
  [INFO] <<< spring-boot-maven-plugin:2.1.7.RELEASE:run (default-cli) < test-compile @ couchmovies <<<
  [INFO]
  [INFO]
  [INFO] --- spring-boot-maven-plugin:2.1.7.RELEASE:run (default-cli) @ couchmovies ---
  
   ___                 _                       _
  / __\___  _   _  ___| |__   /\/\   _____   _(_) ___  ___
 / /  / _ \| | | |/ __| '_ \ /    \ / _ \ \ / / |/ _ \/ __|
/ /__| (_) | |_| | (__| | | / /\/\ \ (_) \ V /| |  __/\__ \
\____/\___/ \__,_|\___|_| |_\/    \/\___/ \_/ |_|\___||___/

 shell:>
```

# Commands

## help

```
shell:>help
AVAILABLE COMMANDS

Admin Commands
        load-movies: Load movies
        load-ratings: Load ratings

Built-In Commands
        clear: Clear the shell screen.
        exit, quit: Exit the shell.
        help: Display help about available commands.
        script: Read and execute commands from a file.
        stacktrace: Display the full stacktrace of the last error.

User Commands
        find-all-movies: list my last ratings
        find-my-ratings: list my last ratings
        find-top-ten-movies: Analyse top movies
        rate-movie: Rate a movie
        search-movie: Search for a movie
```

## help over command

```
shell:>help load-movies


NAME
	load-movies - Load movies

SYNOPSYS
	load-movies [[--limit] long]

OPTIONS
	--limit  long

		[Optional, default = 0]
```

## rate a movie and list my last ratings

```
shell:>rate-movie --movie-id 1 --user-id 1 --rating 4.98
shell:>find-my-ratings
+--------------------------------------------------+--------+------------+
| title                                            | rating | date       |
+--------------------------------------------------+--------+------------+
| Toy Story (1995)                                 | 4.98   | 2021-03-17 | < latest
| Jumanji (1995)                                   | 4      | 2021-03-17 |
| Stigmata (1999)                                  | 3      | 2009-10-27 |
| RoboCop 2 (1990)                                 | 2.5    | 2009-10-27 |
| Nurse Betty (2000)                               | 3.5    | 2009-10-27 |
| Spawn (1997)                                     | 1.5    | 2009-10-27 |
| Weekend at Bernie's (1989)                       | 1.5    | 2009-10-27 |
| Weird Science (1985)                             | 4.5    | 2009-10-27 |
| Better Off Dead... (1985)                        | 4.5    | 2009-10-27 |
| Kalifornia (1993)                                | 3.5    | 2009-10-27 |
| Do the Right Thing (1989)                        | 4.5    | 2009-10-27 |
| Waiting for Guffman (1996)                       | 4.5    | 2009-10-27 |
| Falling Down (1993)                              | 4      | 2009-10-27 |
| Running Man, The (1987)                          | 3.5    | 2009-10-27 |
| ¡Three Amigos! (1986)                            | 4      | 2009-10-27 |
| Event Horizon (1997)                             | 2.5    | 2009-10-27 |
| Three Colors: Blue (Trois couleurs: Bleu) (1993) | 3.5    | 2009-10-27 |
| Hollow Man (2000)                                | 2      | 2009-10-27 |
+--------------------------------------------------+--------+------------+

```

## have an error? don't sweat *stacktrace* will help you out!

```
shell:>rate maverick iceman loren ipsum
No command found for 'rate maverick iceman loren ipsum'
Details of the error have been omitted. You can use the stacktrace command to print the full stacktrace.
shell:>stacktrace
org.springframework.shell.CommandNotFound: No command found for 'rate maverick iceman loren ipsum'
	at org.springframework.shell.Shell.evaluate(Shell.java:180)
	at org.springframework.shell.Shell.run(Shell.java:134)
	at org.springframework.shell.jline.InteractiveShellApplicationRunner.run(InteractiveShellApplicationRunner.java:84)
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:795)
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:785)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:333)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1311)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1300)
	at com.couchbase.demo.couchmovies.Application.main(Application.java:13)
```
