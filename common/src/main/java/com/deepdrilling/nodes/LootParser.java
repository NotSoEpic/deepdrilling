package com.deepdrilling.nodes;

import com.deepdrilling.DrillMod;
import com.deepdrilling.mixin.loottable.CompositeEntryBaseMixin;
import com.deepdrilling.mixin.loottable.LootItemAccessor;
import com.deepdrilling.mixin.loottable.LootPoolAccessor;
import com.google.common.collect.ImmutableMap;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Inspired by <a href="https://github.com/fzzyhmstrs/EMI_loot/blob/master/src/main/java/fzzyhmstrs/emi_loot/parser/LootTableParser.java#L42">EMI Loot's</a> system
 */
public class LootParser {
    public static Map<OreNode, LootEntry> knownTables = ImmutableMap.of();
    public static boolean hasLoaded = false;

    public static void invalidate() {
        knownTables = ImmutableMap.of();
        hasLoaded = false;
    }

    // SERVER SIDE LOADING
    public static void parseOreNodes(LootDataManager manager, Map<Block, OreNode> nodes) {
        Set<OreNode> added = new HashSet<>();
        ImmutableMap.Builder<OreNode, LootEntry> builder = ImmutableMap.builder();
        for (OreNode node : nodes.values()) {
            if (added.add(node))
                builder.put(node, parseOreNode(manager, node));
        }
        knownTables = builder.build();
        hasLoaded = true;
    }

    private static LootEntry parseOreNode(LootDataManager manager, OreNode node) {
        return new LootEntry(
                getItems(manager, node.earthTable),
                getItems(manager, node.commonTable),
                getItems(manager, node.rareTable)
        );
    }

    private static Set<Item> getItems(LootDataManager manager, String tableName) {
        Set<Item> items = new HashSet<>();
        LootTable table = manager.getLootTable(new ResourceLocation(tableName));
        if (table != LootTable.EMPTY) {
            parseTable(table, items);
        } else {
            DrillMod.LOGGER.warn("Couldn't find loot table {}", tableName);
        }
        return items;
    }

    private static void parseTable(LootTable table, Set<Item> items) {
        for (LootPool pool : getPools(table)) {
            for (LootPoolEntryContainer entry : ((LootPoolAccessor)pool).getEntries()) {
                parseEntry(entry, items);
            }
        }
    }

    @ExpectPlatform
    public static Iterable<LootPool> getPools(LootTable table) {
        throw new AssertionError();
    }

    // todo: probably missing numerous variants, but it works Good Enough for now
    private static void parseEntry(LootPoolEntryContainer entry, Set<Item> items) {
        if (entry instanceof LootItem lootItem) {
            parseLootItem(lootItem, items);
        } else if (entry instanceof CompositeEntryBase compositeEntry) {
            parseCompositeEntry(compositeEntry, items);
        }
    }

    private static void parseLootItem(LootItem lootItem, Set<Item> items) {
        items.add(((LootItemAccessor)lootItem).getItem());
    }

    private static void parseCompositeEntry(CompositeEntryBase compositeEntry, Set<Item> items) {
        for (LootPoolEntryContainer child : ((CompositeEntryBaseMixin)compositeEntry).getChildren()) {
            parseEntry(child, items);
        }
    }

    public static ResourceLocation PACKET_ID = DrillMod.id("node_loot");
    // SERVER -> CLIENT SYNCING
    public static void sendToPlayer(ServerPlayer player) {
        if (!hasLoaded) {
            DrillMod.LOGGER.warn("Tried to send unloaded node loot to player! Generating now");
            parseOreNodes(player.server.getLootData(), OreNodes.getNodeMap());
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(knownTables.size());
        for (Map.Entry<OreNode, LootEntry> entry : knownTables.entrySet()) {
            buf.writeUtf(entry.getKey().name.toString());
            writeItems(buf, entry.getValue().earth);
            writeItems(buf, entry.getValue().common);
            writeItems(buf, entry.getValue().rare);
        }
        player.connection.send(new ClientboundCustomPayloadPacket(PACKET_ID, buf));
    }

    private static void writeItems(FriendlyByteBuf buf, Set<Item> items) {
        buf.writeInt(items.size());
        for (Item item : items) {
            buf.writeInt(Item.getId(item));
        }
    }

    // CLIENT <- SERVER SYNCING
    public static void receiveFromServer(FriendlyByteBuf buf) {
        invalidate();
        int nodeCount = buf.readInt();
        ImmutableMap.Builder<OreNode, LootEntry> builder = ImmutableMap.builder();
        for (int i = 0; i < nodeCount; i++) {
            ResourceLocation name = new ResourceLocation(buf.readUtf());
            for (OreNode node : OreNodes.getNodeMap().values()) {
                if (node.name.equals(name)) {
                    builder.put(node, new LootEntry(
                            readItems(buf),
                            readItems(buf),
                            readItems(buf)
                    ));
                    break;
                }
            }
        }
        knownTables = builder.build();
    }

    private static Set<Item> readItems(FriendlyByteBuf buf) {
        Set<Item> items = new HashSet<>();
        int itemCount = buf.readInt();
        for (int i = 0; i < itemCount; i++) {
            items.add(Item.byId(buf.readInt()));
        }
        return items;
    }

    public record LootEntry(Set<Item> earth, Set<Item> common, Set<Item> rare){}
}
