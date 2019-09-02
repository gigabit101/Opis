//package mcp.mobius.opis.gui.overlay.entperchunk;
//
//import mapwriter.api.IMapMode;
//import mapwriter.api.IMapView;
//import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
//import mcp.mobius.opis.data.holders.newtypes.DataEntity;
//import mcp.mobius.opis.gui.events.MouseEvent;
//import mcp.mobius.opis.gui.interfaces.IWidget;
//import mcp.mobius.opis.gui.widgets.tableview.TableRow;
//import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
//import mcp.mobius.opis.network.PacketManager;
//import mcp.mobius.opis.network.enums.Message;
//import mcp.mobius.opis.network.packets.client.PacketReqData;
//import net.minecraft.client.Minecraft;
//
//public class TableEntities extends ViewTable {
//
//    IMapView mapView;
//    IMapMode mapMode;
//    OverlayEntityPerChunk overlay;
//
//    public TableEntities(IWidget parent, OverlayEntityPerChunk overlay) {
//        super(parent);
//        this.overlay = overlay;
//    }
//
//    public void setMap(IMapView mapView, IMapMode mapMode) {
//        this.mapView = mapView;
//        this.mapMode = mapMode;
//    }
//
//    @Override
//    public void onMouseClick(MouseEvent event) {
//        TableRow row = getRow(event.x, event.y);
//        if (row != null) {
//            int eid = ((DataEntity) row.getObject()).eid;
//            int dim = ((DataEntity) row.getObject()).pos.dim;
//            PacketManager.sendToServer(new PacketReqData(Message.COMMAND_TELEPORT_TO_ENTITY, new TargetEntity(eid, dim)));
//            Minecraft.getMinecraft().setIngameFocus();
//        }
//    }
//}
