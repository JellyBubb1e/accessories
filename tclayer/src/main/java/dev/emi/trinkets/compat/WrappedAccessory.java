package dev.emi.trinkets.compat;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketEnums;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class WrappedAccessory implements Trinket {

    public final Accessory accessory;

    public WrappedAccessory(Accessory accessory){
        this.accessory = accessory;
    }

    @Override
    public void tick(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        accessory.tick(stack, reference);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        accessory.onEquip(stack, reference);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        accessory.onUnequip(stack, reference);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        return accessory.canEquip(stack, reference);
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        return accessory.canUnequip(stack, reference);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference ref, LivingEntity entity, ResourceLocation location) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        var builder = new AccessoryAttributeBuilder(reference);

        accessory.getDynamicModifiers(stack, reference, builder);

        return builder.getAttributeModifiers(true);
    }

    @Override
    public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        var damageSource = entity.getLastDamageSource();

        if(damageSource == null) damageSource = entity.level().damageSources().generic();

        return TrinketEnums.convert(accessory.getDropRule(stack, reference, damageSource));
    }

    @Override
    public void onBreak(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        accessory.onBreak(stack, reference);
    }

    @Override
    public boolean canEquipFromUse(ItemStack stack, LivingEntity entity) {
        return accessory.canEquipFromUse(stack, io.wispforest.accessories.api.slot.SlotReference.of(entity, "", 0));
    }

    @Override
    public Holder<SoundEvent> getEquipSound(ItemStack stack, SlotReference ref, LivingEntity entity) {
        var slotName = ((WrappedTrinketInventory) ref.inventory()).container.getSlotName();

        var reference = io.wispforest.accessories.api.slot.SlotReference.of(entity, slotName, ref.index());

        var data = accessory.getEquipSound(stack, reference);

        if(data == null) return null;

        return data.event();
    }
}
