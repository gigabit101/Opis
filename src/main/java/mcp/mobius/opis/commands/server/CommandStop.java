package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.profiler.Profilers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;

public class CommandStop extends CommandBase implements IOpisCommand {

    @Override
    public String getCommandNameOpis() {
        return getName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
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

    @Override
    public String getDescription() {
        return "Ends a run before completion.";
    }

    @Override
    public String getName() {
        return "opis_stop";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Opis.profilerRun = false;
        Profilers.dissableProfilers(Side.SERVER);
        sender.sendMessage(new TextComponentString(String.format("\u00A7oOpis stopped.")));
    }
}
