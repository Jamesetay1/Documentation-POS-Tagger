# Model Training Documentation
## Overview
This guide will show you how to run training on a given set of inputs, generate a model, and test the result.
## 1: Location of Assets
The programs used for this portion of the project are located in the NLPModel project within the git directory. This project was made using intellij.
## 2: Train Model
To train a model, you need to get the tagged JSON files and put them into a directory which is easy for you to access. To train the model, you need to run the ``train(input, model, output, temp)`` command, where each input is a string representing a directory.
* `Input` is the directory where the tagged JSON files are located.
* `model` is the directory where you would like to store a model. Make sure to include a filename at the end of your directory path.
*  `output` will be the location of the files which were used for training. This will let you know what files were successfully used.
* `temp` is the directory where JSON files are turned into NLP files. This is not overly important, but still required.
After this is run, you should now have a model you can use in your models folder. This may take time and a lot of memory.

## 3: Testing the Model
### 3A: Tagging of JSON
To tag data, simply run the ``tag(input, model, output)`` command located in the tagging package. 
* `input` is the directory holding the json data to be tagged.
* `model` is the path to the specific model file you wish to use
* `output` is the path where the tagged output XML files will be located.
### 3B: Conversion of XML to JSON
In order to grade the results, we need to convert the XML files back into the nice JSON format. Hopefully, the tagger did this automatically. Check the output to make sure. If it did not, run ``XMLtoJSON.XMLtoJSON()`` on the contents of every file, and re save them.
### 3C: JSON Compare
Finally, to test the results and get an output struct, run ``JSONCompare.doTest(correctDir, taggedDir)`` , which returns a `TestResults` Object containing the results of the complete test. 
* `correctDir` is the directory of the original, correct files, used as the basis for correctness.
* `taggedDir` is the directory of the tagged results which need to be tested.

[NOTE] _THE FILES IN THE TAGGED DIRECTORY MUST HAVE THE SAME NAMES AS THEIR COUNTERPART IN THE CORRECT DIRECTORY._

The function used to do this and print out the nice miss summary is located in `TestingTests.main`.