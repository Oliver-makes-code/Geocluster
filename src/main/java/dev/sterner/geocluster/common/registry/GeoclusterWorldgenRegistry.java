package dev.sterner.geocluster.common.registry;

import com.google.common.collect.Lists;
import dev.sterner.geocluster.Geocluster;
import dev.sterner.geocluster.common.world.feature.DepositFeature;
import dev.sterner.geocluster.mixin.BiomeModificationContextImplMixin;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;


public interface GeoclusterWorldgenRegistry {
    List<PlacementModifier> placement = Lists.newArrayList(HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(320)));

    Feature<DefaultFeatureConfig> DEPOSIT_FEATURE = registerFeature("deposits", new DepositFeature(DefaultFeatureConfig.CODEC));
    RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> CONFIGURED_DEPOSIT_FEATURE = registerConfigured("deposits_configured", DEPOSIT_FEATURE);
    RegistryEntry<PlacedFeature> PLACED_DEPOSIT_FEATURE = PlacedFeatures.register("geocluster:deposits_placed", CONFIGURED_DEPOSIT_FEATURE, placement);

    /* Replaced with ChunkGeneratorSettingsMixin
    Feature<DefaultFeatureConfig> REMOVE_VEINS_FEATURE = registerFeature("remove_veins", new RemoveVeinsFeature(DefaultFeatureConfig.CODEC));
    RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> CONFIGURED_REMOVE_VEINS_FEATURE = registerConfigured("remove_veins_configured", REMOVE_VEINS_FEATURE);
    RegistryEntry<PlacedFeature> PLACED_REMOVE_VEINS_FEATURE = PlacedFeatures.register("geocluster:remove_veins_placed", CONFIGURED_REMOVE_VEINS_FEATURE, placement);
     */

    static void init() {

        BiomeModification modifications = BiomeModifications.create(Geocluster.id("worldgen"));

        modifications.add(ModificationPhase.REMOVALS, BiomeSelectors.all(), ctx -> {
            Iterable<RegistryEntry<PlacedFeature>> registryEntries = getPlacedFeaturesByTag(ctx, GeoclusterTagRegistry.ORES_TO_REMOVE);
            for (RegistryEntry<PlacedFeature> placedFeatureHolder : registryEntries) {
                if (placedFeatureHolder.getKey().isPresent()) {
                    ctx.getGenerationSettings().removeFeature(placedFeatureHolder.getKey().get());
                }
            }
        });

        modifications.add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), ctx -> {
            ctx.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, PLACED_DEPOSIT_FEATURE.getKey().get());
            //ctx.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, PLACED_REMOVE_VEINS_FEATURE.getKey().get());
        });

    }

    private static Iterable<RegistryEntry<PlacedFeature>> getPlacedFeaturesByTag(BiomeModificationContext ctx, TagKey<PlacedFeature> placedFeatureTagKey) {
        DynamicRegistryManager dynamicRegistryManager = ((BiomeModificationContextImplMixin) ctx).getRegistries();
        Registry<PlacedFeature> placedFeatureRegistry = dynamicRegistryManager.get(RegistryKeys.PLACED_FEATURE);
        return placedFeatureRegistry.iterateEntries(placedFeatureTagKey);
    }

    static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String id, F feature) {
        return Registry.register(Registries.FEATURE, id, feature);
    }

    static RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> registerConfigured(String id, Feature<DefaultFeatureConfig> feature) {
        return ConfiguredFeatures.register(Geocluster.id(id).toString(), feature);
    }

    static RegistryKey<PlacedFeature> placedFeature(Identifier id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, id);
    }

}
