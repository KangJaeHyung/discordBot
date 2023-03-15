package loaSSalmuckBot.com.Listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.math3.util.ContinuedFraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.service.DiscordService;
import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.LostArkDto.ArmoryProfile;
import loaSSalmuckBot.com.util.LoaRestAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
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
public class CommandListener extends ListenerAdapter {

	@Autowired
	public DiscordService discordService;
	
	@Autowired
	public VoiceService voiceService;

	@Autowired
	private LoaRestAPI api;


	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		String command = event.getName();

		switch (command) {
		case "채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createGivenCreateChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("채널 이동 이벤트 생성이 완료 되지 못하였습니다. 보이스채널인지 다시한번 확인 해 주세요.").queue();
			}
			
			break;
		}
		case "유튜브채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createGivenYutubeChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("유튜브 채널 이벤트 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			
			break;
		}
		case "신입채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createGivenWelcomeChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("신입 환영 채널 이벤트 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			
			break;
		}
		case "공지채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createGivenNotiChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("공지 이벤트 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			break;
		}
		case "채널설정삭제": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.deleteGivenCreateChannel(event);
				event.reply("채널 이동 이벤트가 삭제되었습니다.").queue();
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("채널 이동 이벤트 생성이 삭제되지 못 하였습니다. 다시 시도해 주세요").queue();
			}
			
			break;
		}
		case "채널확인": {
				voiceService.getChannel(event);
			
			break;
		}
		case "공략": {
			discordService.raid(event);
			break;
		}
		
		case "경매": {
			this.auction(event);
			break;
		}
		case "대화": {
			try {
				event.reply("AI가 채팅을 치는 중 입니다...").queue();
				String chat = event.getOption("chat").getAsString();
				CompletableFuture<String> dap = api.getChatGpt(chat,discordService.getBeforeUserChat(event.getMember().getId()));
				dap.thenAccept(response -> {
					event.getHook().editOriginal(event.getMember().getAsMention()+": "+chat +System.getProperty("line.separator")+System.getProperty("line.separator")+response).queue();
					discordService.saveUserChat(event.getMember().getId(), chat,response);
		        });
				break;
//				event.editMessage(dap).queue();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}	
		}
		case "연동": {
			try {
				event.reply("연동중 입니다...").queue();
				String user = event.getOption("user").getAsString();
				CompletableFuture<ArmoryProfile> dap = api.getLoaUser(user);
				dap.thenAccept(response -> {
					if(response==null) event.getHook().editOriginal("유저 정보를 가져올 수 없습니다. 다시 확인해 주세요.").queue();
					try {
						String name = response.getCharacterName()+"/"+response.getCharacterClassName()+"/"+Math.floor(Float.parseFloat(response.getItemMaxLevel().replace(",","")));
						if( event.getOption("member")==null) {
							event.getMember().modifyNickname(name).queue();
							for(Role role:  event.getMember().getRoles()) {
								if(role.getName().equals("운영진")||role.getName().equals("부길드장")) continue;
								event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
							}
							event.getGuild().addRoleToMember(event.getMember(),event.getGuild().getRolesByName(response.getCharacterClassName(), true).get(0)).queue();
							event.getGuild().addRoleToMember(event.getMember(),event.getGuild().getRolesByName("길드원", true).get(0)).queue();
						}else {
							Member member =event.getOption("member").getAsMember();
							member.modifyNickname(name).queue();
							for(Role role:  member.getRoles()) {
								if(role.getName().equals("운영진")||role.getName().equals("부길드장")) continue;
								event.getGuild().removeRoleFromMember(member, role).queue();
							}
							event.getGuild().addRoleToMember(member,event.getGuild().getRolesByName(response.getCharacterClassName(), true).get(0)).queue();
							event.getGuild().addRoleToMember(member,event.getGuild().getRolesByName("길드원", true).get(0)).queue();
						}
						MessageEmbed embed = new EmbedBuilder().setTitle(":scales: **연동 완료**").setColor(new Color(157, 216, 75)).setAuthor(response.getCharacterName())
								.setImage(response.getCharacterImage())
								.setFooter("[머쓱해요]").build();
						
						event.getHook().editOriginalEmbeds(embed).queue();
						
					} catch (Exception e) {
						event.getHook().editOriginal("닉네임 변경중 문제가 발생하였습니다.").queue();
						e.printStackTrace();
					}
		        });
				
				break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}	
			
		}
		case "엘릭서": {
			try {
				event.reply("잠시만 기다려주세요...").queue();
				String user = event.getOption("user").getAsString();
				CompletableFuture<MessageEmbed> dap = api.getElixir(user);
				dap.thenAccept(response -> {
					event.getHook().editOriginalEmbeds(response).queue();
//					discordService.saveUserChat(event.getMember().getId(), chat,response);
		        });
				break;
//				event.editMessage(dap).queue();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}	
		}
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
		data.add(Commands.slash("공략", "해당 레이드 공략을 보여줍니다.").addOptions(new OptionData(OptionType.STRING,  "raid", "레이드 및 어비스던전",true)
				.addChoice("발탄", "valtan")
				.addChoice("비아키스", "biackiss")
				.addChoice("쿠크세이튼", "kuoku")
				.addChoice("아브렐슈드", "abrelshud")
				.addChoice("카양겔", "kayangel")
				.addChoice("일리아칸", "illiakan")
				.addChoice("상아탑", "tower")
				));
		data.add(Commands.slash("대화", "AI와 대화를 할 수 있습니다.").addOption(OptionType.STRING, "chat", "채팅", true));
		data.add(Commands.slash("공지채널설정", "신입 채널을 이벤트를 부여 합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("신입채널설정", "신입 채널을 이벤트를 부여 합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("유튜브채널설정", "유튜브 알람 이벤트를 부여합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("채널설정", "음성 채널 생성 및 이동 이벤트를 부여합니다.")
				.addOption(OptionType.CHANNEL, "channel", "채널이름", true)
				.addOption(OptionType.STRING, "name", "생성될 채널이름", true));
		data.add(Commands.slash("채널확인", "이벤트가 걸려있는 체널 목록을 보여줍니다."));		
		data.add(Commands.slash("채널설정삭제", "해당 채널 이벤트를 삭제합니다")
				.addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("연동", "현재 디스코드 닉네임을 로스트아크api와 연동하여 닉네임을 변경합니다.").addOption(OptionType.STRING, "user", "유저", true).addOption(OptionType.USER, "member", "변경할 유저", false));
		data.add(Commands.slash("엘릭서", "엘릭서 증가 수치를 보여줍니다").addOption(OptionType.STRING, "user", "유저", true));
				
		
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
		discordService.choiceFormate(event);
	}
	
	public boolean roleCheck(SlashCommandInteractionEvent event) {
		
		List<Role> list =  event.getMember().getRoles();
		boolean isRole= false;
		for(Role role:list) {
			if(role.getId().equals("832801295336341516")||role.getId().equals("832801296645488651")||role.getId().equals("832801297865506826")){
				isRole=true;
				break;
			}
		}
		return isRole;
		
	}
	
}