package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;

public class CommandStop extends CommandBase implements IOpisCommand {

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
		modOpis.profilerRun = false;
		ProfilerSection.desactivateAll(Side.SERVER);
		sender.sendMessage(new TextComponentString(String.format("\u00A7oOpis stopped.")));
	}
}
