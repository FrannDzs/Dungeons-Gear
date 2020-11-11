package com.infamous.dungeons_gear.armor;

import com.google.common.collect.Multimap;
import com.infamous.dungeons_gear.DungeonsGear;
import com.infamous.dungeons_gear.armor.models.ScaleMailModel;
import com.infamous.dungeons_gear.init.DeferredItemInit;
import com.infamous.dungeons_gear.interfaces.IArmor;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.UUID;

public class ScaleMailItem extends ArmorItem implements IArmor {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    private final boolean unique;
    private final int damageReduceAmount;
    private final float toughness;

    public ScaleMailItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties, boolean unique) {
        super(armorMaterial, slotType, properties);
        this.unique = unique;

        this.damageReduceAmount = armorMaterial.getDamageReductionAmount(slot);
        this.toughness = armorMaterial.getToughness();
    }


    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if(stack.getItem() == DeferredItemInit.SCALE_MAIL.get()){
            return DungeonsGear.MODID + ":textures/models/armor/scale_mail.png";
        }
        else if(stack.getItem() == DeferredItemInit.HIGHLAND_ARMOR.get() || stack.getItem() == DeferredItemInit.HIGHLAND_ARMOR_HELMET.get()){
            return DungeonsGear.MODID + ":textures/models/armor/highland_armor.png";
        }
        else return "";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
         return (A) new ScaleMailModel<>(1.0F, slot, entityLiving);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<String, AttributeModifier> modifierMultimap = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == this.slot) {
            modifierMultimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.damageReduceAmount, AttributeModifier.Operation.ADDITION));
            modifierMultimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
            if(this.unique) {
                modifierMultimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor attack damage boost", 0.3D * 0.5D, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }

        return modifierMultimap;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack){
        if(this.unique) return Rarity.RARE;
        return Rarity.UNCOMMON;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);

        if (this.unique) {
            list.add(new StringTextComponent(TextFormatting.WHITE + "" + TextFormatting.ITALIC + "A wise armorer made this armor with care."));
            //if(this.slot == EquipmentSlotType.CHEST){
                list.add(new StringTextComponent(TextFormatting.GREEN + "Gain Speed After Jumping (Swiftfooted I)"));
            //}
        }
        else{
            list.add(new StringTextComponent(TextFormatting.WHITE + "" + TextFormatting.ITALIC + "This armor, crafted near the shores of a great sea, was inspired by the scales of fish."));
        }
    }
}
