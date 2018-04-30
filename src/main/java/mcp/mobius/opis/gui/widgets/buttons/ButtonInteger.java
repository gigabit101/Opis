package mcp.mobius.opis.gui.widgets.buttons;

import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LabelFixedFont;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;

public class ButtonInteger extends ButtonBase {

    protected int state = 0;
    protected int nStates;

    public ButtonInteger(IWidget parent, String... texts) {
        super(parent);

        nStates = texts.length;

        for (int i = 0; i < texts.length; i++) {
            String labelName = String.format("Label_%d", i);
            addWidget(labelName, new LabelFixedFont(this, texts[i]));
            getWidget(labelName).hide();
            getWidget(labelName).setGeometry(new WidgetGeometry(50.0D, 50.0D, 100.0D, 20.0D, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));
        }
        getWidget("Label_0").show();
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        super.onMouseClick(event);

        if (event.button == 0) {
            state += 1;
        }

        if (state >= nStates) {
            state = 0;
        }

        for (int i = 0; i < nStates; i++) {
            getWidget(String.format("Label_%d", i)).hide();
        }

        getWidget(String.format("Label_%d", state)).show();
    }

}
