//package mcp.mobius.opis.gui.overlay;
//
//import mapwriter.api.*;
//import mcp.mobius.opis.api.IMessageHandler;
//import mcp.mobius.opis.data.holders.ISerializable;
//import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
//import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
//import mcp.mobius.opis.data.holders.basetypes.SerialInt;
//import mcp.mobius.opis.data.holders.basetypes.TicketData;
//import mcp.mobius.opis.data.managers.ChunkManager;
//import mcp.mobius.opis.gui.events.MouseEvent;
//import mcp.mobius.opis.gui.interfaces.CType;
//import mcp.mobius.opis.gui.interfaces.IWidget;
//import mcp.mobius.opis.gui.interfaces.WAlign;
//import mcp.mobius.opis.gui.widgets.LayoutBase;
//import mcp.mobius.opis.gui.widgets.LayoutCanvas;
//import mcp.mobius.opis.gui.widgets.WidgetGeometry;
//import mcp.mobius.opis.gui.widgets.tableview.TableRow;
//import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
//import mcp.mobius.opis.network.PacketBase;
//import mcp.mobius.opis.network.PacketManager;
//import mcp.mobius.opis.network.enums.Message;
//import mcp.mobius.opis.network.packets.client.PacketReqChunks;
//import mcp.mobius.opis.network.packets.client.PacketReqData;
//import net.minecraft.client.Minecraft;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.fml.common.Optional;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.awt.*;
//import java.util.ArrayList;
//
//@Optional.Interface(modid = "mapwriter", iface = "mapwriter.api.IMwDataProvider")
//public class OverlayLoadedChunks implements IMwDataProvider, IMessageHandler {
//
//    public static OverlayLoadedChunks INSTANCE = new OverlayLoadedChunks();
//
//    public class TicketTable extends ViewTable {
//
//        IMapView mapView;
//        IMapMode mapMode;
//        OverlayLoadedChunks overlay;
//
//        public TicketTable(IWidget parent, OverlayLoadedChunks overlay) {
//            super(parent);
//            this.overlay = overlay;
//        }
//
//        public void setMap(IMapView mapView, IMapMode mapMode) {
//            this.mapView = mapView;
//            this.mapMode = mapMode;
//        }
//
//        @Override
//        public void onMouseClick(MouseEvent event) {
//            TableRow row = getRow(event.x, event.y);
//            if (row != null) {
//                CoordinatesChunk coord = ((TicketData) row.getObject()).coord;
//
//                if (mapView.getX() != coord.x || mapView.getZ() != coord.z || mapView.getDimension() != coord.dim) {
//                    mapView.setDimension(coord.dim);
//                    mapView.setViewCentre(coord.x, coord.z);
//                    overlay.requestChunkUpdate(mapView.getDimension(), MathHelper.ceil(mapView.getX()) >> 4, MathHelper.ceil(mapView.getZ()) >> 4);
//                } else {
//                    PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_BLOCK, new CoordinatesBlock(coord)));
//                    Minecraft.getMinecraft().setIngameFocus();
//                }
//            }
//        }
//    }
//
//    public class ChunkOverlay implements IMwChunkOverlay {
//
//        Point coord;
//        boolean forced;
//
//        public ChunkOverlay(int x, int z, boolean forced) {
//            coord = new Point(x, z);
//            this.forced = forced;
//        }
//
//        @Override
//        public Point getCoordinates() {
//            return coord;
//        }
//
//        @Override
//        public int getColor() {
//            return forced ? 0x500000ff : 0x5000ff00;
//        }
//
//        @Override
//        public float getFilling() {
//            return 1.0f;
//        }
//
//        @Override
//        public boolean hasBorder() {
//            return true;
//        }
//
//        @Override
//        public float getBorderWidth() {
//            return 0.5f;
//        }
//
//        @Override
//        public int getBorderColor() {
//            return 0xff000000;
//        }
//
//    }
//
//    CoordinatesChunk selectedChunk = null;
//    public boolean showList = false;
//    public LayoutCanvas canvas = null;
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
//        ArrayList<IMwChunkOverlay> overlays = new ArrayList<>();
//
//        for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getLoadedChunks()) {
//            overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, chunk.metadata != 0));
//        }
//        return overlays;
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public ILabelInfo getLabelInfo(int i, int i1) {
//        return null;
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public String getStatusString(int dim, int bX, int bY, int bZ) {
//        int xChunk = bX >> 4;
//        int zChunk = bZ >> 4;
//
//        for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getLoadedChunks()) {
//            if (chunk.chunkX == xChunk && chunk.chunkZ == zChunk && chunk.metadata == 0) {
//                return ", Game loaded";
//            } else if (chunk.chunkX == xChunk && chunk.chunkZ == zChunk && chunk.metadata == 1) {
//                return ", Force loaded";
//            }
//        }
//
//        return "";
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onMiddleClick(int dim, int bX, int bZ, IMapView mapview) {
//        int chunkX = bX >> 4;
//        int chunkZ = bZ >> 4;
//        requestChunkUpdate(dim, chunkX, chunkZ);
//    }
//
//    private void requestChunkUpdate(int dim, int chunkX, int chunkZ) {
//        ArrayList<CoordinatesChunk> chunks = new ArrayList<>();
//        for (int x = -5; x <= 5; x++) {
//            for (int z = -5; z <= 5; z++) {
//                chunks.add(new CoordinatesChunk(dim, chunkX + x, chunkZ + z));
//                if (chunks.size() >= 1) {
//                    PacketManager.sendToServer(new PacketReqChunks(dim, chunks));
//                    chunks.clear();
//                }
//            }
//        }
//
//        if (chunks.size() > 0) {
//            PacketManager.sendToServer(new PacketReqChunks(dim, chunks));
//        }
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onDimensionChanged(int dimension, IMapView mapview) {
//        PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_LOADED, new SerialInt(dimension)));
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onMapCenterChanged(double vX, double vZ, IMapView mapview) {
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onZoomChanged(int level, IMapView mapview) {
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onOverlayActivated(IMapView mapview) {
//        selectedChunk = null;
//        PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_LOADED, new SerialInt(mapview.getDimension())));
//        PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_TICKETS));
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onOverlayDeactivated(IMapView mapview) {
//        showList = false;
//        selectedChunk = null;
//        PacketManager.sendToServer(new PacketReqData(Message.COMMAND_UNREGISTER));
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onDraw(IMapView mapview, IMapMode mapmode) {
//    }
//
//    @SideOnly (Side.CLIENT)
//    public void setupTable(ArrayList<TicketData> tickets) {
//        if (canvas == null) {
//            canvas = new LayoutCanvas();
//        }
//
//        LayoutBase layout = (LayoutBase) canvas.addWidget("Table", new LayoutBase(null));
//        layout.setGeometry(new WidgetGeometry(100.0, 0.0, 30.0, 100.0, CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
//        layout.setBackgroundColors(0x90202020, 0x90202020);
//
//        TicketTable table = (TicketTable) layout.addWidget("Table_", new TicketTable(null, this));
//
//        table.setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
//        table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER).setColumnsTitle("Pos", "Mod", "Chunks").setColumnsWidth(50, 25, 25).setRowColors(0xff808080, 0xff505050).setFontSize(1.0f);
//
//        for (TicketData data : tickets) {
//            table.addRow(data, String.format("[%s %s %s]", data.coord.dim, data.coord.chunkX, data.coord.chunkZ), data.modID, String.valueOf(data.nchunks));
//        }
//
//        showList = true;
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public boolean onMouseInput(IMapView mapview, IMapMode mapmode) {
//        if (canvas != null && canvas.shouldRender() && canvas.hasWidgetAtCursor()) {
//            ((TicketTable) canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
//            canvas.handleMouseInput();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean handleMessage(Message msg, PacketBase rawdata) {
//        switch (msg) {
//            case LIST_CHUNK_TICKETS: {
//                ArrayList<TicketData> arr = new ArrayList<>();
//                for (ISerializable s : rawdata.array) {
//                    arr.add((TicketData) s);
//                }
//                setupTable(arr);
//                break;
//            }
//            default:
//                return false;
//        }
//        return true;
//    }
//}
