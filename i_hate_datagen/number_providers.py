class Constant:
    def __init__(self, value=1):
        self.value = value

    def get(self):
        return {
            "type": "constant", "value": self.value
        }


class Uniform(Constant):
    def __init__(self, low, high):
        self.min = min(low, high)
        self.max = max(low, high)

    def get(self):
        return {
            "min": self.min, "max": self.max
        }


def parse(value) -> Constant:
    if isinstance(value, float) or isinstance(value, int):
        return Constant(value)
    if isinstance(value, tuple):
        return Uniform(value[0], value[1])
    raise ValueError("Unsupported value for number provider:", value)
