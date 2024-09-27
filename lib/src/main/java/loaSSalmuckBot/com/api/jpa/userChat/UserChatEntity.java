package loaSSalmuckBot.com.api.jpa.userChat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name= "userChat")
@Table(name = "user_chat")
@DynamicUpdate
public class UserChatEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name ="id")							private Long id;
	@Column(name = "user_id") 					private String userId;
	@Column(name = "request_chat") 				private String requestChat;
	@Column(name = "response_chat")				private String responseChat;
}
 