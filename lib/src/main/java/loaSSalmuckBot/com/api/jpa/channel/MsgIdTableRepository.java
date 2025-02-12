package loaSSalmuckBot.com.api.jpa.channel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import loaSSalmuckBot.com.Listener.dto.Given;

public interface MsgIdTableRepository extends JpaRepository<MsgIdTableEntity, Long>{
	public MsgIdTableEntity findByChannelId(String channelId);
}
