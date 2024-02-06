from resource import Resource


class Ingredient:
    def __init__(self, name: Resource | str):
        self.name = Resource(name)

    def finalize(self) -> dict:
        raise RuntimeError("Raw use of Ingredient class")

    def __eq__(self, other):
        return type(self) is type(other) and self.name == other.name

    def __hash__(self):
        return hash(str(type(self)) + "@" + str(self.name))


class Item(Ingredient):
    def finalize(self) -> dict:
        return {
            "item": str(self.name)
        }


class ItemTag(Ingredient):
    def finalize(self) -> dict:
        return {
            "tag": str(self.name)
        }


class Fluid(Ingredient):
    def __init__(self, name: Resource | str, amount: int):
        super().__init__(name)
        self.amount = amount

    def finalize(self) -> dict:
        return {
            "fluid": str(self.name),
            "amount": self.amount
        }


class FluidTag(Ingredient):
    def __init__(self, name: Resource | str, amount: int):
        super().__init__(name)
        self.amount = amount

    def finalize(self) -> dict:
        return {
            "fluidTag": str(self.name),
            "amount": self.amount
        }
