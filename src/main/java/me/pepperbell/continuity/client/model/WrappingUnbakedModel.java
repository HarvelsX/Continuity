package me.pepperbell.continuity.client.model;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

public abstract class WrappingUnbakedModel implements UnbakedModel {
	protected final UnbakedModel wrapped;
	protected boolean isBaking;

	public WrappingUnbakedModel(UnbakedModel wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return wrapped.getModelDependencies();
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> parents) {
		this.wrapped.setParents(parents);
	}

	@Override
	@Nullable
	public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		if (isBaking) {
			return null;
		}
		isBaking = true;

		BakedModel bakedWrapped = this.wrapped.bake(baker, textureGetter, rotationContainer, modelId);

		BakedModel baked = wrapBaked(bakedWrapped, baker, textureGetter, rotationContainer, modelId);
		isBaking = false;
		return baked;
	}

	@Nullable
	public abstract BakedModel wrapBaked(@Nullable BakedModel bakedWrapped, Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId);
}
