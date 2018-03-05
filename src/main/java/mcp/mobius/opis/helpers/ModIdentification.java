package mcp.mobius.opis.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

//TODO Covers: Rewrite this, MC has a mod container.. why? what?
public class ModIdentification {

    public static HashMap<String, String> modSource_Name = new HashMap<String, String>();
    public static HashMap<String, String> modSource_ID = new HashMap<String, String>();
    public static HashMap<Integer, String> itemMap = new HashMap<Integer, String>();
    public static HashMap<String, String> keyhandlerStrings = new HashMap<String, String>();

    public static void init() {

        for (ModContainer mod : Loader.instance().getModList()) {
            modSource_Name.put(mod.getSource().getName(), mod.getName());
            modSource_ID.put(mod.getSource().getName(), mod.getModId());
        }

        //TODO : Update this to match new version (1.7.2)
        modSource_Name.put("1.6.2.jar", "Minecraft");
        modSource_Name.put("1.6.3.jar", "Minecraft");
        modSource_Name.put("1.6.4.jar", "Minecraft");
        modSource_Name.put("1.7.2.jar", "Minecraft");
        modSource_Name.put("Forge", "Minecraft");
        modSource_ID.put("1.6.2.jar", "Minecraft");
        modSource_ID.put("1.6.3.jar", "Minecraft");
        modSource_ID.put("1.6.4.jar", "Minecraft");
        modSource_ID.put("1.7.2.jar", "Minecraft");
        modSource_ID.put("Forge", "Minecraft");
    }

    public static String nameFromObject(Object obj) {
        String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        try {
            objPath = URLDecoder.decode(objPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String modName = "<Unknown>";
        for (String s : modSource_Name.keySet())
            if (objPath.contains(s)) {
                modName = modSource_Name.get(s);
                break;
            }

        if (modName.equals("Minecraft Coder Pack"))
            modName = "Minecraft";

        return modName;
    }

    public static String nameFromStack(ItemStack stack) {
        try {
            //String modID = itemMap.get(itemstack.itemID);
            //ModContainer mod = ModIdentification.findModContainer(modID);
            ModContainer mod = FMLCommonHandler.instance().findContainerFor(stack.getItem().getRegistryName().getResourceDomain());
            return mod == null ? "Minecraft" : mod.getName();
        } catch (NullPointerException e) {
            //System.out.printf("NPE : %s\n",itemstack.toString());
            return "";
        }
    }

    public static String getStackName(int id, int meta) {
        ItemStack is;
        String name = String.format("te.%d.%d", id, meta);

        try {
            is = new ItemStack(Block.getBlockById(id), 1, meta);
            name = is.getDisplayName();
        } catch (Exception e) {
        }

        return name;
    }

    public static String getModStackName(int id, int meta) {
        ItemStack is;
        String modID = "<UNKNOWN>";

        try {
            is = new ItemStack(Block.getBlockById(id), 1, meta);
            modID = nameFromStack(is);
        } catch (Exception e) {
        }

        return modID;
    }
}
