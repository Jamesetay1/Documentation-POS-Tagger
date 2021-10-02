# AutoTagger
A simple rule-based tagger for use in producing training data for more sophisticated models.

### Strengths
 - Easy to add new tags
 - Very fast
 - Configurable to different languages

### Shortcomings
 - Cannot identify all tokens with a tag (manual work needed afterwords)
 - Not extensible for use with normal language
 
 ## Documentation
 This project uses Maven. The following guide will show you how to install all dependencies and run the project using  the Maven CLI. Alternatively, you can open the project in an IDE such as IntelliJ that can automate a lot of this.
 
First, Ensure a version of Maven is installed on your machine: 
```shell script
mvn -v
```

Then install all of the dependencies:
```shell script
mvn install
```

You can then run AutoTagger:
```shell script
mvn exec:java -Dexec.mainClass=auto.Main <path to rules> <path to directory>
```

`path to rules`: Is the rule set to use for tagging code. Currently, this will be `rules/java.json` within this repository.

`path to directory`: Is the directory containing `.json` files (with arbitrarily much directory nesting allowed) that should be tagged. These files should be in format:

```json
{
  "tokens": [
    {
      "code": <boolean>,
      "token": <string>
    },
    ...
  ],
  "type": "tokens"
}
```

The output will be a JSON file in the format:
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

If the `tag` for a token is not present, then the AutoTagger failed to automatically tag the token. You will need to manually tag this token or add a new rule to the rule set.
