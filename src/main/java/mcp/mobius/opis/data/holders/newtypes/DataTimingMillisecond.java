package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataTimingMillisecond extends DataTiming {

    public DataTimingMillisecond() {
        timing = 0.0D;
    }

    public DataTimingMillisecond(double timing) {
        this.timing = timing;
    }

    public String toString() {
        return String.format("%.3f ms", timing / 1000.0 / 1000.0);
    }

    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeDouble(timing);
    }

    public static DataTimingMillisecond readFromStream(DataInputStream stream) throws IOException {
        return new DataTimingMillisecond(stream.readDouble());
    }

}
