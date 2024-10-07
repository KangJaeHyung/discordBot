package loaSSalmuckBot.com.Listener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.DiscordService;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
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
						.addActionRow(Button.primary("set_birthday", "생일 설정하기") // '생일 설정하기' 버튼 생성
						).build();

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
			TextInput yearInput = TextInput.create("year", "월 (YY)", TextInputStyle.SHORT)
	                .setPlaceholder("예: 95 비 공개일 경우 00")
	                .setRequired(true)
	                .build();
			 TextInput monthInput = TextInput.create("month", "월 (MM)", TextInputStyle.SHORT)
		                .setPlaceholder("예: 12")
		                .setRequired(true)
		                .build();

		     TextInput dayInput = TextInput.create("day", "일 (DD)", TextInputStyle.SHORT)
		                .setPlaceholder("예: 25")
		                .setRequired(true)
		                .build();

		      Modal modal = Modal.create("birthday_modal", "생일을 설정해주세요 😊")
		                    .addComponents(ActionRow.of(yearInput),ActionRow.of(monthInput), ActionRow.of(dayInput))
		                    .build();
	        // 모달 창을 띄움
	        event.replyModal(modal).queue();
		}
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
	    if (event.getModalId().equals("birthday_modal")) {
	    	try {
	    		String yearS = event.getValue("year").getAsString();
		        String monthS = event.getValue("month").getAsString();
		        String dayS = event.getValue("day").getAsString();
		        
		        Integer year = Integer.parseInt(yearS);
		        Integer month = Integer.parseInt(monthS);
		        Integer day = Integer.parseInt(dayS);
		        if(year>=100) throw new Exception("년도 에러");
		        if(month>=13||month==0 ) throw new Exception("월 에러");
		        if(day>=32||day==0) throw new Exception("일 에러");
		        
		     // 연도 처리를 위한 로직
		        if (year >= 0 && year <= 24) {
		            year += 2000;
		        } else {
		            year += 1900;
		        }

		        try {
		            // LocalDate로 변환하여 반환
		        	Date d = java.sql.Date.valueOf(LocalDate.of(year, month, day));
		        	
		        	discordService.setBirthday(event.getMember().getId(), d);
		        } catch (DateTimeParseException e) {
		            throw new IllegalArgumentException("Invalid date values.", e);
		        }
		        
		        event.reply("생일이 성공적으로 설정되었습니다: "+ year+"년 "+ month + "월 " + day + "일").setEphemeral(true).queue();
		        
		        
		    } catch (Exception e) {
		    	event.reply("생일이 정상적이지 않습니다 다시 시도해 주십시오.").setEphemeral(true).queue();
			}
	    	
	        
	        
	        // 사용자가 입력한 생일 정보를 저장하거나 처리
	        
	    }
	}

}
