from __future__ import annotations
import datagen
import loot_pools
from pathlib import Path
from resource import Resource

to_add: dict[Resource, Table] = dict()


class Table:
    def __init__(self, key, values=None):
        if key in to_add:
            raise RuntimeError("Key", key, "already defined for loot tables!")

        if values is None:
            values = []
        if not isinstance(values, list):
            values = [values]
        self.key = Resource(key)
        self.values: list[loot_pools.Pool] = values
        to_add[self.key] = self

    def add(self, values):
        if values is loot_pools.Pool:
            values = [values]
        self.values.append(values)


def add(key, values):
    key = Resource(key)
    if key in to_add:
        to_add[key].add(values)
        return to_add[key]
    return Table(key, values)


def drop_self(block):
    return Table(block, loot_pools.Pool().add_item(block))


def finalize(resources: Path):
    for loc in to_add:
        full_loc = Path("data", loc.namespace, "loot_tables", loc.location)
        full_table = {
            "type": "minecraft:empty",
            "pools": [pool.finalize() for pool in to_add[loc].values]
        }
        datagen.generate(resources, full_loc, full_table)
