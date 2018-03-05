package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.Opis;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;

import mcp.mobius.opis.commands.IOpisCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.DimensionManager;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Set;

public class CommandChunkDump extends CommandBase implements IOpisCommand{

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
		return "Unused";
	}

	@Override
	public String getName() {
		return "chunkdump";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		Opis.log.log(Level.INFO, "== CHUNK DUMP ==");

		HashMap<ChunkPos, Boolean> chunkStatus = new HashMap<ChunkPos, Boolean>();

		Integer[] worldIDs = DimensionManager.getIDs();
		for (Integer worldID : worldIDs){
			Set<ChunkPos> persistantChunks = DimensionManager.getWorld(worldID).getPersistentChunks().keySet();
			Set<ChunkPos> chunks = (Set<ChunkPos>)DimensionManager.getWorld(worldID).getChunkProvider();

			for (ChunkPos chunk : chunks){
				Opis.log.log(Level.INFO, String.format("Dim : %s, %s, Forced : %s", worldID, chunk, persistantChunks.contains(chunk)));
				chunkStatus.put(chunk, persistantChunks.contains(chunk));
			}
		}
	}
}
