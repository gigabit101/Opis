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
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return transformer.transform(transformedName, basicClass);
    }
}
