# Senior Design - May 2021 Group 35
# Part of Speech Tagging for Software Documentation
# Universal HTML Parsing Module

import os

print("Finding Tokenizer classpath")
for root in os.walk('../Tokenization'):
    if root[0].endswith('classes'):
        print('Tokenizer classpath found at {}'.format(root[0]))
        os.environ['CLASSPATH'] = root[0]

import re

from config import load_config, load_urls
from content import TokenSet, get_content
from lxml import html

from load import Loader
from output import write_token_set, clear_output_directory

config = load_config()
code_tags = config['code-tags']

token_set_id = 0

loader = Loader()

def get_xpaths(url):
    for website in config['websites']:
        for matcher in website['matchers']:
            if re.match(matcher, url) is not None:
                return website['xpaths']

    return None


def parse_xpath(document, xpath):
    results = document.xpath(xpath)

    tokens = []
    for result in results:
        tokens += get_content(result, code_tags)

    return tokens

def parse_url(url):
    xpaths = get_xpaths(url)
    if xpaths is None:
        print('No rules found to match the URL %s' % url)
        exit(1)

    file = loader.load(url)
    if file is None:
        print("Error getting URL %s" % url)
        exit(1)

    tree = html.fromstring(file)

    tokens = []
    for xpath in xpaths:
        tokens += parse_xpath(tree, xpath)

    token_set = TokenSet(tokens)

    global token_set_id
    write_token_set(token_set, 'output/tokens_%d.json' % token_set_id)
    token_set_id += 1


if __name__ == '__main__':
    os.makedirs("output", exist_ok=True)
    clear_output_directory("output")

    for url in load_urls():
        parse_url(url)
