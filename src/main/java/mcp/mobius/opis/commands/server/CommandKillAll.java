package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.events.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.StringUtils;

public class CommandKillAll extends CommandBase implements IOpisCommand {

    @Override
    public String getCommandNameOpis() {
        return this.getName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "Kills all entities of the given name.";
    }

    @Override
    public String getName() {
        return "opis_killall";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "opis_killall";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) return;

        String searchname = StringUtils.join(args, " ").toLowerCase();

        int nkilled = EntityManager.INSTANCE.killAll(searchname);

        if (nkilled == -1) {
            sender.sendMessage(new TextComponentString(String.format("\u00A7oSeriously ? I can't, seriously, I can't. I should remove you from the OP list !")));
            return;
        }

        sender.sendMessage(new TextComponentString(String.format("\u00A7oKilled %d entities of type %s", nkilled, searchname)));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof DedicatedServer) return true;
        if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
        return PlayerTracker.INSTANCE.isPrivileged(((EntityPlayerMP) sender).getGameProfile().getName());
    }
}
