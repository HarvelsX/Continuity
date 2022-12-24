package me.pepperbell.continuity.client.model;

import java.util.function.Function;

import net.minecraft.client.render.model.*;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class EmissiveUnbakedModel extends WrappingUnbakedModel {
	public EmissiveUnbakedModel(UnbakedModel wrapped) {
		super(wrapped);
	}

	@Override
	public @Nullable BakedModel wrapBaked(@Nullable BakedModel bakedWrapped, Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		if (bakedWrapped == null || bakedWrapped.isBuiltin()) {
			return bakedWrapped;
		}
		return new EmissiveBakedModel(bakedWrapped);
	}
}
