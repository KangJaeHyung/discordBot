package loaSSalmuckBot.com.Listener.service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Service
@Slf4j
public class VoiceService {

	@Autowired
	public VoiceChannelRepository voiceChannelRepository;

	public static List<VoiceChannelEntity> channel;

	@PostConstruct
	public void init() {
		channel = this.findAll();
		log.info("channel {}",channel);
	}

	public void createGivenCreateChannel(SlashCommandInteractionEvent event) throws Exception {
		VoiceChannelEntity entity = new VoiceChannelEntity();
		VoiceChannel channel = event.getOption("channel").getAsChannel().asVoiceChannel();
		String creName = event.getOption("name").getAsString();
		Integer person = event.getOption("person") == null ? null : event.getOption("person").getAsInt();
		if (voiceChannelRepository.findByGuildIdAndChannelId(channel.getGuild().getId(), channel.getId())
				.isPresent()) {
			event.reply("이미 채널 생성 및 이동부여 상태가 있습니다. 권한 삭제 후 다시 시도해 주세요.").queue();
			return;
		}
		entity.setGuildId(channel.getGuild().getId());
		entity.setChannelId(channel.getId());
		entity.setGiven(Given.CRECHAN);
		entity.setName(channel.getName());
		entity.setCreateName(creName);
		entity.setPerson(person);

		voiceChannelRepository.save(entity);
		VoiceService.channel.add(entity);
		event.reply("**" + channel.getName() + "** 채널 이동 이벤트 생성이 완료 되었습니다.").queue();
	}

	public void deleteGivenCreateChannel(SlashCommandInteractionEvent event) throws Exception {
		StandardGuildChannel channel = event.getOption("channel").getAsChannel().asStandardGuildChannel();
		voiceChannelRepository.deleteByGuildIdAndChannelId(channel.getGuild().getId(), channel.getId());
		VoiceService.channel=this.findAll();
	}

	public VoiceChannelEntity findGiven(String guildId, String channelId) {
		Optional<VoiceChannelEntity> entityOp = voiceChannelRepository.findByGuildIdAndChannelId(guildId, channelId);

		if (!entityOp.isPresent())
			return null;
		VoiceChannelEntity entity = entityOp.get();
		if (!entity.getGiven().equals(Given.CRECHAN))
			return null;
		return entity;
	}

	public void getChannel(SlashCommandInteractionEvent event) {
//		List<VoiceChannelEntity> entitys= voiceChannelRepository.findAll();

		StringBuilder builder = new StringBuilder();
		channel.forEach(entity -> {
			builder.append("[**" + entity.getName() + "**] ");
			if (entity.getCreateName() != null)
				builder.append("**" + entity.getCreateName() + "**");
			builder.append(entity.getGiven().getValue());
			builder.append(System.getProperty("line.separator"));
		}
		);
//		for(VoiceChannelEntity entity:) {
//				
//		}
//			
		MessageEmbed embed = new EmbedBuilder().setTitle(":satellite: **채널 이벤트 목록**").setColor(new Color(157, 216, 75))
				.addField("**목록**", builder.toString(), false).setFooter("각 이벤트 삭제 기능은 /채널이벤트삭제 명령어를 사용하시면 됩니다.")
				.build();

		event.replyEmbeds(embed).queue();

	}

	public void createGivenYutubeChannel(SlashCommandInteractionEvent event) {
		TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
		if (voiceChannelRepository.countByGiven(Given.YUTUBECHAN) > 0) {
			event.reply("이미 유튜브 알림 이벤트가 부여가 되어 있습니다. 권한 삭제 후 다시 시도해 주세요.").queue();
			return;
		}
		this.createChannel(Given.YUTUBECHAN, channel);
		event.reply("**" + channel.getName() + "** 유튜브 알림 이벤트 생성이 완료 되었습니다.").queue();
	}

	public void createGivenNotiChannel(SlashCommandInteractionEvent event) {
		TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
		if (voiceChannelRepository.findByChannelIdAndGiven(channel.getId(), Given.NOTICHAN).isPresent()) {
			event.reply("이미 공지 알림 이벤트가 부여가 되어 있습니다. 권한 삭제 후 다시 시도해 주세요.").queue();
			return;
		}
		this.createChannel(Given.NOTICHAN, channel);
		event.reply("**" + channel.getName() + "** 공지 알림 이벤트 생성이 완료 되었습니다.").queue();
	}

	public void createGivenWelcomeChannel(SlashCommandInteractionEvent event) {
		TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
		if (voiceChannelRepository.findByChannelIdAndGiven(channel.getId(), Given.WELCOMECHAN).isPresent()
				|| voiceChannelRepository.countByGiven(Given.WELCOMECHAN) > 0) {
			event.reply("이미 신입 환영 알림 이벤트가 부여가 되어 있습니다. 권한 삭제 후 다시 시도해 주세요.").queue();
			return;
		}
		this.createChannel(Given.WELCOMECHAN, channel);
		event.reply("**" + channel.getName() + "** 신입 환영 알림 이벤트 생성이 완료 되었습니다.").queue();
	}
	
	
	public void createBotChannel(SlashCommandInteractionEvent event) {
		TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
		if (voiceChannelRepository.findByChannelIdAndGiven(channel.getId(), Given.BOTCHAN).isPresent()) {
			event.reply("이미 봇채널 이벤트가 부여가 되어 있습니다.").queue();
			return;
		}
		this.createChannel(Given.BOTCHAN, channel);
		event.reply("**" + channel.getName() + "** 봇채널 생성이 완료 되었습니다.").queue();
	}

	private void createChannel(Given given, StandardGuildChannel channel) {
		VoiceChannelEntity entity = new VoiceChannelEntity();
		entity.setGuildId(channel.getGuild().getId());
		entity.setChannelId(channel.getId());
		entity.setGiven(given);
		entity.setName(channel.getName());
		VoiceService.channel.add(entity);
		voiceChannelRepository.save(entity);
	}

	public VoiceChannelEntity getChannelByGiven(Given given) {
		VoiceChannelEntity entity = voiceChannelRepository.findByGiven(given);
		return entity;
	}

	public List<VoiceChannelEntity> findAll() {

		
		return voiceChannelRepository.findAll();
	}
}
