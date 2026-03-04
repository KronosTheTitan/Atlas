package name.atlas.mixin.client;

import name.atlas.AtlasClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft_Keybinds {
    @Inject(method = "tick()V", at = @At("RETURN"))
    private void atlas$onTick(CallbackInfo callbackInfo){
        Profiler.get().push("atlas_keybinds");

        AtlasClient.handleKeybinds((Minecraft) (Object) this);

        Profiler.get().pop();
    }
}
//,
//		"MixinMinecraft_Keybinds"