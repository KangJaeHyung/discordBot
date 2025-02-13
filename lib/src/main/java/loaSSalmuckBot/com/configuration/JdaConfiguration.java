package loaSSalmuckBot.com.configuration;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import loaSSalmuckBot.com.Listener.BirthChannelListener;
import loaSSalmuckBot.com.Listener.ChatChannelListener;
import loaSSalmuckBot.com.Listener.CommandListener;
import loaSSalmuckBot.com.Listener.GuildListener;
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
	 
	 @Value("${isServer}")
	 private Boolean isServer;

	@Bean
    public JDA jda() {
    	JDA jda = JDABuilder
				.createLight(isServer?discordToken1:discordToken2,
						EnumSet.allOf(GatewayIntent.class))
//				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableCache(CacheFlag.VOICE_STATE).setMemberCachePolicy(MemberCachePolicy.DEFAULT).build();
                jda.addEventListener(chattingListener(), voiceChannelListener(), myApplicationCommand(),birthChannelListener(),chatChannelListener());
		
        return jda;
    }
    
    @Bean
    public CommandListener myApplicationCommand() {
        return new CommandListener();
    }
    @Bean
    public VoiceChannelListener voiceChannelListener() {
        return new VoiceChannelListener();
    }
    @Bean
    public BirthChannelListener birthChannelListener() {
        return new BirthChannelListener();
    }
    @Bean
    public GuildListener chattingListener() {
        return new GuildListener();
    }

    @Bean
    public ChatChannelListener chatChannelListener() {
        return new ChatChannelListener();
    }
    
}