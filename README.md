# Pre-requisites

- Couchbase Server 6.5.x
- Java 1.8.x
- Maven 3.x

> Setup your environment as described on Java SDK docs [here](https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html)
# Download the dataset on the user's home folder:

```
$ cd ~
$ wget http://files.grouplens.org/datasets/movielens/ml-latest.zip
```

# Extract the zip file and rename the directory to Movies:

```
$ nice unzip -j "ml-latest.zip"
$ mv ml-latest Movies
``` 

# Create movies bucket 

> Create your "movies" bucket as described on Rest APIs docs [here](https://docs.couchbase.com/server/current/rest-api/rest-bucket-create.html)

# Configure settings on `resources/application.properties`

Adjust to your environment settings 
```
# Cluster nodes
## List of nodes comma separated. At least (replica number + 1) nodes here
com.couchbase.demo.couchmovies.connection-string=couchbase://*****
# Bucket + Credentials
com.couchbase.demo.couchmovies.bucket-name=*****
com.couchbase.demo.couchmovies.username=*****
com.couchbase.demo.couchmovies.password=******
```

# Run the application

```
$ mvn spring-boot:run
  
  [INFO] Scanning for projects...
  [INFO]
  [INFO] -------------------< com.couchbase.demo:couchmovies >-------------------
  [INFO] Building couchmovies 1.0.0
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
        rate: rate a movie
```

## help over command

```
shell:>help rate


NAME
	rate - rate a movie

SYNOPSYS
	rate [[--user-id] long]  [[--movie-id] long]  [[--rating] long]

OPTIONS
	--user-id  long

		[Optional, default = 1]

	--movie-id  long

		[Optional, default = 1]

	--rating  long

		[Optional, default = 1]
```