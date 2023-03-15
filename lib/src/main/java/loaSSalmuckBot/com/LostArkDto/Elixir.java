package loaSSalmuckBot.com.LostArkDto;

import lombok.Data;
import lombok.Getter;

@Data
public class Elixir {
	private EquipmentEnum equipment;
	private Option first;
	private Option second;
	
	public void setFirstOp(String name, Integer level){
		first= new Option();
		first.setLevel(level);
		first.setName(name);
	}
	public void setSecondOp(String name, Integer level){
		second= new Option();
		second.setLevel(level);
		second.setName(name);
	}
	
	@Data
	public class Option{
		private String name;
		private Integer level;
	}
	
	@Getter
	public enum EquipmentEnum{
		head("투구"),
		top("상의"),
		pants("하의"),
		shoulder("어깨"),
		gloves("장갑"),
		non("비어있음");
		
		private final String value;
		
		EquipmentEnum(String value) {
			this.value=value;
		}
		
	}
}
