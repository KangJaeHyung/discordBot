package loaSSalmuckBot.com.Listener;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


@Component
public class GuildListener extends ListenerAdapter {

	@Autowired
	private VoiceService voiceService;
	

	
//	@Override
//	public void onMessageReceived(MessageReceivedEvent event) {
//		MessageChannelUnion channel = event.getChannel();
//		String msg = event.getMessage().getContentRaw();
//		System.out.println("channelId: "+channel.getIdLong()+", name"+event.getMember().getUser().getName()+", content"+event.getMessage().getContentRaw());
//		
//		String[] msgSplit = msg.split(" ");
//		if(msgSplit[0].equals("!유저생성")) {
//		}
//	}
	

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		VoiceChannelEntity entity = voiceService.getChannelByGiven(Given.WELCOMECHAN);
		if(entity==null) return;
		StringBuilder builder = new StringBuilder();
		builder.append(":one:닉네임은 인게임 아이디로 변경");
		builder.append(System.getProperty("line.separator"));
		builder.append(":two:디스코드 이용시 길드원간 매너를 필수");
		builder.append(System.getProperty("line.separator"));
		builder.append(":three:사유없이 탈퇴한 경우 재가입이 불가");
		builder.append(System.getProperty("line.separator"));
		builder.append(":four:길드 컨텐츠 강제없는 자유");
		builder.append(System.getProperty("line.separator"));
		builder.append(":five:활동이 저조한 경우 추방 될 수 있음");
		MessageEmbed embed = new EmbedBuilder()
				.setAuthor("로스트아크 카마인 오드길드")
				.setTitle("**어서오세요. 반갑습니다!**").setColor(new Color(221, 245, 166))

				.setThumbnail("https://cdn-longterm.mee6.xyz/plugins/welcome/images/832794285178355714/0074d4b6bdfc6b7bc3c4dd60c36d5595f5f2687c033090e5c2abb8154407c538.gif")
				.setDescription(event.getMember().getAsMention()+"님, **카마인 오드 길드**에 오신 것을 환영합니다. :hugging:")
				.addField(":loudspeaker: 잠시만 기다리시면 운영진이 등업 해드립니다!", "<@&832801295336341516><@&832801296645488651><@&832801297865506826>", false)
				.addField(":warning: **가입 전 꼭 확인해주세요!**", builder.toString(), false)
				.setImage("https://cdn-longterm.mee6.xyz/plugins/welcome/images/832794285178355714/76e1dcbb050177ef4917a6ac6e69c9e851d816008a3a1b2e4c6d4444e0963e8a.gif")
				.setFooter("잘 부탁드립니다!!").build();
		
		event.getGuild().getTextChannelById(entity.getId()).sendMessageEmbeds(embed).queue();
		
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		VoiceChannelEntity entity = voiceService.getChannelByGiven(Given.WELCOMECHAN);
		if(entity==null) return;
		event.getGuild().getTextChannelById(entity.getId()).sendMessage(event.getUser().getName()+"님 안녕히 가세요.").queue();
	}
	


}
