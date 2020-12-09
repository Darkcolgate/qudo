package me.replydev.mcping.data;

import com.google.gson.annotations.SerializedName;
import me.replydev.mcping.rawData.ExtraDescription;

public class ExtraResponse extends MCResponse {

    @SerializedName("description")
    private ExtraDescription description;

    public FinalResponse toFinalResponse(){
        return new FinalResponse(players,version,favicon,description.getText());
    }

}
