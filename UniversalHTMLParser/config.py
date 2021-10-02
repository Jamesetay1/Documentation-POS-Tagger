import json


def load_config():
    with open('rules.json') as file:
        return json.load(file)


def load_urls():
    with open("urls.txt") as file:
        lines = []
        for line in file:
            if not line.isspace():
                lines.append(line.strip())

        return lines
