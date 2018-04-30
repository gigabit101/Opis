package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.events.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;

public class CommandRmPrivileged extends CommandBase implements IOpisCommand {

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "Remove a user from the privileged list of users.";
    }

    @Override
    public String getCommandNameOpis() {
        return "opis_rmpriv";
    }

    @Override
    public String getName() {
        return "opis_rmpriv";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (PlayerTracker.INSTANCE.isPrivileged(args[0])) {
            PlayerTracker.INSTANCE.rmPrivilegedPlayer(args[0]);
            sender.sendMessage(new TextComponentString(String.format("Player %s removed from Opis user list.", args[0])));
        } else {
            sender.sendMessage(new TextComponentString(String.format("Player %s not found in list.", args[0])));
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
        return PlayerTracker.INSTANCE.isAdmin(((EntityPlayerMP) sender).getGameProfile().getName());
    }
}
