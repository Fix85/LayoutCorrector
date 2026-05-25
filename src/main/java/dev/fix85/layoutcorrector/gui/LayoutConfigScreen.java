package dev.fix85.layoutcorrector.gui;

import dev.fix85.layoutcorrector.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayoutConfigScreen extends Screen {
    private final Screen parent;

    private TextFieldWidget aliasField;
    private TextFieldWidget targetField;
    private TextFieldWidget prefixField;

    private ButtonWidget enabledBtn;
    private ButtonWidget correctLayoutBtn;

    private final List<Map.Entry<String, String>> aliasesToDraw = new ArrayList<>();
    private int extraAliasesCount = 0;

    public LayoutConfigScreen(Screen parent) {
        super(Text.translatable("layoutcorrector.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;

        int leftX = cx - 180;
        int colW = 110;

        enabledBtn = ButtonWidget.builder(buildOnOff("layoutcorrector.gui.enabled", Config.get().enabled),
                b -> {
                    Config.get().enabled = !Config.get().enabled;
                    refresh();
                })
                .dimensions(leftX, 55, colW, 20)
                .tooltip(Tooltip.of(Text.translatable("layoutcorrector.gui.tooltip.enabled")))
                .build();
        addDrawableChild(enabledBtn);

        correctLayoutBtn = ButtonWidget.builder(buildOnOff("layoutcorrector.gui.correct_layout", Config.get().correctLayout),
                b -> {
                    Config.get().correctLayout = !Config.get().correctLayout;
                    refresh();
                })
                .dimensions(leftX, 80, colW, 20)
                .tooltip(Tooltip.of(Text.translatable("layoutcorrector.gui.tooltip.correct_layout")))
                .build();
        addDrawableChild(correctLayoutBtn);

        prefixField = new TextFieldWidget(this.textRenderer, leftX, 105, colW, 20,
                Text.literal("chat prefix"));
        prefixField.setPlaceholder(Text.literal("."));
        prefixField.setMaxLength(4);
        prefixField.setText(Config.get().chatPrefix);
        prefixField.setChangedListener(text -> {
            if (!text.isEmpty()) {
                Config.get().chatPrefix = text;
                Config.save();
            }
        });
        addDrawableChild(prefixField);

        int midX = cx - 55;
        int midW = 110;

        aliasField = new TextFieldWidget(this.textRenderer, midX, 55, midW, 20,
                Text.literal("Russian command"));
        aliasField.setPlaceholder(Text.translatable("layoutcorrector.gui.placeholder_alias"));
        aliasField.setMaxLength(32);
        addDrawableChild(aliasField);

        targetField = new TextFieldWidget(this.textRenderer, midX, 80, midW, 20,
                Text.literal("English command"));
        targetField.setPlaceholder(Text.translatable("layoutcorrector.gui.placeholder_target"));
        targetField.setMaxLength(32);
        addDrawableChild(targetField);

        addDrawableChild(ButtonWidget.builder(Text.translatable("layoutcorrector.gui.add_update"), b -> {
            String alias = aliasField.getText().trim().toLowerCase().replace("/", "");
            String target = targetField.getText().trim().toLowerCase().replace("/", "");
            if (!alias.isEmpty() && !target.isEmpty()) {
                Config.get().aliases.put(alias, target);
                Config.save();
            }
            aliasField.setText("");
            targetField.setText("");
            refresh();
        }).dimensions(midX, 105, midW, 20)
                .tooltip(Tooltip.of(Text.translatable("layoutcorrector.gui.tooltip.add_remove_btn")))
                .build());

        int rightX = cx + 70;
        int rightW = 115;

        aliasesToDraw.clear();
        extraAliasesCount = 0;
        int customY = 55;
        int displayedCount = 0;

        for (Map.Entry<String, String> entry : Config.get().aliases.entrySet()) {
            if (displayedCount < 5) {
                aliasesToDraw.add(entry);
                String labelText = "✖ " + entry.getKey() + " ➔ " + entry.getValue();
                if (labelText.length() > 18) {
                    labelText = labelText.substring(0, 16) + "..";
                }
                final String keyToRemove = entry.getKey();
                ButtonWidget removeBtn = ButtonWidget.builder(Text.literal(labelText), btn -> {
                    Config.get().aliases.remove(keyToRemove);
                    Config.save();
                    refresh();
                }).dimensions(rightX, customY, rightW, 18).build();
                addDrawableChild(removeBtn);
                customY += 20;
                displayedCount++;
            } else {
                extraAliasesCount++;
            }
        }

        addDrawableChild(ButtonWidget.builder(Text.translatable("layoutcorrector.gui.clear_list"), b -> {
            Config.get().aliases.clear();
            Config.save();
            refresh();
        }).dimensions(rightX, 165, rightW, 20)
                .tooltip(Tooltip.of(Text.translatable("layoutcorrector.gui.tooltip.clear_list")))
                .build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("layoutcorrector.gui.reset"), b -> {
            Config.resetToDefaults();
            refresh();
        }).dimensions(cx - 110, 200, 105, 20)
                .tooltip(Tooltip.of(Text.translatable("layoutcorrector.gui.tooltip.reset")))
                .build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("layoutcorrector.gui.done"), b -> close())
                .dimensions(cx + 5, 200, 105, 20)
                .tooltip(Tooltip.of(Text.translatable("layoutcorrector.gui.tooltip.done")))
                .build());
    }

    private Text buildOnOff(String key, boolean value) {
        String state = value ? "§aON" : "§cOFF";
        return Text.translatable(key).append(": " + state);
    }

    private void refresh() {
        Config.save();
        clearAndInit();
    }

    @Override
    public void close() {
        Config.save();
        if (this.client != null) this.client.setScreen(parent);
    }

    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int leftX = cx - 180;

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, 12, 0xFFFFFF);

        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("layoutcorrector.gui.general"), cx - 125, 42, 0xAAAAAA);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("layoutcorrector.gui.alias_creator"), cx, 42, 0xAAAAAA);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("layoutcorrector.gui.aliases_list"), cx + 127, 42, 0xAAAAAA);

        context.drawTextWithShadow(this.textRenderer,
                Text.translatable("layoutcorrector.gui.chat_prefix"),
                leftX, 96, 0xAAAAAA);

        if (extraAliasesCount > 0) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.translatable("layoutcorrector.gui.more_items", String.valueOf(extraAliasesCount)),
                    cx + 127, 155, 0x888888);
        }
    }
}
