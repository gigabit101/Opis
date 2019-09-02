package mcp.mobius.opis.asm;

import codechicken.asm.*;
import codechicken.asm.transformers.FieldWriter;
import codechicken.asm.transformers.MethodInjector;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Map;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;

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
            forceMap(mapping);
            transformer.add(new MethodInjector(mapping, null, blocks.get("i_FMLCH_PreWorldTick"), true));
            mapping = new ObfMapping("net/minecraftforge/fml/common/FMLCommonHandler", "onPostWorldTick", "(Lnet/minecraft/world/World;)V");
            forceMap(mapping);
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
            mapping = new ObfMapping("net/minecraft/network/NettyPacketEncoder", "encode", "(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;Lio/netty/buffer/ByteBuf;)V");
            transformer.add(new MethodInjector(mapping, blocks.get("n_NPE_encode"), blocks.get("i_NPE_encode"), false));
        }
        {
            mapping = new ObfMapping("net/minecraftforge/fml/common/network/internal/FMLProxyPacket", "func_148833_a", "(Lnet/minecraft/network/INetHandler;)V");
            forceMap(mapping);
            transformer.add(new MethodInjector(mapping, null, blocks.get("i_FMLPP_processPacket"), true));
            mapping = new ObfMapping("net/minecraftforge/fml/common/network/FMLOutboundHandler", "write", "(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;Lio/netty/channel/ChannelPromise;)V");
            transformer.add(new MethodInjector(mapping, blocks.get("n_FMLOH_write"), blocks.get("i_FMLOH_write"), false));
        }
        {
            mapping = new ObfMapping("net/minecraftforge/fml/common/eventhandler/ASMEventHandler", "handler_method", "Ljava/lang/String;");
            transformer.add(new FieldWriter(ACC_PRIVATE | ACC_FINAL, mapping));
            mapping = new ObfMapping("net/minecraftforge/fml/common/eventhandler/ASMEventHandler", "<init>", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Lnet/minecraftforge/fml/common/ModContainer;Z)V");
            transformer.add(new MethodInjector(mapping, blocks.get("i_init"), true));
            mapping = new ObfMapping("net/minecraftforge/fml/common/eventhandler/ASMEventHandler", "invoke", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)V");
            ASMBlock needle = blocks.get("n_invoke");
            transformer.add(new MethodInjector(mapping, needle, blocks.get("i_invoke_pre"), true));
            transformer.add(new MethodInjector(mapping, needle, blocks.get("i_invoke_post"), false));
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return transformer.transform(name, basicClass);
    }

    public static void forceMap(ObfMapping mapping) {
        if(ObfMapping.obfuscated) {
            mapping.map(ObfMapping.obfMapper);
        }
    }
}
