package me.pepperbell.continuity.client.mixin;

import me.pepperbell.continuity.client.resource.CTMPropertiesLoader;
import me.pepperbell.continuity.client.resource.EmissiveSuffixLoader;
import me.pepperbell.continuity.client.resource.ResourcePackUtil;
import me.pepperbell.continuity.client.util.biome.BiomeHolderManager;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin {
	@Inject(method = "reloadModels", at = @At("RETURN"), cancellable = true)
	private static void onStartReload(ResourceManager manager, Executor executor, CallbackInfoReturnable<CompletableFuture<Map<Identifier, JsonUnbakedModel>>> cir) {
		CompletableFuture<Map<Identifier, JsonUnbakedModel>> future = cir.getReturnValue();
		cir.setReturnValue(CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
			ResourcePackUtil.setup(manager);
			BiomeHolderManager.clearCache();

			EmissiveSuffixLoader.load(manager);
			CTMPropertiesLoader.clearAll();
			CTMPropertiesLoader.loadAll(manager);
		}, executor), future).thenApplyAsync(unused -> future.join(), executor));
	}

	@Inject(method = "reload", at = @At("RETURN"), cancellable = true)
	private void onEndReload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		cir.setReturnValue(cir.getReturnValue().thenAcceptAsync(unused -> {
			ResourcePackUtil.clear();
			BiomeHolderManager.refreshHolders();
		}, applyExecutor));
	}
}
