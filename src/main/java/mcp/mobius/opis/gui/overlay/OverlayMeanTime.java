//package mcp.mobius.opis.gui.overlay;
//
//import mapwriter.api.*;
//import mcp.mobius.opis.Opis;
//import mcp.mobius.opis.api.IMessageHandler;
//import mcp.mobius.opis.data.holders.ISerializable;
//import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
//import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
//import mcp.mobius.opis.data.holders.basetypes.SerialInt;
//import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
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
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.fml.common.Optional;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.awt.*;
//import java.util.ArrayList;
//
//@Optional.Interface(modid = "mapwriter", iface = "mapwriter.api.IMwDataProvider")
//public class OverlayMeanTime implements IMwDataProvider, IMessageHandler {
//
//    public static OverlayMeanTime INSTANCE = new OverlayMeanTime();
//
//    public class EntitiesTable extends ViewTable {
//
//        IMapView mapView;
//        IMapMode mapMode;
//        OverlayMeanTime overlay;
//
//        public EntitiesTable(IWidget parent, OverlayMeanTime overlay) {
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
//                CoordinatesBlock coord = ((DataBlockTileEntity) row.getObject()).pos;
//
//                if (mapView.getX() != coord.x || mapView.getZ() != coord.z) {
//                    mapView.setViewCentre(coord.x, coord.z);
//                    overlay.requestChunkUpdate(mapView.getDimension(), MathHelper.ceil(mapView.getX()) >> 4, MathHelper.ceil(mapView.getZ()) >> 4);
//                } else {
//                    PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_BLOCK, coord));
//                    Minecraft.getMinecraft().setIngameFocus();
//                }
//            }
//        }
//    }
//
//    public class ChunkOverlay implements IMwChunkOverlay {
//
//        Point coord;
//        int nentities;
//        double time;
//        double minTime;
//        double maxTime;
//        boolean selected;
//
//        public ChunkOverlay(int x, int z, int nentities, double time, double mintime, double maxtime, boolean selected) {
//            coord = new Point(x, z);
//            this.nentities = nentities;
//            this.time = time;
//            minTime = mintime;
//            maxTime = maxtime;
//            this.selected = selected;
//        }
//
//        @Override
//        public Point getCoordinates() {
//            return coord;
//        }
//
//        @Override
//        public int getColor() {
//            //System.out.printf("%s\n", this.maxTime);
//            double scaledTime = time / maxTime;
//            int red = MathHelper.ceil(scaledTime * 255.0);
//            int blue = 255 - MathHelper.ceil(scaledTime * 255.0);
//            //System.out.printf("%s\n", red);
//
//            return (200 << 24) + (red << 16) + blue;
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
//            return selected ? 0xffffffff : 0xff000000;
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
//        double minTime = 9999;
//        double maxTime = 0;
//
//        for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getChunkMeanTime().keySet()) {
//            minTime = Math.min(minTime, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum());
//            maxTime = Math.max(maxTime, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum());
//        }
//
//        for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getChunkMeanTime().keySet()) {
//            if (selectedChunk != null) {
//                overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).tileEntities, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum(), minTime, maxTime, chunk.equals(selectedChunk)));
//            } else {
//                overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).tileEntities, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum(), minTime, maxTime, false));
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
//        int xChunk = bX >> 4;
//        int zChunk = bZ >> 4;
//        CoordinatesChunk chunkCoord = new CoordinatesChunk(dim, xChunk, zChunk);
//
//        if (ChunkManager.INSTANCE.getChunkMeanTime().containsKey(chunkCoord)) {
//            if (Opis.microseconds) {
//                return String.format("%.3f \u00B5s", ChunkManager.INSTANCE.getChunkMeanTime().get(chunkCoord).getDataSum());
//            } else {
//                return String.format(", %.5f ms", ChunkManager.INSTANCE.getChunkMeanTime().get(chunkCoord).getDataSum() / 1000.0);
//            }
//        } else {
//            return "";
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
//
//        if (ChunkManager.INSTANCE.getChunkMeanTime().containsKey(clickedChunk)) {
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
//        if (selectedChunk != null) {
//            PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_TILEENTS, selectedChunk));
//        }
//
//        //ArrayList<CoordinatesChunk> chunks = new ArrayList<CoordinatesChunk>();
//        //for (int x = -5; x <= 5; x++)
//        //	for (int z = -5; z <= 5; z++)
//        //		chunks.add(new CoordinatesChunk(dim, x, z));
//        //PacketDispatcher.sendPacketToServer(Packet_ReqChunks.create(chunks));
//
//    }
//
//    public void setSelectedChunk(int dim, int chunkX, int chunkZ) {
//        selectedChunk = new CoordinatesChunk(dim, chunkX, chunkZ);
//
//        if (selectedChunk != null) {
//            PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_TILEENTS, selectedChunk));
//        }
//    }
//
//    @Override
//    @Optional.Method(modid = "mapwriter")
//    public void onDimensionChanged(int dimension, IMapView mapview) {
//        PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_TIMING, new SerialInt(dimension)));
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
//        PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_TIMING, new SerialInt(mapview.getDimension())));
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
//        if (canvas == null) {
//            canvas = new LayoutCanvas();
//        }
//        //TODO: Investigate why margins were removed, and if there is another way we can detect this.
//        //TODO: Also in OverlayEntityPerChunk
//        //		if (mapmode.marginLeft() != 0){
//        //			this.canvas.hide();
//        //			return;
//        //		}
//
//        if (!showList) {
//            canvas.hide();
//        } else {
//            canvas.show();
//            canvas.draw();
//        }
//
//    }
//
//    @SideOnly (Side.CLIENT)
//    public void setupTable(ArrayList<ISerializable> entities) {
//        if (canvas == null) {
//            canvas = new LayoutCanvas();
//        }
//
//        LayoutBase layout = (LayoutBase) canvas.addWidget("Table", new LayoutBase(null));
//        //layout.setGeometry(new WidgetGeometry(100.0,0.0,300.0,100.0,CType.RELXY, CType.REL_Y, WAlign.RIGHT, WAlign.TOP));
//        layout.setGeometry(new WidgetGeometry(100.0, 0.0, 30.0, 100.0, CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
//        layout.setBackgroundColors(0x90202020, 0x90202020);
//
//        EntitiesTable table = (EntitiesTable) layout.addWidget("Table_", new EntitiesTable(null, this));
//
//        table.setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
//        table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
//                //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
//                .setColumnsTitle("Type", "Pos", "Update Time").setColumnsWidth(50, 25, 25).setRowColors(0xff808080, 0xff505050).setFontSize(1.0f);
//
//        for (ISerializable uncasted : entities) {
//
//            DataBlockTileEntity data = (DataBlockTileEntity) uncasted;
//
//            ItemStack is;
//            String name = String.format("te.%d.%d", data.id, data.meta);
//
//            try {
//                is = new ItemStack(Block.getBlockById(data.id), 1, data.meta);
//                name = is.getDisplayName();
//            } catch (Exception e) {
//            }
//
//			/*
//        	if (name.equals(data.getType()))
//        		name = LanguageRegistry.instance().getStringLocalization(data.getType());
//        	if (name.isEmpty())
//        		name = data.getType();
//        	*/
//
//            if (Opis.microseconds) {
//                table.addRow(data, name, String.format("[ %s %s %s ]", data.pos.x, data.pos.y, data.pos.z), data.update.asMillisecond().toString());
//            } else {
//                table.addRow(data, name, String.format("[ %s %s %s ]", data.pos.x, data.pos.y, data.pos.z), data.update.toString());
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
//            ((EntitiesTable) canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
//            canvas.handleMouseInput();
//            return true;
//        }
//        return false;
//    }
//
//    private void requestChunkUpdate(int dim, int chunkX, int chunkZ) {
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
//            case LIST_CHUNK_TILEENTS: {
//                setupTable(rawdata.array);
//                break;
//            }
//            default:
//                return false;
//        }
//
//        return true;
//    }
//
//}
