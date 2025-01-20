package loaSSalmuckBot.com.api.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import loaSSalmuckBot.com.util.ScheduleUtil;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
	@Autowired
	private ScheduleUtil scheduleUtil;

	@GetMapping()
	public String init() {
		scheduleUtil.test();
		return "success";
	}

	@GetMapping("/{id}")
	public String auth(@PathVariable("id") Integer id) {
		scheduleUtil.setAuth(id);
		return "success";
	}


}
