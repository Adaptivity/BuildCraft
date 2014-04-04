/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.blueprints;

import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.BuildCraftBuilders;
import buildcraft.api.blueprints.Schematic;
import buildcraft.builders.TileAbstractBuilder;
import buildcraft.core.blueprints.BuildingSlotBlock.Mode;
import buildcraft.core.inventory.InventoryIterator;
import buildcraft.core.inventory.InventoryIterator.IInvSlot;
import buildcraft.core.utils.BlockUtil;

public class BptBuilderTemplate extends BptBuilderBase {

	private LinkedList<BuildingSlotBlock> buildList = new LinkedList<BuildingSlotBlock>();
	private BuildingSlotIterator iterator;

	public BptBuilderTemplate(BlueprintBase bluePrint, World world, int x, int y, int z) {
		super(bluePrint, world, x, y, z);

		for (int j = bluePrint.sizeY - 1; j >= 0; --j) {
			for (int i = 0; i < bluePrint.sizeX; ++i) {
				for (int k = 0; k < bluePrint.sizeZ; ++k) {
					int xCoord = i + x - blueprint.anchorX;
					int yCoord = j + y - blueprint.anchorY;
					int zCoord = k + z - blueprint.anchorZ;

					Schematic slot = bluePrint.contents[i][j][k];

					if (slot == null) {
						BuildingSlotBlock b = new BuildingSlotBlock();

						b.schematic = null;
						b.x = xCoord;
						b.y = yCoord;
						b.z = zCoord;
						b.mode = Mode.ClearIfInvalid;

						buildList.add(b);
					}
				}
			}
		}

		buildList.add(null);

		for (int j = 0; j < bluePrint.sizeY; ++j) {
			for (int i = 0; i < bluePrint.sizeX; ++i) {
				for (int k = 0; k < bluePrint.sizeZ; ++k) {
					int xCoord = i + x - blueprint.anchorX;
					int yCoord = j + y - blueprint.anchorY;
					int zCoord = k + z - blueprint.anchorZ;

					Schematic slot = bluePrint.contents[i][j][k];

					if (slot != null) {
						BuildingSlotBlock b = new BuildingSlotBlock();

						b.schematic = slot;
						b.x = xCoord;
						b.y = yCoord;
						b.z = zCoord;

						b.mode = Mode.Build;

						buildList.add(b);
					}
				}
			}
		}

		iterator = new BuildingSlotIterator(buildList);
	}

	private void checkDone() {
		if (buildList.size() == 0) {
			done = true;
		} else {
			done = false;
		}
	}

	@Override
	public BuildingSlot getNextBlock(World world, TileAbstractBuilder inv) {
		if (buildList.size() != 0) {
			BuildingSlotBlock slot = internalGetNextBlock(world, inv, buildList);
			checkDone();

			if (slot != null) {
				return slot;
			}
		}

		checkDone();

		return null;
	}

	public BuildingSlotBlock internalGetNextBlock(World world, TileAbstractBuilder inv, LinkedList<BuildingSlotBlock> list) {
		BuildingSlotBlock result = null;

		IInvSlot firstSlotToConsume = null;

		for (IInvSlot invSlot : InventoryIterator.getIterable(inv, ForgeDirection.UNKNOWN)) {
			ItemStack stack = invSlot.getStackInSlot();

			if (stack != null && stack.stackSize > 0) {
				firstSlotToConsume = invSlot;
				break;
			}
		}

		iterator.startIteration();

		while (iterator.hasNext()) {
			BuildingSlotBlock slot = iterator.next();

			if (slot == null) {
				break;
			} else if (slot.mode == Mode.ClearIfInvalid) {
				if (BlockUtil.isSoftBlock(world, slot.x, slot.y, slot.z)) {
					iterator.remove();
				} else {
					int hardness = (int) context
							.world()
							.getBlock(slot.x, slot.y, slot.z)
							.getBlockHardness(context.world(), slot.x, slot.y,
									slot.z) + 1;

					for (int i = 0; i < hardness; ++i) {
						slot.addStackConsumed(new ItemStack(
								BuildCraftBuilders.buildToolBlock));
					}

					result = slot;
					iterator.remove();

					break;
				}
			} else if (slot.mode == Mode.Build) {
				if (!BlockUtil.isSoftBlock(world, slot.x, slot.y, slot.z)) {
					iterator.remove();
				} else {
					if (firstSlotToConsume != null) {
						slot.addStackConsumed(firstSlotToConsume
								.decreaseStackInSlot());
						result = slot;
						iterator.remove();

						break;
					}
				}
			}
		}

		return result;
	}
}
