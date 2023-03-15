package loaSSalmuckBot.com.util;

import java.awt.Color;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import loaSSalmuckBot.com.LostArkDto.ArmoryProfile;
import loaSSalmuckBot.com.LostArkDto.Elixir;
import loaSSalmuckBot.com.LostArkDto.Elixir.EquipmentEnum;
import loaSSalmuckBot.com.LostArkDto.Elixir.Option;
import loaSSalmuckBot.com.LostArkDto.Engraving;
import loaSSalmuckBot.com.LostArkDto.Equipment;
import loaSSalmuckBot.com.api.jpa.userChat.UserChatEntity;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Component
@Slf4j
public class LoaRestAPI {
	ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	
	
//	@Autowired
//	private  JDA jda;
//	
	@Value("${lostark.api.profile}") 
	private  String profileUrl;
	
	@Value("${lostark.apiKey}") 
	private  String apiKey;
	
	
	
	@Async
	public CompletableFuture<ArmoryProfile> getLoaUser (String userName) throws Exception {
		// 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
		RestTemplateBuilder builder = new RestTemplateBuilder();
	    RestTemplate restTemplate = builder
	            .setConnectTimeout(Duration.ofSeconds(30))
	            .setReadTimeout(Duration.ofSeconds(30))
	            .build();

	    // 2. HTTP 요청 본문 생성
	    Map<String, Object> requestBody = new HashMap<>();

        // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
        HttpHeaders header = new HttpHeaders();
        header.add("accept", "application/json");
        header.add("authorization", "bearer "+apiKey);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), header);

        // 4. 요청 URL을 정의해줍니다.
        String url = "https://developer-lostark.game.onstove.com/armories/characters/"+userName+"/profiles";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(new String(url.getBytes(),"UTF-8")).build(false);

        // 5. exchange() 메소드로 api를 호출합니다.
        
        try {
        	ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        	if(!response.getStatusCode().equals(HttpStatus.OK))  	return  CompletableFuture.completedFuture(new ArmoryProfile(true));
        	ArmoryProfile profile = mapper.readValue(response.getBody(), ArmoryProfile.class);
	    	log.info("response : {}",profile);
	    	return  CompletableFuture.completedFuture(profile);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(null);
		}
	}
	
	
	@Async
	public CompletableFuture<MessageEmbed> getElixir(String userName) throws Exception {
		// 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
		RestTemplateBuilder builder = new RestTemplateBuilder();
	    RestTemplate restTemplate = builder
	            .setConnectTimeout(Duration.ofSeconds(30))
	            .setReadTimeout(Duration.ofSeconds(30))
	            .build();

	    // 2. HTTP 요청 본문 생성
	    Map<String, Object> requestBody = new HashMap<>();

        // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
        HttpHeaders header = new HttpHeaders();
        header.add("accept", "application/json");
        header.add("authorization", "bearer "+apiKey);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), header);

        // 4. 요청 URL을 정의해줍니다.
        String equipmentUrl = "https://developer-lostark.game.onstove.com/armories/characters/"+userName+"/equipment";
        String profilesUrl = "https://developer-lostark.game.onstove.com/armories/characters/"+userName+"/profiles";
        String engravingsUrl = "https://developer-lostark.game.onstove.com/armories/characters/"+userName+"/engravings";
        UriComponents equipmentUri = UriComponentsBuilder.fromHttpUrl(new String(equipmentUrl.getBytes(),"UTF-8")).build(false);
        UriComponents profilesUri = UriComponentsBuilder.fromHttpUrl(new String(profilesUrl.getBytes(),"UTF-8")).build(false);
        UriComponents engravingsUri = UriComponentsBuilder.fromHttpUrl(new String(engravingsUrl.getBytes(),"UTF-8")).build(false);

        // 5. exchange() 메소드로 api를 호출합니다.
        
        try {
        	ResponseEntity<String>  equipmentResponse = restTemplate.exchange(equipmentUri.toString(), HttpMethod.GET, entity, String.class);
        	ResponseEntity<String>  profilesResponse = restTemplate.exchange(profilesUri.toString(), HttpMethod.GET, entity, String.class);
        	ResponseEntity<String>  engravingsResponse = restTemplate.exchange(engravingsUri.toString(), HttpMethod.GET, entity, String.class);
        	if(!equipmentResponse.getStatusCode().equals(HttpStatus.OK))  	{
        		EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(":scales: **점검중입니다!!**").setAuthor(userName).setColor(new Color(157, 216, 75));
        		return  CompletableFuture.completedFuture(embedBuilder.build());
        	}
        	
        	List<Equipment> equi = mapper.readValue(equipmentResponse.getBody(),  new TypeReference<List<Equipment>>() {});
        	ArmoryProfile profile = mapper.readValue(profilesResponse.getBody(), ArmoryProfile.class);
        	HashMap<String,Object> engravings = mapper.readValue(engravingsResponse.getBody(), HashMap.class);
        	List<Elixir> elixirs = formatEquipment(equi);
        	MessageEmbed embed = buildElixirEmbed(elixirs,userName,profile,engravings,equi);
	    	return  CompletableFuture.completedFuture(embed);
		} catch (Exception e) {
			e.printStackTrace();
			return CompletableFuture.completedFuture(null);
		}
	}
	
	

	
	
	
	
	private void topSetting(Integer topLevel,String setTop,Option option) {
		switch (option.getName()) {
			case "강맹 (질서)" : {
				topLevel+=topLevel;
				setTop="강맹";
				break;
			}
			case "달인 (질서)" : {
				topLevel+=topLevel;		
				setTop="달인";
				break;
			}
			case "선봉대 (질서)" : {
				topLevel+=topLevel;
				setTop="선봉대";
				break;
			}
			case "칼날 방패 (질서)" : {
				topLevel+=topLevel;
				setTop="칼날 방패";
				break;
			}
			case "행운 (질서)" : {
				topLevel+=topLevel;
				setTop="행운";
				break;
			}
			case "회심 (질서)" : {
				topLevel+=topLevel;
				setTop="회심";
				break;
			}
		}
	}
	
	private void gloveSetting(Integer gloveLevel,String setglove,Option option) {
		switch (option.getName()) {
			case "강맹 (혼돈)" : {
				gloveLevel+=gloveLevel;
				setglove="강맹";
				break;
			}
			case "달인 (혼돈)" : {
				gloveLevel+=gloveLevel;		
				setglove="달인";
				break;
			}
			case "선봉대 (혼돈)" : {
				gloveLevel+=gloveLevel;
				setglove="선봉대";
				break;
			}
			case "칼날 방패 (혼돈)" : {
				gloveLevel+=gloveLevel;
				setglove="칼날 방패";
				break;
			}
			case "행운 (혼돈)" : {
				gloveLevel+=gloveLevel;
				setglove="행운";
				break;
			}
			case "회심 (혼돈)" : {
				gloveLevel+=gloveLevel;
				setglove="회심";
				break;
			}
		}
	}
	
	final static Integer attack[] = {122,253,383,575,767};
	final static Integer status[] = {864,1782,2700,4050,5400};
	final static Integer weapon[] = {236,488,740,1110,1480};
	final static Float topDamge[] = {0.23f,0.47f,0.72f,1.08f,1.44f};
	final static Float bossDamage[] = {0.38f,0.79f,1.20f,1.80f,2.40f};
	final static Float addDamage[] = {0.49f,1.02f,1.55f,2.32f,3.10f};
	final static Float criDamage[] = {1.12f,2.31f,3.50f,5.25f,7.00f};
	final static Float gloveDamage[] = {0.23f,0.47f,0.72f,1.08f,1.44f};
	final static Integer weaponDamgeList[] = {37831,38673,39534,40413,41313,42232,43172,44132,45114,46118,47145,48194,49266,50362,52051,53796,55599,57463,59390,61381,63439,65566,67764,70036,72384,74811,28789,34959,43185,47298,54494,75185,78084,81095};
 	
	private void damgeSetting(
			ArmoryProfile profile,
			HashMap<String, Object> engravings,
			List<Equipment> equi, Integer attackPoint,
			Integer weaponAttackPoint,
			Integer statusPoint,
			Integer topLevel,
			Integer bossLevel,
			Integer addLevel,
			Integer criLevel,
			Integer gloveLevel,
			Integer allLevel,
			String setglove,
			String setTop) {
		
		Float engravingsAttack = this.engravingSetting(engravings)/100;
		//기본 공격력 = (스탯*무공/6)^(1/2)
		Integer nowCharaterAttackPoint = Integer.parseInt(profile.getStats().get(7).get("value").toString());
		
		Integer weaponLevel = 0;
		if(equi.get(0).getGrade().equals("고대")) {
			weaponLevel = Integer.parseInt(equi.get(0).getName().split(" ")[0].replace("+",""));
		}else if(equi.get(0).getGrade().equals("에스더")) {
			weaponLevel = 25+Integer.parseInt(equi.get(0).getName().split(" ")[0].replace("+",""));
		}
		Integer weaponeDamge= weaponDamgeList[weaponLevel-1];
		Integer nowCharaterStat = ((int)(nowCharaterAttackPoint/engravingsAttack)^2)*6/(weaponeDamge+weaponAttackPoint);
		Integer noElixirAttackPoint  = (((nowCharaterStat-statusPoint)*weaponeDamge)^(1/2))/6;
		Integer noElixirtTotaldamege = (int) (noElixirAttackPoint*1.6*1.5*engravingsAttack);
//		Integer nowCharaterAttackPoint = noElixirAttackPoint*1.6*1.5*engravingsAttack;
		
		
		
	}
	
	
	private Float engravingSetting(HashMap<String, Object> engravings) {
		Float hap = 100f;
		for(HashMap<String,Object> map :((List<HashMap<String,Object>>) engravings.get("Effects"))){
			Pattern pattern = Pattern.compile("(.*?)\\s*Lv\\.\\s*([0-9]+)");
	        Matcher matcher = pattern.matcher(map.get("name").toString());

	        String text = "";
	        int level = 0;

	        if (matcher.find()) {
	            text = matcher.group(1).trim();
	            level = Integer.parseInt(matcher.group(2));
	            
	        }
	        hap =+Engraving.getEngravingByName(text).getLevel()[level-1];
		}
		 		
		
		return hap;
	}


	private MessageEmbed buildElixirEmbed(List<Elixir> elixirs,String name, ArmoryProfile profile, HashMap<String, Object> engravings, List<Equipment> equi) {
		EmbedBuilder builder = new EmbedBuilder().setTitle(":scales: **엘릭서 정보**").setAuthor(name).setColor(new Color(157, 216, 75));
		Integer attackPoint = 0;
		Integer weaponAttackPoint = 0;
		Integer statusPoint = 0;
		Integer topLevel =0;
		Integer bossLevel =0;
		Integer addLevel =0;
		Integer criLevel =0;
		Integer gloveLevel =0;
		Integer allLevel = 0;
		
		String setglove = "";
		String setTop = "";
		for(Elixir elixir:elixirs ) {
			String text= "";
			if(elixir.getFirst()!=null) {
				Integer	level1 = elixir.getFirst().getLevel();
				String	name1 = elixir.getFirst().getName();
				attackPoint =+ (name1.equals("공격력")?LoaRestAPI.attack[level1-1]:0);
				weaponAttackPoint =+(name1.equals("무기 공격력")?LoaRestAPI.weapon[level1-1]:0);
				statusPoint =+(name1.equals("지능")||name1.equals("민첩")||name1.equals("힘")?LoaRestAPI.weapon[level1-1]:0);
				weaponAttackPoint =+(name1.equals("무기 공격력")?LoaRestAPI.weapon[level1-1]:0);
				bossLevel =+(name1.equals("보스 피해")?level1:0);
				addLevel =+(name1.equals("추가 피해")?level1:0);
				criLevel =+(name1.equals("치명타 피해")?level1:0);
				this.topSetting(topLevel, setTop, elixir.getFirst());
				this.gloveSetting(gloveLevel, setglove, elixir.getFirst());
				
				text= text+name1+level1+"lv";
				
				
				allLevel += level1;
			}
			if(elixir.getSecond()!=null) {
				Integer	level2 = elixir.getSecond().getLevel();
				String	name2 = elixir.getSecond().getName();
				attackPoint =+ (name2.equals("공격력")?LoaRestAPI.attack[level2-1]:0);
				weaponAttackPoint =+(name2.equals("무기 공격력")?LoaRestAPI.weapon[level2-1]:0);
				statusPoint =+(name2.equals("지능")||name2.equals("민첩")||name2.equals("힘")?LoaRestAPI.weapon[level2-1]:0);
				bossLevel =+(name2.equals("보스 피해")?level2:0);
				addLevel =+(name2.equals("추가 피해")?level2:0);
				criLevel =+(name2.equals("치명타 피해")?level2:0);
				this.topSetting(topLevel, setTop, elixir.getFirst());
				this.gloveSetting(gloveLevel, setglove, elixir.getFirst());
				
				text =text+System.getProperty("line.separator")+name2+level2+"lv";
				
				
				allLevel += level2;
			}
			builder.addField(elixir.getEquipment().getValue(),text,true);
		}
		builder.addField("**총 레벨 합**",allLevel+"lv",false);
		
		
		damgeSetting(profile, engravings, equi,attackPoint, weaponAttackPoint, statusPoint, topLevel, bossLevel, addLevel, criLevel, gloveLevel, allLevel, setglove, setTop);
		
		return builder.build();
	}
	
	
	

	
	private List<Elixir> formatEquipment(List<Equipment> profile)  throws Exception {
		List<Elixir> elixirs = new ArrayList<>();
		for(int i = 1 ; i <6 ;i++) {
			Elixir dto = new Elixir();
			Equipment equi = profile.get(i);
			switch (equi.getType()) {
				case "투구": {
					dto.setEquipment(EquipmentEnum.head);
					break;
				}
				case "상의": {
					dto.setEquipment(EquipmentEnum.top);
					break;
				}
				case "하의": {
					dto.setEquipment(EquipmentEnum.pants);
					break;
				}
				case "장갑": {
					dto.setEquipment(EquipmentEnum.gloves);
					break;
				}
				case "어깨": {
					dto.setEquipment(EquipmentEnum.shoulder);
					break;
				}
				default : {
					dto.setEquipment(EquipmentEnum.non);
					elixirs.add(dto);
					continue;
				}
			}
			HashMap<String,Object> equitooltip = mapper.readValue(equi.getTooltip(), HashMap.class);
			System.out.println(equitooltip);
			HashMap<String,Object> element_008 =(HashMap<String,Object>) equitooltip.get("Element_008");
			if(!element_008.get("type").equals("IndentStringGroup")) {
				elixirs.add(dto);
				continue;
			}
			
			HashMap<String,Object> elixir = (HashMap<String,Object>)((HashMap<String,Object>) ((HashMap<String,Object>)element_008.get("value")).get("Element_000")).get("contentStr");
			if(elixir.get("Element_000")!=null) {
				String first = ((HashMap<String,Object>) elixir.get("Element_000")).get("contentStr").toString();
				Pattern pattern = Pattern.compile("</FONT>(.*?)<FONT"); // 패턴 추출을 위한 정규식
				Matcher matcher = pattern.matcher(first);
				String name = "";
				Integer level=null;
				if (matcher.find()) {
					 name = matcher.group(1);
				}

				pattern = Pattern.compile("Lv\\.(\\d+)"); // 패턴 추출을 위한 정규식
				matcher = pattern.matcher(first);

				if (matcher.find()) {
					level = Integer.parseInt(matcher.group(1));
				}
				dto.setFirstOp(name, level);
			}
			if(elixir.get("Element_001")!=null) {
				String second = ((HashMap<String,Object>) elixir.get("Element_001")).get("contentStr").toString();
				Pattern pattern = Pattern.compile("</FONT>\\s*(.+?)\\s*<FONT"); // 패턴 추출을 위한 정규식
				Matcher matcher = pattern.matcher(second);
				String name = "";
				Integer level=null;
				if (matcher.find()) {
					 name = matcher.group(1);
				}

				pattern = Pattern.compile("Lv\\.(\\d+)"); // 패턴 추출을 위한 정규식
				matcher = pattern.matcher(second);

				if (matcher.find()) {
					level = Integer.parseInt(matcher.group(1));
				}
				dto.setSecondOp(name, level);
			}
			elixirs.add(dto);	
		}
		log.info("elixirs : {}",elixirs);
		return elixirs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Value("${gpt.auth}")
	private String gptAuth;
	@Value("${gpt.url}")
	private String gptUrl;
	
	@Async
	public CompletableFuture<String> getChatGpt(String prompt, List<UserChatEntity> beforeEntitys) throws Exception {
	    // 1. RestTemplate 객체 생성
	    RestTemplateBuilder builder = new RestTemplateBuilder();
	    RestTemplate restTemplate = builder
	            .setConnectTimeout(Duration.ofSeconds(30))
	            .setReadTimeout(Duration.ofSeconds(30))
	            .build();

	    // 2. HTTP 요청 본문 생성
	    Map<String, Object> requestBody = new HashMap<>();
	    
	    List<HashMap<String,Object>> messages = new ArrayList<>();
	    for(UserChatEntity beforeEntity:beforeEntitys) {
	    	HashMap<String,Object> map1 = new HashMap<>();	    
		    map1.put("role", "user");
		    map1.put("content", beforeEntity.getRequestChat());
		    messages.add(map1);
		    HashMap<String,Object> map2 = new HashMap<>();	    
		    map2.put("role", "assistant");
		    map2.put("content",  beforeEntity.getResponseChat());
		    messages.add(map2);
	    }
	    HashMap<String,Object> map3 = new HashMap<>();	    
	    map3.put("role", "user");
	    map3.put("content", prompt);
	    messages.add(map3);
	    requestBody.put("model", "gpt-3.5-turbo");
	    requestBody.put("messages", messages);
	    requestBody.put("max_tokens", 2048);
	    requestBody.put("temperature", 0.8);

	    // 3. HTTP 요청 헤더 생성
	    HttpHeaders header = new HttpHeaders();
	    header.add("Content-Type", "application/json");
	    header.add("authorization", "Bearer " + gptAuth);

	    // 4. HTTP 요청 엔티티 생성
	    HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), header);

	    // 5. HTTP 요청 URL 생성
	    UriComponents uri = UriComponentsBuilder.fromHttpUrl(gptUrl).build(false);

	    // 6. HTTP 요청 수행 및 결과 반환
	    log.info("request : {}",entity);
	    HashMap<String, Object> profile=null;
	    try {
	    	ResponseEntity<String> response = restTemplate.exchange(uri.toUri(), HttpMethod.POST, entity, String.class);
	    	profile =mapper.readValue(response.getBody(), HashMap.class);
	    	log.info("response : {}",profile);
	    	return  CompletableFuture.completedFuture(((List<HashMap<String, HashMap<String, Object>>>) profile.get("choices")).get(0).get("message").get("content").toString());
		} catch (Exception e) {
			return CompletableFuture.completedFuture("죄송합니다 말을 이해 못했습니다.");
		}
	   
	}
}
