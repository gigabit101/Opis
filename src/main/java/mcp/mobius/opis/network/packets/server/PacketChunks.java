//package mcp.mobius.opis.network.packets.server;
//
//import io.netty.buffer.ByteBuf;
//import mapwriter.Mw;
//import mapwriter.region.MwChunk;
//import mcp.mobius.opis.network.PacketBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class PacketChunks extends PacketBase {
//
//    public int dim;
//    public MwChunk[] chunks;
//
//    @Override
//    public void encode(ByteBuf output) {
//    }
//
//    @Override
//    public void decode(ByteBuf input) {
//    }
//
//    @Override
//    @SideOnly (Side.CLIENT)
//    public void actionClient(World world, EntityPlayer player) {
//        Mw.getInstance().chunkManager.updateSurfaceChunks();
//    }
//}
