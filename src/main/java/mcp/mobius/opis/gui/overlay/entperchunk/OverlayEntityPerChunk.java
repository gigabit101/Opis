package mcp.mobius.opis.gui.overlay.entperchunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import mapwriter.api.*;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.newtypes.DataChunkEntities;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LayoutBase;
import mcp.mobius.opis.gui.widgets.LayoutCanvas;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqChunks;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OverlayEntityPerChunk implements IMwDataProvider, IMessageHandler {

	public static OverlayEntityPerChunk INSTANCE = new OverlayEntityPerChunk();

	class ReducedData implements Comparable {
		CoordinatesChunk chunk;
		int amount;

		public ReducedData(CoordinatesChunk chunk, int amount){
			this.chunk = chunk;
			this.amount = amount;
		}

		@Override
		public int compareTo(Object arg0) {
			return ((ReducedData)arg0).amount - this.amount;
		}

	}

	public boolean    showList = false;
	public LayoutCanvas canvas = null;
	public HashMap<CoordinatesChunk, Integer> overlayData = new HashMap<CoordinatesChunk, Integer>();
	public ArrayList<ReducedData> reducedData = new ArrayList<ReducedData>();
	public ArrayList<DataEntity> entStats    = new ArrayList<DataEntity>();
	public CoordinatesChunk selectedChunk = null;

	public void setEntStats(ArrayList<ISerializable> data){
		this.entStats.clear();
		for (ISerializable stat : data)
			this.entStats.add((DataEntity)stat);
	}

	public void reduceData(){
		this.reducedData.clear();
		for (CoordinatesChunk chunk : this.overlayData.keySet())
			this.reducedData.add(new ReducedData(chunk, this.overlayData.get(chunk)));
		Collections.sort(this.reducedData);
	}

	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();

		int minEnts = 9999;
		int maxEnts = 0;

		for (CoordinatesChunk chunk : overlayData.keySet()){
			minEnts = Math.min(minEnts, overlayData.get(chunk));
			maxEnts = Math.max(maxEnts, overlayData.get(chunk));
		}

		for (CoordinatesChunk chunk : overlayData.keySet()){
			if (chunk.dim == dim)
				if (this.selectedChunk != null)
					overlays.add(new OverlayElement(chunk.toChunkCoordIntPair().x, chunk.toChunkCoordIntPair().z, minEnts, maxEnts, overlayData.get(chunk), chunk.equals(this.selectedChunk)));
				else
					overlays.add(new OverlayElement(chunk.toChunkCoordIntPair().x, chunk.toChunkCoordIntPair().z, minEnts, maxEnts, overlayData.get(chunk), false));
		}
		return overlays;
	}

    @Override
    public ILabelInfo getLabelInfo(int i, int i1) {
        return null;
    }

    @Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		CoordinatesChunk chunk = new CoordinatesChunk(dim, bX >> 4, bZ >> 4);
		if (this.overlayData.containsKey(chunk))
			return String.format(", entities: %d", this.overlayData.get(chunk));
		else
			return ", entities: 0";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, IMapView mapview) {
		this.showList = false;

		int chunkX = bX >> 4;
		int chunkZ = bZ >> 4;
		CoordinatesChunk clickedChunk = new CoordinatesChunk(dim, chunkX, chunkZ);
		CoordinatesChunk prevSelected = this.selectedChunk;

		if (this.overlayData.containsKey(clickedChunk)){
			if (this.selectedChunk == null)
				this.selectedChunk = clickedChunk;
			else if (this.selectedChunk.equals(clickedChunk))
				this.selectedChunk = null;
			else
				this.selectedChunk = clickedChunk;
		} else {
			this.selectedChunk = null;
		}

		if (this.selectedChunk == null)
			this.showList = true;

		if (prevSelected == null && this.selectedChunk != null)
			PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_ENTITIES, this.selectedChunk));

		else if (this.selectedChunk != null && !this.selectedChunk.equals(prevSelected))
			PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_ENTITIES, this.selectedChunk));

		else if (this.selectedChunk == null)
			PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_ENTITIES));
	}

	@Override
	public void onDimensionChanged(int dimension, IMapView mapview) {
        PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_ENTITIES, new SerialInt(dimension)));
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, IMapView mapview) {}

	@Override
	public void onZoomChanged(int level, IMapView mapview) {}

	@Override
	public void onOverlayActivated(IMapView mapview) {
		PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_ENTITIES));
	}

	@Override
	public void onOverlayDeactivated(IMapView mapview) {}

	@Override
	public void onDraw(IMapView mapview, IMapMode mapmode) {}

	@SideOnly (Side.CLIENT)
	public void setupChunkTable(){
		if (this.canvas == null)
			this.canvas = new LayoutCanvas();

		if (this.canvas.hasWidget("Table"))
			this.canvas.delWidget("Table");

		LayoutBase layout = (LayoutBase)this.canvas.addWidget("Table", new LayoutBase(null));
		layout.setGeometry(new WidgetGeometry(100.0,0.0,20.0,100.0,CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
		layout.setBackgroundColors(0x90202020, 0x90202020);

		TableChunks table  = (TableChunks)layout.addWidget("Table_", new TableChunks(null, this));

		table.setGeometry(new WidgetGeometry(0.0,0.0,100.0,100.0,CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER)
	    	 .setColumnsTitle("Pos", "N Entities")
			 .setColumnsWidth(75,25)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);

		int nrows = 0;
		for (ReducedData data : this.reducedData){
				table.addRow(data, data.chunk.toString(), String.valueOf(data.amount));
				nrows++;
				if (nrows > 100) break;
		}

		this.showList = true;
	}

	@SideOnly(Side.CLIENT)
	public void setupEntTable(){
		if (this.canvas == null)
			this.canvas = new LayoutCanvas();

		if (this.canvas.hasWidget("Table"))
			this.canvas.delWidget("Table");

		LayoutBase layout = (LayoutBase)this.canvas.addWidget("Table", new LayoutBase(null));
		layout.setGeometry(new WidgetGeometry(100.0,0.0,20.0,100.0,CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
		layout.setBackgroundColors(0x90202020, 0x90202020);

		TableEntities table  = (TableEntities)layout.addWidget("Table_", new TableEntities(null, this));

		table.setGeometry(new WidgetGeometry(0.0,0.0,100.0,100.0,CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER)
	    	 .setColumnsTitle("Name", "Pos")
			 .setColumnsWidth(75,25)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);

		int nrows = 0;
		for (DataEntity data : this.entStats){

			String name = data.name.toString();

			table.addRow(data, name, String.format("[ %d %d %d ]", data.pos.x, data.pos.y, data.pos.z));
			nrows++;
			if (nrows > 100) break;
		}

		this.showList = true;
	}

	@Override
	public boolean onMouseInput(IMapView mapview, IMapMode mapmode) {
		if (this.canvas != null && this.canvas.shouldRender() && ((LayoutCanvas)this.canvas).hasWidgetAtCursor()){

			if (this.canvas.getWidget("Table").getWidget("Table_") instanceof TableEntities)
				((TableEntities)this.canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
			else
				((TableChunks)this.canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);

			this.canvas.handleMouseInput();
			return true;
		}
		return false;
	}

	public void requestChunkUpdate(int dim, int chunkX, int chunkZ){
		ArrayList<CoordinatesChunk> chunks = new ArrayList<CoordinatesChunk>();

		for (int x = -5; x <= 5; x++){
			for (int z = -5; z <= 5; z++){
				chunks.add(new CoordinatesChunk(dim, chunkX + x, chunkZ + z));
				if (chunks.size() >= 1){
					PacketManager.sendToServer(new PacketReqChunks(dim, chunks));
					chunks.clear();
				}
			}
		}

		if (chunks.size() > 0)
			PacketManager.sendToServer(new PacketReqChunks(dim, chunks));
	}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		switch(msg){
		case LIST_CHUNK_ENTITIES:{
			this.setEntStats(rawdata.array);
			this.setupEntTable();
			break;
		}
		//TODO look into why this is Unhandled
		case OVERLAY_CHUNK_ENTITIES:{
			HashMap<CoordinatesChunk, Integer> chunkStatus = new HashMap<CoordinatesChunk, Integer>();
			for (ISerializable s : rawdata.array){
				chunkStatus.put(((DataChunkEntities)s).chunk, ((DataChunkEntities)s).entities);
			}
			this.overlayData = chunkStatus;
			this.reduceData();
			this.setupChunkTable();
		}

		default:
			return false;
		}
		return true;
	}
	
}
