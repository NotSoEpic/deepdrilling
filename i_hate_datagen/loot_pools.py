from resource import Resource
import number_providers


class Pool:
    def __init__(self, rolls=1):
        self.rolls = number_providers.parse(rolls)
        self.entries = []

    def add_item(self, item, count=1, weight=1):
        entry = {
            "type": "minecraft:item",
            "weight": weight,
            "name": str(Resource(item))
        }
        if count != 1:
            count = number_providers.parse(count)
            entry["functions"] = [{
                "function": "minecraft:set_count",
                "add": False,
                "count": count.get()
            }]
        self.entries.append(entry)
        return self

    def finalize(self):
        return {
            "entries": self.entries,
            "rolls": self.rolls.get()
        }
