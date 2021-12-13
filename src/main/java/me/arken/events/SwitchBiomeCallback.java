package me.arken.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.biome.Biome;

public interface SwitchBiomeCallback {

    Event<SwitchBiomeCallback> EVENT = EventFactory.createArrayBacked(SwitchBiomeCallback.class, (listeners) -> (player, biome) -> {
        for (SwitchBiomeCallback listener : listeners) {
            ActionResult result = listener.onSwitch(player, biome);

            if(result != ActionResult.PASS) return result;
        }

        return ActionResult.PASS;
    });

    ActionResult onSwitch(ClientPlayerEntity player, Biome biome);
}
