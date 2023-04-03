package loaSSalmuckBot.com.api.jpa.channel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import loaSSalmuckBot.com.Listener.dto.Given;

public interface VoiceChannelRepository extends JpaRepository<VoiceChannelEntity, Long>{
	public VoiceChannelEntity findByGiven(Given given);
	public Integer countByGiven(Given given);
	public Optional<VoiceChannelEntity> findByChannelIdAndGiven(String id, Given given);
	public Optional<VoiceChannelEntity> findByGuildIdAndChannelId(String guildId, String channelId);
	public Integer deleteByGuildIdAndChannelId(String guildId, String channelId);
}
