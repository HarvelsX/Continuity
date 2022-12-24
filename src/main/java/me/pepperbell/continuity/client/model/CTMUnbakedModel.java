package me.pepperbell.continuity.client.model;

import com.google.common.collect.ImmutableList;
import me.pepperbell.continuity.api.client.QuadProcessor;
import me.pepperbell.continuity.client.resource.CTMLoadingContainer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class CTMUnbakedModel extends WrappingUnbakedModel {
	private final List<CTMLoadingContainer<?>> containerList;
	@Nullable
	private final List<CTMLoadingContainer<?>> multipassContainerList;

	public CTMUnbakedModel(UnbakedModel wrapped, List<CTMLoadingContainer<?>> containerList, @Nullable List<CTMLoadingContainer<?>> multipassContainerList) {
		super(wrapped);
		this.containerList = containerList;
		this.multipassContainerList = multipassContainerList;
	}

	protected static ImmutableList<QuadProcessor> toProcessorList(List<CTMLoadingContainer<?>> containerList, Function<SpriteIdentifier, Sprite> textureGetter) {
		ImmutableList.Builder<QuadProcessor> listBuilder = ImmutableList.builder();
		for (CTMLoadingContainer<?> container : containerList) {
			listBuilder.add(container.toProcessor(textureGetter));
		}
		return listBuilder.build();
	}

	@Override
	public @Nullable BakedModel wrapBaked(@Nullable BakedModel bakedWrapped, Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		if (bakedWrapped == null || bakedWrapped.isBuiltin()) {
			return bakedWrapped;
		}
		return new CTMBakedModel(bakedWrapped, toProcessorList(containerList, textureGetter), multipassContainerList == null ? null : toProcessorList(multipassContainerList, textureGetter));
	}
}
