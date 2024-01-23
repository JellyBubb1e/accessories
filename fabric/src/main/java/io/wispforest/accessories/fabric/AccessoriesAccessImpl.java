package io.wispforest.accessories.fabric;

import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.AccessoriesHolder;
import io.wispforest.accessories.impl.AccessoriesHolderImpl;
import io.wispforest.accessories.networking.AccessoriesNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class AccessoriesAccessImpl {

    public static AccessoriesAPI getAPI() {
        throw new AssertionError();
    }

    public static AccessoriesHolder getHolder(LivingEntity livingEntity){
        return livingEntity.getAttachedOrCreate(AccessoriesModFabric.HOLDER_ATTACHMENT_TYPE);
    }

    public static AccessoriesNetworkHandler getHandler(){
        return AccessoriesNetworkHandlerImpl.INSTANCE;
    }

    public static Collection<ServerPlayer> getTracking(Entity entity){
        throw new AssertionError();
    }
}
