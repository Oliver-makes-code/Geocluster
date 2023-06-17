package dev.sterner.geocluster;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class GeoclusterConfig extends MidnightConfig {

    @Entry
    public static boolean ONLY_VANILLA_ORES = true;
    @Entry
    public static boolean FORCE_DEEPSLATE_SAMPLE_CAVEGEN = false;
    @Entry
    public static boolean DISABLE_IN_AREA_MESSAGE = false;
    @Entry
    public static boolean REMOVE_VEINS = true;

    @Entry
    public static double CHUNK_SKIP_CHANCE = 0.95D;

    @Entry(min = 1, max = Integer.MAX_VALUE)
    public static int NUMBER_CLUSTERS_PER_CHUNK = 2;

    @Entry
    public static boolean DEBUG_WORLD_GEN = false;

    @Entry(min = 1, max = 256)
    public static int MAX_SAMPLES_PER_CHUNK = 6;

    @Entry
    public static List<String> DEFAULT_REPLACEMENT_MATERIALS = Lists.newArrayList("minecraft:stone", "minecraft:andesite", "minecraft:diorite", "minecraft:granite", "minecraft:netherrack", "minecraft:sandstone", "minecraft:deepslate", "minecraft:tuff", "minecraft:calcite", "minecraft:dripstone_block");

    @Entry
    public static List<String> PROSPECTORS_PICK_DETECTION_BLACKLIST = new ArrayList<>();

    @Entry(min = 1, max = 10)
    public static int PROSPECTORS_PICK_RANGE = 5;

    @Entry(min = 1, max = 10)
    public static int PROSPECTORS_PICK_DIAMETER = 5;

    @Entry
    public static boolean PROSPECTORS_POPUP_RIGHT = true;
}
