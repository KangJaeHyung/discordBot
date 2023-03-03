package loaSSalmuckBot.com.LoaApi;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;

@Slf4j
@Component
public class YoutubeUtil {
	@Value("${google.apiKey}")
	private String apiKey;

	private YouTube youtube;
	
	private ScheduledExecutorService scheduler;
	
	@Value("${google.channelIds}")
	private List<String> channelIds;
	
	
	@Autowired
	private JDA jda;

	@PostConstruct
	public void init() {
		youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequest -> {
		}).setApplicationName("YouTubeNotifier").build();
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this::checkForNewVideos, 0, 5, TimeUnit.MINUTES);
	}
	public void checkForNewVideos() {
		log.info("new videos check...");
		try {
			for(String channelId :channelIds) {
				YouTube.Search.List search = youtube.search().list("id,snippet");
				search.setKey(apiKey);
				search.setChannelId(channelId);
				search.setType("video");
				search.setOrder("date");
				search.setMaxResults(10L);

				SearchListResponse response = search.execute();
				List<SearchResult> items = response.getItems();

				if (items.isEmpty()) {
					System.out.println("No new videos found.");
					return;
				}

			
				for(SearchResult result : items) {
					String videoId = result.getId().getVideoId();
					String title = result.getSnippet().getTitle();
					Date uploadAt = new Date(result.getSnippet().getPublishedAt().getValue());
					Date now = new Date();
//					System.out.println(now.getTime()-300000+" "+uploadAt.getTime()+" "+ now.getTime());
					if(uploadAt.getTime()<=now.getTime()&&uploadAt.getTime()>=now.getTime()-300) {
						String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
						String msg =  "**"+ result.getSnippet().getChannelTitle()+"** 에 새로운 영상이 올라왔습니다." +System.getProperty("line.separator");
						msg =msg + title+System.getProperty("line.separator");
						msg =msg + videoUrl;
						jda.getGuildById("363657553269489664").getTextChannelById("363657553269489668").sendMessage(msg).queue();
					}
				}
			}
		} catch (Exception e) {
			
		}
	
	}

}
