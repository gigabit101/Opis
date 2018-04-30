package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

public class CommandChunkList extends CommandBase implements IOpisCommand {

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
        return "Shows the 20 slowest chunks, in respect to tile entities.";
    }

    @Override
    public String getName() {
        return "opis_chunk";
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

        ArrayList<StatsChunk> chunks = new ArrayList<>();

        if (args.length == 0) {
            chunks = ChunkManager.INSTANCE.getTopChunks(20);
        } else {
            try {
                chunks = ChunkManager.INSTANCE.getTopChunks(Integer.valueOf(args.length));
            } catch (Exception e) {
                return;
            }
        }

        sender.sendMessage(new TextComponentString("[DIM X Z] Time NTEs"));
        for (StatsChunk stat : chunks) {
            sender.sendMessage(new TextComponentString(stat.toString()));
        }
    }
}
