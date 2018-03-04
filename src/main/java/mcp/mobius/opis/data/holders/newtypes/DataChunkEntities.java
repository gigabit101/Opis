package mcp.mobius.opis.data.holders.newtypes;

import java.util.ArrayList;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TileEntityManager;

public class DataChunkEntities implements ISerializable{

	public int entities;
	public CoordinatesChunk chunk;	
	
	public DataChunkEntities(CoordinatesChunk chunk, int entities){
		this.chunk     = chunk;
		this.entities  = entities;
	}
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.chunk.writeToStream(stream);
		stream.writeInt(this.entities);
	}

	public static DataChunkEntities readFromStream(ByteArrayDataInput stream){
		DataChunkEntities retVal = new DataChunkEntities(CoordinatesChunk.readFromStream(stream), stream.readInt());
		return retVal;
	}	
	
}
