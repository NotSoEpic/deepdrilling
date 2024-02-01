package com.deepdrilling.forge;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;

public class WeightedListBuilder<T> {
    private final ArrayList<Pair<Integer, T>> weights = new ArrayList<>();
    WeightedListBuilder<T> add(int weight, T obj) {
        weights.add(new Pair<>(weight, obj));
        return this;
    }

    ArrayList<Pair<Integer, T>> build() {
        return weights;
    }
}
