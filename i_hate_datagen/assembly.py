import recipes
from resource import Resource
from ingredient import Ingredient, Item


class WeightedResult(Ingredient):
    def __init__(self, name: Resource | str, weight: int | float):
        super().__init__(name)
        self.weight = weight

    def finalize(self) -> dict:
        return {
            "item": str(self.name),
            "chance": self.weight
        }


class Sequence:
    def __init__(self, name: Resource | str):
        self.name = Resource(name)
        self.ingredients = []
        self.result: Item = None

    def addTransitional(self, transitional: Item):
        self.ingredients.insert(0, transitional)
        self.result = transitional

    def finalize(self) -> dict:
        if self.result is None:
            raise RuntimeError("sequence never had transitional supplied")
        return {
            "type": str(self.name),
            "ingredients": [ing.finalize() for ing in self.ingredients],
            "results": [self.result.finalize()]
        }


class Deploying(Sequence):
    def __init__(self, item: Ingredient):
        super().__init__(Resource("create", "deploying"))
        self.ingredients.append(item)


class Cutting(Sequence):
    def __init__(self):
        super().__init__(Resource("create", "cutting"))


class Pressing(Sequence):
    def __init__(self):
        super().__init__(Resource("create", "pressing"))


class Assembly(recipes.Recipe):
    def __init__(self, name: Resource, input_item: Ingredient,
                 transitional: Item, sequence: list[Sequence], results: Item | list[WeightedResult], loops=1):
        super().__init__(name, Resource("create", "sequenced_assembly"))
        self.input = input_item
        self.transitional = transitional
        self.sequence = sequence
        for seq in self.sequence:
            seq.addTransitional(transitional)
        self.results = results
        self.loops = loops

    def finalize(self) -> dict:
        d = super().finalize()
        d["ingredient"] = self.input.finalize()
        d["transitionalItem"] = self.transitional.finalize()
        d["loops"] = self.loops
        d["sequence"] = [seq.finalize() for seq in self.sequence]
        if isinstance(self.results, list):
            d["results"] = [res.finalize() for res in self.results]
        else:
            d["results"] = [self.results.finalize()]
        return d
