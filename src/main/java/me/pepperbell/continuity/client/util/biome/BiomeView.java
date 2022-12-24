package me.pepperbell.continuity.client.util.biome;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface BiomeView {
	RegistryEntry<Biome> getBiome(BlockPos pos);
}
