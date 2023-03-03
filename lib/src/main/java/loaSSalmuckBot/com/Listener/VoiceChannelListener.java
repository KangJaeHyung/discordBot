package loaSSalmuckBot.com.Listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class VoiceChannelListener extends ListenerAdapter {


	@Autowired
	private VoiceService voiceService;

	@Override
	public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
		Member member = event.getEntity();
		AudioChannelUnion channelLeft = event.getChannelLeft();
		AudioChannelUnion channelJoined = event.getChannelJoined();

		if (channelJoined != null) {
			this.joinEvent(event);
		}
		if (channelLeft != null) {
			this.leftEvent(event);
		}

	}

	private void joinEvent(GuildVoiceUpdateEvent event) {
		AudioChannelUnion channelJoined = event.getChannelJoined();
		String channelId = channelJoined.getId();
		VoiceChannelEntity entity= voiceService.findGiven(channelId);
		if(entity!=null) {
			createChannel(event, entity.getCreateName());
			return;
		}
	}

	private void leftEvent(GuildVoiceUpdateEvent event) {
		AudioChannelUnion channelLeft = event.getChannelLeft();
		Guild guild = event.getGuild();
		Category category = event.getGuild().getCategoriesByName("보이스채널", true).get(0);
		VoiceChannel voiceChannel = guild.getVoiceChannelById(channelLeft.getIdLong());
		if (voiceChannel.getParentCategory().equals(category) && voiceChannel.getMembers().size() == 0) {
			voiceChannel.delete().queue();
		}
	}

	private void createChannel(GuildVoiceUpdateEvent event, String channelName) {
		Guild guild = event.getGuild();
		Member member = event.getMember();
		AudioChannelUnion currentChannel = member.getVoiceState().getChannel();

		if (currentChannel != null) {
			Category category = event.getGuild().getCategoriesByName("보이스채널", true).get(0);
			VoiceChannel newChannel = category.createVoiceChannel(channelName).complete();
			guild.moveVoiceMember(member, newChannel).queue();

		}

	}

}
