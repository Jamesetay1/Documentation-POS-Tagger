# Senior Design - May 2021 Group 35
# Part of Speech Tagging for Software Documentation
# Universal HTML Parsing Module
from bs4 import BeautifulSoup
import re
import os


def tokenize_tags(file_string, tags):
    """Replace each tag in the "tags" list with a non-xml tag object."""
    for tag_string in tags:
        file_string = file_string.replace("<" + tag_string + ">", "(TAG)" + tag_string + "(TAG)")
        file_string = file_string.replace("</" + tag_string + ">", "(/TAG)" + tag_string + "(/TAG)")

    return file_string


def detokenize_tags(file_string, tags):
    """Replace each tokenized tag with its original form."""
    for tag_string in tags:
        file_string = file_string.replace("(TAG)" + tag_string + "(TAG)", "<" + tag_string + ">")
        file_string = file_string.replace("(/TAG)" + tag_string + "(/TAG)", "</" + tag_string + ">")

    return file_string


def parse_file(filename, tags):
    """Parse the supplied file (filename) with whitelist tags (tags) and produce a textual output."""
    file = open(filename, "r")  # The file being opened
    output = ""                 # The text output from this file

    file_text = file.read()
    soup = BeautifulSoup(tokenize_tags(file_text, tags), 'html.parser')
    output = detokenize_tags(soup.getText(), tags)
    output = re.sub(r"\n{2,}", "\n\n", output)
    output = output.replace(u'\xa0', ' ')
    return output


if __name__ == '__main__':
    tags_to_maintain = ["code", "em", "p", "strong" "li", "pre", "a href", "pre"]
    input_folder = "../Data/Scraped_HTML/Scraped_LeetCode"
    output_folder = "../Data/Parsed_HTML/Parsed_LeetCode"
    for entry in os.scandir(input_folder):
        if (entry.path.endswith(".html")):
            temp = parse_file(entry.path, tags_to_maintain)
            print(entry.path)
            file_name = re.search(r"\/Data\/.*\/.*\\(.*)\.html", entry.path).group(1)
            text_file = open(f"{output_folder}/{file_name}.html", "x", encoding="utf-8")
            text_file.write(temp)
    #print(out)