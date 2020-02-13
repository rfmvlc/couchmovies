# Pre-requisites

- Couchbase Server 6.5.x
- Java 1.8.x
- Maven 3.x

> Setup your environment as described on Java SDK docs [here](https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html)
# Dataset

## Download zip file
```
$ wget -c http://files.grouplens.org/datasets/movielens/ml-latest.zip -O movies.zip
```

## Extract the zip file and copy content to movie's folder

> Example
```
$  unzip -j "movies.zip" -d ~/movies
``` 

# Create movies bucket 

> Create your "movies" bucket as described on Rest APIs docs [here](https://docs.couchbase.com/server/current/rest-api/rest-bucket-create.html)

# Configure settings
 
 > File: `resources/application.properties`

Adjust to your environment settings: 
```
# Cluster nodes
## List of nodes comma separated
com.couchbase.demo.couchmovies.connection-string=couchbase://*****
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

 :: Sample application build with Spring Boot (v2.1.7.RELEASE) ::

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

## rate a movie
```
shell:>rate 1 1 4.5
{"rating":4.5,"movieId":1,"type":"rating","userId":1,"key":"rating::1::1","timestamp":1581551670623}
```

## have an error? don't sweat *stacktrace* will help you out!
```
shell:>rate maverick iceman loren ipsum
Too many arguments: the following could not be mapped to parameters: 'ipsum'
Details of the error have been omitted. You can use the stacktrace command to print the full stacktrace.
shell:>stacktrace
java.lang.IllegalArgumentException: Too many arguments: the following could not be mapped to parameters: 'ipsum'
	at org.springframework.util.Assert.isTrue(Assert.java:118)
	at org.springframework.shell.standard.StandardParameterResolver.lambda$resolve$4(StandardParameterResolver.java:218)
	at java.util.concurrent.ConcurrentMap.computeIfAbsent(ConcurrentMap.java:324)
	at org.springframework.shell.standard.StandardParameterResolver.resolve(StandardParameterResolver.java:138)
	at org.springframework.shell.Shell.resolveArgs(Shell.java:316)
	at org.springframework.shell.Shell.evaluate(Shell.java:177)
	at org.springframework.shell.Shell.run(Shell.java:142)
	at org.springframework.shell.jline.InteractiveShellApplicationRunner.run(InteractiveShellApplicationRunner.java:84)
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:771)
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:761)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:319)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1214)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1203)
	at com.couchbase.demo.couchmovies.Application.main(Application.java:14)
```
