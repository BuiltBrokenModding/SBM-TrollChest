package com.builtbroken.sbmtrollchest;

import com.builtbroken.sbmtrollchest.content.BlockTrollChest;
import com.builtbroken.sbmtrollchest.content.TileEntityTrollChest;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid=TrollChest.MODID, name=TrollChest.NAME, version=TrollChest.VERSION, acceptedMinecraftVersions=TrollChest.MC_VERSION)
@EventBusSubscriber
public class TrollChest
{
    public static final String MODID = "sbmtrollchest";
    public static final String NAME = "[SBM] Troll Chest";
    public static final String VERSION = ""; //TODO
    public static final String MC_VERSION = "1.12";
    public static final String PREFIX = MODID + ":";

    public static Block trollChest;
    public static Item trollChestItemBlock;
    public static final ItemStack CHEST_STACK = new ItemStack(Blocks.CHEST); //one instance of the chest stack, used for performance reasons in code that gets called each tick

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
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(trollChestItemBlock = new ItemBlock(trollChest).setRegistryName(TrollChest.trollChest.getRegistryName()));
    }
}
