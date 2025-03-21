package loaSSalmuckBot.com.Listener.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public enum Given {
	CRECHAN("보이스 채널 생성 이벤트"),
	YUTUBECHAN("유튜브 채널 이벤트"),
	NOTICHAN("공지 채널 이벤트"),
	WELCOMECHAN("신입환영 채널 생성 이벤트"),
	BOTCHAN("봇 채널 이벤트"),
	BIRTHCHAN("생일 채널 이벤트"),
	BIRTHCHAN2("생일 채널2 이벤트"),
	CHATCHAN("채팅 채널 이벤트")
	;

	private final String value;
	
	Given(String value) {
		this.value=value;
	}
	
}
