package mcp.mobius.opis.gui.overlay.entperchunk;

import mapwriter.api.IMapMode;
import mapwriter.api.IMapView;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk.ReducedData;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import net.minecraft.util.math.MathHelper;

public class TableChunks extends ViewTable {
    IMapView mapView;
    IMapMode mapMode;
	OverlayEntityPerChunk overlay;		
	
	public TableChunks(IWidget parent, OverlayEntityPerChunk overlay) { 	
		super(parent);
		this.overlay = overlay;			
	}
	
	public void setMap(IMapView mapView, IMapMode mapMode){
	    this.mapView = mapView;
		this.mapMode = mapMode;			
	}
	
	@Override
	public void onMouseClick(MouseEvent event){
		TableRow row = this.getRow(event.x, event.y);
		if (row != null){
			CoordinatesBlock coord = ((ReducedData)row.getObject()).chunk.asCoordinatesBlock();
			this.overlay.selectedChunk = ((ReducedData)row.getObject()).chunk;
			PacketManager.sendToServer(new PacketReqData(Message.LIST_CHUNK_ENTITIES, this.overlay.selectedChunk));
			this.overlay.showList = false;
			
			this.mapView.setDimension(coord.dim);
			this.mapView.setViewCentre(coord.x, coord.z);
			this.overlay.requestChunkUpdate(this.mapView.getDimension(), 
					MathHelper.ceil(this.mapView.getX()) >> 4,
					MathHelper.ceil(this.mapView.getZ()) >> 4);

		}
	}
}
