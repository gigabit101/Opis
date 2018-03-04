package mcp.mobius.opis.swing.panels.timingserver;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.actions.ActionTimingChunks;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class PanelTimingChunks extends JPanelMsgHandler implements ITabPanel{
	private JButtonAccess btnRun;
	private JButtonAccess btnTeleport;
	private JButtonAccess btnCenter;

	/**
	 * Create the panel.
	 */
	public PanelTimingChunks() {
		setLayout(new MigLayout("", "[][][grow][]", "[][grow]"));
		
		btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
		add(btnCenter, "cell 0 0");
		btnCenter.addActionListener(new ActionTimingChunks());
		
		btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
		add(btnTeleport, "cell 1 0");
		btnTeleport.addActionListener(new ActionTimingChunks());
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 3 0");
		btnRun.addActionListener(new ActionRunOpis());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 4 1,grow");
		
		table = new JTableStats(
				new String[] {"Dimension", "Position", "TileEntities", "Entities", "Update Time"},
				new Class[]  {Integer.class, String.class, Integer.class, Integer.class, StatAbstract.class}
				);
		scrollPane.setViewportView(table);		
	}

	public JButton getBtnRun() {return btnRun;}
	public JButton getBtnTeleport() {return btnTeleport;}
	public JButton getBtnCenter()   {return btnCenter;}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		switch(msg){
		case LIST_TIMING_CHUNK:{
			this.cacheData(msg, rawdata);
			
			this.getTable().setTableData(rawdata.array);
			
			DefaultTableModel model = table.getModel();
			int               row   = this.getTable().clearTable(StatsChunk.class);		
			
			for (Object o : rawdata.array){
				StatsChunk stat = (StatsChunk)o;
				model.addRow(new Object[]  {
						 stat.getChunk().dim,
						 String.format("[ %4d %4d ]", 	stat.getChunk().chunkX, stat.getChunk().chunkZ),
						 stat.tileEntities,
					     stat.entities,
					     stat});
			}
			
			this.getTable().dataUpdated(row);			
			
			break;
		}
		case STATUS_START:{
			this.getBtnRun().setText("Running...");
			break;
		}
		case STATUS_STOP:{
			this.getBtnRun().setText("Run Opis");
			break;
		}		
		case STATUS_RUNNING:{
			this.getBtnRun().setText("Running...");
			break;
		}		
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.TIMINGCHUNKS;
	}	
	
	@Override
	public boolean refreshOnString(){
		return true;
	}	
}
