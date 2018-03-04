package mcp.mobius.opis.gui.overlay;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import mapwriter.Mw;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LayoutBase;
import mcp.mobius.opis.gui.widgets.LayoutCanvas;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqChunks;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.swing.widgets.JTableStats;

public enum OverlayLoadedChunks implements IMwDataProvider, IMessageHandler {
	INSTANCE;
	
	public class TicketTable extends ViewTable{
		MapView mapView;
		MapMode mapMode;
		OverlayLoadedChunks overlay;
		
		public TicketTable(IWidget parent, OverlayLoadedChunks overlay) { 	
			super(parent);
			this.overlay = overlay;
		}
		
		public void setMap(MapView mapView, MapMode mapMode){
		    this.mapView = mapView;
			this.mapMode = mapMode;			
		}
		
		@Override
		public void onMouseClick(MouseEvent event){
			TableRow row = this.getRow(event.x, event.y);
			if (row != null){
				CoordinatesChunk coord = ((TicketData)row.getObject()).coord;
				
				if (this.mapView.getX() != coord.x || this.mapView.getZ() != coord.z || this.mapView.getDimension() != coord.dim){
					this.mapView.setDimension(coord.dim);
					this.mapView.setViewCentre(coord.x, coord.z);
					this.overlay.requestChunkUpdate(this.mapView.getDimension(), 
							MathHelper.ceiling_double_int(this.mapView.getX()) >> 4, 
							MathHelper.ceiling_double_int(this.mapView.getZ()) >> 4);
				} else {
					PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_BLOCK, new CoordinatesBlock(coord)));					
					Minecraft.getMinecraft().setIngameFocus();
				}
			}
		}
	}	
	
	public class ChunkOverlay implements IMwChunkOverlay{

		Point coord;
		boolean forced;
		
		public ChunkOverlay(int x, int z, boolean forced){
			this.coord = new Point(x, z);
			this.forced = forced;
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {	return this.forced ? 0x500000ff : 0x5000ff00; }

		@Override
		public float getFilling() {	return 1.0f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.5f; }

		@Override
		public int getBorderColor() { return 0xff000000; }
		
	}	
	
	//public static OverlayLoadedChunks instance = new OverlayLoadedChunks();
	//private OverlayLoadedChunks(){};
	
	CoordinatesChunk selectedChunk = null;
	public boolean    showList = false;
	public LayoutCanvas canvas = null;
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX,	double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();
		
		for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getLoadedChunks()){
			overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, chunk.metadata == 0 ? false : true));
		}
		return overlays;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		int xChunk = bX >> 4;
		int zChunk = bZ >> 4;
		
		for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getLoadedChunks()){
			if (chunk.chunkX == xChunk && chunk.chunkZ == zChunk && chunk.metadata == 0)
				return ", Game loaded";
			
			else if (chunk.chunkX == xChunk && chunk.chunkZ == zChunk && chunk.metadata == 1)
				return ", Force loaded";			
		}		
		
		return "";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview) {
		int chunkX = bX >> 4;
		int chunkZ = bZ >> 4;
		this.requestChunkUpdate(dim, chunkX, chunkZ);
	}

	private void requestChunkUpdate(int dim, int chunkX, int chunkZ){
		ArrayList<CoordinatesChunk> chunks = new ArrayList<CoordinatesChunk>();
		//HashSet<ChunkCoordIntPair> chunkCoords = new HashSet<ChunkCoordIntPair>(); 

		for (int x = -5; x <= 5; x++){
			for (int z = -5; z <= 5; z++){
				chunks.add(new CoordinatesChunk(dim, chunkX + x, chunkZ + z));
				if (chunks.size() >= 1){
					PacketManager.sendToServer(new PacketReqChunks(dim, chunks));	
					chunks.clear();
				}
			}
		}

		/*
		for (int x = -5; x <= 5; x++){
			for (int z = -5; z <= 5; z++){
				CoordinatesChunk coord = new CoordinatesChunk(dim, chunkX + x, chunkZ + z); 
				chunks.add(coord);
				chunkCoords.add(coord.toChunkCoordIntPair());
			}
		}
		
		if (chunks.size() > 0){
			Mw.instance.chunkManager.addForcedChunks(chunkCoords);
			PacketDispatcher.sendPacketToServer(Packet_ReqChunks.create(dim, chunks));
		}
		*/
		
		if (chunks.size() > 0)
			PacketManager.sendToServer(new PacketReqChunks(dim, chunks));				
	}
	
	@Override
	public void onDimensionChanged(int dimension, MapView mapview) {
		PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_LOADED, new SerialInt(dimension)));
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, MapView mapview) {
	}

	@Override
	public void onZoomChanged(int level, MapView mapview) {
	}

	@Override
	public void onOverlayActivated(MapView mapview) {
		this.selectedChunk = null;
		PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_LOADED, new SerialInt(mapview.getDimension())));		
		PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_TICKETS));		
	}

	@Override
	public void onOverlayDeactivated(MapView mapview) {
		this.showList = false;
		this.selectedChunk = null;		
		PacketManager.sendToServer(new PacketReqData(Message.COMMAND_UNREGISTER));
	}

	@Override
	public void onDraw(MapView mapview, MapMode mapmode) {
		if (this.canvas == null)
			this.canvas = new LayoutCanvas();
		
		if (mapmode.marginLeft != 0){
			this.canvas.hide();
			return;
		}
		
		if (!this.showList)
			this.canvas.hide();
		else{
			this.canvas.show();		
			this.canvas.draw();
		}
	}

	@SideOnly(Side.CLIENT)
	public void setupTable(ArrayList<TicketData> tickets){
		if (this.canvas == null)
			this.canvas = new LayoutCanvas();		
		
		LayoutBase layout = (LayoutBase)this.canvas.addWidget("Table", new LayoutBase(null));
		//layout.setGeometry(new WidgetGeometry(100.0,0.0,300.0,100.0,CType.RELXY, CType.REL_Y, WAlign.RIGHT, WAlign.TOP));
		layout.setGeometry(new WidgetGeometry(100.0,0.0,30.0,100.0,CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));
		layout.setBackgroundColors(0x90202020, 0x90202020);
		
		TicketTable  table  = (TicketTable)layout.addWidget("Table_", new TicketTable(null, this));
		
		table.setGeometry(new WidgetGeometry(0.0,0.0,100.0,100.0,CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oPos", "\u00a7a\u00a7oMod", "\u00a7a\u00a7oChunks")
	    	 .setColumnsTitle("Pos", "Mod", "Chunks")
			 .setColumnsWidth(50,25,25)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);			 

		
		for (TicketData data : tickets)
			//table.addRow(data, String.format("[%s %s %s]", data.coord.dim, data.coord.chunkX, data.coord.chunkZ), "\u00a79\u00a7o" + data.modID, String.valueOf(data.nchunks));
			table.addRow(data, String.format("[%s %s %s]", data.coord.dim, data.coord.chunkX, data.coord.chunkZ), data.modID, String.valueOf(data.nchunks));

		this.showList = true;
	}	
	
	@Override
	public boolean onMouseInput(MapView mapview, MapMode mapmode) {
		if (this.canvas != null && this.canvas.shouldRender() && ((LayoutCanvas)this.canvas).hasWidgetAtCursor()){
			((TicketTable)this.canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
			this.canvas.handleMouseInput();
			return true;
		}
		return false;
	}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		switch(msg){
		case LIST_CHUNK_TICKETS:{
			ArrayList<TicketData> arr = new ArrayList<TicketData>();
			for (ISerializable s : rawdata.array)
				arr.add((TicketData)s);
			
			this.setupTable(arr);
			
			break;
		}
		default:
			return false;
			
		}
		return true;
	}
}
