package com.builtbroken.sbmtrollchest;

import net.minecraftforge.common.config.Config;

@Config(modid=TrollChest.MODID)
public class TrollChestConfig
{
    @Config.Comment("How far away the item can teleport in the x direction")
    @Config.Name("distanceX")
    @Config.RangeInt(min=1, max=160) //10 chunks (x 16 blocks) is the default server view distance
    public static int distanceX = 16;
    @Config.Comment("How far away the item can teleport in the y direction")
    @Config.Name("distanceY")
    @Config.RangeInt(min=1, max=255)
    public static int distanceY = 16;
    @Config.Comment("How far away the item can teleport in the z direction")
    @Config.Name("distanceZ")
    @Config.RangeInt(min=1, max=160) //10 chunks (x 16 blocks) is the default server view distance
    public static int distanceZ = 16;
}
