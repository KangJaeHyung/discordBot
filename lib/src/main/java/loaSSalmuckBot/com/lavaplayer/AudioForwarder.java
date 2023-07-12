package loaSSalmuckBot.com.lavaplayer;

import java.nio.ByteBuffer;

import javax.annotation.Nullable;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

public class AudioForwarder implements AudioSendHandler{
	
	  private final AudioPlayer player;
	    private final Guild guild;
	    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
	    private final MutableAudioFrame frame = new MutableAudioFrame();
	    private int time;

	    public AudioForwarder(AudioPlayer player, Guild guild) {
	        this.player = player;
	        this.guild = guild;
	        frame.setBuffer(buffer);
	    }

	    @Override
	    public boolean canProvide() {
	        boolean canProvide = player.provide(frame);
	        if(!canProvide) {
	            time += 20;
	            if(time >= 300000) {
	                time = 0;
	                guild.getAudioManager().closeAudioConnection();
	            }
	        } else {
	            time = 0;
	        }
	        return canProvide;
	    }

	    @Nullable
	    @Override
	    public ByteBuffer provide20MsAudio() {
	        return buffer.flip();
	    }

	    @Override
	    public boolean isOpus() {
	        return true;
	    }
}
