package com.deepdrilling.nodes;

import com.deepdrilling.DrillMod;
import com.google.common.collect.ImmutableMap;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
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
    public static Map<Block, LootEntry> knownTables = ImmutableMap.of();
    public static boolean hasLoaded = false;

    public static void invalidate() {
        knownTables = ImmutableMap.of();
        hasLoaded = false;
    }

    // SERVER SIDE LOADING
    public static void parseOreNodes(LootDataManager manager, Map<Block, OreNode> nodes) {
        ImmutableMap.Builder<Block, LootEntry> builder = ImmutableMap.builder();
        for (Map.Entry<Block, OreNode> entry : nodes.entrySet()) {
            builder.put(entry.getKey(), parseOreNode(manager, entry.getValue()));
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
            for (LootPoolEntryContainer entry : getEntries(pool)) {
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
        items.add(getItem(lootItem));
    }

    private static void parseCompositeEntry(CompositeEntryBase compositeEntry, Set<Item> items) {
        for (LootPoolEntryContainer child : getChildren(compositeEntry)) {
            parseEntry(child, items);
        }
    }

    public static ResourceLocation PACKET_ID = DrillMod.id("node_loot");
    // SERVER -> CLIENT SYNCING
    public static void sendToPlayer(ServerPlayer player) {
        if (!hasLoaded) {
            DrillMod.LOGGER.info("Tried to send unloaded node loot to player! Generating now");
            parseOreNodes(player.server.getLootData(), OreNodes.getNodeMap());
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(knownTables.size());
        for (Map.Entry<Block, LootEntry> entry : knownTables.entrySet()) {
            buf.writeInt(BuiltInRegistries.BLOCK.getId(entry.getKey()));
            writeItems(buf, entry.getValue().earth);
            writeItems(buf, entry.getValue().common);
            writeItems(buf, entry.getValue().rare);
        }
        player.connection.send(new ClientboundCustomPayloadPacket(PACKET_ID, buf));
    }

    private static void writeItems(FriendlyByteBuf buf, Set<Item> items) {
        buf.writeInt(items.size());
        items.stream().map(Item::getId).sorted().forEach(buf::writeInt);
    }

    // CLIENT <- SERVER SYNCING
    public static void receiveFromServer(FriendlyByteBuf buf) {
        invalidate();
        int nodeCount = buf.readInt();
        ImmutableMap.Builder<Block, LootEntry> builder = ImmutableMap.builder();
        for (int i = 0; i < nodeCount; i++) {
            readNode(buf, builder);
        }
        knownTables = builder.build();
    }

    private static void readNode(FriendlyByteBuf buf, ImmutableMap.Builder<Block, LootEntry> builder) {
        Block block = BuiltInRegistries.BLOCK.byId(buf.readInt());
        LootEntry entry = new LootEntry(
                readItems(buf),
                readItems(buf),
                readItems(buf)
        );
        builder.put(block, entry);
    }

    private static Set<Item> readItems(FriendlyByteBuf buf) {
        Set<Item> items = new HashSet<>();
        int itemCount = buf.readInt();
        for (int i = 0; i < itemCount; i++) {
            items.add(Item.byId(buf.readInt()));
        }
        return items;
    }

    // this is only necessary because common mixins REFUSE to work for both dev and production environments at the same time
    @ExpectPlatform
    public static Iterable<LootPoolEntryContainer> getEntries(LootPool pool) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Item getItem(LootItem item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Iterable<LootPoolEntryContainer> getChildren(CompositeEntryBase compositeEntry) {
        throw new AssertionError();
    }

    public record LootEntry(Set<Item> earth, Set<Item> common, Set<Item> rare){}
}
