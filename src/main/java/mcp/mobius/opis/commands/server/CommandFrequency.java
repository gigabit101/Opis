package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.commands.IOpisCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandFrequency extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandNameOpis() {
		return this.getName();
	}
	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	


	@Override
	public String getDescription() {
		return "Sets the delay in ticks between 2 data points."; 
	}

	@Override
	public String getName() {
		return "opis_delay";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) return;
		try{
			Opis.profilerDelay = Integer.valueOf(args[0]);
			sender.sendMessage(new TextComponentString(String.format("\u00A7oOpis delay set to %s ticks.", args[0])));

		} catch (Exception e){}
	}
}
