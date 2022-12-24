package me.pepperbell.continuity.client.resource;

import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceType;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;

public class OptifineAtlasSource implements AtlasSource {
	private final String source;
	private final String prefix;

	public OptifineAtlasSource(String source, String prefix) {
		this.source = source;
		this.prefix = prefix;
	}

	@Override
	public void load(ResourceManager resourceManager, SpriteRegions regions) {
		ResourceFinder resourceFinder = new ResourceFinder("optifine/" + this.source, ".png");
		resourceFinder.findResources(resourceManager).forEach((identifier, resource) ->
				regions.add(resourceFinder.toResourceId(identifier).withPrefixedPath(this.prefix), resource));
	}

	@Override
	public AtlasSourceType getType() {
		return null;
	}
}
