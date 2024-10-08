package loaSSalmuckBot.com.Listener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.DiscordService;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import loaSSalmuckBot.com.api.jpa.user.UserEntity;
import loaSSalmuckBot.com.api.jpa.user.UserRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Component
public class BirthChannelListener extends ListenerAdapter {

	@Autowired
	private VoiceChannelRepository voiceChannelRepository;

	public static String  msgId = null;
	
	@Autowired
	private DiscordService discordService;
		
	@Autowired
	private  JDA jda;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@PostConstruct
	public void init() {
	    System.out.println("빈 초기화됨");
	}
	
	@PreDestroy
	public void onDestroy() {
		VoiceChannelEntity entity = voiceChannelRepository.findByGiven(Given.BIRTHCHAN);
		TextChannel channel = jda.getGuildById(entity.getGuildId()).getTextChannelById(entity.getChannelId());
		System.out.println("메세지 삭제");
		if (channel != null) {
			if (null != msgId)
				channel.deleteMessageById(msgId);
		} else {
			System.out.println("채널이 없습니다");
		}
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		// 봇이 준비되면 실행되는 이벤트
		System.out.println("봇이 준비되었습니다");
		// 생일 채널에 메시지를 보냄
		VoiceChannelEntity entity = voiceChannelRepository.findByGiven(Given.BIRTHCHAN);
		if (entity != null) {
			TextChannel channel = event.getJDA().getGuildById(entity.getGuildId())
					.getTextChannelById(entity.getChannelId());
			// 먼저 기존의 메시지를 삭제
			if (channel != null) {
				System.out.println("메세지 보내기");
				if(null != msgId) channel.deleteMessageById(msgId);
				MessageCreateData message = new MessageCreateBuilder().setContent("# 생일을 설정하려면 아래 버튼을 눌러주세요.")
						.addActionRow(Button.primary("set_birthday", "생일 설정하기"),Button.secondary("month_birthday", "이번달 생일자 보기"),Button.secondary("all_birthday", "전체 생일 보기"))
						.build();

				channel.sendMessage(message).queue(t -> msgId = t.getId());
			} else {
				System.out.println("채널이 없습니다");
			}
		}
 
	}
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if (event.getComponentId().equals("set_birthday")) {
			 // 모달 창을 생성
			 TextInput monthInput = TextInput.create("month", "월 (MM)", TextInputStyle.SHORT)
		                .setPlaceholder("예: 12")
		                .setRequired(true)
		                .build();

		     TextInput dayInput = TextInput.create("day", "일 (DD)", TextInputStyle.SHORT)
		                .setPlaceholder("예: 25")
		                .setRequired(true)
		                .build();

		      Modal modal = Modal.create("birthday_modal", "생일을 설정해주세요 😊")
		                    .addComponents(ActionRow.of(monthInput), ActionRow.of(dayInput))
		                    .build();
	        // 모달 창을 띄움
	        event.replyModal(modal).queue();
	        return;
		}
		if (event.getComponentId().equals("month_birthday")) {
			List<UserEntity> users = userRepository.findAll();
			List<UserEntity> birthUsers = new ArrayList<>();
			for(UserEntity user : users) {
	            if(user.getBirthDate().getMonth() == new Date().getMonth()) {
	                birthUsers.add(user);
	            }
	        }
			String msg = "# **길드원 생일 확인** 🎂 \r\n";
			for(UserEntity birthUser : birthUsers) {
				msg += "🎈**"+birthUser.getNickName() + "**-" + (birthUser.getBirthDate().getMonth()+1) +"월 "+ birthUser.getBirthDate().getDate() + "일 \r\n";
			}
			event.reply(msg).setEphemeral(true).setEphemeral(true).queue();
	        return;
		}
		if (event.getComponentId().equals("all_birthday")) {
			List<UserEntity> users = userRepository.findAll();
			String msg = "# **길드원 생일 확인** 🎂 \r\n";
			for(UserEntity birthUser : users) {
				msg += "🎈**"+birthUser.getNickName() + "**-" + (birthUser.getBirthDate().getMonth()+1) +"월 "+ birthUser.getBirthDate().getDate() + "일 \r\n";
			}
			event.reply(msg).setEphemeral(true).setEphemeral(true).queue();
	        return;
		}
		
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
	    if (event.getModalId().equals("birthday_modal")) {
	    	try {
		        String monthS = event.getValue("month").getAsString();
		        String dayS = event.getValue("day").getAsString();
		        

		        Integer month = Integer.parseInt(monthS);
		        Integer day = Integer.parseInt(dayS);

		        if(month>=13||month==0 ) throw new Exception("월 에러");
		        if(day>=32||day==0) throw new Exception("일 에러");
		        

		        try {
		            // LocalDate로 변환하여 반환
		        	Date d = java.sql.Date.valueOf(LocalDate.of(2000, month, day));
		        	
		        	discordService.setBirthday(event.getMember().getId(), d);
		        } catch (DateTimeParseException e) {
		            throw new IllegalArgumentException("Invalid date values.", e);
		        }
		        
		        event.reply("생일이 성공적으로 설정되었습니다: "+ month + "월 " + day + "일").setEphemeral(true).queue();
		        
		        
		    } catch (Exception e) {
		    	event.reply("생일이 정상적이지 않습니다 다시 시도해 주십시오.").setEphemeral(true).queue();
			}
	    	
	        
	        
	        // 사용자가 입력한 생일 정보를 저장하거나 처리
	        
	    }
	}

}
