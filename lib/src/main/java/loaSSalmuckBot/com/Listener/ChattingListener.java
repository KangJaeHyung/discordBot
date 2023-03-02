package loaSSalmuckBot.com.Listener;

import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


@Component
public class ChattingListener extends ListenerAdapter {

	
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
//		MessageChannelUnion channel = event.getChannel();
//		String msg = event.getMessage().getContentRaw();
//		System.out.println("channelId: "+channel.getIdLong()+", name"+event.getMember().getNickname()+", content"+event.getMessage().getContentRaw());
//		
//		String[] msgSplit = msg.split(" ");
//		if(msgSplit[0].equals("!유저생성")) {
//			
//		}
	}
	

	

}
