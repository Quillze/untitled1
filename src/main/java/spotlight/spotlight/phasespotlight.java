package spotlight.spotlight;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PhaseSpotlight extends Item {

    public PhaseSpotlight(Settings settings) {
        super(settings);
    }

    // Override the onItemRightClick method to implement the sword and axe disabling ability
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Check if the player is in the second stage of the item's upgrade
        if (stack.getOrCreateSubTag("phase_spotlight").getBoolean("upgraded") == true) {
            // Disable the player's sword and axe for 5 seconds
            user.disableShield(true);
            user.getItemCooldownManager().set(this, 100);
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    // Override the onItemUse method to implement the phasing ability
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = player.getStackInHand(context.getHand());

        // Check if the player is in the first stage of the item's upgrade
        if (stack.getOrCreateSubTag("phase_spotlight").getBoolean("upgraded") == false) {
            // Teleport the player to the other side of the wall
            if (player.canClip()) {
                Direction direction = context.getSide();
                BlockPos offsetPos = pos.offset(direction);
                if (player.teleport(offsetPos.getX() + 0.5, offsetPos.getY() + 0.5, offsetPos.getZ() + 0.5, false)) {
                    stack.damage(1, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }

    // Override the onItemCrafted method to upgrade the item to the second stage
    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isClient && stack.getOrCreateSubTag("phase_spotlight").getBoolean("upgraded") == false) {
            // Upgrade the item to the second stage
            stack.getOrCreateSubTag("phase_spotlight").putBoolean("upgraded", true);
        }
    }
}
