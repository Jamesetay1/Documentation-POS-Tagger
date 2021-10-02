class FieldSummary:
    """A field summary is a struct containing information about a summary of a field."""
    def __init__(self):
        self.type = "NONE"
        self.name = "NONE"
        self.desc = "NONE"

    def __str__(self):
        return "{:>25} {:<30} {}".format(str(self.type), str(self.name), str(self.desc))


class ConstructorSummary:
    """A constructor summary is a struct containing information about a constructor's summary."""
    def __init__(self):
        self.name = "NONE"
        self.args = "NONE"
        self.desc = "NONE"

    def __str__(self):
        return "{:>25} {}".format(str(self.name) + "(" + str(self.args) + ")", str(self.desc))


class MethodSummary:
    """A method summary is a struct containing information about a method's summary."""
    def __init__(self):
        self.ret = "NONE"
        self.name = "NONE"
        self.args = "NONE"
        self.desc = "NONE"
        self.id = "NONE"

    def __str__(self):
        return "{:>25} {:<30} {}".format(str(self.ret), str(self.name) + "(" + str(self.args) + ")", str(self.desc))
