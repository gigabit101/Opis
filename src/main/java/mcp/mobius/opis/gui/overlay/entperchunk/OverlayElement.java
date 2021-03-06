//package mcp.mobius.opis.gui.overlay.entperchunk;
//
//import mapwriter.api.IMwChunkOverlay;
//import net.minecraft.util.math.MathHelper;
//
//import java.awt.*;
//
//public class OverlayElement implements IMwChunkOverlay {
//
//    Point coord;
//    int minEnts;
//    int maxEnts;
//    int ents;
//    boolean selected;
//
//    public OverlayElement(int x, int z, int minEnts, int maxEnts, int ents, boolean selected) {
//        coord = new Point(x, z);
//        this.minEnts = minEnts;
//        this.maxEnts = maxEnts;
//        this.ents = ents;
//        this.selected = selected;
//    }
//
//    @Override
//    public Point getCoordinates() {
//        return coord;
//    }
//
//    @Override
//    public int getColor() {
//        double scaledAmount = (double) ents / (double) maxEnts;
//        int red = MathHelper.ceil(scaledAmount * 255.0);
//        int blue = 255 - MathHelper.ceil(scaledAmount * 255.0);
//
//        return (200 << 24) + (red << 16) + blue;
//    }
//
//    @Override
//    public float getFilling() {
//        return 1.0f;
//    }
//
//    @Override
//    public boolean hasBorder() {
//        return true;
//    }
//
//    @Override
//    public float getBorderWidth() {
//        return 0.5f;
//    }
//
//    @Override
//    public int getBorderColor() {
//        return selected ? 0xffffffff : 0xff000000;
//    }
//
//}
