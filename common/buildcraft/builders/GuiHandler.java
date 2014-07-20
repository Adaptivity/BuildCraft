/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.builders;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

import buildcraft.builders.gui.ContainerArchitect;
import buildcraft.builders.gui.ContainerBlueprintLibrary;
import buildcraft.builders.gui.ContainerBuilder;
import buildcraft.builders.gui.ContainerFiller;
import buildcraft.builders.gui.GuiArchitect;
import buildcraft.builders.gui.GuiBlueprintLibrary;
import buildcraft.builders.gui.GuiBuilder;
import buildcraft.builders.gui.GuiFiller;
import buildcraft.builders.urbanism.ContainerUrbanist;
import buildcraft.builders.urbanism.GuiUrbanist;
import buildcraft.builders.urbanism.TileUrbanist;
import buildcraft.commander.ContainerMap;
import buildcraft.commander.GuiMap;
import buildcraft.commander.TileMap;
import buildcraft.core.GuiIds;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z)) {
			return null;
		}

		TileEntity tile = world.getTileEntity(x, y, z);

		switch (id) {

		case GuiIds.ARCHITECT_TABLE:
			if (!(tile instanceof TileArchitect)) {
				return null;
			}
			return new GuiArchitect(player, (TileArchitect) tile);

		case GuiIds.BLUEPRINT_LIBRARY:
			if (!(tile instanceof TileBlueprintLibrary)) {
				return null;
			}
			return new GuiBlueprintLibrary(player, (TileBlueprintLibrary) tile);

		case GuiIds.BUILDER:
			if (!(tile instanceof TileBuilder)) {
				return null;
			}
			return new GuiBuilder(player.inventory, (TileBuilder) tile);

		case GuiIds.FILLER:
			if (!(tile instanceof TileFiller)) {
				return null;
			}
			return new GuiFiller(player.inventory, (TileFiller) tile);

		case GuiIds.URBANIST:
			if (!(tile instanceof TileUrbanist)) {
				return null;
			}
			return new GuiUrbanist(player.inventory, (TileUrbanist) tile);

		case GuiIds.MAP:
			if (!(tile instanceof TileMap)) {
				return null;
			}
			return new GuiMap(player.inventory, (TileMap) tile);

		default:
			return null;
		}

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (!world.blockExists(x, y, z)) {
			return null;
		}

		TileEntity tile = world.getTileEntity(x, y, z);

		switch (id) {

		case GuiIds.ARCHITECT_TABLE:
			if (!(tile instanceof TileArchitect)) {
				return null;
			}
			return new ContainerArchitect(player, (TileArchitect) tile);

		case GuiIds.BLUEPRINT_LIBRARY:
			if (!(tile instanceof TileBlueprintLibrary)) {
				return null;
			}
			return new ContainerBlueprintLibrary(player, (TileBlueprintLibrary) tile);

		case GuiIds.BUILDER:
			if (!(tile instanceof TileBuilder)) {
				return null;
			}
			return new ContainerBuilder(player.inventory, (TileBuilder) tile);

		case GuiIds.FILLER:
			if (!(tile instanceof TileFiller)) {
				return null;
			}
			return new ContainerFiller(player.inventory, (TileFiller) tile);

		case GuiIds.URBANIST:
			if (!(tile instanceof TileUrbanist)) {
				return null;
			} else {
				return new ContainerUrbanist(player.inventory, (TileUrbanist) tile);
			}

		case GuiIds.MAP:
			if (!(tile instanceof TileMap)) {
				return null;
			} else {
				return new ContainerMap((TileMap) tile);
			}

		default:
			return null;
		}
	}

}
