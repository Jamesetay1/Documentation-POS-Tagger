import csv
import sys

# We require two files to check for differences between
if len(sys.argv) != 3:
    print("Usage: consistency_checker [file1] [file2]")
    exit(-1)

# Load the two files
f1 = open(sys.argv[1], "r")
f2 = open(sys.argv[2], "r")

# Load each csv into an array of arrays
csv1 = csv.reader(f1)
csv2 = csv.reader(f2)
f1_rows = []
f2_rows = []

for row in csv1:
    f1_rows.append(row)

for row in csv2:
    f2_rows.append(row)

# Test 1: Are there a similar number of rows?
if len(f1_rows) != len(f2_rows):
    print("Row numbers are different.")
    exit(-2)

# If the row numbers are the same, we can delete the duplicate sentence rows
del f1_rows[0::2]
del f2_rows[0::2]

# Now compare the output of each sentence's POS
errors = 0
print("Beginning error search. Row / Word indices start at 0.")
for i in range(0, len(f1_rows)):
    row1 = f1_rows[i]
    row2 = f2_rows[i]

    # Are the rows the same length?
    if len(row1) != len(row2):
        print("[ROW " + str(i) + "] Unequal number of parts of speech.")
        errors += 1
        continue

    # If the rows are the same length, is each POS the same?
    for pos in range(0, len(row1)):
        word1 = row1[pos]
        word2 = row2[pos]
        if word1 != word2:
            print("[ROW " + str(i) + " WORD " + str(pos) + "] POS are not the same. " + word1 + " vs. " + word2)
            errors += 1

# We have made it through the arrays. Let the user know.
print("Analysis Complete. " + str(errors) + " errors found.")