package mcp.mobius.opis.swing.panels.timingserver;

import mcp.mobius.opis.Opis;
import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.helpers.ModIdentification;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.actions.ActionTimingTileEnts;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class PanelTimingTileEnts extends JPanelMsgHandler implements ITabPanel{
	private JButtonAccess btnCenter;
	private JButtonAccess btnTeleport;
	private JButtonAccess btnReset;
	private JButtonAccess btnRun;
	private JLabel lblSummary;

	/**
	 * Create the panel.
	 */
	public PanelTimingTileEnts() {
		setLayout(new MigLayout("", "[][][][grow][]", "[][grow][]"));
		
		btnCenter = new JButtonAccess("Center Map", AccessLevel.NONE);
		add(btnCenter, "cell 0 0");
		btnCenter.addActionListener(new ActionTimingTileEnts());
		
		btnTeleport = new JButtonAccess("Teleport", AccessLevel.PRIVILEGED);
		add(btnTeleport, "cell 1 0");
		btnTeleport.addActionListener(new ActionTimingTileEnts());
		
		btnReset = new JButtonAccess("Reset Highlight", AccessLevel.PRIVILEGED);
		btnReset.setEnabled(false);
		add(btnReset, "cell 2 0");
		btnReset.addActionListener(new ActionTimingTileEnts());
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 4 0");
		btnRun.addActionListener(new ActionRunOpis());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 5 1,grow");
		
		table = new JTableStats(
				new String[] {"Type", "Mod", "Dim", "Pos", "Update Time"},
				new Class[]  {String.class, String.class, Integer.class, Object.class, DataTiming.class}
				);
		scrollPane.setViewportView(table);			
		
		lblSummary = new JLabel("New label");
		add(lblSummary, "cell 0 2 5 1,alignx center");
	}

	public JButton getBtnCenter()  {return btnCenter;}
	public JButton getBtnTeleport(){return btnTeleport;}
	public JButton getBtnReset()   {return btnReset;}
	public JButton getBtnRun()     {return btnRun;}
	public JLabel  getLblSummary() {return lblSummary;}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		switch(msg){
		case LIST_TIMING_TILEENTS:{
			this.cacheData(msg, rawdata);
			
			this.getTable().setTableData(rawdata.array);
			
			DefaultTableModel model = table.getModel();
			int               row   = this.getTable().clearTable(DataBlockTileEntity.class);

			for (Object o : rawdata.array){
				DataBlockTileEntity data = (DataBlockTileEntity)o;
				String name  = ModIdentification.getStackName(data.id, data.meta);
				String modID = ModIdentification.getModStackName(data.id, data.meta);
				model.addRow(new Object[]  {
						 name,
						 modID,
					     data.pos.dim,
					     String.format("[ %4d %4d %4d ]", data.pos.x, data.pos.y, data.pos.z),  
					     data.update});
			}
			
			this.getTable().dataUpdated(row);			
			
			break;
		}
		case VALUE_TIMING_TILEENTS:{
			this.getLblSummary().setText(String.format("Total update time : %s", ((DataTiming)rawdata.value).toString()));	
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
		case CLIENT_HIGHLIGHT_BLOCK:{
			Opis.selectedBlock = (CoordinatesBlock)rawdata.value;
			this.getBtnReset().setEnabled(true);
			break;
		}
		default:
			return false;
			
		}
		return true;
	}

	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.TIMINGTILEENTS;
	}		
	
	@Override
	public boolean refreshOnString(){
		return true;
	}	
}
