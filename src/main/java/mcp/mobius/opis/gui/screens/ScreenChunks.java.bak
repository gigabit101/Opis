package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.overlay.OverlayMeanTime;

public class ScreenChunks extends ScreenBase {

	public class ChunksTable extends ViewTable{
		public ChunksTable(IWidget parent) { 	
			super(parent);
		}
		
		@Override
		public void onMouseClick(MouseEvent event){
			TableRow row = this.getRow(event.x, event.y);
			if (row != null){
				CoordinatesChunk coord = ((ChunkStats)row.getObject()).getChunk();
				OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.chunkX, coord.chunkZ);
				MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
				this.mc.displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));
			}
		}
	}	
	
	
	
	public ScreenChunks(GuiScreen parent, ArrayList<ChunkStats> chunks) {
		super(parent);
		
		ChunksTable table = (ChunksTable)this.getRoot().addWidget("Table", new ChunksTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Dim", "Pos", "N TEs", "N Ents", "Update Time")
			 .setColumnsWidth(20, 20, 20, 20, 20)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);		

		for (ChunkStats data : chunks){
			if (modOpis.microseconds)
				table.addRow(data, 
						String.format("%3d", data.getChunk().dim),
					    String.format("[ %4d %4d ]", 	data.getChunk().chunkX, data.getChunk().chunkZ),
					    String.format("%d", data.tileEntities),
					    String.format("%d", data.entities),
					    //String.format("%.5f ms",data.updateTime/1000.0));
					    String.format("%.5f \u00B5s",data.getDataSum()));
			else
				table.addRow(data, 
						String.format("%3d", data.getChunk().dim),
					    String.format("[ %4d %4d ]", 	data.getChunk().chunkX, data.getChunk().chunkZ),
					    String.format("%d", data.tileEntities),
					    String.format("%d", data.entities),
					    String.format("%.5f ms",data.getDataSum()/1000.0));
		}	    
	    
	}
		
	
}
