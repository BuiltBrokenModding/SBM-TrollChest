package com.builtbroken.sbmtrollchest.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import com.builtbroken.sbmtrollchest.TrollChestConfig;
import com.builtbroken.sbmtrollchest.block.BlockTrollChest;
import com.builtbroken.sbmtrollchest.inventory.DoubleTrollChestItemHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityTrollChest extends TileEntityLockableLoot implements ITickable //copied from TileEntityChest to remove the chest types
{
    private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
    public boolean adjacentChestChecked;
    public TileEntityTrollChest adjacentChestZNeg;
    public TileEntityTrollChest adjacentChestXPos;
    public TileEntityTrollChest adjacentChestXNeg;
    public TileEntityTrollChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;

    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.chestContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.chest";
    }

    public static void registerFixesChest(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityTrollChest.class, new String[] {"Items"}));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
        doubleChestHandler = null;
    }

    @SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityTrollChest chestTe, EnumFacing side)
    {
        if (chestTe.isInvalid())
        {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked)
        {
            switch (side)
            {
                case NORTH:

                    if (this.adjacentChestZNeg != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case SOUTH:

                    if (this.adjacentChestZPos != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case EAST:

                    if (this.adjacentChestXPos != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case WEST:

                    if (this.adjacentChestXNeg != chestTe)
                    {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    /**
     * Performs the check for adjacent chests to determine if this chest is double or not.
     */
    public void checkForAdjacentChests()
    {
        if (!this.adjacentChestChecked)
        {
            if (this.world == null || !this.world.isAreaLoaded(this.pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbors
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
            this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
            this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
            this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
        }
    }

    @Nullable
    protected TileEntityTrollChest getAdjacentChest(EnumFacing side)
    {
        BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos))
        {
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityTrollChest)
            {
                TileEntityTrollChest tileEntityTrollChest = (TileEntityTrollChest)tileentity;
                tileEntityTrollChest.setNeighbor(this, side.getOpposite());
                return tileEntityTrollChest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos posIn)
    {
        if (this.world == null)
        {
            return false;
        }
        else
        {
            Block block = this.world.getBlockState(posIn).getBlock();
            return block instanceof BlockTrollChest;
        }
    }

    /**
     * Spawns the given stack at a random location within the radius given in the config file around this chest
     * @param stack The stack
     * @return true if a spawn position was found, false otherwhise
     */
    private boolean tpToRandomLocation(ItemStack stack)
    {
        if(!world.isRemote)
        {
            for(int i = 0; i < 25; i++) //try 25 times, fail if not teleported
            {
                BlockPos randPos = getRandomRelativePos();

                if(world.isAirBlock(randPos))
                {
                    Block.spawnAsEntity(world, randPos, stack);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return A random BlockPos relative to this tile's position within the range specified in the config file
     */
    private BlockPos getRandomRelativePos()
    {
        int randX = randomNumberInclNeg(-TrollChestConfig.distanceX, TrollChestConfig.distanceX);
        int randY = randomNumberInclNeg(-TrollChestConfig.distanceY, TrollChestConfig.distanceY);
        int randZ = randomNumberInclNeg(-TrollChestConfig.distanceZ, TrollChestConfig.distanceZ);

        return new BlockPos(getPos().getX() + randX, getPos().getY() + randY, getPos().getZ() + randZ);
    }

    /**
     * Generates a random number within bounds that can be both positive and negative
     * @param min The minimum number (inclusive)
     * @param max The maximum number (inclusive)
     * @return A random number within the given bounds
     */
    private int randomNumberInclNeg(int min, int max)
    {
        return world.rand.nextInt(max + 1 - min) + min; //thanks stack overflow
    }

    /**
     * Teleports the given stack to a random player's inventory. If the inventory is full, the stack will be spawned at the player's feet
     * @param stack The stack
     */
    private void tpToPlayerInventory(ItemStack stack)
    {
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        EntityPlayerMP player = players.get(world.rand.nextInt(players.size()));
        int invStackSize = player.inventory.getInventoryStackLimit();

        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack slotStack = player.inventory.getStackInSlot(i);

            if(slotStack.getItem() == stack.getItem()) //stacks contain the same item, so try to merge
            {
                if(invStackSize - slotStack.getCount() >= stack.getCount()) //all of the stack can fit into the slot
                {
                    slotStack.grow(stack.getCount()); //put it into the slot
                    stack.setCount(0);
                    break; //no further processing needed
                }
                else //the stack does not completely fit into the slot
                {
                    int toRemove = invStackSize - slotStack.getCount();

                    slotStack.setCount(invStackSize); //fill the stack
                    stack.shrink(toRemove); //shrink the stack and continue searching for more slots to merge into
                }
            }
        }

        if(stack.getCount() != 0) //the complete stack could not be merged, so drop into the world
            Block.spawnAsEntity(player.getEntityWorld(), player.getPosition(), stack);
    }

    @Override
    public void update()
    {
        //troll logic
        if(!world.isRemote && numPlayersUsing == 0 && !isEmpty())
        {
            for(int i = 0; i < getSizeInventory(); i++)
            {
                ItemStack stack = getStackInSlot(i);

                if(!stack.isEmpty())
                {
                    //50/50 chance for either to happen
                    if(world.rand.nextInt(2) == 0)
                    {
                        if(!tpToRandomLocation(stack)) //if false, tried too long to find a valid block position, so try again next time
                            continue;
                    }
                    else
                        tpToPlayerInventory(stack);

                    setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }

        this.checkForAdjacentChests();
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F)))
            {
                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
        {
            double d1 = i + 0.5D;
            double d2 = k + 0.5D;

            if (this.adjacentChestZPos != null)
            {
                d2 += 0.5D;
            }

            if (this.adjacentChestXPos != null)
            {
                d1 += 0.5D;
            }

            this.world.playSound((EntityPlayer)null, d1, j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += 0.1F;
            }
            else
            {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && f2 >= 0.5F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
            {
                double d3 = i + 0.5D;
                double d0 = k + 0.5D;

                if (this.adjacentChestZPos != null)
                {
                    d0 += 0.5D;
                }

                if (this.adjacentChestXPos != null)
                {
                    d3 += 0.5D;
                }

                this.world.playSound((EntityPlayer)null, d3, j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockTrollChest)
        {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    public DoubleTrollChestItemHandler doubleChestHandler;

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if(doubleChestHandler == null || doubleChestHandler.needsRefresh())
                doubleChestHandler = DoubleTrollChestItemHandler.get(this);
            if (doubleChestHandler != null && doubleChestHandler != DoubleTrollChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE)
                return (T) doubleChestHandler;
        }
        return super.getCapability(capability, facing);
    }

    public IItemHandler getSingleChestHandler()
    {
        return super.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    @Override
    public String getGuiID()
    {
        return "minecraft:chest";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        this.fillWithLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
    }
}