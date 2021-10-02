# Project Overview
This project was my Senior design project for SE491 and SE492 Iowa State.  
We created a POS tagger specifically for Software Documentation by adding a new tag set, getting new training data, and using various models - ultimately ending on a CRF to get the best results.

# Table of Contents
* Important Directories are marked with a (*)
## `Consistency Checking`
A small set of utilities used to check out consistency between the python and java versions of stanford NLP (stanza vs corenlp). This is not useful for the main project.
## `Javadoc Parser`
A small parser which attempted to parse javadoc HTML directly. Overall, this is a deprecated library, and was scrapped in favor of the Universal HTML parser.
## `JsonToNLP`
A small module used to parse JSON formatted data into NLP formatted data. This module was not used, and was scrapped in favor of the converter in NLPModel. This is not recommended for use.
## `Other`
A small temporary directory full of files. The live-parser resides here, but is not really useful (it was an offline version of the coreNLP online parser).
## (*)`Tokenization`
The tokenization module for the pipeline. This is a java module capable of tokenizing and sentence splitting plaintext HTML input. The tokenization module is one of the more important modules, and is used within the universal HTML parser.
## (*)`AutoTagging`
Autotagging contains the auto tagger module used for automatically tagging JSON formatted datafiles. The autotagger will automatically tag any obvious missing tags (pure english, numbers, etc) and leave the rest for manual tagging.
## (*)`Manual Tagger`
The manual tagger is a javafx gui used to tag untagged data in a directory full of JSON datafiles. For any manual tagging, this tool will be of good use. Additionally, the patcher is located here, which can tag all of a given token with a specified tag.
## `Data`
The data directory contains all of the data used for training / our iteration of work. Here you can see examples of each type of data used for our first iteration of the project.
## (*)`UniversalHTMLParser`
The universal HTML parser is located here, and is capable of taking in URL inputs and spitting out tokenized JSON data for training / tagging. See the associated readme for more information.
## `TrainingDir`
The training directory contains all of the data used for training the model, as well as the current iteration of the model. This directory is referenced by the NLP model application.
## (*)`NLP Model`
The NLP model contains all tagging, training, and grading utilities for the pipeline. See the associated readme for more information on how to use this project.
