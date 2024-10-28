package loaSSalmuckBot.com.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import loaSSalmuckBot.com.Listener.dto.Given;
import loaSSalmuckBot.com.LostArkDto.ArmoryProfile;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelEntity;
import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import loaSSalmuckBot.com.api.jpa.user.UserEntity;
import loaSSalmuckBot.com.api.jpa.user.UserRepository;
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

	@Autowired
	private  JDA jda;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VoiceChannelRepository voiceChannelRepository;

	@Value("${lostark.api.profile}")
	private String profileUrl;

	@Value("${lostark.apiKey}")
	private String loaApiKey;



	private static final String oddGuild = "832794285178355714";
	private static final String subGuildMaster = "832801296645488651";
	private static final String guildManager = "832801297865506826";
	private static final String guildMamber = "995938730286264460";
	private static final String birthRole = "1293844791364292608";
	
	
	public void test() {
		VoiceChannelEntity voice =  voiceChannelRepository.findByGiven(Given.BIRTHCHAN2);
		Guild guild = jda.getGuildCache().getElementById(oddGuild);
		List<UserEntity> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "birthDate"));
		List<UserEntity> birthUsers = new ArrayList<>();
		System.out.println(voice);
		TextChannel channel = jda.getGuildById(oddGuild).getTextChannelById(voice.getChannelId());
		
		
		if(channel == null) return;
		
		for(String id :msgIds) {
			channel.deleteMessageById(id).queue();
		}
		msgIds.clear();
		for(UserEntity user : users) {
			if(user.getBirthDate()==null) continue;
            if(user.getBirthDate().getMonth() == new Date().getMonth() && user.getBirthDate().getDate() == new Date().getDate()) {
                birthUsers.add(user);
            }
        }
		System.out.println(birthUsers);
		for (UserEntity birthUser : birthUsers) {
			guild.retrieveMemberById(birthUser.getUserId()).useCache(false).queue(member -> {
				guild.addRoleToMember(member, guild.getRoleById(birthRole)).queue();
				channel.sendMessage("오늘은 " + birthUser.getNickName() + "님의 생일입니다!").queue(t -> msgIds.add(t.getId()));
			});

		}
	}
	

	@Scheduled(cron = "0 5 0 * * *") // 매일 0시 5분 0초에 실행
	public void checkUserInfo() {
		log.info("refresh user info...");
		List<Role> roles = new ArrayList<>();
//		roles.add(jda.getGuildById(oddGuild).getRoleById(subGuildMaster));
//		roles.add();
		Guild guild = jda.getGuildById(oddGuild);
		Task<List<Member>> guildMembers = guild.findMembersWithRoles(jda.getGuildById(oddGuild).getRolesByName("길드원", true).get(0));
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
				UserEntity user = userRepository.findById(member.getId()).orElse(new UserEntity());
				user.setUserId(member.getId());
				user.setNickName(profile.getCharacterName());
				user.setUserClass(profile.getCharacterClassName());
				userRepository.save(user);
				guild.removeRoleFromMember(member,  guild.getRoleById(birthRole)).queue();
				String afterNick = profile.getCharacterName()+"/"+profile.getCharacterClassName()+"/"+(int)Math.floor(Float.parseFloat(profile.getItemMaxLevel().replace(",","")));	
				member.modifyNickname(afterNick).queue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info("member {}" , member.getNickname());
				e.printStackTrace();
			}
		}
		
		//유저 생일자 찾기
		List<UserEntity> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "birthDate"));
		List<UserEntity> birthUsers = new ArrayList<>();
		TextChannel channel = jda.getGuildById(oddGuild).getTextChannelById(voiceChannelRepository.findByGiven(Given.BIRTHCHAN2).getChannelId());
		if(channel == null) return;
		for(String id :msgIds) {
			channel.deleteMessageById(id).queue();
		}
		msgIds.clear();
		for(UserEntity user : users) {
            if(user.getBirthDate().getMonth() == new Date().getMonth() && user.getBirthDate().getDate() == new Date().getDate()) {
                birthUsers.add(user);
            }
        }
		for(UserEntity birthUser : birthUsers) {
			guild.addRoleToMember(guild.getMemberById(birthUser.getUserId()), guild.getRoleById(birthRole)).queue();
			
            channel.sendMessage("오늘은 " + birthUser.getNickName() + "님의 생일입니다!").queue(t -> msgIds.add(t.getId()));
            
		}
            
	}
	
	
	static private List<String> msgIds = new ArrayList<>();

}
