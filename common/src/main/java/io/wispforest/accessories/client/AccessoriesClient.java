package io.wispforest.accessories.client;

import com.mojang.blaze3d.platform.Window;
import io.wispforest.accessories.Accessories;
import io.wispforest.accessories.AccessoriesInternals;
import io.wispforest.accessories.AccessoriesInternalsClient;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.compat.AccessoriesConfig;
import io.wispforest.accessories.data.EntitySlotLoader;
import io.wispforest.accessories.networking.server.ScreenOpen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.EntityHitResult;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AccessoriesClient {
    public static final Event<WindowResizeCallback> WINDOW_RESIZE_CALLBACK_EVENT = EventFactory.createArrayBacked(WindowResizeCallback.class, callbacks -> (client, window) -> {
        for (var callback : callbacks) callback.onResized(client, window);
    });

    public static boolean IS_PLAYER_INVISIBLE = false;

    public static void init(){
        AccessoriesInternalsClient.registerToMenuTypes();
    }

    public interface WindowResizeCallback {
        void onResized(Minecraft client, Window window);
    }

    private static boolean displayUnusedSlotWarning = false;

    public static boolean attemptToOpenScreen() {
        return attemptToOpenScreen(false);
    }

    public static boolean attemptToOpenScreen(boolean targetingLookingEntity) {
        var player = Minecraft.getInstance().player;

        if(targetingLookingEntity) {
            var result = ProjectileUtil.getHitResultOnViewVector(player, e -> e instanceof LivingEntity, (player.isCreative() ? 4.5 : 4));

            var bl = !(result instanceof EntityHitResult entityHitResult) ||
                    !(entityHitResult.getEntity() instanceof LivingEntity living)
                    || EntitySlotLoader.getEntitySlots(living).isEmpty();

            if(bl) return false;

            AccessoriesInternals.getNetworkHandler().sendToServer(new ScreenOpen(true));
        } else {
            var slots = AccessoriesAPI.getUsedSlotsFor(player);

            var holder = player.accessoriesHolder();

            if(holder == null) return false;

            if(slots.isEmpty() && !holder.showUnusedSlots() && !displayUnusedSlotWarning && !Accessories.getConfig().clientData.disableEmptySlotScreenError) {
                player.displayClientMessage(Component.literal("[Accessories]: No Used Slots found by any mod directly, such will show empty unless a item is found to implement slots!"), false);

                displayUnusedSlotWarning = true;
            }

            AccessoriesInternals.getNetworkHandler().sendToServer(new ScreenOpen());
        }

        return true;
    }
}