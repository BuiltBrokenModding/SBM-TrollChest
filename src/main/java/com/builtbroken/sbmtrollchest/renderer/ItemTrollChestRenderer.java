package com.builtbroken.sbmtrollchest.renderer;

import com.builtbroken.sbmtrollchest.TrollChest;
import com.builtbroken.sbmtrollchest.tileentity.TileEntityTrollChest;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class ItemTrollChestRenderer extends TileEntityItemStackRenderer
{
    private final TileEntityTrollChest dummy = new TileEntityTrollChest();

    @Override
    public void renderByItem(ItemStack item)
    {
        if(Block.getBlockFromItem(item.getItem()) == TrollChest.trollChest)
            TileEntityRendererDispatcher.instance.render(dummy, 0.0D, 0.0D, 0.0D, 0.0F);
        else
            super.renderByItem(item);
    }
}
