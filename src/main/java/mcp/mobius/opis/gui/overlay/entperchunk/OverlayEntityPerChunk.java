//package mcp.mobius.opis.gui.overlay.entperchunk;
//
//import mapwriter.api.*;
//import mcp.mobius.opis.api.IMessageHandler;
//import mcp.mobius.opis.data.holders.ISerializable;
//import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
//import mcp.mobius.opis.data.holders.basetypes.SerialInt;
//import mcp.mobius.opis.data.holders.newtypes.DataChunkEntities;
//import mcp.mobius.opis.data.holders.newtypes.DataEntity;
//import mcp.mobius.opis.gui.interfaces.CType;
//import mcp.mobius.opis.gui.interfaces.WAlign;
//import mcp.mobius.opis.gui.widgets.LayoutBase;
//import mcp.mobius.opis.gui.widgets.LayoutCanvas;
//import mcp.mobius.opis.gui.widgets.WidgetGeometry;
//import mcp.mobius.opis.network.PacketBase;
//import mcp.mobius.opis.network.PacketManager;
//import mcp.mobius.opis.network.enums.Message;
//import mcp.mobius.opis.network.packets.client.PacketReqChunks;
//import mcp.mobius.opis.network.packets.client.PacketReqData;
//import net.minecraftforge.fml.common.Optional;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//
//@Optional.Interface(modid = "mapwriter", iface = "mapwriter.api.IMwDataProvider")
//public class OverlayEntityPerChunk implements IMwDataProvider, IMessageHandler {
//
//    public static OverlayEntityPerChunk INSTANCE = new OverlayEntityPerChunk();
//
//    class ReducedData implements Comparable {
//
//        CoordinatesChunk chunk;
//        int amount;
//
//        public ReducedData(CoordinatesChunk chunk, int amount) {
//            this.chunk = chunk;
//            this.amount = amount;
//        }
//
//        @Override
//        public int compareTo(Object arg0) {
//            return ((ReducedData) arg0).amount - amount;
//        }
//
//    }
//
//    public boolean showList = false;
//    public LayoutCanvas canvas = null;
//    public HashMap<CoordinatesChunk, Integer> overlayData = new HashMap<>();
//    public ArrayList<ReducedData> reducedData = new ArrayList<>();
//    public ArrayList<DataEntity> entStats = new ArrayList<>();
//    public CoordinatesChunk selectedChunk = null;
//
//    public void setEntStats(ArrayList<ISerializable> data) {
//        entStats.clear();
//        for (ISerializable stat : data) {
//            entStats.add((DataEntity) stat);
//        }
//    }
//
//    public void reduceData() {
//        reducedData.clear();
//        for (CoordinatesChunk chunk : overlayData.keySet()) {
//            reducedData.add(new ReducedData(chunk, overlayData.get(chunk)));
//        }
//        Collections.sort(reducedData);
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
//        ArrayList<IMwChunkOverlay> overlays = new ArrayList<>();
//
//        int minEnts = 9999;
//        int maxEnts = 0;
//
//        for (CoordinatesChunk chunk : overlayData.keySet()) {
//            minEnts = Math.min(minEnts, overlayData.get(chunk));
//            maxEnts = Math.max(maxEnts, overlayData.get(chunk));
//        }
//
//        for (CoordinatesChunk chunk : overlayData.keySet()) {
//            if (chunk.dim == dim) {
//                if (selectedChunk != null) {
//                    overlays.add(new OverlayElement(chunk.toChunkCoordIntPair().x, chunk.toChunkCoordIntPair().z, minEnts, maxEnts, overlayData.get(chunk), chunk.equals(selectedChunk)));
//                } else {
//                    overlays.add(new OverlayElement(chunk.toChunkCoordIntPair().x, chunk.toChunkCoordIntPair().z, minEnts, maxEnts, overlayData.get(chunk), false));
//                }
//            }
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
//        CoordinatesChunk chunk = new CoordinatesChunk(dim, bX >> 4, bZ >> 4);
//        if (overlayData.containsKey(chunk)) {
//            return String.format(", entities: %d", overlayData.get(chunk));
//        } else {
//            return ", entities: 0";
//        }
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onMiddleClick(int dim, int bX, int bZ, IMapView mapview) {
//        showList = false;
//
//        int chunkX = bX >> 4;
//        int chunkZ = bZ >> 4;
//        CoordinatesChunk clickedChunk = new CoordinatesChunk(dim, chunkX, chunkZ);
//        CoordinatesChunk prevSelected = selectedChunk;
//
//        if (overlayData.containsKey(clickedChunk)) {
//            if (selectedChunk == null) {
//                selectedChunk = clickedChunk;
//            } else if (selectedChunk.equals(clickedChunk)) {
//                selectedChunk = null;
//            } else {
//                selectedChunk = clickedChunk;
//            }
//        } else {
//            selectedChunk = null;
//        }
//
//        if (selectedChunk == null) {
//            showList = true;
//        }
//
//        if (prevSelected == null && selectedChunk != null) {
//            PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_ENTITIES, selectedChunk));
//        } else if (selectedChunk != null && !selectedChunk.equals(prevSelected)) {
//            PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_ENTITIES, selectedChunk));
//        } else if (selectedChunk == null) {
//            PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_ENTITIES));
//        }
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onDimensionChanged(int dimension, IMapView mapview) {
//        PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_ENTITIES, new SerialInt(dimension)));
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
//        PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_ENTITIES));
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onOverlayDeactivated(IMapView mapview) {
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onDraw(IMapView mapview, IMapMode mapmode) {
//    }
//
//    @SideOnly (Side.CLIENT)
//    public void setupChunkTable() {
//        if (canvas == null) {
//            canvas = new LayoutCanvas();
//        }
//
//        if (canvas.hasWidget("Table")) {
//            canvas.delWidget("Table");
//        }
//
//        LayoutBase layout = (LayoutBase) canvas.addWidget("Table", new LayoutBase(null));
//        layout.setGeometry(new WidgetGeometry(100.0, 0.0, 20.0, 100.0, CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
//        layout.setBackgroundColors(0x90202020, 0x90202020);
//
//        TableChunks table = (TableChunks) layout.addWidget("Table_", new TableChunks(null, this));
//
//        table.setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
//        table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER).setColumnsTitle("Pos", "N Entities").setColumnsWidth(75, 25).setRowColors(0xff808080, 0xff505050).setFontSize(1.0f);
//
//        int nrows = 0;
//        for (ReducedData data : reducedData) {
//            table.addRow(data, data.chunk.toString(), String.valueOf(data.amount));
//            nrows++;
//            if (nrows > 100) {
//                break;
//            }
//        }
//
//        showList = true;
//    }
//
//    @SideOnly (Side.CLIENT)
//    public void setupEntTable() {
//        if (canvas == null) {
//            canvas = new LayoutCanvas();
//        }
//
//        if (canvas.hasWidget("Table")) {
//            canvas.delWidget("Table");
//        }
//
//        LayoutBase layout = (LayoutBase) canvas.addWidget("Table", new LayoutBase(null));
//        layout.setGeometry(new WidgetGeometry(100.0, 0.0, 20.0, 100.0, CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
//        layout.setBackgroundColors(0x90202020, 0x90202020);
//
//        TableEntities table = (TableEntities) layout.addWidget("Table_", new TableEntities(null, this));
//
//        table.setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
//        table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER).setColumnsTitle("Name", "Pos").setColumnsWidth(75, 25).setRowColors(0xff808080, 0xff505050).setFontSize(1.0f);
//
//        int nrows = 0;
//        for (DataEntity data : entStats) {
//
//            String name = data.name.toString();
//
//            table.addRow(data, name, String.format("[ %d %d %d ]", data.pos.x, data.pos.y, data.pos.z));
//            nrows++;
//            if (nrows > 100) {
//                break;
//            }
//        }
//
//        showList = true;
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public boolean onMouseInput(IMapView mapview, IMapMode mapmode) {
//        if (canvas != null && canvas.shouldRender() && canvas.hasWidgetAtCursor()) {
//
//            if (canvas.getWidget("Table").getWidget("Table_") instanceof TableEntities) {
//                ((TableEntities) canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
//            } else {
//                ((TableChunks) canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
//            }
//
//            canvas.handleMouseInput();
//            return true;
//        }
//        return false;
//    }
//
//    public void requestChunkUpdate(int dim, int chunkX, int chunkZ) {
//        ArrayList<CoordinatesChunk> chunks = new ArrayList<>();
//
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
//    public boolean handleMessage(Message msg, PacketBase rawdata) {
//        switch (msg) {
//            case LIST_CHUNK_ENTITIES: {
//                setEntStats(rawdata.array);
//                setupEntTable();
//                break;
//            }
//            //TODO look into why this is Unhandled
//            case OVERLAY_CHUNK_ENTITIES: {
//                HashMap<CoordinatesChunk, Integer> chunkStatus = new HashMap<>();
//                for (ISerializable s : rawdata.array) {
//                    chunkStatus.put(((DataChunkEntities) s).chunk, ((DataChunkEntities) s).entities);
//                }
//                overlayData = chunkStatus;
//                reduceData();
//                setupChunkTable();
//            }
//
//            default:
//                return false;
//        }
//        return true;
//    }
//
//}
