package name.atlas;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class AtlasClient implements ClientModInitializer {
	/// Keymap
	private static final KeyMapping.Category atlasKeybindCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("atlas", "keybinds"));

	/// Keybinds
	private static final KeyMapping checkCompassKeybind = KeyBindingHelper.registerKeyBinding(new KeyMapping("atlas.keybind.checkCompass", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, atlasKeybindCategory));
	private static final KeyMapping checkDistanceSpyglassKeybind = KeyBindingHelper.registerKeyBinding(new KeyMapping("atlas.keybind.checkDistanceSpyglass", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_T, atlasKeybindCategory));

	/// Runs every tick to handle the keybinds.
	public static void handleKeybinds(Minecraft minecraft) {
		handleCompass(minecraft);
		handleSpyglass(minecraft);
	}

	/// This handles the compass keybind and logic.
	private static void handleCompass(Minecraft minecraft){
		try {
			if(!checkCompassKeybind.consumeClick())
				return;
			if(minecraft.player == null)
				return;
			if(!minecraft.player.isCrouching())
				return;
			if(minecraft.player.getMainHandItem().getItem() != Items.COMPASS && minecraft.player.getOffhandItem().getItem() != Items.COMPASS)
				return;

			float rotation = minecraft.player.getYHeadRot() % 360;

			rotation = Math.round(rotation);

			if(rotation < 0)
				rotation += 360;

			rotation += 180;

			if(rotation >= 360)
				rotation -= 360;



			String direction = " ERROR";

			if(rotation >= 45 && rotation < 135)
				direction = " (East)";
			if(rotation >= 135 && rotation < 225)
				direction = " (South)";
			if(rotation >= 225 && rotation < 315)
				direction = " (West)";
			if(rotation >= 315 || rotation < 45)
				direction = " (North)";

			minecraft.player.displayClientMessage(
					Component.translatable(String.valueOf((int)rotation) + direction),
					false);
		}
		catch (Exception ignored) {

		}
	}

	/// This handles the spyglass keybind and logic.
	private static void handleSpyglass(Minecraft minecraft){
		try {
			if(!checkDistanceSpyglassKeybind.consumeClick())
				return;
			if(minecraft.player == null)
				return;
			if(minecraft.player.getMainHandItem().getItem() != Items.SPYGLASS)
				return;

			BlockPos pos = TargetUtil.getLookedAtBlock();
			if(pos == null){
				minecraft.player.displayClientMessage(
						Component.translatable("---- M"),
						false);

				return;
			}

			Vec3 tPos = new Vec3(pos.getX(),pos.getY(),pos.getZ());
			Vec3 ePos = minecraft.player.getEyePosition();

			Vec3 vec = new Vec3(tPos.x - ePos.x, tPos.y - ePos.y, tPos.z - ePos.z);

			float distance = (float) Math.sqrt((vec.x * vec.x) + (vec.z * vec.z));
			distance = (float) Math.sqrt((distance * distance) + (vec.y * vec.y));
			distance -= 1;
			int i_distance = (int)Math.round(distance);

			minecraft.player.displayClientMessage(
					Component.translatable(String.valueOf(i_distance) + " M"),
					false);

			// TODO: add logic to find the distance to the block the player is looking at.
		}
		catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}