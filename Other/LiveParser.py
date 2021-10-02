import stanza
import sys

if len(sys.argv) != 2:
    print("Usage: LiveParser [file]")
    exit(-1)

# Open the file for reading
file = open(sys.argv[1], "r")


# Setup the nlp processor
stanza.download('en')           # Download english language model
nlp = stanza.Pipeline('en')     # Instantiate a new pipeline for english

# Generate an Array to hold lines
arr_out = []

# Process the File
for line in file:
    line = line.strip()
    top = ""
    bot = ""
    if line != "":
        processed = nlp(line)
        for sentence in processed.sentences:
            for word in sentence.words:
                top += word.text + ", "
                bot += word.xpos + ", "
            top = top[:-1] + "\n"
            bot = bot[:-1] + "\n"
            arr_out.append(top)
            arr_out.append(bot)

# Write the file
out = open("Output.csv", "w")
out.writelines(arr_out)
out.close()