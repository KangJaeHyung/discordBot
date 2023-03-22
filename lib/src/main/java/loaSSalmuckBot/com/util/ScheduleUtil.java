package loaSSalmuckBot.com.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.Listener.service.VoiceService;
import loaSSalmuckBot.com.LostArkDto.ArmoryProfile;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.concurrent.Task;

@Slf4j
@Component
@EnableScheduling
public class ScheduleUtil {
	ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

	private final YouTube youtube;
	private final List<String> channelIds;
	private final VoiceService voiceService;
	private final JDA jda;
	private final String apiKey;

	private ScheduledFuture<?> scheduledFuture;

	@Value("${lostark.api.profile}")
	private String profileUrl;

	@Value("${lostark.apiKey}")
	private String loaApiKey;

	@Autowired
	public ScheduleUtil(@Value("${google.apiKey}") String apiKey,
			@Value("${google.channelIds}") List<String> channelIds, VoiceService voiceService, JDA jda) {
		youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequest -> {
		}).setApplicationName("YouTubeNotifier").build();
		this.channelIds = channelIds;
		this.voiceService = voiceService;
		this.jda = jda;
		this.apiKey = apiKey;

	}
	
	@Scheduled(fixedRate = 3600000) // 5분(300000ms)마다 실행
	public void checkForNewVideos() {
		log.info("new videos check...");
		VoiceChannelEntity entity = voiceService.getChannelByGiven(Given.YUTUBECHAN);
		if (entity == null)
			return;
		log.info("channel {}", entity.getId());
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

				if (items.size()==0) {
					log.info("No new videos found.");
					return;
				}

				for (SearchResult result : items) {
					String videoId = result.getId().getVideoId();
					String title = result.getSnippet().getTitle();
					Date uploadAt = new Date(result.getSnippet().getPublishedAt().getValue());
					Date now = new Date();
//					System.out.println(now.getTime()-300000+" "+uploadAt.getTime()+" "+ now.getTime());
					if (uploadAt.getTime() <= now.getTime() && uploadAt.getTime() >= now.getTime() - 3600) {
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

	private static final String oddGuild = "832794285178355714";
	private static final String subGuildMaster = "832801296645488651";
	private static final String guildManager = "832801297865506826";
	private static final String guildMamger = "995938730286264460";

	@Scheduled(cron = "0 0 0 * * *") // 5분(300000ms)마다 실행
	public void checkUserInfo() {
		log.info("refresh user info...");
		List<Role> roles = new ArrayList<>();
//		roles.add(jda.getGuildById(oddGuild).getRoleById(subGuildMaster));
//		roles.add();
		Guild guild = jda.getGuildById(oddGuild);
		Task<List<Member>> guildMembers = guild.findMembersWithRoles(jda.getGuildById(oddGuild).getRoleById(guildMamger));
		Task<List<Member>> guildManagers = guild.findMembersWithRoles(jda.getGuildById(oddGuild).getRoleById(guildManager));
		Task<List<Member>> subMaster = guild.findMembersWithRoles(jda.getGuildById(oddGuild).getRoleById(subGuildMaster));
		List<Member> members = new ArrayList<>();
		members.addAll(guildMembers.get());
		members.addAll(guildManagers.get());
		members.addAll(subMaster.get());
		log.info("guild roles : {}", jda.getGuildById(oddGuild).getRoles());
		log.info("member : {}",members);
		for (Member member : members) {
			try {
				String beforeNick = member.getNickname();
				log.info("refresh member : {}", beforeNick);
				beforeNick = beforeNick.split("/")[0];
				// 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
				RestTemplate restTemplate = LoaRestAPI.makeRestTemplate(true);
				// 2. HTTP 요청 본문 생성
				Map<String, Object> requestBody = new HashMap<>();
				// 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
				HttpHeaders header = new HttpHeaders();
				header.add("accept", "application/json");
				header.add("authorization", "bearer " + loaApiKey);
				HttpEntity<String> entity;

				entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), header);
				// 4. 요청 URL을 정의해줍니다.
				String url = "https://developer-lostark.game.onstove.com/armories/characters/" + beforeNick
						+ "/profiles";
//				UriComponents uri = UriComponentsBuilder.fromHttpUrl(new String(url.getBytes(), "UTF-8")).build(false);
				// 5. exchange() 메소드로 api를 호출합니다.
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,
						String.class);
				if(!response.getStatusCode().equals(HttpStatus.OK)) return;
				ArmoryProfile profile = mapper.readValue(response.getBody(), ArmoryProfile.class);
				log.info("response : {}", profile);
				String afterNick = profile.getCharacterName()+"/"+profile.getCharacterClassName()+"/"+(int)Math.floor(Float.parseFloat(profile.getItemMaxLevel().replace(",","")));	
				for(Role role:  member.getRoles()) {
					if(role.getName().equals("운영진")||
							role.getName().equals("부길드장")||
							role.getName().equals("길드원")||
							role.getName().equals("서버 부스터")||
							role.getName().equals("모험섬")||
							role.getName().equals("섬령전")||
							role.getName().equals("필드 보스")||
							role.getName().equals("유령선")||
							role.getName().equals("로웬 습격")
							) continue;
					guild.removeRoleFromMember(member, role).queue();
				}
				member.modifyNickname(afterNick).queue();
				guild.addRoleToMember(member,guild.getRolesByName(profile.getCharacterClassName(), true).get(0)).queue();
				guild.addRoleToMember(member,guild.getRolesByName("길드원", true).get(0)).queue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info("member {}" , member.getNickname());
				e.printStackTrace();
			}
		}
	}
	
	//기본 공격력 = (스탯*무공/6)^(1/2)
}
