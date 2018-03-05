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
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return transformer.transform(transformedName, basicClass);
    }
}
