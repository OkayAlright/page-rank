# Page Ranker

This is a simple scala/spark page ranking app/commandline tool.

## Requirements:
 - SBT version 0.13.12
 - Scala 2.11.8
 - Java 8


## To Build:

Clone this repo locally and run the following:

    $ sbt assembly
    $ sbt test

If both finish without error, a .jar will be places in the *target* folder
that will be a fat-jar of the app and all it's dependencies.

Feel free to move this .jar wherever you please.

## To Use:

Usage:

    $ pageRanker [--outputDest <path>] ((--depth <num> fullURL) | --useToyNotation filePath

There are two modes for the app *WebCrawler* and *ToyNotation*. Both have an optional
*--outputDest* flag to save off the results of the ranking.

### WebCrawler:

WebCrawler requires some integer for the **--depth** flag; the number will be used as a
depth for the crawler to continue through links it finds. Following the depth number should
be the full URL you wish the crawler to start at (https://google.com, for example).

Note that this will take some time to process, each page much be downloaded and parsed for links.

### ToyNotations

This mode will parse a file (specified as a string right after the *--useToyNotation* flag).
The file must be two space-separated strings on each line to represent a link form left-string
to right-string for as many lines as possible. Take this example graph:

    url_1 url_4
    url_2 url_1
    url_3 url_2
    url_3 url_1
    url_4 url_3
    url_4 url_1

This spec in a file will be parsed and ranked.


## End Notes:

There is a lot of logging that happens to stdout, so it is in your interest to use the
option to save results to a file.

Thanks!
