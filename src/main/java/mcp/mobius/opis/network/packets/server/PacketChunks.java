package mcp.mobius.opis.network.packets.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mapwriter.Mw;
import mapwriter.region.MwChunk;
import mcp.mobius.opis.network.PacketBase;

public class PacketChunks extends PacketBase{

	public int  dim;
	public MwChunk[] chunks;	
	
	@Override
	public void encode(ByteArrayDataOutput output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decode(ByteArrayDataInput input) {
		// TODO Auto-generated method stub
		
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player){
    	Mw.instance.chunkManager.forceChunks(this.chunks);
    }		
	
}
