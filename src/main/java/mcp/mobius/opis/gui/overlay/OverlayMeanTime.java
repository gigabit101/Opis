package mcp.mobius.opis.gui.overlay;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
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

public enum OverlayMeanTime implements IMwDataProvider, IMessageHandler{
	INSTANCE;
	
	public class EntitiesTable extends ViewTable{
		MapView mapView;
		MapMode mapMode;
		OverlayMeanTime overlay;		
		
		public EntitiesTable(IWidget parent, OverlayMeanTime overlay) { 	
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
				CoordinatesBlock coord = ((DataBlockTileEntity)row.getObject()).pos;
				
				if (this.mapView.getX() != coord.x || this.mapView.getZ() != coord.z){
					this.mapView.setViewCentre(coord.x, coord.z);
					this.overlay.requestChunkUpdate(this.mapView.getDimension(), 
							MathHelper.ceiling_double_int(this.mapView.getX()) >> 4, 
							MathHelper.ceiling_double_int(this.mapView.getZ()) >> 4);
				}
				else{
					//modOpis.selectedBlock = coord;
					PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_BLOCK, coord));
					Minecraft.getMinecraft().setIngameFocus();
				}
			}
		}
	}
	
	public class ChunkOverlay implements IMwChunkOverlay{

		Point coord;
		int nentities;
		double time;
		double minTime;
		double maxTime;
		boolean selected;
		
		public ChunkOverlay(int x, int z, int nentities, double time, double mintime, double maxtime, boolean selected){
			this.coord     = new Point(x, z);
			this.nentities = nentities;
			this.time      = time;
			this.minTime   = mintime;
			this.maxTime   = maxtime;
			this.selected  = selected;
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {
			//System.out.printf("%s\n", this.maxTime);
			double scaledTime = this.time / this.maxTime;
			int    red        = MathHelper.ceiling_double_int(scaledTime * 255.0);
			int    blue       = 255 - MathHelper.ceiling_double_int(scaledTime * 255.0);
			//System.out.printf("%s\n", red);
			
			return (200 << 24) + (red << 16) + (blue); 
		}
		
		@Override
		public float getFilling() {	return 1.0f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.5f; }

		@Override
		public int getBorderColor() { return this.selected ? 0xffffffff : 0xff000000; }
		
	}		
	
	CoordinatesChunk selectedChunk = null;
	public boolean    showList = false;
	public LayoutCanvas canvas = null;
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX,	double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();
		
		double minTime = 9999;
		double maxTime = 0;

		for (CoordinatesChunk chunk :   ChunkManager.INSTANCE.getChunkMeanTime().keySet()){
			minTime = Math.min(minTime, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum());
			maxTime = Math.max(maxTime, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum());
		}
		
		for (CoordinatesChunk chunk : ChunkManager.INSTANCE.getChunkMeanTime().keySet()){
			if (this.selectedChunk != null)
				overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).tileEntities, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum(), minTime, maxTime, chunk.equals(this.selectedChunk)));
			else
				overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).tileEntities, ChunkManager.INSTANCE.getChunkMeanTime().get(chunk).getDataSum(), minTime, maxTime, false));
		}
		return overlays;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		int xChunk = bX >> 4;
		int zChunk = bZ >> 4;
		CoordinatesChunk chunkCoord = new CoordinatesChunk(dim, xChunk, zChunk);
		
		if (ChunkManager.INSTANCE.getChunkMeanTime().containsKey(chunkCoord))
			if (modOpis.microseconds)
				return String.format("%.3f \u00B5s", ChunkManager.INSTANCE.getChunkMeanTime().get(chunkCoord).getDataSum());
			else
				return String.format(", %.5f ms", ChunkManager.INSTANCE.getChunkMeanTime().get(chunkCoord).getDataSum()/1000.0);

		else
			return "";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview) {
		this.showList = false;
		
		int chunkX = bX >> 4;
		int chunkZ = bZ >> 4;		
		CoordinatesChunk clickedChunk = new CoordinatesChunk(dim, chunkX, chunkZ); 
		
		if (ChunkManager.INSTANCE.getChunkMeanTime().containsKey(clickedChunk)){
			if (this.selectedChunk == null)
				this.selectedChunk = clickedChunk;
			else if (this.selectedChunk.equals(clickedChunk))
				this.selectedChunk = null;
			else
				this.selectedChunk = clickedChunk;
		} else {
			this.selectedChunk = null;
		}
		
		if (this.selectedChunk != null)
			PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_TILEENTS, this.selectedChunk));
		
		//ArrayList<CoordinatesChunk> chunks = new ArrayList<CoordinatesChunk>();
		//for (int x = -5; x <= 5; x++)
		//	for (int z = -5; z <= 5; z++)
		//		chunks.add(new CoordinatesChunk(dim, x, z));
		//PacketDispatcher.sendPacketToServer(Packet_ReqChunks.create(chunks));

	}

	public void setSelectedChunk(int dim, int chunkX, int chunkZ){
		this.selectedChunk = new CoordinatesChunk(dim, chunkX, chunkZ);
		
		if (this.selectedChunk != null)
			PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_TILEENTS, this.selectedChunk));		
	}
	
	@Override
	public void onDimensionChanged(int dimension, MapView mapview) {
		PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_TIMING, new SerialInt(dimension)));
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
		PacketManager.sendToServer(new PacketReqData(Message.OVERLAY_CHUNK_TIMING, new SerialInt(mapview.getDimension())));		
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
	public void setupTable(ArrayList<ISerializable> entities){
		if (this.canvas == null)
			this.canvas = new LayoutCanvas();		
		
		LayoutBase layout = (LayoutBase)this.canvas.addWidget("Table", new LayoutBase(null));
		//layout.setGeometry(new WidgetGeometry(100.0,0.0,300.0,100.0,CType.RELXY, CType.REL_Y, WAlign.RIGHT, WAlign.TOP));
		layout.setGeometry(new WidgetGeometry(100.0,0.0,30.0,100.0,CType.RELXY, CType.RELXY, WAlign.RIGHT, WAlign.TOP));		
		layout.setBackgroundColors(0x90202020, 0x90202020);
		
		EntitiesTable  table  = (EntitiesTable)layout.addWidget("Table_", new EntitiesTable(null, this));
		
		table.setGeometry(new WidgetGeometry(0.0,0.0,100.0,100.0,CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Type", "Pos", "Update Time")
			 .setColumnsWidth(50,25,25)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);
	    
		for (ISerializable uncasted : entities){
			
			DataBlockTileEntity data = (DataBlockTileEntity)uncasted;
			
			ItemStack is;
			String name  = String.format("te.%d.%d", data.id, data.meta);
			
			try{
				is = new ItemStack(Block.getBlockById(data.id), 1, data.meta);
				name  = is.getDisplayName();
			}  catch (Exception e) {	}
			
			/*
        	if (name.equals(data.getType()))
        		name = LanguageRegistry.instance().getStringLocalization(data.getType());
        	if (name.isEmpty())
        		name = data.getType();	
        	*/
        	
			if (modOpis.microseconds)
				table.addRow(data, name, String.format("[ %s %s %s ]", data.pos.x, data.pos.y, data.pos.z),  data.update.asMillisecond().toString());			
			else
				table.addRow(data, name, String.format("[ %s %s %s ]", data.pos.x, data.pos.y, data.pos.z),  data.update.toString());				
		}

		this.showList = true;
	}

	@Override
	public boolean onMouseInput(MapView mapview, MapMode mapmode) {
		if (this.canvas != null && this.canvas.shouldRender() && ((LayoutCanvas)this.canvas).hasWidgetAtCursor()){
			((EntitiesTable)this.canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
			this.canvas.handleMouseInput();
			return true;
		}
		return false;
	}

	private void requestChunkUpdate(int dim, int chunkX, int chunkZ){
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
		case LIST_CHUNK_TILEENTS:{
			this.setupTable(rawdata.array);
			break;
		}
		default:
			return false;
		}
		
		
		return true;
	}	
	
}
