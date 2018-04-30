package mcp.mobius.opis.gui.widgets.tableview;

import mcp.mobius.opis.gui.helpers.UIException;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LayoutBase;
import mcp.mobius.opis.gui.widgets.ViewportScrollable;
import mcp.mobius.opis.gui.widgets.WidgetBase;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import org.lwjgl.util.Point;

public class ViewTable extends WidgetBase {

    int ncolumns = -1;
    int nrows = 0;
    double[] widths;
    String[] texts;
    WAlign[] aligns;
    float fontSize = 1.0f;
    boolean init = false;
    int rowColorOdd = 0x50505050;
    int rowColorEven = 0x50808080;

    public ViewTable(IWidget parent) {
        super(parent);
        addWidget("Titles", new TableRow(null, fontSize)).setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 16.0, CType.REL_X, CType.REL_X, WAlign.LEFT, WAlign.TOP));
        ((TableRow) getWidget("Titles")).setColors(0x00000000, 0x00000000);
        addWidget("Viewport", new ViewportScrollable(null)).setGeometry(new WidgetGeometry(0.0, 16.0, 100.0, 90.0, CType.REL_X, CType.RELXY, WAlign.LEFT, WAlign.TOP));
        ((ViewportScrollable) getWidget("Viewport")).attachWidget(new LayoutBase(null)).setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 0.0, CType.RELXY, CType.REL_X, WAlign.LEFT, WAlign.TOP));

    }

    @Override
    public void draw(Point pos) {
    }

    public ViewTable setColumnsWidth(double... widths) {
        if (ncolumns == -1) {
            ncolumns = widths.length;
        } else if (ncolumns != widths.length) {
            throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", ncolumns, widths.length));
        }

        this.widths = widths;
        ((TableRow) getWidget("Titles")).setColumnsWidth(widths);
        return this;
    }

    public ViewTable setRowColors(int even, int odd) {
        rowColorEven = even;
        rowColorOdd = odd;
        return this;
    }

    public ViewTable setColumnsTitle(String... strings) {
        if (ncolumns == -1) {
            ncolumns = strings.length;
        } else if (ncolumns != strings.length) {
            throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", ncolumns, strings.length));
        }

        texts = strings;
        ((TableRow) getWidget("Titles")).setColumnsText(strings);

        return this;
    }

    public ViewTable setColumnsAlign(WAlign... aligns) {
        if (ncolumns == -1) {
            ncolumns = aligns.length;
        } else if (ncolumns != aligns.length) {
            throw new UIException(String.format("Number of columns mismatch. Expecting %d, got %d", ncolumns, aligns.length));
        }

        this.aligns = aligns;
        ((TableRow) getWidget("Titles")).setColumnsAlign(aligns);

        return this;
    }

    public ViewTable addRow(String... strings) {
        addRow(null, strings);
        return this;
    }

    public ViewTable addRow(Object obj, String... strings) {
        IWidget tableLayout = ((ViewportScrollable) getWidget("Viewport")).getAttachedWidget();
        tableLayout.setSize(100.0, (nrows + 1) * 16);

        TableRow newRow = new TableRow(null, fontSize);
        newRow.setColumnsWidth(widths);
        newRow.setColumnsText(strings);
        newRow.setColumnsAlign(aligns);
        if (nrows % 2 == 1) {
            newRow.setColors(rowColorOdd, rowColorOdd);
        } else {
            newRow.setColors(rowColorEven, rowColorEven);
        }
        newRow.setGeometry(new WidgetGeometry(0.0, 16 * nrows, 100.0, 16, CType.REL_X, CType.REL_X, WAlign.LEFT, WAlign.TOP));
        newRow.attachObject(obj);

        tableLayout.addWidget(String.format("Row_%03d", nrows), newRow);

        nrows += 1;

        return this;
    }

    public TableRow getRow(double x, double y) {
        ViewportScrollable viewport = (ViewportScrollable) getWidget("Viewport");
        IWidget layout = viewport.getAttachedWidget();
        TableRow row = (TableRow) layout.getWidgetAtLayer(x, y - viewport.getOffset(), 1);

        return row;
    }

    public void setFontSize(float size) {
        fontSize = size;
    }

}
