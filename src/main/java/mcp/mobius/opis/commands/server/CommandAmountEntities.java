package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.managers.EntityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandAmountEntities extends CommandBase implements IOpisCommand{
	
	@Override
	public String getCommandNameOpis() {
		return this.getName();
	}

	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }	

	@Override
	public String getDescription() {
		return "Opens a summary of the number of entities on the server, by type.";
	}

	@Override
	public String getName() {
		return "opis_nent";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender icommandsender, String[] args) throws CommandException {
		if (icommandsender instanceof EntityPlayerMP){
			icommandsender.sendMessage(new TextComponentString("DEPRECATED ! Please run /opis instead."));
			return;
		}


		ArrayList<AmountHolder> ents;

		if (args.length == 1 && args[0].equals("all"))
			ents = EntityManager.INSTANCE.getCumulativeEntities(false);
		else
			ents = EntityManager.INSTANCE.getCumulativeEntities(true);

		for (AmountHolder s : ents)
			icommandsender.sendMessage(new TextComponentString(String.format("%s : %s", s.key, s.value)));
	}
}
