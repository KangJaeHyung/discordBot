package loaSSalmuckBot.com.LostArkDto;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class ArmoryProfile {
    private String characterImage;
	private Integer expeditionLevel; 
	private String pvpGradeName;
	private Integer townLevel;
	private String townName;
	private String title;
	private String guildMemberGrade;
	private String guildName;
	private Integer usingSkillPoint;
	private Integer totalSkillPoint;
	private List<HashMap<String,Object>> stats;
	private List<HashMap<String,Object>> tendencies;
	private String serverName;
	private String characterName;
	private String characterLevel;
	private String characterClassName;
	private String itemAvgLevel;
	private String itemMaxLevel;
}
