package mcp.mobius.opis.helpers;

import net.minecraft.nbt.*;

import java.io.*;

//This will die with packet rewrite.
@Deprecated
public class NBTUtil {

    public static NBTBase getTag(String key, NBTTagCompound tag) {
        String[] path = key.split("\\.");

        NBTTagCompound deepTag = tag;
        for (String i : path) {
            if (deepTag.hasKey(i)) {
                if (deepTag.getTag(i) instanceof NBTTagCompound) {
                    deepTag = deepTag.getCompoundTag(i);
                } else {
                    return deepTag.getTag(i);
                }
            } else {
                return null;
            }
        }
        return deepTag;
    }
	
	/*
	public static NBTTagCompound setTag(String key, NBTTagCompound targetTag, NBTBase addedTag){
		String[] path = key.split("\\.");
		
		NBTTagCompound deepTag = targetTag;
		for (int i = 0; i < path.length - 1; i++){
			if (!deepTag.hasKey(path[i]))
				deepTag.setCompoundTag(path[i], new NBTTagCompound());
			
			deepTag = deepTag.getCompoundTag(path[i]);
		}
		
		deepTag.setTag(path[path.length - 1], addedTag);		
		
		return targetTag;
	}
	
	public static NBTTagCompound createTag(NBTTagCompound inTag, HashSet<String> keys){
		if (keys.contains("*")) return inTag;
		
		NBTTagCompound outTag = new NBTTagCompound();
		
		for (String key : keys){
			NBTBase tagToAdd = getTag(key, inTag);
			//System.out.printf("%s\n", tagToAdd);
			if (tagToAdd != null)
				outTag = setTag(key, outTag, tagToAdd);
		}
		
		return outTag;
	}
	*/

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream ostream) throws IOException {
        if (par0NBTTagCompound == null) {
            ostream.writeShort(-1);
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CompressedStreamTools.writeCompressed(par0NBTTagCompound, out);
            byte[] abyte = out.toByteArray();

            if (abyte.length > 32000) {
                ostream.writeShort(-1);
            } else {
                ostream.writeShort((short) abyte.length);
                ostream.write(abyte);
            }
        }
    }

    public static NBTTagCompound readNBTTagCompound(DataInputStream istream) throws IOException {
        short short1 = istream.readShort();

        if (short1 < 0) {
            return null;
        } else {
            byte[] abyte = new byte[short1];
            istream.readFully(abyte);
            return CompressedStreamTools.readCompressed(new ByteArrayInputStream(abyte));
        }
    }

    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        NBTBase subtag = tag.getTag(keyname);
        if (subtag instanceof NBTTagInt) {
            return tag.getInteger(keyname);
        }
        if (subtag instanceof NBTTagShort) {
            return tag.getShort(keyname);
        }
        if (subtag instanceof NBTTagByte) {
            return tag.getByte(keyname);
        }

        return 0;
    }

}

