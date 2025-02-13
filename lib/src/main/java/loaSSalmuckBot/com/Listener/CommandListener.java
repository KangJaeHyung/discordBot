package loaSSalmuckBot.com.Listener;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.DiscordService;
import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.LostArkDto.ArmoryProfile;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.util.LoaRestAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
		String guildId = event.getGuild().getId();
		String channelId = event.getChannel().getId();
		List<VoiceChannelEntity> entitys = new ArrayList<>();
		
		VoiceService.channel.forEach(entity -> {
			if(entity.getGiven().equals(Given.BOTCHAN)) entitys.add(entity);
			
		});
		
		if (entitys.size() == 0) {
			if(!command.equals("봇채널설정")) {
				event.reply("봇 채널이 아닙니다! 봇 채널을 설정 해주세요!").setEphemeral(true).queue();
				return;
			}
			
		} else {
			boolean isBotChan = false;
			String text = "봇 채널이 아닙니다! 봇 채널을 이용해 주세요.";
			for (VoiceChannelEntity entity : entitys) {
				if(entity.getChannelId().equals(channelId)) isBotChan= true;
				text = text + "\r\n" + event.getGuild().getGuildChannelById(entity.getChannelId()).getAsMention();
			}
			if(!isBotChan) {
				event.reply(text).setEphemeral(true).queue();
				return;
			}
		}
		

		
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
		case "봇채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createBotChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("봇 채널 이벤트 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			
			break;
		}
		case "생일채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createBirthDayChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("생일채널 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			
			break;
		}
		case "생일알람채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createBirthDayAlChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("생일알람채널 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			
			break;
		}

		case "채팅채널설정": {
			try {
				if(!roleCheck(event)) {
					event.reply("권한이 없습니다.").queue();
					return;
				}
				voiceService.createChatChannel(event);
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("채팅채널 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
			}
			break;
		}

//		case "공지채널설정": {
//			try {
//				if(!roleCheck(event)) {
//					event.reply("권한이 없습니다.").queue();
//					return;
//				}
//				voiceService.createGivenNotiChannel(event);
//			} catch (Exception e) {
//				e.printStackTrace();
//				event.reply("공지 이벤트 생성이 완료 되지 못하였습니다. 텍스트채널인지 다시한번 확인 해 주세요.").queue();
//			}
//			break;
//		}
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
		case "연동": {
			try {
				event.reply("연동중 입니다...").queue();
				String user = event.getOption("user").getAsString();
				CompletableFuture<ArmoryProfile> dap = api.getLoaUser(user);
				dap.thenAccept(response -> {
					if(response==null) {
						event.getHook().editOriginal("유저 정보를 가져올 수 없습니다. 다시 확인해 주세요.").queue();
						return;
					}
					try {
						String name = response.getCharacterName()+"/"+response.getCharacterClassName()+"/"+ (int)Math.floor(Double.parseDouble(response.getItemMaxLevel().replace(",","")));
						String memberId =event.getOption("member")==null?event.getMember().getId():event.getOption("member").getAsMember().getId();
						String userClass =response.getCharacterClassName();
						MessageEmbed embed = new EmbedBuilder().setTitle(":scales: **연동 완료**").setAuthor(response.getCharacterName()).setColor(new Color(157, 216, 75))
								.addField("Nickname",name,true)
								.addField("Member", memberId, true)
								.addField("Class", userClass,true)
								.addField("name",response.getCharacterName(),true)
								.setImage(response.getCharacterImage())
								.setFooter("승인 후 등업됩니다").build();
						
						event.getHook().editOriginalEmbeds(embed).setContent("").setActionRow(Button.success("approveIntegration", "승인"), Button.danger("rejectIntegration", "반려")).queue();
						
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
		case "게스트": {
			if(!roleCheck(event)) {
				event.reply("권한이 없습니다.").queue();
				return;
			}
			Member member =event.getOption("member").getAsMember();
			for(Role role:  member.getRoles()) {
				event.getGuild().removeRoleFromMember(member, role).queue();
			}
			member.modifyNickname("(Guest)"+member.getUser().getName()).queue();
			event.getGuild().addRoleToMember(member,event.getGuild().getRolesByName("게스트", true).get(0)).queue();
			event.reply("게스트 설정이 완료되었습니다.").queue();
			break;
			
		}
		case "생일삭제": {
			if(!roleCheck(event)) {
				event.reply("권한이 없습니다.").queue();
				return;
			}
			Member member =event.getOption("member").getAsMember();
			discordService.setBirthday(member.getId(),null);
			event.reply(member.getAsMention() +"의 생일을 삭제 하였습니다.").queue();
			break;
			
		}
		case "생일설정": {
			
            String memberId = event.getOption("member")==null?event.getMember().getId():event.getOption("member").getAsMember().getId();
            String date = "2000"+event.getOption("date").getAsString();
            Date date2 = null;
          //birthday 를 date로 변환
			try {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate localDate = LocalDate.parse(date, formatter);
				date2 = java.sql.Date.valueOf(localDate);
			} catch (Exception e) {
				event.reply("생일을 정확히 입렵해주세요 ex)0307").queue();
				return;
			}
    		
			discordService.setBirthday(memberId, date2);
            
            event.reply("생일이 지정되었습니다.").queue();
            break;
		}
//		case "play": {
//				discordService.playMusic(event);
//				break;
////			
//		}
//		case "프로필": {
//			
//			}
		}

	}
	
	
	public void initClass(List<String> classes) {
		classes.add("워로드");
		classes.add("버서커");
		classes.add("디스트로이어");
		classes.add("홀리나이트");
		classes.add("슬레이어");
		classes.add("배틀마스터");
		classes.add("인파이터");
		classes.add("기공사");
	    classes.add("창술사");
	    classes.add("스트라이커");
	    classes.add("브레이커");
	    classes.add("데빌헌터");
	    classes.add("블래스터");
	    classes.add("호크아이");
	    classes.add("스카우트");
	    classes.add("건슬링어");
	    classes.add("서머너");
	    classes.add("아르카나");
	    classes.add("바드");
	    classes.add("소서리스");
	    classes.add("데모닉");
	    classes.add("리퍼");
	    classes.add("블레이드");
	    classes.add("소울이터");
	    classes.add("도화가");
	    classes.add("기상술사");
	    
		
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if(event.getComponentId().equals("approveIntegration")) {
			if (!event.getMember().getRoles().stream().anyMatch(role -> role.getName().equals("운영진")
					|| role.getName().equals("부길드장") || role.getName().equals("길드장") || role.getName().equals("길드원"))) {
				event.reply("이 버튼을 사용할 권한이 없습니다.").setEphemeral(true).queue();
				return;
			}
			String nickname = event.getMessage().getEmbeds().get(0).getFields().get(0).getValue();
			String memberId = event.getMessage().getEmbeds().get(0).getFields().get(1).getValue();
			String userClass = event.getMessage().getEmbeds().get(0).getFields().get(2).getValue();
			String name = event.getMessage().getEmbeds().get(0).getFields().get(3).getValue();
			// 비동기 방식으로 멤버를 가져옴
	        event.getGuild().retrieveMemberById(memberId).useCache(false).queue(member -> {
	            List<String> classes = new ArrayList<>();
	            initClass(classes);
	            // 기존 역할 제거
	            for(Role role: member.getRoles()) {
	                if(classes.contains(role.getName())) {
	                    event.getGuild().removeRoleFromMember(member, role).queue();
	                }
	            }
	            
	            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	            // 역할 추가
	            event.getGuild().addRoleToMember(member, event.getGuild().getRolesByName("길드원", true).get(0)).queue();
	            event.getGuild().addRoleToMember(member, event.getGuild().getRolesByName(userClass, true).get(0)).queue();
	            // 닉네임 변경
	            member.modifyNickname(nickname).queue( uccess -> {
                    System.out.println("닉네임 변경 성공");
                }, fail -> {
                	System.out.println("닉네임 변경 실패");
                })	;
	            
	            Runnable retryTask = () -> {
	                // 역할 다시 추가
	                event.getGuild().addRoleToMember(member, event.getGuild().getRolesByName("길드원", true).get(0)).queue();
	                event.getGuild().addRoleToMember(member, event.getGuild().getRolesByName(userClass, true).get(0)).queue();

	                // 닉네임 다시 변경
	                member.modifyNickname(nickname).queue(success -> {
	                    System.out.println("닉네임 변경 재시도 성공");
	                }, fail -> {
	                    System.out.println("닉네임 변경 재시도 실패");
	                });
	            };
	            
	            scheduler.schedule(retryTask, 2, TimeUnit.SECONDS);
	            
	            // 데이터베이스 업데이트
	            discordService.setUser(memberId, name, userClass);
	            // 메시지 업데이트
	            event.getChannel().editMessageEmbedsById(event.getMessageId(), event.getMessage().getEmbeds().get(0))
	                .setActionRow(Button.success("approveIntegration", "승인").asDisabled(), Button.danger("rejectIntegration", "반려").asDisabled())
	                .queue();
	            event.reply("변경 되었습니다.").queue();
	        });
		}else if(event.getComponentId().equals("rejectIntegration")){
			event.getChannel().editMessageEmbedsById(event.getMessageId(), event.getMessage().getEmbeds().get(0))
			.setActionRow(Button.success("approveIntegration", "승인").asDisabled(), Button.danger("rejectIntegration", "반려").asDisabled())
            .queue();
			 event.reply("반려 되었습니다.").queue();
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
//		data.add(Commands.slash("공지채널설정", "신입 채널을 이벤트를 부여 합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("신입채널설정", "신입 채널을 이벤트를 부여 합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("유튜브채널설정", "유튜브 알람 이벤트를 부여합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("봇채널설정", "봇 채널 이벤트를 부여합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("채널설정", "음성 채널 생성 및 이동 이벤트를 부여합니다.")
				.addOption(OptionType.CHANNEL, "channel", "채널이름", true)
				.addOption(OptionType.STRING, "name", "생성될 채널이름", true)
				.addOption(OptionType.INTEGER, "person", "최대 인원", false));
		data.add(Commands.slash("채널확인", "이벤트가 걸려있는 체널 목록을 보여줍니다."));		
		data.add(Commands.slash("채널설정삭제", "해당 채널 이벤트를 삭제합니다")
				.addOption(OptionType.CHANNEL, "channel", "채널이름", true));
		data.add(Commands.slash("연동", "현재 디스코드 닉네임을 로스트아크api와 연동하여 닉네임을 변경합니다.").addOption(OptionType.STRING, "user", "유저", true).addOption(OptionType.USER, "member", "변경할 유저", false));
		data.add(Commands.slash("게스트", "해당 맴버를 게스트 설정을 합니다.").addOption(OptionType.USER, "member", "변경할 유저", true));
		data.add(Commands.slash("play", "노래를 재생 합니다.").addOption(OptionType.STRING, "name", "재생할 노래 이름", true));
		data.add(Commands.slash("생일설정", "내 생일을 설정 합니다.").addOption(OptionType.STRING, "date", "생일 4자리 숫자 ex)0307", true).addOption(OptionType.USER,"member","변경할 유저", false));
		data.add(Commands.slash("생일채널설정", "생일 채널 이벤트를 부여합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));	
		data.add(Commands.slash("채팅채널설정", "채팅 채널 이벤트를 부여합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));	
		data.add(Commands.slash("생일알람채널설정", "생일 알람 채널 이벤트를 부여합니다.").addOption(OptionType.CHANNEL, "channel", "채널이름", true));	
		data.add(Commands.slash("생일삭제", "생일 삭제 설정 합니다.").addOption(OptionType.USER,"member","삭제할 유저", true));
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
			if (role.getId().equals("832801295336341516") || role.getId().equals("1293063948681216060")
					|| role.getId().equals("1296373081148751973") || role.getId().equals("1296396814957678612")
					|| role.getId().equals("1296396872361050152") || role.getId().equals("1296396862764351488")) {
				isRole = true;
				break;
			}
		}
		return isRole;
		
	}
	
}