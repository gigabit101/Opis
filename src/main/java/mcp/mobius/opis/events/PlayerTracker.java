package mcp.mobius.opis.events;

import codechicken.lib.util.ServerUtils;
import com.mojang.authlib.GameProfile;
import mcp.mobius.opis.Opis;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.swing.SelectedTab;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.HashSet;

//public class PlayerTracker implements IPlayerTracker{
public enum PlayerTracker {
    INSTANCE;

    private PlayerTracker() {
    }

    public HashSet<EntityPlayerMP> playersSwing = new HashSet<EntityPlayerMP>();         //This is the list of players who have opened the UI
    //public HashSet<Player> playersOpis  = new HashSet<Player>();		 //This is the list of players who have opened the UI or used the command line
    public HashMap<String, Boolean> filteredAmount = new HashMap<String, Boolean>(); //Should the entity amount be filtered or not
    public HashMap<EntityPlayerMP, OverlayStatus> playerOverlayStatus = new HashMap<EntityPlayerMP, OverlayStatus>();
    public HashMap<EntityPlayerMP, Integer> playerDimension = new HashMap<EntityPlayerMP, Integer>();
    public HashMap<EntityPlayerMP, SelectedTab> playerTab = new HashMap<EntityPlayerMP, SelectedTab>();
    private HashSet<String> playerPrivileged = new HashSet<String>();

    public SelectedTab getPlayerSelectedTab(EntityPlayerMP player) {
        return this.playerTab.get(player);
    }

    public AccessLevel getPlayerAccessLevel(EntityPlayerMP player) {
        return this.getPlayerAccessLevel(player.getGameProfile().getName());
    }

    public AccessLevel getPlayerAccessLevel(String name) {
        GameProfile profile = ServerUtils.mc().getPlayerList().getPlayerByUsername(name).getGameProfile();

        if (ServerUtils.mc().getPlayerList().canSendCommands(profile) || ServerUtils.mc().isSinglePlayer())
            return AccessLevel.ADMIN;
        else if (playerPrivileged.contains(name))
            return AccessLevel.PRIVILEGED;
        else
            return AccessLevel.NONE;
    }

    public void addPrivilegedPlayer(String name, boolean save) {
        this.playerPrivileged.add(name);
        if (save) {
            Opis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[]{}, Opis.commentPrivileged).set(playerPrivileged.toArray(new String[]{}));
            Opis.instance.config.save();
        }
    }

    public void addPrivilegedPlayer(String name) {
        this.addPrivilegedPlayer(name, true);
    }

    public void rmPrivilegedPlayer(String name) {
        this.playerPrivileged.remove(name);
        Opis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[]{}, Opis.commentPrivileged).set(playerPrivileged.toArray(new String[]{}));
        Opis.instance.config.save();
    }

    public void reloeadPriviligedPlayers() {
        String[] users = Opis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[]{}, Opis.commentPrivileged).getStringList();
        for (String s : users)
            PlayerTracker.INSTANCE.addPrivilegedPlayer(s, false);
    }

    public boolean isAdmin(EntityPlayerMP player) {
        return this.getPlayerAccessLevel(player).ordinal() >= AccessLevel.ADMIN.ordinal();
    }

    public boolean isAdmin(String name) {
        return this.getPlayerAccessLevel(name).ordinal() >= AccessLevel.ADMIN.ordinal();
    }

    public boolean isPrivileged(EntityPlayerMP player) {
        return this.getPlayerAccessLevel(player).ordinal() >= AccessLevel.PRIVILEGED.ordinal();
    }

    public boolean isPrivileged(String name) {
        return this.getPlayerAccessLevel(name).ordinal() >= AccessLevel.PRIVILEGED.ordinal();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        this.playerOverlayStatus.remove(event.player);
        this.playerDimension.remove(event.player);
        //this.playersOpis.remove(player);
        this.playersSwing.remove(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PacketManager.validateAndSend(new NetDataValue(Message.STATUS_CURRENT_TIME, new SerialLong(System.currentTimeMillis())), (EntityPlayerMP) event.player);

		/*
		if (manager instanceof MemoryConnection){
			System.out.printf("Adding SSP player to list of privileged users\n");
			PlayerTracker.INSTANCE.addPrivilegedPlayer(((EntityPlayer)event.player).getGameProfile().getName(), false);
		}
		*/

        StringCache.INSTANCE.syncCache((EntityPlayerMP) event.player);
    }
}
