package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.events.OpisServerEventHandler;
import mcp.mobius.opis.events.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;

public class CommandEntityCreate extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "opis_enttrace";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].equals("full")) {
            OpisServerEventHandler.printEntityFull = true;
            OpisServerEventHandler.printEntityTrace = true;
        } else {
            OpisServerEventHandler.printEntityTrace = !OpisServerEventHandler.printEntityTrace;
            OpisServerEventHandler.printEntityFull = false;
        }

        sender.sendMessage(new TextComponentString(String.format("Entity trace is %s", OpisServerEventHandler.printEntityTrace)));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof DedicatedServer) return true;
        if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
        return PlayerTracker.INSTANCE.isPrivileged(((EntityPlayerMP) sender).getGameProfile().getName());
    }
}
