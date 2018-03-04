package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.client.gui.GuiScreen;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.stats.StatsMod;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;

public class ScreenMods extends ScreenBase {

	public ScreenMods(GuiScreen parent, ArrayList<StatsMod> mods) {
		super(parent);
		
		Collections.sort(mods);
		
		ViewTable table = (ViewTable)this.getRoot().addWidget("Table", new ViewTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
		/*
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
	    	 .setColumnsTitle("Mod", "N tiles", "Total", "Mean time", "Geom", "Min", "Max", "Mode", "Variance")
			 .setColumnsWidth(20, 10, 10, 10, 10, 10, 10, 10, 10)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);
		*/

	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
	     	.setColumnsTitle("Mod", "N tiles", "Total", "Mean time", "Median", "Max")
	     	.setColumnsWidth(25, 15, 15, 15, 15, 15)
	     	.setRowColors(0xff808080, 0xff505050)
	     	.setFontSize(1.0f);		
		
	    /*
		for (ModStats data : mods){
			table.addRow(data, 
				     	 String.format("%s", data.getModID()),
					     String.format("%d", data.ntes),
					     String.format("%.5f ms", data.total    / 1000.0),
					     String.format("%.5f ms", data.meanTime / 1000.0),
					     String.format("%.5f ms", data.meanGeom / 1000.0),
					     String.format("%.5f ms", data.minTime  / 1000.0),
					     String.format("%.5f ms", data.maxTime  / 1000.0),
					     String.format("%.5f ms", data.mode     / 1000.0),
					     String.format("%.5f ms", data.variance / 1000.0)					     
					);
		}
		*/	    

		for (StatsMod data : mods){
			if (modOpis.microseconds)			
				table.addRow(data, 
				     	 String.format("%s", data.getModID()),
					     String.format("%d", data.ntes),
					     //String.format("%.5f ms", data.total    / 1000.0),
					     //String.format("%.5f ms", data.meanTime / 1000.0),
					     //String.format("%.5f ms", data.median   / 1000.0),
					     //String.format("%.5f ms", data.maxTime  / 1000.0)
					     String.format("%.3f \u00B5s", data.total),
					     String.format("%.3f \u00B5s", data.meanTime),
					     String.format("%.3f \u00B5s", data.median),
					     String.format("%.3f \u00B5s", data.maxTime)					     
					);
			else
				table.addRow(data, 
				     	 String.format("%s", data.getModID()),
					     String.format("%d", data.ntes),
					     String.format("%.5f ms", data.total    / 1000.0),
					     String.format("%.5f ms", data.meanTime / 1000.0),
					     String.format("%.5f ms", data.median   / 1000.0),
					     String.format("%.5f ms", data.maxTime  / 1000.0)
					);				
		}	    
	    
	}	
	
}
