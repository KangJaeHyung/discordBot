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

    @Autowired
    private JDA jda;

    public static String msgId = null;


    @Scheduled(fixedDelay = 300000 )//5분마다
	public void resetMsg() {
		VoiceChannelEntity entity = voiceChannelRepository.findByGiven(Given.CHATCHAN);
		TextChannel channel = jda.getGuildById(entity.getGuildId()).getTextChannelById(entity.getChannelId());
		if (channel != null) {
			MsgIdTableEntity msgIdTableEntity = msgIdTableRepository.findById(entity.getChannelId()).orElse(null);
			if (msgIdTableEntity != null) {
				channel.deleteMessageById(msgIdTableEntity.getMsgId()).queue();
			} else if (msgId != null) {
				channel.deleteMessageById(msgId).queue();
			}

            // 임베드 생성
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("🔊 음성채널 이용 안내");
            embed.setDescription("음성채널을 편리하게 이용하기 위한 안내입니다.");
            
            // 규칙 섹션
            embed.addField("📌사용 규칙", 
                "🔸 레이드, 종합 게임, 수다 등 다양한 용도로 사용할 수 있습니다.\n" +
                "🔸 채널 생성 시, 주제에 맞는 적절한 이름을 입력해주세요.\n" +
                "🔸 개설한 채널의 목적에 맞게 활용해주세요.\n" +
                "🔸 필요할 경우 채널 이름 변경 버튼을 눌러 수정할 수 있습니다.", false);

            // 사용 방법 섹션
            embed.addField("🚀 사용 방법", 
                "1⃣ 토크 채널에 입장하세요.\n" +
                "2⃣ 채널 생성 버튼을 눌러 새로운 음성 채널을 만드세요.\n" +
                "3⃣ 원하는 주제에 맞게 채널명을 입력하세요.\n" +
                "4⃣ 채널 상태 설정에서 채널의 상태를 변경할 수 있습니다.", false);

            // 추가 안내 섹션
            embed.addField("💡 추가 안내", 
                "생성된 채널은 게스트도 자유롭게 이용할 수 있습니다", false);

            embed.setColor(new Color(114, 137, 218)); // 디스코드 블루 컬러
            embed.setFooter("✅ 원활한 이용을 위해 규칙을 지켜주세요! 😊", null);

			MessageCreateData message = new MessageCreateBuilder()
                    .setEmbeds(embed.build())
					.addActionRow(
                        Button.success("create_channel", "공간 생성"),
                        Button.secondary("show_channel", "생성된 채널 보기")
                    )
					.build();

			channel.sendMessage(message).queue(t ->{
				msgId = t.getId();
				MsgIdTableEntity msgIdTableEntity2 = new MsgIdTableEntity();
				msgIdTableEntity2.setChannelId(entity.getChannelId());
				msgIdTableEntity2.setMsgId(t.getId());
				msgIdTableRepository.save(msgIdTableEntity2);
			} );
			
		} else {
			System.out.println("채널이 없습니다");
		}
	}

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("create_channel")) {
            // 텍스트 입력 필드 생성
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

            // 모달 생성
            Modal modal = Modal.create("create_voice_channel", "공간 이름을 입력해주세요.")
                    .addActionRow(nameInput, userLimitInput)
                    .build();

            // 모달 표시
            event.replyModal(modal).queue();
			return;

        }

		if (event.getComponentId().equals("show_channel")) {
			VoiceChannelEntity entity = voiceChannelRepository.findByGiven(Given.CHATCHAN);
			TextChannel channel = jda.getGuildById(entity.getGuildId()).getTextChannelById(entity.getChannelId());
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
                    VoiceChannel voiceChannel = jda.getVoiceChannelById(channelId);
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
            VoiceChannel selectedChannel = jda.getVoiceChannelById(selectedChannelId);
            
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
            VoiceChannel channel = jda.getVoiceChannelById(channelId);
            
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
				Category category = jda.getGuildById(event.getGuild().getId()).getCategoryById("943364697560850482");
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
