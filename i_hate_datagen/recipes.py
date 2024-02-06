from __future__ import annotations

import datagen
import ingredient
from resource import Resource
from pathlib import Path

to_add: dict[Resource, Recipe] = dict()


def resultItem(name: Resource | str, count: int = 1) -> dict:
    return {
        "item": str(Resource(name)),
        "count": count
    }


class Recipe:
    def __init__(self, name: Resource, type: Resource):
        self.name = name
        self.type = type

        to_add[self.name] = self

    def finalize(self) -> dict:
        return {
            "type": str(self.type)
        }


class Shapeless(Recipe):
    def __init__(self, name: Resource, result: dict, ingredients: list[ingredient.Ingredient]):
        super().__init__(name, Resource("minecraft", "crafting_shapeless"))
        self.ingredients = ingredients
        self.result = result

    def finalize(self) -> dict:
        d = super().finalize()
        d["ingredients"] = [ing.finalize() for ing in self.ingredients]
        d["result"] = self.result
        return d


class Shaped(Recipe):
    def __init__(self, name: Resource, result: dict, shape: list[list[ingredient.Ingredient | None]]):
        h = len(shape)
        w = len(shape[0])
        if h == 0 or h > 3 or w == 0 or w > 3:
            raise ValueError("Improperly shaped recipe for", name)
        for i in range(h):
            if w != len(shape[i]):
                raise ValueError("Improperly shaped recipe for", name)

        self.shape = shape
        self.result = result
        super().__init__(name, Resource("minecraft", "crafting_shaped"))

    def keyify(self) -> tuple[list, dict]:
        keys = ["A", "B", "C", "D", "E", "F", "G", "H", "I"]
        pattern = [""] * len(self.shape)
        tmpkey = dict()
        for i in range(len(self.shape)):
            for j in range(len(self.shape[i])):
                v = self.shape[i][j]
                if v is None:
                    pattern[i] += " "
                else:
                    if v not in tmpkey.keys():
                        tmpkey[v] = keys.pop()
                    pattern[i] += tmpkey[v]
        key = dict()
        for k, v in tmpkey.items():
            key[v] = k.finalize()
        return pattern, key

    def finalize(self) -> dict:
        d = super().finalize()
        pattern, key = self.keyify()
        d["pattern"] = pattern
        d["key"] = key
        d["result"] = self.result
        return d


def finalize(resources: Path):
    for loc in to_add:
        full_loc = Path("data", loc.namespace, "recipes", loc.location)
        datagen.generate(resources, full_loc, to_add[loc].finalize())
