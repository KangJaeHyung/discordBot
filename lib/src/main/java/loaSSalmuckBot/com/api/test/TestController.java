package loaSSalmuckBot.com.api.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

	@Autowired
	private VoiceChannelRepository repository;

	@GetMapping("/{id}")
	public VoiceChannelEntity getVoiceChannel(@RequestParam("id") Long id) {
		return repository.findById(id).get();
	}
	
	@GetMapping()
	public List<VoiceChannelEntity> getVoiceChannel() {
		return repository.findAll();
	}

}
