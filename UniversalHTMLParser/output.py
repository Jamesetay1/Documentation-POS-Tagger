import glob
import json
import os


class DictEncoder(json.JSONEncoder):
    def default(self, o):
        return o.__dict__


def clear_output_directory(output_directory):
    files = glob.glob(os.path.join(output_directory, "*"))
    for f in files:
        os.remove(f)


def write_token_set(token_set, output_directory):
    with open(output_directory, 'w') as file:
        json.dump(token_set, file, cls=DictEncoder, indent=2)