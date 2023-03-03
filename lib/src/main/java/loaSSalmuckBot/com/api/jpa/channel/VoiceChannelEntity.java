package loaSSalmuckBot.com.api.jpa.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name= "voiceChannel")
@Table(name = "voice_channel")
@DynamicUpdate
public class VoiceChannelEntity {

	@Id
	@Column(name = "id") 						private String id;
	@Column(name = "name") 						private String name;
	@Column(name = "given")						private String given;
	@Column(name = "creat_name") 				private String createName;
}
