package me.pepperbell.continuity.client.mixin;

import me.pepperbell.continuity.client.resource.OptifineAtlasSource;
import net.minecraft.client.texture.atlas.AtlasLoader;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AtlasLoader.class)
public class AtlasLoaderMixin {
	@Inject(method = "of", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void addCustomAtlasSource(ResourceManager resourceManager, Identifier id, CallbackInfoReturnable<AtlasLoader> cir, Identifier identifier, List<AtlasSource> list) {
		if (id.getPath().equals("blocks")) {
			list.add(new OptifineAtlasSource("ctm", "optifine/ctm/"));
		}
	}
}
