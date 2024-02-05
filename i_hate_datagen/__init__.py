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
                    "deepdrilling:drill_overclock",
                    "deepdrilling:sludge_pump"])
tags.add(tags.pickaxe, ["deepdrilling:drill_core",
                        "deepdrilling:collection_filter",
                        "deepdrilling:drill_overclock",
                        "deepdrilling:sludge_pump"])

loot_tables.drop_self("deepdrilling:drill_core")
loot_tables.drop_self("deepdrilling:collection_filter")
loot_tables.drop_self("deepdrilling:drill_overclock")
loot_tables.drop_self("deepdrilling:sludge_pump")
drill_head("deepdrilling:andesite_drill_head")
drill_head("deepdrilling:brass_drill_head")
drill_head("deepdrilling:copper_drill_head")


loot_tables.add(Resource("deepdrilling", "ore_nodes/crimsite/earth"), loot_pools.Pool((2, 3))
                .add_item("create:crimsite", (2, 3), 2)
                .add_item("minecraft:tuff", (3, 4), 2)
                .add_item("minecraft:deepslate", (3, 4), 2)
                .add_item("create:limestone", (3, 4), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/crimsite/common"), loot_pools.Pool((1, 2))
                .add_item("minecraft:redstone", (1, 2), 1)
                .add_item("create:crushed_raw_iron", (1, 2), 2)
                .add_item("minecraft:iron_nugget", (5, 11), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/crimsite/rare"), loot_pools.Pool((2, 3))
                .add_item("minecraft:deepslate_iron_ore", (1, 2), 2)
                .add_item("minecraft:raw_iron", (1, 2), 1)
                .add_item("minecraft:redstone", (1, 2), 1)
                )

loot_tables.add(Resource("deepdrilling", "ore_nodes/asurine/earth"), loot_pools.Pool((2, 3))
                .add_item("create:asurine", (2, 3), 2)
                .add_item("minecraft:tuff", (3, 4), 2)
                .add_item("minecraft:deepslate", (3, 4), 2)
                .add_item("minecraft:calcite", (3, 4), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/asurine/common"), loot_pools.Pool((1, 2))
                .add_item("minecraft:gunpowder", (1, 2), 1)
                .add_item("create:crushed_raw_zinc", (1, 2), 2)
                .add_item("create:zinc_nugget", (5, 11), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/asurine/rare"), loot_pools.Pool((2, 3))
                .add_item("create:deepslate_zinc_ore", (1, 2), 2)
                .add_item("create:raw_zinc", (1, 2), 1)
                .add_item("minecraft:gunpowder", (1, 2), 1)
                )

loot_tables.add(Resource("deepdrilling", "ore_nodes/ochrum/earth"), loot_pools.Pool((2, 3))
                .add_item("create:ochrum", (2, 3), 2)
                .add_item("minecraft:tuff", (3, 4), 2)
                .add_item("minecraft:deepslate", (3, 4), 2)
                .add_item("minecraft:dripstone_block", (3, 4), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/ochrum/common"), loot_pools.Pool((1, 2))
                .add_item("minecraft:quartz", (1, 2), 1)
                .add_item("create:crushed_raw_gold", (1, 2), 2)
                .add_item("minecraft:gold_nugget", (5, 11), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/ochrum/rare"), loot_pools.Pool((2, 3))
                .add_item("minecraft:deepslate_gold_ore", (1, 2), 2)
                .add_item("minecraft:raw_gold", (1, 2), 1)
                .add_item("minecraft:quartz", (1, 2), 1)
                )

loot_tables.add(Resource("deepdrilling", "ore_nodes/veridium/earth"), loot_pools.Pool((2, 3))
                .add_item("create:veridium", (2, 3), 2)
                .add_item("minecraft:tuff", (3, 4), 2)
                .add_item("minecraft:andesite", (3, 4), 2)
                .add_item("minecraft:smooth_basalt", (3, 4), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/veridium/common"), loot_pools.Pool((1, 2))
                .add_item("minecraft:clay_ball", (3, 5), 1)
                .add_item("create:crushed_raw_copper", (1, 2), 2)
                .add_item("create:copper_nugget", (5, 11), 2)
                )
loot_tables.add(Resource("deepdrilling", "ore_nodes/veridium/rare"), loot_pools.Pool((2, 3))
                .add_item("minecraft:deepslate_copper_ore", (1, 2), 2)
                .add_item("minecraft:raw_copper", (1, 2), 1)
                .add_item("minecraft:clay_ball", (3, 5), 1)
                )

tags.finalize(resources)
loot_tables.finalize(resources)
