package com.builtbroken.sbmtrollchest;

import com.builtbroken.sbmtrollchest.block.BlockTrollChest;
import com.builtbroken.sbmtrollchest.proxy.IProxy;
import com.builtbroken.sbmtrollchest.renderer.TileEntityTrollChestRenderer;
import com.builtbroken.sbmtrollchest.tileentity.TileEntityTrollChest;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid=TrollChest.MODID, name=TrollChest.NAME, version=TrollChest.VERSION, acceptedMinecraftVersions=TrollChest.MC_VERSION)
@EventBusSubscriber
public class TrollChest
{
    public static final String MODID = "sbmtrollchest";
    public static final String NAME = "[SBM] Troll Chest";
    public static final String VERSION = ""; //TODO
    public static final String MC_VERSION = "1.12";
    public static final String PREFIX = MODID + ":";

    @SidedProxy(clientSide="com.builtbroken.sbmtrollchest.proxy.ClientProxy", serverSide="com.builtbroken.sbmtrollchest.proxy.ServerProxy")
    public static IProxy proxy;

    public static Block trollChest;
    public static Item trollChestItemBlock;
    public static final ItemStack CHEST_STACK = new ItemStack(Blocks.CHEST);

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        FMLInterModComms.sendMessage("waila", "register", "com.builtbroken.sbmtrollchest.compat.WailaDataProvider.callbackRegister");
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.builtbroken.sbmtrollchest.compat.TOPDataProvider");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(trollChest = new BlockTrollChest());
        GameRegistry.registerTileEntity(TileEntityTrollChest.class, trollChest.getRegistryName());
        trollChestItemBlock = new ItemBlock(trollChest).setRegistryName(TrollChest.trollChest.getRegistryName());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(trollChestItemBlock);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(trollChest), 0, new ModelResourceLocation(new ResourceLocation(TrollChest.MODID, "troll_chest"), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrollChest.class, new TileEntityTrollChestRenderer());
        proxy.registerStackRenderers();
    }
}
