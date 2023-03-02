package loaSSalmuckBot.com.Listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.LoaApi.LoaRestAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Component
public class MyApplicationCommand extends ListenerAdapter {

	@Autowired
	public DiscordService service;

	@Autowired
	private LoaRestAPI api;

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		String command = event.getName();

		switch (command) {
		case "채널생성": {
			if (!event.getChannel().getId().equals("1078478527847993384")) {
				event.reply("지정된 채널이 아닙니다!").setEphemeral(true).queue();
				return;
			}
			this.createChannel(event);
			break;
		}
		case "발탄": {
			service.valtan(event);
			break;
		}
		case "비아키스": {
			service.biackiss(event);
			break;
		}
		case "쿠크세이튼": {
			service.kuoku(event);
			break;
		}
		case "아브렐슈드": {
			service.abrelshud(event);
			break;
		}
		case "카양겔": {
			service.kayangel(event);
			break;
		}
		case "일리아칸": {
			service.illiakan(event);
			break;
		}
		case "상아탑": {
			service.tower(event);
			break;
		}
		case "경매": {
			this.auction(event);
			break;
		}
		case "대화": {
			try {
				event.reply("AI가 채팅을 치는 중 입니다...").setEphemeral(true).queue();
				CompletableFuture<String> dap = api.getChatGpt(event.getOption("chat").getAsString());
				dap.thenAccept(response -> {
					event.getHook().editOriginal(response).queue();
		        });
				
//				event.editMessage(dap).queue();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
//		
//		case "프로필": {
//			
//			}
		}

	}
	
	
	
	
	
	

	private void createChannel(SlashCommandInteractionEvent event) {
		Guild guild = event.getGuild();
		Member member = event.getMember();
		AudioChannelUnion currentChannel = member.getVoiceState().getChannel();

		if (currentChannel != null) {
			Category category = event.getGuild().getCategoriesByName("보이스채널", true).get(0);
			VoiceChannel newChannel = category.createVoiceChannel("🔈 | " + event.getOption("name").getAsString())
					.complete();
			guild.moveVoiceMember(member, newChannel).queue();
			event.reply("생성완료!").queue();
		} else {
			Category category = event.getGuild().getCategoriesByName("보이스채널", true).get(0);
			VoiceChannel newChannel = category.createVoiceChannel("🔈 | " + event.getOption("name").getAsString())
					.complete();
			Invite invite = newChannel.createInvite().complete();
			event.reply("생성완료!" + System.getProperty("line.separator") + invite.getUrl()).setEphemeral(true).queue();
		}
	}

	@Override
	public void onGuildReady(GuildReadyEvent event) {
		List<CommandData> data = new ArrayList<>();
		data.add(Commands.slash("경매", "선점 입찰가 및 1/N 입찰가를 알려줍니다").addOption(OptionType.INTEGER, "gold", "가격", true)
				.addOption(OptionType.INTEGER, "person", "인원수", false));
		data.add(Commands.slash("채널생성", "음성 채널을 생성합니다.").addOption(OptionType.STRING, "name", "채널이름", true));
		data.add(Commands.slash("발탄", "발탄 공략을 보여줍니다.").addOption(OptionType.INTEGER, "gateway", "관문", false));
		data.add(Commands.slash("비아키스", "비아키스 공략을 보여줍니다.").addOption(OptionType.INTEGER, "gateway", "관문", false));
		data.add(Commands.slash("쿠크세이튼", "쿠크세이튼 공략을 보여줍니다.").addOption(OptionType.INTEGER, "gateway", "관문", false));
		data.add(Commands.slash("아브렐슈드", "아브렐슈드 공략을 보여줍니다.").addOption(OptionType.INTEGER, "gateway", "관문", false));
		data.add(Commands.slash("카양겔", "카양겔 공략을 보여줍니다.").addOptions(new OptionData(OptionType.STRING,  "gateway", "관문")
				.addChoice("파수꾼", "griffon")
				.addChoice("티엔", "tien")
				.addChoice("프리우나", "priuna")
				.addChoice("라우리엘", "lauriel")));
		
		data.add(Commands.slash("일리아칸", "일리아칸 공략을 보여줍니다.").addOption(OptionType.INTEGER, "gateway", "관문", false));
		data.add(Commands.slash("상아탑", "상아탑 공략을 보여줍니다.").addOption(OptionType.INTEGER, "gateway", "관문", false));
		data.add(Commands.slash("대화", "AI와 대화를 할 수 있습니다.").addOption(OptionType.STRING, "chat", "채팅", true));
				
		
//		data.add(Commands.slash("유저생성","유저 생성합니다.").addOption(OptionType.USER, "user", "유저" , true));
		event.getGuild().updateCommands().addCommands(data).queue();

	}
	
	
	
	
	
	

	private void auction(SlashCommandInteractionEvent event) {

		String userTag = event.getUser().getAsTag();
		Integer gold = event.getOption("gold").getAsInt();
		Integer person = event.getOption("person") != null ? event.getOption("person").getAsInt() : -1;
		Double eightPeopleDis = gold * 0.95 * 7 / 8;
		Double eightPeopleDisBefore = gold * 0.95 * 7 / 8 * 1.1;
		Double fourPeopleDis = gold * 0.95 * 3 / 4;
		Double fourPeopleDisBefore = gold * 0.95 * 3 / 4 * 1.1;
		StringBuilder strBuild = new StringBuilder();
		if (person != -1) {
			Double personDis = gold * 0.95 * (person - 1) / person;
			Double personDisBefore = gold * 0.95 * (person - 1) / person * 1.1;
			strBuild.append("**" + person + "인**  ");
			strBuild.append(personDis.intValue() + ":gem:");
			strBuild.append(personDisBefore.intValue() + ":gem:");
		} else {
			strBuild.append("**8인** ");
			strBuild.append(eightPeopleDis.intValue() + ":gem:  ");
			strBuild.append(eightPeopleDisBefore.intValue() + ":gem:");
			strBuild.append(System.getProperty("line.separator"));
			strBuild.append("**4인**  ");
			strBuild.append(fourPeopleDis.intValue() + ":gem:  ");
			strBuild.append(fourPeopleDisBefore.intValue() + ":gem:");
		}

		MessageEmbed embed = new EmbedBuilder().setTitle(":scales: **경매**").setColor(new Color(157, 216, 75))
				.setThumbnail("https://cdn-lostark.game.onstove.com/EFUI_IconAtlas/Use/Use_9_24.png")
				.addField("**입력 금액**", gold.toString() + ":gem:", false)
				.addField("입찰가(인원,분배,선점)", strBuild.toString(), false)
				.setFooter("쌀먹 하고 싶으면 분배금과 선점금 사이로 입찰 하시면 됩니다! [머쓱해요]").build();

//		event.getChannel().sendMessageEmbeds(null)
		event.replyEmbeds(embed).setEphemeral(true).queue();
//		  List<SelectOption> options = new ArrayList<>(Arrays.asList(
//	                SelectOption.of("option_1", "Option 1"),
//	                SelectOption.of("option_2", "Option 2"),
//	                SelectOption.of("option_3", "Option 3")
//	        ));
//        
//        SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("Select an option").addOptions(options).build();
//        
//		event.replyEmbeds(embed).addActionRow(menu).queue();
	}
	
	
	
	
	
	
	@Override
	public void onStringSelectInteraction(StringSelectInteractionEvent event) {
		service.choiceFormate(event);
	}
	
	
	
}