/*
 * Copyright (c) 2018-2023 C4
 *
 * This file is part of Curios, a mod made for Minecraft.
 *
 * Curios is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curios is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curios.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import top.theillusivec4.curios.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Designed to be directly implemented on {@link Item} objects.<br/><br/>
 * Curios will automatically create and attach {@link ICurio} capability to any ItemStacks that contain items
 * implementing this interface, redirecting all calls made on such capability to respective methods here.
 *
 * @author Extegral
 */

public interface ICurioItem {

  /**
   * Default instance of {@link ICurio}, where all calls are redirected by default methods
   * of this interface to avoid needlessly copying over code from there.
   */

  ICurio defaultInstance = () -> ItemStack.EMPTY;

  /**
   * Called during automatic capability attachment to any ItemStack containing this {@link ICurioItem} instance.
   *
   * @param stack ItemStack in question
   * @return true to allow attach {@link ICurio} capability to this ItemStack; false to prevent attachment.
   */

  default boolean hasCurioCapability(ItemStack stack) {
    return true;
  }

  /**
   * Called every tick on both client and server while the ItemStack is equipped.
   *
   * @param slotContext The context for the slot that the ItemStack is in
   * @param stack       The ItemStack in question
   */
  default void curioTick(SlotContext slotContext, ItemStack stack) {
    defaultInstance.curioTick(slotContext);
  }

