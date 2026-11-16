package com.horrormod.client.renderer;

import com.horrormod.HorrorMod;
import com.horrormod.client.model.StalkerModel;
import com.horrormod.entity.StalkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StalkerRenderer extends MobRenderer<StalkerEntity, StalkerModel<StalkerEntity>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HorrorMod.MODID, "textures/entity/stalker.png");

    public StalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new StalkerModel<>(context.bakeLayer(StalkerModel.LAYER_LOCATION)), 0.6F);
        this.addLayer(new StalkerEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(StalkerEntity entity) {
        return TEXTURE;
    }
}
