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

public enum PlayerTracker {
    INSTANCE;

    PlayerTracker() {
    }

    public HashSet<EntityPlayerMP> playersSwing = new HashSet<>();         //This is the list of players who have opened the UI
    public HashMap<String, Boolean> filteredAmount = new HashMap<>(); //Should the entity amount be filtered or not
    public HashMap<EntityPlayerMP, OverlayStatus> playerOverlayStatus = new HashMap<>();
    public HashMap<EntityPlayerMP, Integer> playerDimension = new HashMap<>();
    public HashMap<EntityPlayerMP, SelectedTab> playerTab = new HashMap<>();
    private HashSet<String> playerPrivileged = new HashSet<>();

    public SelectedTab getPlayerSelectedTab(EntityPlayerMP player) {
        return playerTab.get(player);
    }

    public AccessLevel getPlayerAccessLevel(EntityPlayerMP player) {
        return getPlayerAccessLevel(player.getGameProfile().getName());
    }

    public AccessLevel getPlayerAccessLevel(String name) {
        GameProfile profile = ServerUtils.mc().getPlayerList().getPlayerByUsername(name).getGameProfile();

        if (ServerUtils.mc().getPlayerList().canSendCommands(profile) || ServerUtils.mc().isSinglePlayer()) {
            return AccessLevel.ADMIN;
        } else if (playerPrivileged.contains(name)) {
            return AccessLevel.PRIVILEGED;
        } else {
            return AccessLevel.NONE;
        }
    }

    public void addPrivilegedPlayer(String name, boolean save) {
        playerPrivileged.add(name);
        if (save) {
            Opis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[] {}, Opis.commentPrivileged).set(playerPrivileged.toArray(new String[] {}));
            Opis.instance.config.save();
        }
    }

    public void addPrivilegedPlayer(String name) {
        addPrivilegedPlayer(name, true);
    }

    public void rmPrivilegedPlayer(String name) {
        playerPrivileged.remove(name);
        Opis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[] {}, Opis.commentPrivileged).set(playerPrivileged.toArray(new String[] {}));
        Opis.instance.config.save();
    }

    public void reloeadPriviligedPlayers() {
        String[] users = Opis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[] {}, Opis.commentPrivileged).getStringList();
        for (String s : users) {
            PlayerTracker.INSTANCE.addPrivilegedPlayer(s, false);
        }
    }

    public boolean isAdmin(EntityPlayerMP player) {
        return getPlayerAccessLevel(player).ordinal() >= AccessLevel.ADMIN.ordinal();
    }

    public boolean isAdmin(String name) {
        return getPlayerAccessLevel(name).ordinal() >= AccessLevel.ADMIN.ordinal();
    }

    public boolean isPrivileged(EntityPlayerMP player) {
        return getPlayerAccessLevel(player).ordinal() >= AccessLevel.PRIVILEGED.ordinal();
    }

    public boolean isPrivileged(String name) {
        return getPlayerAccessLevel(name).ordinal() >= AccessLevel.PRIVILEGED.ordinal();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        playerOverlayStatus.remove(event.player);
        playerDimension.remove(event.player);
        playersSwing.remove(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PacketManager.validateAndSend(new NetDataValue(Message.STATUS_CURRENT_TIME, new SerialLong(System.currentTimeMillis())), (EntityPlayerMP) event.player);
        StringCache.INSTANCE.syncCache((EntityPlayerMP) event.player);
    }
}
