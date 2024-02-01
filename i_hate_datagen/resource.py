from __future__ import annotations


class Resource:
    def __init__(self, namespace: [str, Resource], location: [str, None] = None):
        self.namespace = ""
        self.location = ""

        if isinstance(namespace, Resource):
            self.namespace = namespace.namespace
            self.location = namespace.location
        elif namespace.count(":") == 0 and location is not None:
            self.namespace = namespace
            self.location = location
        elif namespace.count(":") == 1 and location is None:
            self.namespace = namespace.split(":")[0]
            self.location = namespace.split(":")[1]
        else:
            raise ValueError("Improper Resource input: ", namespace, location)

    def __str__(self):
        return f"{self.namespace}:{self.location}"

    def __repr__(self):
        return str(self)

    def __hash__(self):
        return hash(str(self))
