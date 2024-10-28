package loaSSalmuckBot.com.api.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import loaSSalmuckBot.com.util.ScheduleUtil;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

	@Autowired
	private VoiceChannelRepository repository;
	
	@Autowired
	private ScheduleUtil scheduleUtil;

	@GetMapping("/{id}")
	public VoiceChannelEntity getVoiceChannel(@PathVariable("id") Long id) {
		return repository.findById(id).get();
	}
	
	
	@GetMapping()
	public String init() {
		scheduleUtil.test();
		return "success";
	}


}
