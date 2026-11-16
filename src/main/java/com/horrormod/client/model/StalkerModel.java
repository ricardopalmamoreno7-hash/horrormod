package com.horrormod.client.model;

import com.horrormod.HorrorMod;
import com.horrormod.entity.StalkerEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Modelo 3D del Acechador (textura 128x128).
 *
 * Jerarquia:
 *   root
 *    |- body (pivote en la cadera, se inclina hacia adelante)
 *    |    |- head -> jaw, right_horn, left_horn
 *    |    |- right_arm -> right_hand
 *    |    |- left_arm  -> left_hand
 *    |    |- spine
 *    |- right_leg
 *    |- left_leg
 */
public class StalkerModel<T extends StalkerEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(HorrorMod.MODID, "stalker"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public StalkerModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Torso delgado y alto. Pivote en la cadera (y = 6).
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 18.0F, 4.0F),
                PartPose.offset(0.0F, 6.0F, 0.0F));

        // Columna con pua en la espalda
        body.addOrReplaceChild("spine",
                CubeListBuilder.create()
                        .texOffs(60, 0).addBox(-1.0F, -17.0F, 2.0F, 2.0F, 14.0F, 2.0F),
                PartPose.ZERO);

        // Cabeza alargada, adelantada como si estuviera encorvado
        PartDefinition head = body.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offset(0.0F, -18.0F, -1.0F));

        // Mandibula con bisagra en la parte de atras
        head.addOrReplaceChild("jaw",
                CubeListBuilder.create()
                        .texOffs(30, 0).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, -1.0F, 2.0F));

        // Cuernos
        head.addOrReplaceChild("right_horn",
                CubeListBuilder.create()
                        .texOffs(70, 0).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, -8.0F, 0.0F, -0.35F, 0.0F, -0.45F));
        head.addOrReplaceChild("left_horn",
                CubeListBuilder.create()
                        .texOffs(76, 0).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, -8.0F, 0.0F, -0.35F, 0.0F, 0.45F));

        // Brazos larguisimos con manos y garras
        PartDefinition rightArm = body.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(0, 40).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 26.0F, 3.0F),
                PartPose.offset(-5.5F, -16.0F, 0.0F));
        rightArm.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                        .texOffs(28, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F)
                        .texOffs(84, 0)
                        .addBox(-1.8F, 5.0F, -0.5F, 1.0F, 5.0F, 1.0F)
                        .addBox(-0.5F, 5.0F, -0.5F, 1.0F, 5.0F, 1.0F)
                        .addBox(0.8F, 5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 26.0F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(14, 40).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 26.0F, 3.0F),
                PartPose.offset(5.5F, -16.0F, 0.0F));
        leftArm.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                        .texOffs(46, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F)
                        .texOffs(84, 0)
                        .addBox(-1.8F, 5.0F, -0.5F, 1.0F, 5.0F, 1.0F)
                        .addBox(-0.5F, 5.0F, -0.5F, 1.0F, 5.0F, 1.0F)
                        .addBox(0.8F, 5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 26.0F, 0.0F));

        // Piernas largas y flacas
        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 72).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 18.0F, 3.0F),
                PartPose.offset(-2.5F, 6.0F, 0.0F));
        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(14, 72).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 18.0F, 3.0F),
                PartPose.offset(2.5F, 6.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        boolean aggressive = entity.isAggressive();
        float breathe = Mth.sin(ageInTicks * 0.07F);
        float swing = limbSwing * 0.6662F;

        // Postura encorvada
        this.body.xRot = 0.30F + breathe * 0.02F + (aggressive ? 0.15F : 0.0F);

        // Cabeza sigue la mirada
        this.head.yRot = Mth.clamp(netHeadYaw, -60.0F, 60.0F) * Mth.DEG_TO_RAD;
        this.head.xRot = 0.35F + headPitch * Mth.DEG_TO_RAD;

        // Mandibula: abierta, temblando; mas abierta al atacar
        this.jaw.xRot = (aggressive ? 0.95F : 0.25F) + Mth.sin(ageInTicks * 0.2F) * 0.06F;

        // Piernas
        this.rightLeg.xRot = Mth.cos(swing) * 1.1F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(swing + Mth.PI) * 1.1F * limbSwingAmount;

        if (aggressive) {
            // Brazos al frente, garras listas
            float tremble = Mth.sin(ageInTicks * 0.3F) * 0.08F;
            this.rightArm.xRot = -1.35F + tremble;
            this.leftArm.xRot = -1.35F - tremble;
            this.rightArm.zRot = 0.10F;
            this.leftArm.zRot = -0.10F;
        } else {
            // Brazos colgando y arrastrandose; compensan la inclinacion del torso
            float hang = -this.body.xRot * 0.7F;
            this.rightArm.xRot = hang + Mth.cos(swing + Mth.PI) * 0.7F * limbSwingAmount + breathe * 0.03F;
            this.leftArm.xRot = hang + Mth.cos(swing) * 0.7F * limbSwingAmount - breathe * 0.03F;
            this.rightArm.zRot = 0.08F + breathe * 0.02F;
            this.leftArm.zRot = -0.08F - breathe * 0.02F;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
