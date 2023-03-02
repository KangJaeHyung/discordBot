package loaSSalmuckBot.com.Listener;

import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class VoiceChannelListener extends ListenerAdapter {


	final String VOICECHANNEL_1 = "992081495147696229";
	final String VOICECHANNELNAME_1="🔈 | 4인 숙련";
	final String VOICECHANNEL_2 = "992082096820592803";
	final String VOICECHANNELNAME_2="🔈 | 8인 숙련";
	final String VOICECHANNEL_3 = "992082717464338672";
	final String VOICECHANNELNAME_3="🔈 | 4인 트라이";
	final String VOICECHANNEL_4 = "992082365725810718";
	final String VOICECHANNELNAME_4="🔈 | 8인 트라이";
	
	final String VOICECHANNEL_5 = "1041736836525084713";
	final String VOICECHANNELNAME_5="🔈 | 기타 게임1";
	final String VOICECHANNEL_6 = "1041736879378268210";
	final String VOICECHANNELNAME_6="🔈 | 기타 게임2";
	final String VOICECHANNEL_7 = "992083959523586159";
	final String VOICECHANNELNAME_7="🔈 | 기타 게임3";
	
	
	

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
		switch (channelId) {
			case VOICECHANNEL_1: {
				createChannel(event, VOICECHANNELNAME_1);
				break;
				}
			case VOICECHANNEL_2: {
				createChannel(event, VOICECHANNELNAME_2);
				break;
				}
			case VOICECHANNEL_3: {
				createChannel(event, VOICECHANNELNAME_3);
				break;
				}
			case VOICECHANNEL_4: {
				createChannel(event, VOICECHANNELNAME_4);
				break;
				}
			case VOICECHANNEL_5: {
				createChannel(event, VOICECHANNELNAME_5);
				break;
				}
			case VOICECHANNEL_6: {
				createChannel(event, VOICECHANNELNAME_6);
				break;
				}
			case VOICECHANNEL_7: {
				createChannel(event, VOICECHANNELNAME_7);
				break;
				}
			case "1078518672735355013": {
				createChannel(event, VOICECHANNELNAME_7);
				break;
				}
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
