package loaSSalmuckBot.com.util;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

@Component
public class LoaRestAPI {
	ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	
	
	
	
	
	public void getLoaUser (String userName) throws Exception {
		// 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000); // 타임아웃 설정 5초
        factory.setReadTimeout(30000); // 타임아웃 설정 5초

        //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        HttpClient httpClient = HttpClientBuilder.create()
                                                .setMaxConnTotal(50)//최대 커넥션 수
                                                .setMaxConnPerRoute(20).build();
        factory.setHttpClient(httpClient);

        // 2. RestTemplate 객체를 생성합니다.
        RestTemplate restTemplate = new RestTemplate(factory);

        // 3. header 설정을 위해 HttpHeader 클래스를 생성한 후 HttpEntity 객체에 넣어줍니다.
        HttpHeaders header = new HttpHeaders();
        header.add("accept", "application/json");
        header.add("authorization", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMDAwOTUifQ.Kmwkh1S1trYXa6M3Wum_nY9Odrz6JUnwoWu3L4B0MX2Y6bW-rttof-E-CsUw575JPIiP_62sYCrbRJCuCSt4YqBptcFHemFrtBiIw_xuhOAUmiAImLGwkGBzUJ_Mw6Bj427Ssv7osMzXA7OF1tLrMx49K52DIVrGy73hR3QWoXhLzg10UTIuT1V7YRvKFAKigjpP0FBA43juHlLNOLOatr0SQ03JjxXRT1AFmnXfB4B5NzgZzWhlwK2_LywHynp3o66LLuTgwKDDMYOyiCn4DSfZYdgPHzqUuJC4jlaDCmysZPjWb4-0lR89R8QV8AnEbs84tq7vgPDuDqu21dR1OA");
        HttpEntity<String> entity = new HttpEntity<String>(header);

        // 4. 요청 URL을 정의해줍니다.
        String url = "https://developer-lostark.game.onstove.com/armories/characters/"+userName+"/profiles";
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(new String(url.getBytes(),"UTF-8")).build(false);

        // 5. exchange() 메소드로 api를 호출합니다.
        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        System.out.println("header : "+response.getHeaders());
        
        
        System.out.println("body : "+response.getBody());
        ArmoryProfile profile = mapper.readValue(response.getBody(), ArmoryProfile.class);
        System.out.println("profile : "+ profile);
	}
	
	@Value("${gpt.auth}")
	private String gptAuth;
	@Value("${gpt.url}")
	private String gptUrl;
	
	@Async
	public CompletableFuture<String> getChatGpt(String prompt) throws Exception {
	    // 1. RestTemplate 객체 생성
	    RestTemplateBuilder builder = new RestTemplateBuilder();
	    RestTemplate restTemplate = builder
	            .setConnectTimeout(Duration.ofSeconds(15))
	            .setReadTimeout(Duration.ofSeconds(15))
	            .build();

	    // 2. HTTP 요청 본문 생성
	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("model", "text-davinci-003");
	    requestBody.put("prompt", prompt);
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
	    return CompletableFuture.supplyAsync(() -> restTemplate.exchange(uri.toUri(), HttpMethod.POST, entity, String.class))
	            .thenApply(response -> {
	                try {
	                    HashMap<String, Object> profile = mapper.readValue(response.getBody(), new TypeReference<HashMap<String, Object>>() {
	                    });
	                    return ((List<HashMap<String, Object>>) profile.get("choices")).get(0).get("text").toString();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                return "제가 이해하지 못했습니다. 다시시도해주세요.";
	            });
	}
}
