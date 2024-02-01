# all facets of datagen refuses to work in the slightest so im writing my own in python
# fuck you java, fuck you gradle, fuck you forge and fabric
from pathlib import Path

from resource import Resource
import tags, loot_tables, loot_pools

resources = Path("../common/src/generated/resources")


def drill_head(block):
    table = loot_tables.drop_self(block)
    first_entry = table.values[0].entries[0]
    if "functions" not in first_entry:
        first_entry["functions"] = []
    first_entry["functions"].append({
        "function": "minecraft:copy_nbt",
        "ops": [
            {
                "op": "replace",
                "source": "Damage",
                "target": "Damage"
            }
        ],
        "source": "block_entity"
    })
    tags.add(tags.pickaxe, block)


tags.add(tags.axe, ["deepdrilling:drill_core",
                    "deepdrilling:collection_filter",
                    "deepdrilling:drill_overclock"])
tags.add(tags.pickaxe, ["deepdrilling:drill_core",
                        "deepdrilling:collection_filter",
                        "deepdrilling:drill_overclock"])

loot_tables.drop_self("deepdrilling:drill_core")
loot_tables.drop_self("deepdrilling:collection_filter")
loot_tables.drop_self("deepdrilling:drill_overclock")
drill_head("deepdrilling:andesite_drill_head")
drill_head("deepdrilling:brass_drill_head")
drill_head("deepdrilling:copper_drill_head")


loot_tables.add(Resource("deepdrilling", "iron/common"), loot_pools.Pool((1, 2))
                .add_item("minecraft:deepslate_iron_ore", (1, 2), 1)
                .add_item("minecraft:raw_iron", (1, 2), 1)
                .add_item("minecraft:redstone_dust", (1, 2), 1)
                .add_item("minecraft:crushed_iron", (1, 2), 2)
                .add_item("minecraft:iron_nugget", (5, 11), 4)
                )

tags.finalize(resources)
loot_tables.finalize(resources)
