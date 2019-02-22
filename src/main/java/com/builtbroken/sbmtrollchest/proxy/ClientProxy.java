package com.builtbroken.sbmtrollchest.proxy;

import com.builtbroken.sbmtrollchest.TrollChest;
import com.builtbroken.sbmtrollchest.renderer.ItemTrollChestRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy implements IProxy
{
    @SideOnly(Side.CLIENT)
    public void registerStackRenderers()
    {
        TrollChest.trollChestItemBlock.setTileEntityItemStackRenderer(new ItemTrollChestRenderer());
    }
}
