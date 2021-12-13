package me.arken;

import me.arken.events.SwitchBiomeCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BiomeSpecificMusic implements ModInitializer {

    @Override
    public void onInitialize() {
        Map sounds = registerSounds();

        SwitchBiomeCallback.EVENT.register((player, biome) -> {
            player.sendChatMessage("New Biome: " + biome.getCategory());

            SoundEvent sound = (SoundEvent) sounds.get(biome.getCategory());
            if(sound != null) {
                player.playSound(sound, SoundCategory.MUSIC, 1f, 1f);
            }

            return ActionResult.PASS;
        });
    }

    private Map registerSounds() {
        File soundDir = FabricLoader.getInstance().getModContainer("biosm").get().getPath("assets/biosm/sounds").toFile();;
        Map<Biome.Category, SoundEvent> sounds = new HashMap<>();
            for(File file : soundDir.listFiles()) {
                String fileName = file.getName().replaceAll(".ogg", "");

                Identifier id = new Identifier("biosm:"+fileName);
                SoundEvent event = new SoundEvent(id);

                sounds.put(Biome.Category.valueOf(fileName.toUpperCase()), event);
                Registry.register(Registry.SOUND_EVENT, id, event);
            }
        return sounds;
    }
}
