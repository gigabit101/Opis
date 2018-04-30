package mcp.mobius.opis.gui.widgets.tableview;

import mcp.mobius.opis.gui.helpers.UIException;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LayoutBase;
import mcp.mobius.opis.gui.widgets.WidgetBase;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import org.lwjgl.util.Point;

public class TableRow extends WidgetBase {

    int ncolumns = -1;
    double[] widths;
    String[] texts;
    WAlign[] aligns;
    float fontSize = 1.0f;
    boolean init = false;
    int bgcolor1 = 0xff505050;
    int bgcolor2 = 0xff505050;
    Object obj;

    public TableRow(IWidget parent, float fontSize) {
        super(parent);

        this.fontSize = fontSize;

        addWidget("Background", new LayoutBase(null)).setGeometry(new WidgetGeometry(50.0, 50.0, 100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
        ((LayoutBase) getWidget("Background")).setBackgroundColors(bgcolor1, bgcolor2);

    }

    public TableRow attachObject(Object obj) {
        this.obj = obj;
        return this;
    }

    public Object getObject() {
        return obj;
    }

    public void setFontSize(float size) {
        fontSize = size;
    }

    public TableRow setColumnsWidth(double... widths) {
        if (ncolumns == -1) {
            ncolumns = widths.length;
        } else if (ncolumns != widths.length) {
            throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", ncolumns, widths.length));
        }

        this.widths = widths;

        return this;
    }

    public TableRow setColumnsText(String... strings) {
        if (ncolumns == -1) {
            ncolumns = strings.length;
        } else if (ncolumns != strings.length) {
            throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", ncolumns, strings.length));
        }

        texts = strings;

        return this;
    }

    public TableRow setColumnsAlign(WAlign... aligns) {
        if (ncolumns == -1) {
            ncolumns = aligns.length;
        } else if (ncolumns != aligns.length) {
            throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", ncolumns, aligns.length));
        }

        this.aligns = aligns;

        return this;
    }

    public TableRow setColors(int bg1, int bg2) {
        bgcolor1 = bg1;
        bgcolor2 = bg2;
        ((LayoutBase) getWidget("Background")).setBackgroundColors(bgcolor1, bgcolor2);
        return this;
    }

    @Override
    public void draw() {
        if (!init) {
            double currentOffset = 0.0;
            for (int i = 0; i < ncolumns; i++) {
                if (!widgets.containsKey(String.format("Cell_%02d", i))) {
                    TableCell cell = (TableCell) addWidget(String.format("Cell_%02d", i), new TableCell(null, texts[i], aligns[i], fontSize));
                    cell.setGeometry(new WidgetGeometry(currentOffset, 50.0, widths[i], 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.CENTER));
                    currentOffset += widths[i];
                }
            }
            init = true;
        }
        super.draw();
    }

    @Override
    public void draw(Point pos) {
    }
}
