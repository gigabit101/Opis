package mcp.mobius.opis.mobiuscore;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Arrays;

public class CoreContainer extends DummyModContainer {
    public CoreContainer()
    {
        super(new ModMetadata());

        ModMetadata md = getMetadata();
        md.modId   = "MobiusCore";
        md.name    = "MobiusCore";
        md.version = "1.2.4";
        md.credits = "ProfMobius";
        md.authorList = Arrays.asList("ProfMobius");
        md.description = "";
        md.url     = "profmobius.blogspot.com";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        //bus.register(this);
        // this needs to return true, otherwise the mod will be deactivated by FML
        return true;
    }
}
