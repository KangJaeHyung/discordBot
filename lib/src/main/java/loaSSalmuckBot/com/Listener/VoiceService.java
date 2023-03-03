package loaSSalmuckBot.com.Listener;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Service
public class VoiceService {

	@Autowired
	public VoiceChannelRepository voiceChannelRepository;

	
	public void createGivenCreateChannel(SlashCommandInteractionEvent event) throws Exception{
		VoiceChannelEntity entity = new VoiceChannelEntity();
		VoiceChannel channel=  event.getOption("channel").getAsChannel().asVoiceChannel();
		String creName = event.getOption("name").getAsString();
		if(!voiceChannelRepository.findById(channel.getId()).isEmpty()) {
			event.reply("이미 채널 생성 및 이동부여 상태가 있습니다. 권한 삭제 후 다시 시도해 주세요.").queue();
			return;
		}
		entity.setId(channel.getId());
		entity.setGiven("CRECHAN");
		entity.setName(channel.getName());
		entity.setCreateName(creName);
		voiceChannelRepository.save(entity);
		event.reply("**"+channel.getName()+"** 채널 이동 이벤트 생성이 완료 되었습니다.").queue();
	}
	
	public void deleteGivenCreateChannel(SlashCommandInteractionEvent event) throws Exception{
		VoiceChannel channel=  event.getOption("channel").getAsChannel().asVoiceChannel();
		voiceChannelRepository.deleteById(channel.getId());
	}


	public VoiceChannelEntity findGiven(String channelId) {
		Optional<VoiceChannelEntity> entityOp = voiceChannelRepository.findById(channelId);
		
		if(entityOp.isEmpty()) return null;
		VoiceChannelEntity entity= entityOp.get();
		if(!entity.getGiven().equals("CRECHAN")) return null;
		return entity;
	}

}
