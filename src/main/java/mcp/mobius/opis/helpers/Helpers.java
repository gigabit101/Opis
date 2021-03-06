package mcp.mobius.opis.helpers;

import net.minecraftforge.fml.relauncher.Side;

public class Helpers {

    public static Side getEffectiveSide() {
        Thread thr = Thread.currentThread();

        if (thr.getName().equals("Server thread") || thr.getName().contains("Netty IO")) {
            return Side.SERVER;
        }

        return Side.CLIENT;
    }
}
