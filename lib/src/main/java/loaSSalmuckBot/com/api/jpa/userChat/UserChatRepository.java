package loaSSalmuckBot.com.api.jpa.userChat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChatEntity, Long>{
	List<UserChatEntity> findTop10ByUserIdOrderByIdDesc(String userId);	
}
