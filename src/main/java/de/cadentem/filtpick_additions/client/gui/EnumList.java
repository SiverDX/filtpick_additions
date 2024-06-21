package de.cadentem.filtpick_additions.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnumList extends ObjectSelectionList<EnumEntry> {
    private final SelectEnumScreen screen;

    public EnumList(final SelectEnumScreen screen, int width, int height, final Class<?> enumClass) {
        super(screen.getMinecraft(), width, height, 40, height - 37, 16);
        this.screen = screen;

        Object[] values = enumClass.getEnumConstants();

        if (values.length == 0) {
            throw new IllegalArgumentException("Class does not contain any enum values: [" + enumClass.getName() + "]");
        }

        for (Object value : values) {
            addEntry(new EnumEntry(this, (Enum<?>) value));
        }
    }

    @Override
    public void setSelected(@Nullable final EnumEntry selected) {
        super.setSelected(selected);
        screen.updateButtonValidity();
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public void select(final Enum<?> enumEntry) {
        if (children().isEmpty()) {
            return;
        }

        Optional<EnumEntry> selected = children().stream().filter(entry -> entry.getValue().equals(enumEntry)).findFirst();
        setSelected(selected.orElse(children().get(0)));
    }
}