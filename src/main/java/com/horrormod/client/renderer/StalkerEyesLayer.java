package com.horrormod.client.renderer;

import com.horrormod.HorrorMod;
import com.horrormod.client.model.StalkerModel;
import com.horrormod.entity.StalkerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

/** Ojos (y el interior de la boca) que brillan en la oscuridad. */
public class StalkerEyesLayer<T extends StalkerEntity> extends EyesLayer<T, StalkerModel<T>> {
    private static final RenderType EYES = RenderType.eyes(
            new ResourceLocation(HorrorMod.MODID, "textures/entity/stalker_eyes.png"));

    public StalkerEyesLayer(RenderLayerParent<T, StalkerModel<T>> parent) {
        super(parent);
    }

    @Override
    public RenderType renderType() {
        return EYES;
    }
}
