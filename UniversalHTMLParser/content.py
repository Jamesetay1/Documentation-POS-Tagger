from jnius import autoclass
import re

tokenizer = autoclass('com.company.Tokenizer')

class Token:
    def __init__(self, token, is_code):
        self.token = token
        self.code = is_code


class TokenSet:
    def __init__(self, tokens):
        self.tokens = tokens
        self.type = "tokens"


def get_content(node, code_tags, is_in_code=False):
    tokens = []

    is_code_tag = node.tag in code_tags
    if is_code_tag:
        tokens += [Token("<%s>" % code_tags[0], True)]

    tokens += tokenize(node.text, is_code_tag or is_in_code)
    for child in node.xpath('*'):
        tokens += get_content(child, code_tags, is_code_tag or is_in_code)

    if is_code_tag:
        tokens += [Token("</%s>" % code_tags[0], True)]

    tokens += tokenize(node.tail, is_in_code)

    return tokens


def tokenize(text, is_code):
    if text is None:
        return []

    tokens = []
    result = tokenizer.Tokenize(text).split()
    x = 0
    for text_token in result:
        if len(text_token) != 0:
            tokens += [Token(text_token, is_code)]

    return tokens
