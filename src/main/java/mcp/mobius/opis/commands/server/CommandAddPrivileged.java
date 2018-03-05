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

public class CommandAddPrivileged extends CommandBase implements IOpisCommand {

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }


    @Override
    public String getDescription() {
        return "Add a user to the privileged list of users.";
    }

    @Override
    public String getCommandNameOpis() {
        return "opis_addpriv";
    }

    @Override
    public String getName() {
        return "opis_addpriv";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        PlayerTracker.INSTANCE.addPrivilegedPlayer(args[0]);
        sender.sendMessage(new TextComponentString(String.format("Player %s added to Opis user list", args[0])));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof DedicatedServer) return true;
        if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
        return PlayerTracker.INSTANCE.isAdmin(((EntityPlayerMP) sender).getGameProfile().getName());
    }
}
