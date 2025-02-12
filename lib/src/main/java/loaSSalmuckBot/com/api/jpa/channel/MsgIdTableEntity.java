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
@Entity(name= "msgIdTable")
@Table(name = "msg_id_table")
@DynamicUpdate
public class MsgIdTableEntity {

	@Id
	@Column(name = "channel_id") 				private String channelId;	
	@Column(name = "msg_id") 					private String msgId;
}
