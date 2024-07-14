package com.deepdrilling.forge;

// todo: what was this even doing? huh??
public class DrillTableProvider /*extends LootTableProvider*/ {
//    static {
//        DrillLoot.weightedList("iron", DrillLoot.LOOT_TYPE.EARTH,
//                new WeightedListBuilder<ItemLike>()
//                        .add(10, AllPaletteStoneTypes.CRIMSITE.baseBlock.get())
//                        .add(10, AllPaletteStoneTypes.TUFF.baseBlock.get())
//                        .add(10, AllPaletteStoneTypes.DEEPSLATE.baseBlock.get())
//                        .add(10, AllPaletteStoneTypes.LIMESTONE.baseBlock.get())
//                        .build(),
//                UniformGenerator.between(2, 3)
//        );
//    }
//    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> subProviders;
//    public DrillTableProvider(DataGenerator generator) {
//        super(generator);
//        subProviders = ImmutableList.of(Pair.of(DrillLoot::new, DrillLoot.PARAMS));
//    }
//
//    @Override
//    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
//        return subProviders;
//    }
//
//    @Override
//    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
//        map.forEach((id, table) -> LootTables.validate(validationtracker, id, table));
//    }
//
//    public static class DrillLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
//        public static final LootContextParamSet PARAMS = LootContextParamSets.EMPTY;
//        private static final Map<ResourceLocation, LootTable.Builder> map = Maps.newHashMap();
//        @Override
//        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
//            map.forEach((writer));
//        }
//
//        public static void weightedList(String source, LOOT_TYPE type, ArrayList<Pair<Integer, ItemLike>> weights, NumberProvider rolls) {
//            LootPool.Builder pool = LootPool.lootPool().setRolls(rolls);
//            for (Pair<Integer, ItemLike> entry : weights) {
//                pool.add(LootItem.lootTableItem(entry.getSecond()).setWeight(entry.getFirst()));
//            }
//            add(source, type, new LootTable.Builder().withPool(pool));
//        }
//
//        protected static void add(String source, LOOT_TYPE type, LootTable.Builder lootTableBuilder) {
//            map.put(DrillMod.id("nodes/" + source + "/" + type.toString()), lootTableBuilder);
//        }
//
//        enum LOOT_TYPE {
//            EARTH("earth"), COMMON("common"), RARE("rare");
//            private final String name;
//            LOOT_TYPE(final String name) {
//                this.name = name;
//            }
//            @Override
//            public String toString() {
//                return this.name;
//            }
//        }
//    }
}
