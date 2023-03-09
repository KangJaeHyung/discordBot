package loaSSalmuckBot.com.api.jpa.channel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import loaSSalmuckBot.com.Listener.dto.Given;

public interface VoiceChannelRepository extends JpaRepository<VoiceChannelEntity, VoiceChannelPk>{
	public VoiceChannelEntity findByGiven(Given given);
	public Integer countByGiven(Given given);
	public Optional<VoiceChannelEntity> findByIdAndGiven(String id, Given given);
}
