package mcp.mobius.opis.commands.server;

import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;

import mcp.mobius.opis.commands.IOpisCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

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

	//TODO
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

//		Opis.log.log(Level.INFO, "== CHUNK DUMP ==");
//
//		HashMap<BlockPos, Boolean> chunkStatus = new HashMap<BlockPos, Boolean>();
//
//		Integer[] worldIDs = DimensionManager.getIDs();
//		for (Integer worldID : worldIDs){
//			Set<BlockPos> persistantChunks = DimensionManager.getWorld(worldID).getPersistentChunks().keySet();
//			Set<ChunkCoordIntPair> chunks = (Set<ChunkCoordIntPair>)DimensionManager.getWorld(worldID).activeChunkSet;
//
//			for (ChunkCoordIntPair chunk : chunks){
//				Opis.log.log(Level.INFO, String.format("Dim : %s, %s, Forced : %s", worldID, chunk, persistantChunks.contains(chunk)));
//				chunkStatus.put(chunk, persistantChunks.contains(chunk));
//			}
//		}
	}
}
