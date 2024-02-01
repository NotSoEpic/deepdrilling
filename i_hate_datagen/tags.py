from __future__ import annotations
import datagen
from pathlib import Path
from resource import Resource

to_add: dict[Resource, Tag] = dict()

axe = Resource("minecraft", "blocks/mineable/axe")
pickaxe = Resource("minecraft", "blocks/mineable/pickaxe")


class Tag:
    def __init__(self, key, values=None):
        if key in to_add:
            raise RuntimeError("key", key, "already defined for tags!")

        if values is None:
            values = set()
        self.key = Resource(key)
        self.values = values
        to_add[key] = self

    def add(self, values):
        if isinstance(values, Tag):
            values = values.values
        if isinstance(values, list):
            values = set(values)
        if not isinstance(values, set):
            values = {values}
        self.values = self.values.union(values)


def add(key, values) -> Tag:
    if key in to_add:
        to_add[key].add(values)
        return to_add[key]
    return Tag(key, values)


def finalize(resources: Path):
    for loc in to_add:
        full_loc = Path("data", loc.namespace, "tags", loc.location)
        full_tag = {
            "values": [str(res) for res in to_add[loc].values]
        }
        datagen.generate(resources, full_loc, full_tag)
