package com.deepdrilling.nodes;

public class OreNodeFormat {
    public String block;
    public DrillLoots loot_tables;
    public String getBlock() {
        return block;
    }
    public void setBlock(String block) {
        this.block = block;
    }
    public DrillLoots getLoot_tables() {
        return loot_tables;
    }
    public void setLoot_tables(DrillLoots loot_tables) {
        this.loot_tables = loot_tables;
    }

    public static class DrillLoots {
        public String earth = "";
        public float earthMod = 1;
        public String common = "";
        public float commonMod = 1;
        public String rare = "";
        public float rareMod = 1;
        public String getEarth() {
            return earth;
        }
        public void setEarth(String earth) {
            this.earth = earth;
        }
        public float getEarthMod() {
            return earthMod;
        }
        public void setEarthMod(float earthMod) {
            this.earthMod = earthMod;
        }
        public String getCommon() {
            return common;
        }
        public void setCommon(String common) {
            this.common = common;
        }
        public float getCommonMod() {
            return commonMod;
        }
        public void setCommonMod(float commonMod) {
            this.commonMod = commonMod;
        }
        public String getRare() {
            return rare;
        }
        public void setRare(String rare) {
            this.rare = rare;
        }
        public float getRareMod() {
            return rareMod;
        }
        public void setRareMod(float rareMod) {
            this.rareMod = rareMod;
        }
    }
}
