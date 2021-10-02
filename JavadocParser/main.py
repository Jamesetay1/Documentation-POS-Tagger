import unicodedata
from bs4 import BeautifulSoup
from DataTypes import *

INCLUDE_CODE_TAGS = False

to_parse = open("docs/api/java/awt/Graphics.html", "r")

# Clean all of the bad spacing out of the file
dirty = BeautifulSoup(to_parse, 'html.parser')
strings = ""
for line in dirty:
    strings += str(line).replace(u'\xa0', ' ')
soup = BeautifulSoup(strings, 'html.parser')


def parse_summary():
    """Returns a tuple of datatypes, (fields, constructors, methods)."""
    # Find the summary tag
    summary = soup.find("div", "summary").li

    # Find each of the sub items
    items = summary.find_all("ul", "blockList")
    field = items[0]
    constructor = items[1]
    method = items[2]

    fields = []
    constructors = []
    methods = []

    # Parse the fields
    for row in field.find_all("tr"):
        # Skip the initial header row
        if row.find("th") is None:
            curr = FieldSummary()
            curr.name = row.find("td", "colLast").code.span.a.string
            type_row = list(row.find("td", "colFirst").code.stripped_strings)
            curr.type = type_row[0] + " " + type_row[1] if len(type_row) == 2 else type_row[0]
            curr.desc = str(row.find("div")).replace('\n', '').replace("</div>", "").replace("<div class=\"block\">", "")
            fields.append(curr)

    # Parse the constructors
    for row in constructor.find_all("tr"):
        # Skip the header row
        if row.find("td") is not None:
            curr = ConstructorSummary()
            curr.name = row.td.code.span.a.string
            curr.args = list(row.td.code.stripped_strings)[1].replace("(", "").replace(")", "")
            curr.desc = str(row.div).replace("\n", "").replace("<div class=\"block\">", "").replace("</div>", "")
            constructors.append(curr)

    # Parse the methods
    for row in method.find_all("tr"):
        # Skip the header row
        if row.find("div") is not None:
            curr = MethodSummary()
            curr.id = row["id"]
            curr.ret = row.find("td", "colFirst").string
            curr.name = row.find("td", "colLast").code.span.a.string
            curr.args = list(row.find("td", "colLast").code.stripped_strings)[1].replace("\n", "").replace("(", "").replace(")", "")
            curr.desc = str(row.div).replace("\n", "").replace("<div class=\"block\">", "").replace("</div>", "")
            print(curr.args)
            methods.append(curr)

    return fields, constructors, methods


def parse_details():
    """Returns a tuple of datatypes, (fields, constructors, methods)."""
    # Find the summary tag
    summary = soup.find("div", "details").li

    # Find each of the sub items
    items = summary.find_all("ul", "blockList")
    correct_items = []

    # Remove the inner items
    for item in items:
        if item.li.a is not None and item.li.a.has_attr("name"):
            correct_items.append(item)

    field = correct_items[0]
    constructor = correct_items[1]
    method = correct_items[2]

    fields = []
    constructors = []
    methods = []

    for row in field.find_all("ul"):
        curr = FieldSummary()
        curr.name = row.find("h4").string
        typestrings = list(row.find("pre").stripped_strings)
        curr.type = typestrings[0] + " " + typestrings[1] if len(typestrings) == 2 else typestrings[0]
        curr.desc = str(row.find("div")).replace("<div class=\"block\">", "").replace("</div>", "").replace("\n", "")
        fields.append(curr)

    for row in constructor.find_all("ul"):
        curr = ConstructorSummary()
        curr.type = row.find("h4").string
        signature = " ".join(row.find("pre").stripped_strings)
        curr.name = signature.split()[1].split('(')[0]
        curr.args = signature[signature.index("("):signature.index(")")][1:].strip()
        curr.desc = str(row.find("div")).replace('\n', '').replace("<div class=\"block\">", "").replace("</div>", "")
        curr.desc = curr.desc.replace("<i>", "").replace("</i>", "").replace("<b>", "").replace("</b>", "").replace("<p>", "").replace("</p>", "")
        constructors.append(curr)

    return fields, constructors, methods


fields, constructors, methods = parse_summary()
f_d, c_d, m_d = parse_details()
print("FINISHED PARSING")
output = open("summary_output.txt", "w")

output.write("\n==========FIELD SUMMARY==========\n")
for field in fields:
    output.write(str(field) + "\n")

output.write("\n==========CONSTRUCTOR SUMMARY==========\n")
for constructor in constructors:
    output.write(str(constructor) + "\n")

output.write("\n==========METHOD SUMMARY==========\n")
for method in methods:
    output.write(str(method) + "\n")

output.write("\n==========FIELD DETAIL==========\n")
for field in f_d:
    output.write(str(field) + "\n")

output.write("\n==========CONSTRUCTOR DETAIL==========\n")
for constructor in c_d:
    output.write(str(constructor) + "\n")

output.close()
