package com.builtbroken.sbmtrollchest.client;

import com.builtbroken.sbmtrollchest.TrollChest;
import com.builtbroken.sbmtrollchest.content.TileEntityTrollChest;

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
        if(Block.getBlockFromItem(item.getItem()) == TrollChest.trollChest) //just to be sure
            TileEntityRendererDispatcher.instance.render(dummy, 0.0D, 0.0D, 0.0D, 0.0F); //use the te rendering code
        else
            super.renderByItem(item);
    }
}
