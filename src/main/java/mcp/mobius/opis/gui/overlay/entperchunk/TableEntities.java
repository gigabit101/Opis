package mcp.mobius.opis.gui.overlay.entperchunk;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk.ReducedData;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;

public class TableEntities extends ViewTable {
	MapView mapView;
	MapMode mapMode;
	OverlayEntityPerChunk overlay;		
	
	public TableEntities(IWidget parent, OverlayEntityPerChunk overlay) { 	
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
			//CoordinatesBlock coord = ((EntityStats)row.getObject()).getCoord();
			//PacketDispatcher.sendPacketToServer(Packet_ReqTeleport.create(coord));
			int eid = ((DataEntity)row.getObject()).eid;
			int dim = ((DataEntity)row.getObject()).pos.dim;
			PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_TO_ENTITY, new TargetEntity(eid, dim)));
			Minecraft.getMinecraft().setIngameFocus();			
		}
	}
}
