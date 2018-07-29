package motherlode.recipe;

import java.util.ArrayList;
import java.util.List;

import motherlode.Motherlode;
import motherlode.api.MotherlodeAPI;
import motherlode.api.recipe.IIngredient;
import motherlode.api.recipe.IMotherlodeRecipe;
import motherlode.api.recipe.IRecipeTable;
import motherlode.recipe.ingredient.NBTIngredient;
import motherlode.recipe.ingredient.OreIngredient;
import motherlode.recipe.ingredient.SimpleIngredient;
import motherlode.recipe.table.RecipeTables;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber(modid = Motherlode.MOD_ID)
public class MotherlodeRecipes {

	@SubscribeEvent
	public static void addCraftingRecipes(RegistryEvent.Register<IMotherlodeRecipe> e) {
		addRecipe("oak_planks", new ItemStack(Blocks.PLANKS, 4, 0), new SimpleIngredient(Blocks.LOG, 1, 0));
		addRecipe("crafting_table", new ItemStack(Blocks.CRAFTING_TABLE), new SimpleIngredient(Blocks.PLANKS, 4, 0));
		addRecipe("sticks", new ItemStack(Items.STICK, 4), new OreIngredient("plankWood", 2));
		addRecipe("wooden_pressure_plate", new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), new OreIngredient("plankWood", 2));
		addRecipe("wooden_button", new ItemStack(Blocks.WOODEN_BUTTON), new OreIngredient("plankWood", 1));
		addRecipe("torch", new ItemStack(Blocks.TORCH, 4), new SimpleIngredient(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), new OreIngredient("stickWood"));
		addRecipe("stone_bricks", new ItemStack(Blocks.STONEBRICK, 4), new SimpleIngredient(Blocks.STONE, 4));
		addRecipe("stone_bricks_slab", new ItemStack(Blocks.STONE_SLAB, 2, 5), new SimpleIngredient(Blocks.STONEBRICK, 1));
		addRecipe("stone_pressure_plate", new ItemStack(Blocks.STONE_PRESSURE_PLATE), new SimpleIngredient(Blocks.STONE, 2));
		addRecipe("stone_button", new ItemStack(Blocks.STONE_BUTTON), new SimpleIngredient(Blocks.STONE, 1));

		addRecipe("test1", new ItemStack(Items.NETHER_STAR), RecipeTables.CRAFTING_TABLE, new OreIngredient("oreDiamond", 6));

		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("beef", "soup");
		addRecipe("test2", new ItemStack(Items.BEEF), RecipeTables.CRAFTING_TABLE, new NBTIngredient(Items.STICK, tag, false));
		addRecipe("test3", new ItemStack(Items.COOKED_BEEF), RecipeTables.CRAFTING_TABLE, new NBTIngredient(Items.STICK, tag, true));

		tag = new NBTTagCompound();
		tag.setString("chicken", "true");
		addRecipe("test4", new ItemStack(Items.CHICKEN), RecipeTables.STONE, new NBTIngredient(Items.MAGMA_CREAM, tag, false), new OreIngredient("blockEmerald", 80));
		addRecipe("test5", new ItemStack(Items.COOKED_CHICKEN), RecipeTables.STONE, new NBTIngredient(Items.MAGMA_CREAM, tag, true), new OreIngredient("blockEmerald", 80));
		addRecipe("test6", new ItemStack(Items.QUARTZ, 64), RecipeTables.STONE, new OreIngredient("blockDiamond", 128), new SimpleIngredient(Items.STICK, 630));
		addRecipe("test7", new ItemStack(Items.ACACIA_BOAT), new SimpleIngredient(Items.WATER_BUCKET, 4));
	}

	private static void addRecipe(String name, ItemStack output, IIngredient... input) {
		MotherlodeAPI.getRecipeRegistry().register(new MotherlodeRecipe(output, null, input).setRegistryName(Motherlode.MOD_ID, name));
	}

	private static void addRecipe(String name, ItemStack output, IRecipeTable table, IIngredient... input) {
		MotherlodeAPI.getRecipeRegistry().register(new MotherlodeRecipe(output, table, input).setRegistryName(Motherlode.MOD_ID, name));
	}

	public static List<IMotherlodeRecipe> getAllPossibleRecipes(NonNullList<ItemStack> stacks, EntityPlayer player, List<IRecipeTable> tables) {
		List<IMotherlodeRecipe> matches = new ArrayList<>();
		for (IMotherlodeRecipe recipe : MotherlodeAPI.getRecipeRegistry())
			if (recipe.matches(stacks, player, tables) != null) matches.add(recipe);
		return matches;
	}

	/**
	 * Gets all valid {@link IRecipeTable}s near a player.  Might be laggy?  Needs testing.
	 */
	public static List<IRecipeTable> getTables(EntityPlayer player) {
		BlockPos pos = player.getPosition();
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		List<IBlockState> states = new ArrayList<>(216);
		for (BlockPos p : BlockPos.getAllInBox(x - 3, y - 3, z - 3, x + 3, y + 3, z + 3)) {
			states.add(player.world.getBlockState(p));
		}

		List<IRecipeTable> tables = new ArrayList<>();
		for (IRecipeTable t : MotherlodeAPI.getTableRegistry()) {
			for (IBlockState state : states) {
				if (t.apply(state)) {
					tables.add(t);
					break;
				}
			}
		}
		return tables;
	}

}
