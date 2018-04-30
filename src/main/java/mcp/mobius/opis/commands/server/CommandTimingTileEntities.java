package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
import mcp.mobius.opis.data.managers.TileEntityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class CommandTimingTileEntities extends CommandBase implements IOpisCommand {

    @Override
    public String getCommandNameOpis() {
        return getName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getDescription() {
        return "Returns the 20 longest TEs to update.";
    }

    @Override
    public String getName() {
        return "opis_te";
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

        List<DataBlockTileEntity> tes = new ArrayList<>();
        if (args.length == 0) {
            tes = TileEntityManager.INSTANCE.getWorses(20);
        } else {
            try {
                tes = TileEntityManager.INSTANCE.getWorses(Integer.valueOf(args[0]));
            } catch (Exception e) {
                return;
            }
        }

        for (DataBlockTileEntity stat : tes) {
            sender.sendMessage(new TextComponentString(stat.toString()));
        }
    }
}
