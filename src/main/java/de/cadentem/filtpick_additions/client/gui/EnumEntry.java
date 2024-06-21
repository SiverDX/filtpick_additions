package de.cadentem.filtpick_additions.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EnumEntry extends ObjectSelectionList.Entry<EnumEntry> {
    private final EnumList parent;
    private final Enum<?> value;

    public EnumEntry(final EnumList parent, final Enum<?> value) {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.literal(value.toString());
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(@NotNull final PoseStack pose, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
        GuiComponent.drawString(pose, parent.getMinecraft().font, value.toString(), left + 5, top + 2, ChatFormatting.WHITE.getColor());
    }

    public Enum<?> getValue() {
        return value;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            parent.setSelected(this);
            return true;
        } else {
            return false;
        }
    }
}
