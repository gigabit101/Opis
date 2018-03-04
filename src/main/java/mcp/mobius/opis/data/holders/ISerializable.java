package mcp.mobius.opis.data.holders;

import io.netty.buffer.ByteBuf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;

public interface ISerializable {
	void writeToStream(ByteArrayDataOutput stream);
	//Object readFromStream(DataInputStream stream) throws IOException;
}
