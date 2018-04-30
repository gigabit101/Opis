package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.events.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommandKill extends CommandBase implements IOpisCommand {

    @Override
    public String getCommandNameOpis() {
        return getName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "Kills the given entity id in the given dimension.";
    }

    @Override
    public String getName() {
        return "opis_kill";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length != 2) {
            return;
        }
        int dim = Integer.valueOf(args[0]);
        int eid = Integer.valueOf(args[1]);

        World world = DimensionManager.getWorld(dim);
        if (world == null) {
            sender.sendMessage(new TextComponentString(String.format("\u00A7oCannot find dim %d in world %d", dim)));
            return;
        }

        Entity entity = world.getEntityByID(eid);
        if (entity == null) {
            sender.sendMessage(new TextComponentString(String.format("\u00A7oCannot find entity %d in dim %d", eid, dim)));
            return;
        }

        entity.setDead();
        sender.sendMessage(new TextComponentString(String.format("\u00A7oKilled entity %d in dim %d", eid, dim)));
        return;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof DedicatedServer) {
            return true;
        }
        if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) {
            return true;
        }
        return PlayerTracker.INSTANCE.isPrivileged(((EntityPlayerMP) sender).getGameProfile().getName());
    }
}
