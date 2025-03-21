package loaSSalmuckBot.com.Listener;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.DiscordService;
import loaSSalmuckBot.com.api.jpa.channel.MsgIdTableEntity;
import loaSSalmuckBot.com.api.jpa.channel.MsgIdTableRepository;
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
	private UserRepository userRepository;

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
			List<UserEntity> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "birthDate"));
			List<UserEntity> birthUsers = new ArrayList<>();
			for(UserEntity user : users) {
				if(user.getBirthDate() == null) continue;
	            if(user.getBirthDate().getMonth() == new Date().getMonth()) {
	                birthUsers.add(user);
	            }
	        }
			String msg = "# **길드원 생일 확인** 🎂 \r\n";
			int count = 1;
			for(UserEntity birthUser : birthUsers) {
				msg += "🎈**"+count+"."+birthUser.getNickName() + "**-" + (birthUser.getBirthDate().getMonth()+1) +"월 "+ birthUser.getBirthDate().getDate() + "일 \r\n";
				count++;
			}
			event.reply(msg).setEphemeral(true).setEphemeral(true).queue();
	        return;
		}
		if (event.getComponentId().equals("all_birthday")) {
			List<UserEntity> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "birthDate"));
			String msg = "# **길드원 생일 확인** 🎂 \r\n";
			int count = 1;
			for(UserEntity birthUser : users) {
				if(birthUser.getBirthDate() == null) continue;
				msg += "🎈**"+count +"." +birthUser.getNickName() + "**-" + (birthUser.getBirthDate().getMonth()+1) +"월 "+ birthUser.getBirthDate().getDate() + "일 \r\n";
				count++;
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
		        	LocalDate birthDate = LocalDate.of(2000, month, day);
		        	// UTC 기준으로 시간을 설정하여 날짜 변환 문제 방지
		        	Date d = Date.from(birthDate.atStartOfDay(ZoneOffset.UTC).toInstant());
		        	
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
