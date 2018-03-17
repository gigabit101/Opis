package mcp.mobius.opis.asm;

import codechicken.asm.ASMBlock;
import codechicken.asm.ASMReader;
import codechicken.asm.ModularASMTransformer;
import codechicken.asm.ObfMapping;
import codechicken.asm.transformers.MethodInjector;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Map;

/**
 * Created by covers1624 on 5/03/18.
 */
public class OpisClassTransformer implements IClassTransformer {

    private ModularASMTransformer transformer;

    public OpisClassTransformer() {
        transformer = new ModularASMTransformer("opis");
        Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/opis/asm/blocks.asm");
        ASMBlock voidReturnNeedle = blocks.get("n_ReturnV");
        ObfMapping mapping;
        {
            mapping = new ObfMapping("net/minecraft/world/World", "func_72939_s", "()V");
            ASMBlock needle = blocks.get("n_TileEntityTick");
            transformer.add(new MethodInjector(mapping, needle, blocks.get("i_TileEntityTickPre"), true));
            transformer.add(new MethodInjector(mapping, needle, blocks.get("i_TileEntityTickPost"), false));
        }
        {
            mapping = new ObfMapping("net/minecraft/world/World", "func_72939_s", "()V");
            ASMBlock needle = blocks.get("n_UpdateEntity");
            transformer.add(new MethodInjector(mapping, needle, blocks.get("i_UpdateEntityPre"), true));
            transformer.add(new MethodInjector(mapping, needle, blocks.get("i_UpdateEntityPost"), false));
        }
        {
            mapping = new ObfMapping("net/minecraft/world/WorldServer", "func_72835_b", "()V");
            transformer.add(new MethodInjector(mapping, null, blocks.get("i_WorldServerTickPre"), true));//Top of method.
            transformer.add(new MethodInjector(mapping, voidReturnNeedle, blocks.get("i_WorldServerTickPost"), true));//Before the return statements.
        }
        {
            mapping = new ObfMapping("net/minecraftforge/fml/common/FMLCommonHandler", "onPreWorldTick", "(Lnet/minecraft/world/World;)V");
            transformer.add(new MethodInjector(mapping, null, blocks.get("i_FMLCH_PreWorldTick"), true));
            mapping = new ObfMapping("net/minecraftforge/fml/common/FMLCommonHandler", "onPostWorldTick", "(Lnet/minecraft/world/World;)V");
            transformer.add(new MethodInjector(mapping, voidReturnNeedle, blocks.get("i_FMLCH_PostWorldTick"), true));
        }
        {
            mapping = new ObfMapping("net/minecraftforge/fml/common/FMLCommonHandler", "onPreServerTick", "()V");
            transformer.add(new MethodInjector(mapping, null, blocks.get("i_FMLCH_PreServerTick"), true));
            mapping = new ObfMapping("net/minecraftforge/fml/common/FMLCommonHandler", "onPostServerTick", "()V");
            transformer.add(new MethodInjector(mapping, voidReturnNeedle, blocks.get("i_FMLCH_PostServerTick"), true));
        }
        {
            mapping = new ObfMapping("net/minecraft/network/NettyPacketDecoder", "decode", "(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List;)V");
            transformer.add(new MethodInjector(mapping, blocks.get("n_NPD_decode"), blocks.get("i_NPD_decode"), true));
        }
        {
            mapping = new ObfMapping("net/minecraft/network/NettyPacketEncoder", "encode", "(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;Lio/netty/buffer/ByteBuf;)V");
            transformer.add(new MethodInjector(mapping, blocks.get("n_NPE_encode"), blocks.get("i_NPE_encode"), false));
        }
        {
            mapping = new ObfMapping("net/minecraftforge/fml/common/network/internal/FMLProxyPacket", "func_148833_a", "(Lnet/minecraft/network/INetHandler;)V");
            transformer.add(new MethodInjector(mapping, null, blocks.get("i_FMLPP_processPacket"), true));
        }
        {
            mapping = new ObfMapping("net/minecraftforge/fml/common/network/FMLOutboundHandler", "write", "(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;Lio/netty/channel/ChannelPromise;)V");
            transformer.add(new MethodInjector(mapping, blocks.get("n_FMLOH_write"), blocks.get("i_FMLOH_write"), false));
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return transformer.transform(transformedName, basicClass);
    }
}
