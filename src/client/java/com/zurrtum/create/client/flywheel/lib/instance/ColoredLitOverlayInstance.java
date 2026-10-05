package com.zurrtum.create.client.flywheel.lib.instance;

import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;

public abstract class ColoredLitOverlayInstance extends dev.engine_room.flywheel.lib.instance.ColoredLitOverlayInstance {
    private byte red = (byte) 0xFF;
    private byte green = (byte) 0xFF;
    private byte blue = (byte) 0xFF;
    private byte alpha = (byte) 0xFF;
    private int light;
    private int overlay = OverlayTexture.NO_OVERLAY;

    protected ColoredLitOverlayInstance(InstanceType<? extends ColoredLitOverlayInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    @Override
    public ColoredLitOverlayInstance colorArgb(int argb) {
        return color(ARGB.red(argb), ARGB.green(argb), ARGB.blue(argb), ARGB.alpha(argb));
    }

    @Override
    public ColoredLitOverlayInstance colorRgb(int rgb) {
        return color(ARGB.red(rgb), ARGB.green(rgb), ARGB.blue(rgb));
    }

    @Override
    public ColoredLitOverlayInstance color(int red, int green, int blue, int alpha) {
        return color((byte) red, (byte) green, (byte) blue, (byte) alpha);
    }

    @Override
    public ColoredLitOverlayInstance color(int red, int green, int blue) {
        return color((byte) red, (byte) green, (byte) blue);
    }

    @Override
    public ColoredLitOverlayInstance color(byte red, byte green, byte blue, byte alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        super.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public ColoredLitOverlayInstance color(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        super.color(red, green, blue);
        return this;
    }

    @Override
    public ColoredLitOverlayInstance color(float red, float green, float blue, float alpha) {
        return color((byte) (red * 255f), (byte) (green * 255f), (byte) (blue * 255f), (byte) (alpha * 255f));
    }

    @Override
    public ColoredLitOverlayInstance color(float red, float green, float blue) {
        return color((byte) (red * 255f), (byte) (green * 255f), (byte) (blue * 255f));
    }

    @Override
    public ColoredLitOverlayInstance light(int light) {
        this.light = light;
        super.light(light);
        return this;
    }

    public int light() {
        return light;
    }

    public ColoredLitOverlayInstance light(int blockLight, int skyLight) {
        return light(LightCoordsUtil.pack(blockLight, skyLight));
    }

    @Override
    public ColoredLitOverlayInstance overlay(int overlay) {
        this.overlay = overlay;
        super.overlay(overlay);
        return this;
    }

    /**
     * Rewrites all shadowed state; required after {@code setVisible(true)} or {@code stealInstance}, which re-seed the slot.
     */
    public ColoredLitOverlayInstance flush() {
        super.color(red, green, blue, alpha);
        super.light(light);
        super.overlay(overlay);
        return this;
    }
}
