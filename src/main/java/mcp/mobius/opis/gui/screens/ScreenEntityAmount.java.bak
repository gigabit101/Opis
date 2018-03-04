package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import net.minecraft.client.gui.GuiScreen;

public class ScreenEntityAmount extends ScreenBase {

	public ScreenEntityAmount(GuiScreen parent, HashMap<String, Integer> stat) {
		super(parent);
		
		ViewTable table = (ViewTable)this.getRoot().addWidget("Table", new ViewTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Type", "Amount")
			 .setColumnsWidth(50, 50)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);		

		for (String name : stat.keySet()){
			//String[] namelst = name.split("\\.");
			//String nameshort = namelst[namelst.length - 1];
			
			table.addRow(new Object(), name, String.valueOf(stat.get(name)));
		}
	}	
	
}
