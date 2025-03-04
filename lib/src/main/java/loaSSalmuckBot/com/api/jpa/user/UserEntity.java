package loaSSalmuckBot.com.api.jpa.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name= "user")
@Table(name = "user")
@DynamicUpdate
public class UserEntity {

	@Id
	@Column(name = "user_id")
	private String userId;
	@Column(name = "nick_name")
	private String nickName;
	@Column(name = "user_role")
	private String userRole;
	@Column(name = "user_class")
	private String userClass;
	@Temporal(TemporalType.DATE)
	@Column(name = "birth_Date")
	private Date birthDate;
}
