package loaSSalmuckBot.com.util;

import java.awt.Color;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
	
	
	
	public static RestTemplate makeRestTemplate(boolean ignoreSsl) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		  if(ignoreSsl) {
		    TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
		      .loadTrustMaterial(null, acceptingTrustStrategy)
		      .build();

		    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
		    CloseableHttpClient httpClient = HttpClients.custom()
		        .setSSLSocketFactory(csf)
		        .build();

		    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		    requestFactory.setHttpClient(httpClient);
		    requestFactory.setConnectTimeout(3 * 1000);
		    requestFactory.setReadTimeout(3 * 1000);
		    
		    return new RestTemplate(requestFactory);
		  }
		  else {
		    return new RestTemplate();
		  }
		}
	
	@Async
	public CompletableFuture<ArmoryProfile> getLoaUser (String userName) throws Exception {
		// 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
	    RestTemplate restTemplate = makeRestTemplate(true);
	    // 2. HTTP 요청 본문 생성
	    Map<String, Object> requestBody = new HashMap<>();

        // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
        HttpHeaders header = new HttpHeaders();
        header.add("accept", "application/json");
        header.add("authorization", "bearer "+apiKey);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), header);

        // 4. 요청 URL을 정의해줍니다.
        String url = "https://developer-lostark.game.onstove.com/armories/characters/"+userName+"/profiles";
//        UriComponents uri = UriComponentsBuilder.fromHttpUrl(new String(url.getBytes(),"UTF-8")).build(false);

        // 5. exchange() 메소드로 api를 호출합니다.
        
        try {
        	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        	if(!response.getStatusCode().equals(HttpStatus.OK))  	return  CompletableFuture.completedFuture(new ArmoryProfile(true));
        	ArmoryProfile profile = mapper.readValue(response.getBody(), ArmoryProfile.class);
	    	return  CompletableFuture.completedFuture(profile);
		} catch (Exception e) {
			e.printStackTrace();
			return CompletableFuture.completedFuture(null);
		}
	}
	

	
	
	@Value("${gpt.auth}")
	private String gptAuth;
	@Value("${gpt.url}")
	private String gptUrl;
	
	public String getChatGpt(String nick) throws Exception {
	    // 1. RestTemplate 객체 생성
	    RestTemplateBuilder builder = new RestTemplateBuilder();
	    RestTemplate restTemplate = builder
	            .setConnectTimeout(Duration.ofSeconds(30))
	            .setReadTimeout(Duration.ofSeconds(30))
	            .build();

	    // 2. HTTP 요청 본문 생성
	    Map<String, Object> requestBody = new HashMap<>();
	    
	    List<HashMap<String,Object>> messages = new ArrayList<>();

		HashMap<String, Object> map2 = new HashMap<>();
		map2.put("role", "system");
		map2.put("content","");
		messages.add(map2);

	    HashMap<String,Object> map3 = new HashMap<>();	    
	    map3.put("role", "user");
	    map3.put("content", nick+" 라는 닉네임이란 사람에 생일 축하 메세지를 만들어줘");
	    messages.add(map3);
	    requestBody.put("model", "gpt-3.5-turbo");
	    requestBody.put("messages", messages);
	    requestBody.put("max_tokens", 1024);
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
	    HashMap<String, Object> profile=null;
	    try {
	    	ResponseEntity<String> response = restTemplate.exchange(uri.toUri(), HttpMethod.POST, entity, String.class);
	    	profile =mapper.readValue(response.getBody(), HashMap.class);
	    	List<HashMap<String,Object>> choices= (List<HashMap<String, Object>>) profile.get("choices");
	    	HashMap<String,Object> message=  (HashMap<String, Object>) choices.get(0).get("message");
	    	return message.get("content").toString();
	
		} catch (Exception e) {
			e.printStackTrace();
			return nick+"님 생일 축하드립니다~!";
		}
	   
	}
}
