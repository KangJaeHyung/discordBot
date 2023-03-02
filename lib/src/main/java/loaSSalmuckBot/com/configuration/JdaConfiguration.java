package loaSSalmuckBot.com.configuration;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

//import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import loaSSalmuckBot.com.Listener.ChattingListener;
import loaSSalmuckBot.com.Listener.MyApplicationCommand;
import loaSSalmuckBot.com.Listener.VoiceChannelListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Configuration
@ComponentScan(basePackages= "loaSSalmuckBot.com")
public class JdaConfiguration {

	 @Value("${discord.token_1}")
    private String discordToken1;
	 
	 @Value("${discord.token_2}")
	 private String discordToken2;

	@Bean
    public JDA jda() {
    	JDA jda = JDABuilder
				.createLight(discordToken1+"."+discordToken2,
						EnumSet.allOf(GatewayIntent.class))
				.enableCache(CacheFlag.VOICE_STATE).setMemberCachePolicy(MemberCachePolicy.DEFAULT).build();

		jda.addEventListener(chattingListener(), voiceChannelListener(), myApplicationCommand());
        return jda;
    }
    
    @Bean
    public MyApplicationCommand myApplicationCommand() {
        return new MyApplicationCommand();
    }
    @Bean
    public VoiceChannelListener voiceChannelListener() {
        return new VoiceChannelListener();
    }
    @Bean
    public ChattingListener chattingListener() {
        return new ChattingListener();
    }
}