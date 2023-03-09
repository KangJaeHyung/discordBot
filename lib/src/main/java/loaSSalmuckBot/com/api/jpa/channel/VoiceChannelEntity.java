package loaSSalmuckBot.com.api.jpa.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@IdClass(VoiceChannelPk.class)
public class VoiceChannelEntity {

	@Id
	@Column(name = "guild_id") 					private String guildId;
	@Id
	@Column(name = "id") 						private String id;
	@Column(name = "name") 						private String name;
	@Enumerated(EnumType.STRING)
	@Column(name = "given")						private Given given;
	@Column(name = "creat_name") 				private String createName;
}
