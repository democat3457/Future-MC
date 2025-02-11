package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.Recipes;
import thedarkcolour.futuremc.recipe.SimpleRecipe;
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes;
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes;

import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.applyAction;
import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.toItemStack;

public class SmokerAndBlastFurnace {
    @ZenRegister
    @ZenClass("mods.futuremc.Smoker")
    public static final class Smoker {
        @ZenMethod
        public static void addRecipe(IItemStack input, IItemStack output) {
            applyAction(new AddRecipe(SmokerRecipes.INSTANCE, input, output));
        }

        @ZenMethod
        public static void addRecipe(IOreDictEntry input, IItemStack output) {
            for (IItemStack i : input.getItems()) {
                addRecipe(i, output);
            }
        }

        @ZenMethod
        public static void removeRecipe(IItemStack input) {
            applyAction(new RemoveRecipeForInput(SmokerRecipes.INSTANCE, input));
        }

        // Alias for more clarity if people prefer
        @ZenMethod
        public static void removeRecipeForInput(IItemStack input) {
            removeRecipe(input);
        }

        @ZenMethod
        public static void removeRecipeForOutput(IItemStack output) {
            applyAction(new RemoveRecipeForOutput(SmokerRecipes.INSTANCE, output));
        }

        @ZenMethod
        public static void clearRecipes() {
            SmokerRecipes.INSTANCE.clear();
        }
    }

    @ZenRegister
    @ZenClass("mods.futuremc.BlastFurnace")
    public static final class BlastFurnace {
        @ZenMethod
        public static void addRecipe(IItemStack input, IItemStack output) {
            applyAction(new AddRecipe(BlastFurnaceRecipes.INSTANCE, input, output));
        }

        @ZenMethod
        public static void addRecipe(IOreDictEntry input, IItemStack output) {
            for (IItemStack i : input.getItems()) {
                addRecipe(i, output);
            }
        }

        @ZenMethod
        public static void removeRecipe(IItemStack input) {
            applyAction(new RemoveRecipeForInput(BlastFurnaceRecipes.INSTANCE, input));
        }

        // Alias for more clarity if people prefer
        @ZenMethod
        public static void removeRecipeForInput(IItemStack input) {
            removeRecipe(input);
        }

        @ZenMethod
        public static void removeRecipeForOutput(IItemStack output) {
            applyAction(new RemoveRecipeForOutput(BlastFurnaceRecipes.INSTANCE, output));
        }

        @ZenMethod
        public static void clearRecipes() {
            BlastFurnaceRecipes.INSTANCE.clear();
        }
    }

    private static final class AddRecipe implements IAction {
        private final Recipes<SimpleRecipe> recipes;
        private final ItemStack input, output;

        private AddRecipe(Recipes<SimpleRecipe> recipes, IItemStack input, IItemStack output) {
            this.recipes = recipes;
            this.input   = toItemStack(input);
            this.output  = toItemStack(output);
        }

        @Override
        public void apply() {
            recipes.addRecipe(input, output);
        }

        @Override
        public String describe() {
            return recipes.getClass().getSimpleName() + ": Adding recipe (input: " + input.toString() + ") -> (output: " + output.toString() + ")";
        }
    }

    private static abstract class RemoveRecipeFor implements IAction {
        protected final Recipes<SimpleRecipe> recipes;
        protected final ItemStack item;

        private RemoveRecipeFor(Recipes<SimpleRecipe> recipes, IItemStack item) {
            this.recipes = recipes;
            this.item = toItemStack(item);
        }

        @Override
        public String describe() {
            return recipes.getClass().getSimpleName() + ": " + getClass().getSimpleName() + " " + item.getItem().getRegistryName();
        }
    }

    private static final class RemoveRecipeForInput extends RemoveRecipeFor {
        private RemoveRecipeForInput(Recipes<SimpleRecipe> recipes, IItemStack input) {
            super(recipes, input);
        }

        @Override
        public void apply() {
            recipes.removeRecipeForInput(item);
        }
    }

    private static final class RemoveRecipeForOutput extends RemoveRecipeFor {
        private RemoveRecipeForOutput(Recipes<SimpleRecipe> recipes, IItemStack input) {
            super(recipes, input);
        }

        @Override
        public void apply() {
            recipes.removeRecipeForInput(item);
        }
    }
}
