package loaSSalmuckBot.com.Listener;

import java.awt.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.api.jpa.channel.MsgIdTableEntity;
import loaSSalmuckBot.com.api.jpa.channel.MsgIdTableRepository;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Component
public class ChatChannelListener extends ListenerAdapter {


    @Autowired
    private VoiceChannelRepository voiceChannelRepository;

    @Autowired
    private MsgIdTableRepository msgIdTableRepository;

    public static String msgId = null;


   

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("create_channel")) {
            TextInput nameInput = TextInput.create("channel_name", "이름", TextInputStyle.SHORT)
                    .setPlaceholder("예) 자유-수다방")
                    .setMinLength(2)
                    .setMaxLength(30)
                    .setRequired(true)
                    .build();

            TextInput userLimitInput = TextInput.create("user_limit", "인원 제한", TextInputStyle.SHORT)
                    .setPlaceholder("예) 8 , 0적을시 무제한")
                    .setMinLength(1)
                    .setMaxLength(3)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("create_voice_channel", "공간 이름을 입력해주세요.")
                    .addActionRow(nameInput)
                    .addActionRow(userLimitInput)
                    .build();

            event.replyModal(modal).queue();
            return;
        }

		if (event.getComponentId().equals("show_channel")) {
			VoiceChannelEntity entity = voiceChannelRepository.findByGiven(Given.CHATCHAN);
			TextChannel channel = event.getGuild().getTextChannelById(entity.getChannelId());
			if (channel != null) {
                if (VoiceChannelListener.newChannels.isEmpty()) {
                    event.reply("현재 생성된 채널이 없습니다.").setEphemeral(true).queue();
                    return;
                }
                // SelectMenu 생성
                StringSelectMenu.Builder menuBuilder = StringSelectMenu.create("voice_channels")
                    .setPlaceholder("수정하고 싶은 채널을 선택하세요")
                    .setMaxValues(1);

                // 채널 목록 추가
                for (String channelId : VoiceChannelListener.newChannels) {
                    VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(channelId);
                    if (voiceChannel != null) {
                        String description = String.format("현재 %d명 / %s", 
                            voiceChannel.getMembers().size(),
                            voiceChannel.getUserLimit() == 0 ? "무제한" : voiceChannel.getUserLimit() + "명");

                        menuBuilder.addOption(
                            voiceChannel.getName(),  // 채널 이름
                            channelId,               // 채널 ID
                            description             // 설명 (인원 정보)
                        );
                    }
                }

                MessageCreateData message = new MessageCreateBuilder()
                    .setContent("🎙️ 생성된 음성 채널 목록")
                    .addActionRow(menuBuilder.build())
                    .build();

                event.reply(message).setEphemeral(true).queue();
			}
		}
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("voice_channels")) {
            String selectedChannelId = event.getValues().get(0);
            VoiceChannel selectedChannel = event.getGuild().getVoiceChannelById(selectedChannelId);
            
            if (selectedChannel != null) {
                // 채널 수정을 위한 모달 생성
                TextInput nameInput = TextInput.create("edit_channel_name", "새로운 이름", TextInputStyle.SHORT)
                        .setPlaceholder("예) 자유-수다방")
                        .setMinLength(2)
                        .setMaxLength(30)
                        .setValue(selectedChannel.getName())  // 현재 채널명을 기본값으로 설정
                        .setRequired(true)
                        .build();

                TextInput userLimitInput = TextInput.create("edit_user_limit", "인원 제한", TextInputStyle.SHORT)
                        .setPlaceholder("예) 8 , 0적을시 무제한")
                        .setMinLength(1)
                        .setMaxLength(3)
                        .setValue(String.valueOf(selectedChannel.getUserLimit()))  // 현재 인원제한을 기본값으로 설정
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("edit_voice_channel_" + selectedChannelId, "채널 설정 수정")
                        .addActionRow(nameInput)
                        .addActionRow(userLimitInput)
                        .build();

                event.replyModal(modal).queue();
            }
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith("edit_voice_channel_")) {
            String channelId = event.getModalId().replace("edit_voice_channel_", "");
            VoiceChannel channel = event.getGuild().getVoiceChannelById(channelId);
            
            if (channel != null) {
                String newName = event.getValue("edit_channel_name").getAsString();
                int newUserLimit = Integer.parseInt(event.getValue("edit_user_limit").getAsString());
                
                channel.getManager()
                    .setName(newName)
                    .setUserLimit(newUserLimit)
                    .queue(success -> event.reply("채널 설정이 수정되었습니다.").setEphemeral(true).queue());
            }
        } else if (event.getModalId().equals("create_voice_channel")) {
            ModalMapping nameInput = event.getValue("channel_name");
			ModalMapping userLimitInput = event.getValue("user_limit");
            if (nameInput != null) {
                String channelName = nameInput.getAsString();
				int userLimit = Integer.parseInt(userLimitInput.getAsString());
                // TODO: 여기에 실제 채널 생성 로직 구현
				Category category = 	event.getGuild().getCategoryById("943364697560850482");
				VoiceChannel newChannel = null;
				if(userLimit == 0){
					newChannel = category.createVoiceChannel(channelName).complete();
				}else{
					newChannel = category.createVoiceChannel(channelName).setUserlimit(userLimit).complete();
				}
				VoiceChannelListener.newChannels.add(newChannel.getId());
				event.getGuild().moveVoiceMember(event.getMember(), newChannel).queue();
                event.reply("'" + channelName + "' 공간이 생성되었습니다!").setEphemeral(true).queue();
            }
        }
    }

}
