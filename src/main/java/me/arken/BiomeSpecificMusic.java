package me.arken;

import me.arken.events.SwitchBiomeCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class BiomeSpecificMusic implements ModInitializer {

    @Override
    public void onInitialize() {
        HashMap sounds = registerSounds();

        SwitchBiomeCallback.EVENT.register((player, biome, soundManager) -> {
            player.sendChatMessage("New Biome: " + biome.getCategory());
            MinecraftClient client = MinecraftClient.getInstance();

            SoundEvent sound = (SoundEvent) sounds.get(biome.getCategory());
            client.getMusicTracker().stop();
            if(sound != null) {
                MusicSound musicSound = new MusicSound(sound, 20, 20, true);

                client.getMusicTracker().play(musicSound);
            }

            return ActionResult.PASS;
        });
    }

    private HashMap registerSounds() {
        File registryDir = FabricLoader.getInstance().getModContainer("biosm").get().getPath("assets/biosm/sounds").toFile();
        File soundDir = new File(FabricLoader.getInstance().getGameDir().toFile().getPath()+"//biosm");
        if(!soundDir.exists()) soundDir.mkdirs();
        HashMap<Biome.Category, SoundEvent> sounds = new HashMap<>();

        moveFiles(registryDir, soundDir);

        //Register sounds
        for(File file : soundDir.listFiles()) {
            String fileName = file.getName().replaceAll(".ogg", "");

            Identifier id = new Identifier("biosm:"+fileName);
            SoundEvent event = new SoundEvent(id);

            sounds.put(Biome.Category.valueOf(fileName.toUpperCase()), event);
            Registry.register(Registry.SOUND_EVENT, id, event);
        }
        return sounds;
    }

    private void moveFiles(File registryDir, File soundDir) {
        //Delete old files in registry
        for(File file : registryDir.listFiles()) {
            file.delete();
        }
        for(File file : soundDir.listFiles()) {
            //Copy files to sounds folder
            try {
                Files.copy(file.toPath(), (new File(registryDir.getPath() + "//" + file.getName())).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
