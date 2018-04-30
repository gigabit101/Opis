package mcp.mobius.opis.gui.widgets.buttons;

import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LabelFixedFont;
import mcp.mobius.opis.gui.widgets.LayoutBase;
import mcp.mobius.opis.gui.widgets.WidgetBase;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import org.lwjgl.util.Point;

//TODO : THIS CLASS IS WIP AND CAN'T WORK RIGHT NOW

public class ButtonContainerLabel extends WidgetBase {

    private int nButtons = 0;
    private int columns;
    private int buttonSize;
    private double spacing;

    public ButtonContainerLabel(IWidget parent, int columns, int buttonSize, double spacing) {
        super(parent);
        this.columns = columns;
        this.spacing = spacing;
        this.buttonSize = buttonSize;
    }

    public void addButton(ButtonBase button, String label) {
        String buttonName = String.format("Button_%d", nButtons);
        String layoutName = String.format("Layout_%d", nButtons);
        String layoutLabelName = String.format("LayoutLabel_%d", nButtons);
        String labelName = String.format("Label_%d", nButtons);

        addWidget(layoutName, new LayoutBase(this));
        addWidget(layoutLabelName, new LayoutBase(this));

        int column = (nButtons % columns) * 2;
        int row = nButtons / columns;
        double sizeColumn = 100.0 / (columns * 2);

        getWidget(layoutName).setGeometry(new WidgetGeometry(sizeColumn * (column + 1), spacing * row, sizeColumn, spacing, CType.REL_X, CType.REL_X, WAlign.LEFT, WAlign.TOP));
        getWidget(layoutName).addWidget(buttonName, button);
        getWidget(layoutName).getWidget(buttonName).setGeometry(new WidgetGeometry(50.0, 50.0, buttonSize, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));

        getWidget(layoutLabelName).setGeometry(new WidgetGeometry(sizeColumn * column, spacing * row, sizeColumn, spacing, CType.REL_X, CType.REL_X, WAlign.LEFT, WAlign.TOP));
        getWidget(layoutLabelName).addWidget(labelName, new LabelFixedFont(this, label));
        getWidget(layoutLabelName).getWidget(labelName).setGeometry(new WidgetGeometry(50.0, 50.0, buttonSize, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));

        nButtons += 1;
    }

    @Override
    public void draw(Point pos) {
    }
}
