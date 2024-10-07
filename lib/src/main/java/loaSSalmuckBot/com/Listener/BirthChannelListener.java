package loaSSalmuckBot.com.Listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
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

	@Override
	public void onReady(ReadyEvent event) {
		VoiceChannelEntity entity = voiceChannelRepository.findByGiven(Given.BIRTHCHAN);
		if (entity != null) {
			TextChannel channel = event.getJDA().getTextChannelById(entity.getChannelId());
			// 먼저 기존의 메시지를 삭제
			if (channel != null) {
				// 1. 최근 14일 이내 메시지를 bulkDelete로 삭제
				channel.getIterableHistory().takeAsync(100).thenAccept(messages -> {
					if (!messages.isEmpty()) {
						channel.purgeMessages(messages); // 일괄 삭제
					}
					// 2. 이후 새로운 생일 메시지를 보냄
					 MessageCreateData message = new MessageCreateBuilder()
						        .setContent("생일을 설정하려면 아래 버튼을 눌러주세요.")
						        .addActionRow(
						            Button.primary("set_birthday", "생일 설정하기")  // '생일 설정하기' 버튼 생성
						        ).build();
						    
						    channel.sendMessage(message).queue();
				});
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
	    	String year = event.getValue("year").getAsString();
	        String month = event.getValue("month").getAsString();
	        String day = event.getValue("day").getAsString();
	        
	        // 사용자가 입력한 생일 정보를 저장하거나 처리
	        event.reply("생일이 성공적으로 설정되었습니다: "+ year+"년 "+ month + "월 " + day + "일").setEphemeral(true).queue();
	    }
	}

}
