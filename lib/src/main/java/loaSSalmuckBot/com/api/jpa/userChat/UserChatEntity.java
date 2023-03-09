package loaSSalmuckBot.com.api.jpa.userChat;

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
@Entity(name= "userChat")
@Table(name = "user_chat")
@DynamicUpdate
public class UserChatEntity {

	@Id
	@Column(name = "user_id") 					private String user_id;
	@Column(name = "request_chat") 				private String requestChat;
	@Column(name = "response_chat")				private String responseChat;
}
