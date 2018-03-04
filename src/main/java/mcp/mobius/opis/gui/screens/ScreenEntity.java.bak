package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.network.client.Packet_ReqTeleport;
import mcp.mobius.opis.network.client.Packet_ReqTeleportEID;
import mcp.mobius.opis.data.holders.EntityStats;

public class ScreenEntity extends ScreenBase {

	public class EntitiesTable extends ViewTable{
		public EntitiesTable(IWidget parent) { 	
			super(parent);
		}
		
		@Override
		public void onMouseClick(MouseEvent event){
			TableRow row = this.getRow(event.x, event.y);
			if (row != null){
				int eid = ((EntityStats)row.getObject()).getID();
				int dim = ((EntityStats)row.getObject()).getCoord().dim;
				PacketDispatcher.sendPacketToServer(Packet_ReqTeleportEID.create(eid, dim));
				Minecraft.getMinecraft().setIngameFocus();						
			}
		}
	}	
	
	
	
	public ScreenEntity(GuiScreen parent, ArrayList<EntityStats> stat) {
		super(parent);
		
		EntitiesTable table = (EntitiesTable)this.getRoot().addWidget("Table", new EntitiesTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Type", "ID", "Dim", "Pos", "Update Time", "Data")
			 .setColumnsWidth(30, 15, 15, 15, 15, 10)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);		

		for (EntityStats data : stat){

			//String[] namelst = data.getName().split("\\.");
			//String name = namelst[namelst.length - 1];
			String name = data.getName();
			
			if(modOpis.microseconds)
				table.addRow(data, 
						 name,
						 String.valueOf(data.getID()),
					     String.format("%3d", data.getCoord().dim),
					     String.format("[ %4d %4d %4d ]", 	data.getCoord().x, data.getCoord().y, data.getCoord().z),  
					     String.format("%.3f \u00B5s",data.getGeometricMean()),
					     String.valueOf(data.getNData()));
			else
				table.addRow(data, 
						 name,
						 String.valueOf(data.getID()),						 
					     String.format("%3d", data.getCoord().dim),
					     String.format("[ %4d %4d %4d ]", 	data.getCoord().x, data.getCoord().y, data.getCoord().z),  
					     String.format("%.5f ms",data.getGeometricMean()/1000.0),
					     String.valueOf(data.getNData()));
		}	    
	    
	}	
	
}
