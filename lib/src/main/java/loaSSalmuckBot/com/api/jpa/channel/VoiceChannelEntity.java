package loaSSalmuckBot.com.api.jpa.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import loaSSalmuckBot.com.Listener.dto.Given;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name= "voiceChannel")
@Table(name = "voice_channel")
@DynamicUpdate
public class VoiceChannelEntity {

	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "id") 						private Long id;
	
	@Column(name = "guild_id") 					private String guildId;
	
	@Column(name = "channel_id") 				private String channelId;
	@Column(name = "name") 						private String name;
	@Enumerated(EnumType.STRING)
	@Column(name = "given")						private Given given;
	@Column(name = "creat_name") 				private String createName;
	@Column(name = "person")					private Integer person;
}
