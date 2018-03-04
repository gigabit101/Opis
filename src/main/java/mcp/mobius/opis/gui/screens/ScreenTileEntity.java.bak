package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.LanguageRegistry;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.tools.ModIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class ScreenTileEntity extends ScreenBase {
	
	public class EntitiesTable extends ViewTable{
		public EntitiesTable(IWidget parent) { 	
			super(parent);
		}
		
		@Override
		public void onMouseClick(MouseEvent event){
			TableRow row = this.getRow(event.x, event.y);
			if (row != null){
				CoordinatesBlock coord = ((TileEntityStats)row.getObject()).getCoordinates();
				OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.x >> 4, coord.z >> 4);
				MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
				this.mc.displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));
			}
		}
	}	
	
	
	
	public ScreenTileEntity(GuiScreen parent, ArrayList<TileEntityStats> tes) {
		super(parent);
		
		EntitiesTable table = (EntitiesTable)this.getRoot().addWidget("Table", new EntitiesTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Type", "Mod", "Dim", "Pos", "Update Time")
			 .setColumnsWidth(30, 20, 15, 15, 20)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);		

		for (TileEntityStats data : tes){
        	
			ItemStack is;
			String name  = String.format("te.%d.%d", data.getID(), data.getMeta());
			String modID = "<UNKNOWN>";
			
			try{
				is = new ItemStack(data.getID(), 1, data.getMeta());
				name  = is.getDisplayName();
				modID = ModIdentification.idFromStack(is);
			}  catch (Exception e) {	}

			if(modOpis.microseconds)
				table.addRow(data, 
						 name,
						 modID,
					     String.format("%3d", data.getCoordinates().dim),
					     String.format("[ %4d %4d %4d ]", 	data.getCoordinates().x, data.getCoordinates().y, data.getCoordinates().z),  
					     String.format("%.3f \u00B5s",data.getGeometricMean()));
			else
				table.addRow(data, 
						 name,
						 modID,
					     String.format("%3d", data.getCoordinates().dim),
					     String.format("[ %4d %4d %4d ]", 	data.getCoordinates().x, data.getCoordinates().y, data.getCoordinates().z),  
					     String.format("%.5f ms",data.getGeometricMean()/1000.0));
		}	    
	    
	}
	
}
