package loaSSalmuckBot.com.Listener;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.DiscordService;
import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;

@Component
public class GuildListener extends ListenerAdapter {

	@Autowired
	private VoiceService voiceService;

	@Autowired
	private DiscordService discordService;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		MessageChannelUnion channel = event.getChannel();
		String msg = event.getMessage().getContentRaw();

		String[] msgSplit = msg.split(" ");
		if (msgSplit[0].equals("!check")) {
			VoiceChannelEntity entity = voiceService.getChannelByGiven(Given.WELCOMECHAN);
			System.out.println(entity);
		}
		if (msgSplit[0].equals("!ban")) {
			if (msgSplit.length == 1) {
				channel.sendMessage("명령어가 잘못 되었습니다. \r\n" + "!ban [userId] [ban reason]\r\n" + "userId : id , 필수값\r\n"
						+ "ban reason : 밴사유, 필수아님").queue();
				return;
			} else {
				String id = msgSplit[1].replace("<", "").replace(">", "").replace("@", "");
				String reason = "";
				
				if(msgSplit.length >= 3) {
					for(int i = 2;i<msgSplit.length;i++) {
						reason = reason +" " +msgSplit[i];
					}
				}
				System.out.println(reason);
				
				try {CacheRestAction<Member> banMemberCache = event.getGuild().retrieveMemberById(id);
					Member banMember = banMemberCache.complete();
					event.getGuild().ban(banMember, 0, TimeUnit.SECONDS)
							.reason(reason)
							.queue();
					discordService.userBan(event.getGuild(), id, reason);
					MessageEmbed embed = new EmbedBuilder().setAuthor(banMember.getUser().getName() + "을 추방 하였습니다.")
							.setColor(new Color(157, 216, 75))
							.addField("", "**사유:**" + (reason), false).build();
					channel.sendMessageEmbeds(embed).queue();
				} catch (Exception e) {
					channel.sendMessage("해당 유저는 현재 디스코드에 없습니다.").queue();
					return;
				}

			}
		}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		VoiceChannelEntity entity = voiceService.getChannelByGiven(Given.WELCOMECHAN);
		if (entity == null)
			return;
		StringBuilder builder = new StringBuilder();
		builder.append(":one:디스코드 이용시 길드원간 매너를 필수");
		builder.append(System.getProperty("line.separator"));
		builder.append(":two:사유없이 탈퇴한 경우 재가입이 불가");
		builder.append(System.getProperty("line.separator"));
		builder.append(":three:길드 컨텐츠 강제없는 자유");
		builder.append(System.getProperty("line.separator"));
		builder.append(":four:인게임 및 디스코드 활동이 저조한 경우 추방 될 수 있음");
		MessageEmbed embed = new EmbedBuilder().setAuthor("로스트아크 카마인 오드길드").setTitle("**어서오세요. 반갑습니다!**")
				.setColor(new Color(221, 245, 166))

				.setThumbnail(
						"https://cdn-longterm.mee6.xyz/plugins/welcome/images/832794285178355714/0074d4b6bdfc6b7bc3c4dd60c36d5595f5f2687c033090e5c2abb8154407c538.gif")
				.setDescription(event.getMember().getAsMention() + "님, **카마인 오드 길드**에 오신 것을 환영합니다. :hugging:")
				.addField(":loudspeaker: `/연동 {인게임닉네임}` 명령어를 입력 후 운영진을 찾아주세요!!",
						"<@&832801295336341516><@&832801296645488651><@&832801297865506826>", false)
				.addField(":warning: **가입 전 꼭 꼭 확인해주세요!**", builder.toString(), false)
				.setImage(
						"https://cdn-longterm.mee6.xyz/plugins/welcome/images/832794285178355714/76e1dcbb050177ef4917a6ac6e69c9e851d816008a3a1b2e4c6d4444e0963e8a.gif")
				.setFooter("잘 부탁드립니다!!").build();

		event.getGuild().getTextChannelById(entity.getChannelId()).sendMessageEmbeds(embed).queue();

	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		System.out.println(event.getUser().getName() +"님이 나가셨습니다.");
		discordService.removeUser(event.getUser().getId());
		
	}

}
