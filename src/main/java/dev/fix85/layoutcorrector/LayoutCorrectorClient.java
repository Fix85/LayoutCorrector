package dev.fix85.layoutcorrector;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import dev.fix85.layoutcorrector.gui.LayoutConfigScreen;

public class LayoutCorrectorClient implements ClientModInitializer {
    public static final String MOD_ID = "layoutcorrector";
    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        Config.load();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.layoutcorrector.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                KeyBinding.MISC_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new LayoutConfigScreen(null));
                }
            }
        });

        // Регистрируем события Fabric API для перехвата сообщений и команд
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (!Config.get().enabled) return true;

            // 1. Проверяем раскладку (например, ".ызфцт" -> "/spawn")
            if (Config.get().correctLayout && LayoutTranslator.shouldCorrectLayout(message)) {
                String corrected = LayoutTranslator.translateLayout(message);
                if (corrected.startsWith("/")) {
                    String cmdWithoutSlash = corrected.substring(1);
                    String finalCmd = LayoutTranslator.translateAliases(cmdWithoutSlash);
                    
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {
                        client.player.networkHandler.sendChatCommand(finalCmd);
                    }
                    return false; // Отменяем отправку исходного сообщения
                }
            }

            // 2. Проверяем русские алиасы (например, ".спавн" -> "/spawn")
            if (message.startsWith(".")) {
                String potentialCmd = message.substring(1);
                String aliasTranslated = LayoutTranslator.translateAliases(potentialCmd);
                if (!aliasTranslated.equals(potentialCmd)) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {
                        client.player.networkHandler.sendChatCommand(aliasTranslated);
                    }
                    return false; // Отменяем отправку исходного сообщения
                }
            }

            return true;
        });

        ClientSendMessageEvents.MODIFY_COMMAND.register(command -> {
            if (!Config.get().enabled) return command;

            // 1. Сначала проверяем алиасы (например, "спавн" -> "spawn")
            String aliasTranslated = LayoutTranslator.translateAliases(command);
            if (!aliasTranslated.equals(command)) {
                return aliasTranslated;
            }

            // 2. Затем проверяем раскладку самой команды (например, "ызфцт" -> "spawn")
            if (Config.get().correctLayout && LayoutTranslator.containsRussian(command)) {
                String corrected = LayoutTranslator.translateLayout(command);
                return LayoutTranslator.translateAliases(corrected);
            }

            return command;
        });
    }
}
