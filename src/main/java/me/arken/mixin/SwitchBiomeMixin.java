package me.arken.mixin;

import me.arken.events.SwitchBiomeCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.BiomeEffectSoundPlayer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeEffectSoundPlayer.class)
public class SwitchBiomeMixin {

    @Shadow @Final private ClientPlayerEntity player;
    @Shadow @Nullable private Biome activeBiome;
    @Shadow @Final private SoundManager soundManager;

    @Inject(method = "tick", at = @At(value="INVOKE", target="Lnet/minecraft/world/biome/Biome;getLoopSound()Ljava/util/Optional;"))
    public void onSwitch(CallbackInfo ci) {
        SwitchBiomeCallback.EVENT.invoker().onSwitch(this.player, this.activeBiome, this.soundManager);
    }
}
