package me.pepperbell.continuity.client.mixin;

import me.pepperbell.continuity.client.mixinterface.SpriteExtension;
import me.pepperbell.continuity.client.resource.EmissiveSuffixLoader;
import me.pepperbell.continuity.client.resource.ModelWrappingHandler;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mixin(SpriteLoader.class)
public class SpriteLoaderMixin {
	@Inject(method = "collectStitchedSprites", at = @At("RETURN"))
	private void assignEmissiveSprites(CallbackInfoReturnable<Map<Identifier, Sprite>> cir) {
		String emissiveSuffix = EmissiveSuffixLoader.getEmissiveSuffix();
		if (emissiveSuffix == null) return;

		Map<Identifier, Sprite> map = cir.getReturnValue();
		ModelWrappingHandler.addEmissiveAssigns(
				map.keySet().parallelStream().filter(id -> id.getPath().endsWith(emissiveSuffix)).map(id -> {
					Identifier assignId = id.withPath(path -> path.substring(0, path.length() - emissiveSuffix.length()));
					Sprite assignSprite = map.get(assignId);
					if (assignSprite != null) {
						((SpriteExtension) assignSprite).setEmissiveSprite(map.get(id));
						return Map.entry(assignId, id);
					}
					return null;
				}).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
		);
	}
}
