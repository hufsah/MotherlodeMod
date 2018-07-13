package motherlode.util;

import motherlode.Motherlode;
import motherlode.client.MotherlodeModels;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class InitUtil {
	public static void setup(Item item, String name, ModelCompound modelCompound) {
		item.setRegistryName(new ResourceLocation(Motherlode.MOD_ID, name));
		item.setUnlocalizedName(Motherlode.MOD_ID + "." + name);
		item.setCreativeTab(Motherlode.TAB);
		if (modelCompound != null) {
			setModel(modelCompound);
		}
	}

	public static void setup(Item item, String name) {
		setup(item, name, null);
	}

	public static void setup(Block block, String name, ModelCompound modelCompound) {
		block.setRegistryName(new ResourceLocation(Motherlode.MOD_ID, name));
		block.setUnlocalizedName(Motherlode.MOD_ID + "." + name);
		block.setCreativeTab(Motherlode.TAB);
		if (modelCompound != null) {
			setModel(modelCompound);
		}
	}

	public static void setup(Block block, String name) {
		setup(block, name, null);
	}

	public static void setModel(ModelCompound modelCompound) {
		MotherlodeModels.MODELS.add(modelCompound);
	}
}
