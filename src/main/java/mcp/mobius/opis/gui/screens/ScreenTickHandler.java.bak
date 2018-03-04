package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import net.minecraft.client.gui.GuiScreen;

public class ScreenTickHandler extends ScreenBase {

	public ScreenTickHandler(GuiScreen parent, ArrayList<TickHandlerStats> stat) {
		super(parent);
		
		ViewTable table = (ViewTable)this.getRoot().addWidget("Table", new ViewTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Name", "Update Time")
			 .setColumnsWidth(50,50)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);		

		for (TickHandlerStats data : stat){

			if(modOpis.microseconds)
				table.addRow(data, 
						 data.getName(),
					     String.format("%.3f \u00B5s",data.getGeometricMean()));
			else
				table.addRow(data, 
						 data.getName(),
					     String.format("%.5f ms",data.getGeometricMean()/1000.0));
		}	    
	    
	}		
	
}
