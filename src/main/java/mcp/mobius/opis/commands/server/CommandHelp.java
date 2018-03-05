package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandHelp extends CommandBase implements IOpisCommand {

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
        return "This message.";
    }

    @Override
    public String getName() {
        return "opis_help";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        IOpisCommand[] commands = {
                new CommandStart(),
                new CommandStop(),
                new CommandReset(),
                new CommandFrequency(),
                new CommandTicks(),

                new CommandChunkList(),
                new CommandTimingTileEntities(),

                new CommandTimingEntities(),
                new CommandAmountEntities(),

                new CommandKill(),
                new CommandKillAll(),

                new CommandAddPrivileged(),
                new CommandRmPrivileged()
        };

        for (IOpisCommand cmd : commands)
            sender.sendMessage(new TextComponentString(String.format("/%s : %s", cmd.getCommandNameOpis(), cmd.getDescription())));
    }
}
