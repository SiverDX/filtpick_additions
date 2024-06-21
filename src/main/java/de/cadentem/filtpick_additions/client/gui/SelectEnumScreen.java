package de.cadentem.filtpick_additions.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SelectEnumScreen extends Screen {
    private final Screen parent;
    private final Class<?> enumClass;
    private final Consumer<Enum<?>> action;
    private final Supplier<Enum<?>> initialValue;

    private EnumList list;
    private Button doneButton;

    protected SelectEnumScreen(final Screen parent, final Class<?> enumClass, final Consumer<Enum<?>> action, final Supplier<Enum<?>> initialValue) {
        super(Component.translatable("gui.filtpick_additions.config_screen_title"));
        this.parent = parent;
        this.enumClass = enumClass;
        this.action = action;
        this.initialValue = initialValue;
    }

    @Override
    protected void init() {
        getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        list = new EnumList(this, width, height, enumClass);
        addWidget(list);
        doneButton = addRenderableWidget(new Button(width / 2 - 155, height - 28, 150, 20, CommonComponents.GUI_DONE, button -> {
            getMinecraft().setScreen(parent);
            EnumEntry selected = list.getSelected();

            if (selected != null) {
                action.accept(selected.getValue());
            }
        }));
        addRenderableWidget(new Button(width / 2 + 5, height - 28, 150, 20, CommonComponents.GUI_CANCEL, button -> getMinecraft().setScreen(parent)));
        list.select(initialValue.get());
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(@NotNull final PoseStack pose, int mouseX, int mouseY, float partialTick) {
        renderDirtBackground(0);
        list.render(pose, mouseX, mouseY, partialTick);
        drawCenteredString(pose, font, title, width / 2, 8, ChatFormatting.WHITE.getColor());
        super.render(pose, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        getMinecraft().setScreen(parent);
    }

    public void updateButtonValidity() {
        doneButton.active = list.getSelected() != null;
    }
}
