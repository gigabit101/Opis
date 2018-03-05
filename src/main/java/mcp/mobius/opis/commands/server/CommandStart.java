package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.profiler.ProfilerSection;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;

public class CommandStart extends CommandBase implements IOpisCommand {

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
		return "Starts a run.";
	}

	@Override
	public String getName() {
		return "opis_start";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			sender.sendMessage(new TextComponentString("DEPRECATED ! Please run /opis instead."));
			return;
		}

		MetaManager.reset();
		Opis.profilerRun = true;
		ProfilerSection.activateAll(Side.SERVER);

		PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_START, new SerialInt(Opis.profilerMaxTicks)));
		sender.sendMessage(new TextComponentString(String.format("\u00A7oOpis started with a tick delay %s.", Opis.profilerDelay)));
	}
}
