package com.builtbroken.sbmtrollchest.client;

import com.builtbroken.sbmtrollchest.TrollChest;
import com.builtbroken.sbmtrollchest.content.TileEntityTrollChest;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class ClientRegistrationHandler
{
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TrollChest.trollChest), 0, new ModelResourceLocation(new ResourceLocation(TrollChest.MODID, "troll_chest"), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrollChest.class, new TileEntityTrollChestRenderer()); //world rendering
        TrollChest.trollChestItemBlock.setTileEntityItemStackRenderer(new ItemTrollChestRenderer()); //inventory rendering
    }
}
