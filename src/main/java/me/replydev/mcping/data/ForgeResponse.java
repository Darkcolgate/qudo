package me.replydev.mcping.data;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.rawData.*;

public class ForgeResponse {

    @SerializedName("description")
    private Description description;

    @SerializedName("players")
    private Players players;

    @SerializedName("version")
    private Version version;

    @SerializedName("modinfo")
    private ForgeModInfo modinfo;

    public FinalResponse toFinalResponse(){
        version.setName(version.getName() + " FML with " + modinfo.getNMods() + " mods");
        return new FinalResponse(players,version,"",description.getText());
    }
}