# ManualTagger
A simple Java Swing GUI for tagging untagged tokens in a dataset.

## Documentation
This project uses Maven. The following guide will show you how to install all dependencies and run the project using the Maven CLI. Alternatively, you can open the project in an IDE such as IntelliJ that can automate a lot of this.

First, Ensure a version of Maven is installed on your machine: 
```shell script
mvn -v
```

Then install all of the dependencies:
```shell script
mvn install
```

You can then run ManualTagger:
```shell script
mvn exec:java -Dexec.mainClass=manual.Main
```

### Using Manual Tagger
Once you have started the application, navigate to the directory containing the data you want to tag (any amount of directory nesting is allowed). The manual tagger will open any file in the format:

```json
{
  "tokens": [
    {
      "tag": <optional string>,
      "token": <string>
    },
    ...
  ],
  "type": "autotagged"
}
```

The user will be prompted to tag any token whose `tag` element is null or empty.

You can exit the application or press stop at any time without losing progress. The files are re-saved each time a token is tagged.