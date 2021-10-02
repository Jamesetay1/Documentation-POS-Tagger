import stanza
import sys
import csv
import re

if len(sys.argv) != 2:
    print("Usage: consistency_writer [sentence file]")
    exit(-1)

# Open the file for reading
file = open(sys.argv[1], "r")
out = open("python_output.csv", "w", newline='')
csv_out = csv.writer(out, quoting=csv.QUOTE_ALL)

# Setup the nlp processor
stanza.download('en')           # Download english language model
nlp = stanza.Pipeline('en')     # Instantiate a new pipeline for english


# Define some functions for file writing
def process_sentence(original, document):
    # First generate arrays for each sentence
    orig_arr = []
    proc_arr = []
    for word in document.sentences[0].words:
        orig_arr.append(str(word.text))
    for word in document.sentences[0].words:
        proc_arr.append(word.xpos)

    # Write each array into the csv file
    csv_out.writerow(orig_arr)
    csv_out.writerow(proc_arr)


# Iterate through the file and process each sentence
sentence_number = 0
for sentence in file:
    processed = nlp(sentence.strip())
    process_sentence(sentence, processed)
    print("Processed sentence: " + str(sentence_number))
    sentence_number += 1


# Close both files.
file.close()
out.close()