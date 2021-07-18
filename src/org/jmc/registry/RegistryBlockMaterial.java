package org.jmc.registry;

import java.util.HashMap;

import org.jmc.BlockMaterial;
import org.jmc.Blockstate;
import org.jmc.registry.BlockstateEntry.ModelInfo;
import org.jmc.registry.BlockstateEntry.ModelListWeighted;
import org.jmc.registry.ModelEntry.RegistryModel;

public class RegistryBlockMaterial extends BlockMaterial {
	private BlockstateEntry bse;
	
	public RegistryBlockMaterial(BlockstateEntry bse) {
		super();
		this.bse = bse;
	}
	
	@Override
	public NamespaceID[] get(Blockstate state, int biomeValue) {
		if (isEmpty()) {
			NamespaceID[] mats = new NamespaceID[1];
			HashMap<String, Integer> textureCount = new HashMap<>();
			int highestUses = 0;
			for (ModelListWeighted modelList : bse.getModelsFor(state)) {
				for (ModelInfo modelInfo : modelList.models) {
					ModelEntry modelEntry = Registries.getModel(modelInfo.id);
					RegistryModel model = modelEntry.generateModel();
					for (String texture : model.textures.values()) {
						textureCount.put(texture, textureCount.getOrDefault(texture, 0) + 1);
						if (textureCount.get(texture) > highestUses) {
							mats[0] = NamespaceID.fromString(texture);
							highestUses++;
						}
					}
				}
			}
			if (mats[0] == null)
				mats[0] = Registries.UNKNOWN_TEX_ID;
			return mats;
		} else {
			return super.get(state, biomeValue);
		}
	}
	
}
