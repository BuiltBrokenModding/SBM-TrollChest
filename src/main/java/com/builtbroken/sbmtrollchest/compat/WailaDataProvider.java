package com.builtbroken.sbmtrollchest.compat;

import java.util.List;

import com.builtbroken.sbmtrollchest.TrollChest;
import com.builtbroken.sbmtrollchest.block.BlockTrollChest;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WailaDataProvider implements IWailaDataProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new WailaDataProvider(), BlockTrollChest.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler config)
    {
        return TrollChest.CHEST_STACK;
    }

    @Override
    public List<String> getWailaHead(ItemStack stack, List<String> head, IWailaDataAccessor data, IWailaConfigHandler config)
    {
        return head;
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> body, IWailaDataAccessor data, IWailaConfigHandler config)
    {
        return body;
    }

    @Override
    public List<String> getWailaTail(ItemStack stack, List<String> tail, IWailaDataAccessor data, IWailaConfigHandler config)
    {
        return tail;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, BlockPos pos)
    {
        return tag;
    }
}
