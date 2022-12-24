package me.pepperbell.continuity.client.mixin;

import me.pepperbell.continuity.client.resource.CTMPropertiesLoader;
import me.pepperbell.continuity.client.resource.ModelWrappingHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> unbakedModels;
	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> modelsToBake;

	@Unique
	private BlockState currentBlockState;

	@Inject(method = "method_4716(Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
	private void onAddBlockStateModel(BlockState state, CallbackInfo ci) {
		currentBlockState = state;
	}

	@Inject(method = "addModel(Lnet/minecraft/client/util/ModelIdentifier;)V", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void afterAddModel(ModelIdentifier id, CallbackInfo ci, UnbakedModel model) {
		if (currentBlockState != null) {
			ModelWrappingHandler.onAddBlockStateModel(id, currentBlockState);
			currentBlockState = null;
		}
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", shift = At.Shift.AFTER))
	private void onFinishAddingModels(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
		ModelWrappingHandler.wrapCTMModels(unbakedModels, modelsToBake);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onTailInit(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
		ModelWrappingHandler.wrapEmissiveModels(unbakedModels, modelsToBake);

		CTMPropertiesLoader.clearAll();
	}
}
