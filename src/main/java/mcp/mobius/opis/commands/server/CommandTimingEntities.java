package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.data.managers.EntityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandTimingEntities extends CommandBase implements IOpisCommand {

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
		return "Returns the 20 longest entities to update.";
	}

	@Override
	public String getName() {
		return "opis_ent";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if (sender instanceof EntityPlayerMP){
			sender.sendMessage(new TextComponentString("DEPRECATED ! Please run /opis instead."));
			return;
		}

		ArrayList<DataEntity> ents = new ArrayList<DataEntity>();
		if (args.length == 0){
			ents = EntityManager.INSTANCE.getWorses(20);
		}
		else{
			try{
				ents = EntityManager.INSTANCE.getWorses(Integer.valueOf(args[0]));
			} catch (Exception e) {return;}
		}

		sender.sendMessage(new TextComponentString("[DIM X Z] Time NTEs"));
		for (DataEntity stat : ents){
			sender.sendMessage(new TextComponentString(stat.toString()));
		}
	}
}
