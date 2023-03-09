package loaSSalmuckBot.com.util;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@Slf4j
@Component
@EnableScheduling
public class YoutubeUtil {
	private final YouTube youtube;
	private final List<String> channelIds;
	private final VoiceService voiceService;
	private final JDA jda;
	private final String apiKey;
	
	private ScheduledFuture<?> scheduledFuture;


	@Autowired
	public YoutubeUtil(@Value("${google.apiKey}") String apiKey, @Value("${google.channelIds}") List<String> channelIds,
			VoiceService voiceService, JDA jda) {
		youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequest -> {
		}).setApplicationName("YouTubeNotifier").build();
		this.channelIds = channelIds;
		this.voiceService = voiceService;
		this.jda = jda;
		this.apiKey=apiKey;
	
	}
	
	@Scheduled(fixedRate = 300000) // 5분(300000ms)마다 실행
	public void checkForNewVideos() {
		log.info("new videos check...");
		VoiceChannelEntity entity = voiceService.getChannelByGiven(Given.YUTUBECHAN);
		if(entity==null) return;
		log.info("channel {}",entity.getId());
		TextChannel channel = jda.getGuildById(entity.getGuildId()).getTextChannelById(entity.getId());
		try {
			for (String channelId : channelIds) {
				YouTube.Search.List search = youtube.search().list("id,snippet");
				search.setKey(apiKey);
				search.setChannelId(channelId);
				search.setType("video");
				search.setOrder("date");
				search.setMaxResults(10L);

				SearchListResponse response = search.execute();
				List<SearchResult> items = response.getItems();

				if (items.isEmpty()) {
					log.info("No new videos found.");
					return;
				}

				for (SearchResult result : items) {
					String videoId = result.getId().getVideoId();
					String title = result.getSnippet().getTitle();
					Date uploadAt = new Date(result.getSnippet().getPublishedAt().getValue());
					Date now = new Date();
//					System.out.println(now.getTime()-300000+" "+uploadAt.getTime()+" "+ now.getTime());
					if (uploadAt.getTime() <= now.getTime() && uploadAt.getTime() >= now.getTime() - 300) {
						String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
						String msg = "**" + result.getSnippet().getChannelTitle() + "** 에 새로운 영상이 올라왔습니다."
								+ System.getProperty("line.separator");
						msg = msg + title + System.getProperty("line.separator");
						msg = msg + videoUrl;
						channel.sendMessage(msg).queue();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			scheduledFuture.cancel(false);
		}

	}

}