  /**
   * Called when the ItemStack is equipped into a slot or its data changes.
   *
   * @param slotContext Context about the slot that the ItemStack was just unequipped from
   * @param prevStack   The previous ItemStack in the slot
   * @param stack       The ItemStack in question
   */
  default void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
    defaultInstance.onEquip(slotContext, prevStack);
  }

  /**
   * Called when the ItemStack is unequipped from a slot or its data changes.
   *
   * @param slotContext Context about the slot that the ItemStack was just unequipped from
   * @param newStack    The new ItemStack in the slot
   * @param stack       The ItemStack in question
   */
  default void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
    defaultInstance.onUnequip(slotContext, newStack);
  }

  /**
   * Determines if the ItemStack can be equipped into a slot.
   *
   * @param slotContext Context about the slot that the ItemStack is attempting to equip into
   * @param stack       The ItemStack in question
   * @return True if the ItemStack can be equipped/put in, false if not
   */
  default boolean canEquip(SlotContext slotContext, ItemStack stack) {
    return defaultInstance.canEquip(slotContext);
  }

  /**
   * Determines if the ItemStack can be unequipped from a slot.
   *
   * @param slotContext Context about the slot that the ItemStack is attempting to unequip from
   * @param stack       The ItemStack in question
   * @return True if the ItemStack can be unequipped/taken out, false if not
   */
  default boolean canUnequip(SlotContext slotContext, ItemStack stack) {
    return defaultInstance.canUnequip(slotContext);
  }

  /**
   * Retrieves a list of tooltips when displaying curio slot information. By default, this will be a
   * list of each slot identifier, translated and in gold text, associated with the curio.
   * <br>
   * If overriding, make sure the user has some indication which slots are associated with the
   * curio.
   *
   * @param tooltips A list of {@link Component} with every slot valid for this curio
   * @param stack    The ItemStack in question
   * @return A list of ITextComponent to display as curio slot information
   */
  default List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
    return defaultInstance.getSlotsTooltip(tooltips);
  }

  /**
   * Retrieves a map of attribute modifiers for the curio.
   * <br>
   * Note that only the identifier is guaranteed to be present in the slot context. For instances
   * where the ItemStack may not be in a curio slot, such as when retrieving item tooltips, the
   * index is -1 and the wearer may be null.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param id        Slot-unique id
   * @return A map of attribute modifiers to apply
   */
  default Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
    return defaultInstance.getAttributeModifiers(slotContext, id);
  }

  default Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
    return defaultInstance.getAttributeModifiers(slotContext, uuid);
  }

  /**
   * Called server-side when the ItemStack is equipped by using it (i.e. from the hotbar), after
   * calling {@link ICurioItem#canEquipFromUse(SlotContext, ItemStack)}.
   * <br>
   * Default implementation plays the equip sound from {@link ICurioItem#getEquipSound(SlotContext, ItemStack)}.
   * This can be overridden to avoid that, but it is advised to always play something as an auditory
   * feedback for players.
   *
   * @param slotContext Context about the slot that the ItemStack was just equipped into
   * @param stack       The ItemStack in question
   */
  default void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
    defaultInstance.onEquipFromUse(slotContext);
  }

  /**
   * Retrieves the equip sound information for the given slot context.
   *
   * @param slotContext Context about the slot that the ItemStack was just equipped into
   * @return {@link ICurio.SoundInfo} containing
   * information about the sound event, volume, and pitch
   */
  @NotNull
  default ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
    return defaultInstance.getEquipSound(slotContext);
  }

  /**
   * Determines if the ItemStack can be automatically equipped into the first available slot when
   * used.
   *
   * @param slotContext Context about the slot that the ItemStack
   * @param stack       The ItemStack in question
   * @return True to enable auto-equipping when the item is used, false to disable
   */
  default boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
    return defaultInstance.canEquipFromUse(slotContext);
  }

  /**
   * Called when rendering break animations and sounds client-side when a worn curio item is
   * broken.
   *
   * @param slotContext Context about the slot that the ItemStack broke in
   * @param stack       The ItemStack in question
   */
  default void curioBreak(SlotContext slotContext, ItemStack stack) {
    defaultInstance.curioBreak(slotContext);
  }

  /**
   * Compares the current ItemStack and the previous ItemStack in the slot to detect any changes and
   * returns true if the change should be synced to all tracking clients. Note that this check
   * occurs every tick so implementations need to code their own timers for other intervals.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param stack       The ItemStack in question
   * @return True to sync the ItemStack change to all tracking clients, false to do nothing
   */
  default boolean canSync(SlotContext slotContext, ItemStack stack) {
    return defaultInstance.canSync(slotContext);
  }

  /**
   * Gets a tag that is used to sync extra curio data from the server to the client. Only used when
   * {@link ICurioItem#canSync(SlotContext, ItemStack)} returns true.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param stack       The ItemStack in question
   * @return Data to be sent to the client
   */
  @NotNull
  default CompoundTag writeSyncData(SlotContext slotContext, ItemStack stack) {
    var compoundTag = defaultInstance.writeSyncData(slotContext);

    return compoundTag != null ? compoundTag : new CompoundTag();
  }

  /**
   * Used client-side to read data tags created by {@link ICurioItem#writeSyncData(SlotContext, ItemStack)}
   * received from the server.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param compound    Data received from the server
   * @param stack       The ItemStack in question
   */
  default void readSyncData(SlotContext slotContext, CompoundTag compound, ItemStack stack) {
    defaultInstance.readSyncData(slotContext, compound);
  }

  /**
   * Determines if the ItemStack should drop on death and persist through respawn. This will persist
   * the ItemStack in the curio slot to the respawned player if applicable.
   *
   * @param slotContext  Context about the slot that the ItemStack is attempting to equip into
   * @param source       The damage source that killed the wearer and triggered the drop
   * @param lootingLevel The level of looting that triggered the drop
   * @param recentlyHit  Whether or not the wearer was recently hit
   * @param stack        The ItemStack in question
   * @return The {@link DropRule} that applies to this curio
   */
  @NotNull
  default DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
    return defaultInstance.getDropRule(slotContext, source, lootingLevel, recentlyHit);
  }

  default DropRule getDropRule(SlotContext slotContext, DamageSource source, boolean recentlyHit, ItemStack stack) {
    return defaultInstance.getDropRule(slotContext, source, recentlyHit);
  }

  /**
   * Retrieves a list of tooltips when displaying curio attribute modifier information returned by
   * {@link ICurio#getAttributeModifiers(SlotContext, ResourceLocation)}. By default, this will display a list
   * similar to the vanilla attribute modifier tooltips.
   *
   * @param tooltips A list of {@link Component} with the attribute modifier information
   * @param stack    The ItemStack in question
   * @return A list of ITextComponent to display as curio attribute modifier information
   */
  default List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
    return defaultInstance.getAttributesTooltip(tooltips);
  }

  /**
   * Allows to set the amount of bonus Fortune levels that are provided by curio.
   * Default implementation returns level of Fortune enchantment on ItemStack.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param lootContext Context for the loot drops
   * @param stack       The ItemStack in question
   * @return Amount of additional Fortune levels that will be applied when mining
   */
  default int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
    return defaultInstance.getFortuneLevel(slotContext, lootContext);
  }

  /**
   * Allows to set the amount of bonus Looting levels that are provided by curio.
   * Default implementation returns level of Looting enchantment on ItemStack.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param source      Damage source that triggers the looting
   * @param target      The target that drops the loot
   * @param baseLooting The original looting level before bonuses
   * @param stack       The ItemStack in question
   * @return Amount of additional Looting levels that will be applied in LootingLevelEvent
   */
  default int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
    return defaultInstance.getLootingLevel(slotContext, source, target, baseLooting);
  }

  /**
   * Determines whether wearing the curio makes nearby piglins neutral, in the same manner as
   * wearing gold armor in vanilla.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @return True if nearby piglins are neutral, false otherwise
   */
  default boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
    return Services.CURIOS.makesPiglinsNeutral(stack, slotContext.entity());
  }

  /**
   * Determines whether wearing the curio will allow the user to walk on powder snow, in the same manner as
   * wearing leather boots in vanilla.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @return True if the user can walk on powder snow, false otherwise
   */
  default boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
    return Services.CURIOS.canWalkOnPowderedSnow(stack, slotContext.entity());
  }

  /**
   * Determines whether wearing the curio masks the user's eyes against Enderman, in the same manner
   * as wearing a pumpkin in vanilla.
   *
   * @param slotContext Context about the slot that the ItemStack is in
   * @param enderMan    The Enderman entity that the user is looking at
   * @return True if it can mask the user from Enderman, false otherwise
   */
  default boolean isEnderMask(SlotContext slotContext, EnderMan enderMan, ItemStack stack) {
    return slotContext.entity() instanceof Player player && Services.CURIOS.isEnderMask(stack, player, enderMan);
  }
}
