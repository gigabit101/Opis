package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.events.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;

public class CommandTicks extends CommandBase implements IOpisCommand {

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
        return "Sets the amount of data points to gather.";
    }

    @Override
    public String getName() {
        return "opis_ticks";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            return;
        }
        try {
            Opis.profilerMaxTicks = Integer.valueOf(args[0]);
            sender.sendMessage(new TextComponentString(String.format("\u00A7oOpis ticks set to %s ticks.", args[0])));

        } catch (Exception e) {
        }
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
