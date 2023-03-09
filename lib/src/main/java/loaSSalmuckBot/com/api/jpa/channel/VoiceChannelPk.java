package loaSSalmuckBot.com.api.jpa.channel;

import java.io.Serializable;

import lombok.Data;

@Data
public class VoiceChannelPk implements Serializable{
	private String guildId;

	private String id;
	
	public VoiceChannelPk(String guildId,String id){
		this.guildId=guildId;
		this.id=id;
	}
	
	public VoiceChannelPk() {
		
	}
}
