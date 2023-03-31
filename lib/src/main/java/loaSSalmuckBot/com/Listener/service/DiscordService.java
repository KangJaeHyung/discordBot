package loaSSalmuckBot.com.Listener.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import loaSSalmuckBot.com.api.jpa.channel.VoiceChannelRepository;
import loaSSalmuckBot.com.api.jpa.userBan.UserBanEntity;
import loaSSalmuckBot.com.api.jpa.userBan.UserBanRepository;
import loaSSalmuckBot.com.api.jpa.userChat.UserChatEntity;
import loaSSalmuckBot.com.api.jpa.userChat.UserChatRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;

@Service
public class DiscordService {
	@Autowired
	public VoiceChannelRepository voiceChannelRepository;

	@Autowired
	public UserChatRepository userChatRepository;
	
	@Autowired
	public UserBanRepository userBanRepository;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	
	public List<UserChatEntity> getBeforeUserChat(String userId) {
		List<UserChatEntity> entityOp=  userChatRepository.findTop10ByUserIdOrderByIdDesc(userId);
		Collections.reverse(entityOp);
		return entityOp;
	}
	
	public void userBan(Guild guild ,String id,String reason) {
		UserBanEntity banEntity = new UserBanEntity();
		banEntity.setUserId(id);
		banEntity.setGuildId(guild.getId());
		banEntity.setBan_reason(reason);
		banEntity.setBanYmdt(new Date());
		userBanRepository.save(banEntity);
	}
	
	
	public void raid(SlashCommandInteractionEvent event) {
		String raid =event.getOption("raid").getAsString();
		switch (raid) {
			case "valtan": {
				valtan(event);
				break;
			}
			case "biackiss": {
				biackiss(event);
				break;
			}
			case "kuoku": {
				kuoku(event);
				break;
			}
			case "abrelshud": {
				abrelshud(event);
				break;
			}
			case "kayangel": {
				kayangel(event);
				break;
			}
			case "illiakan": {
				illiakan(event);
				break;
			}
			case "tower": {
				illiakan(event);
				break;
			}
		}
			
		
	}
	
	public void valtan(SlashCommandInteractionEvent event) {
		Integer gateway = event.getOption("gateway") == null ? 1 : event.getOption("gateway").getAsInt();

		try {
			String imagePath = "classpath:named/valtan_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "valtan_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(
					Arrays.asList(SelectOption.of("발탄 1관문", "valtan_1"), SelectOption.of("발탄 2관문", "valtan_2")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("발탄 " + gateway + "관문").addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void biackiss(SlashCommandInteractionEvent event) {
		Integer gateway = event.getOption("gateway") == null ? 1 : event.getOption("gateway").getAsInt();

		try {
			String imagePath = "classpath:named/biackiss_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "biackiss_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("비아키스 1관문", "biackiss_1"),
					SelectOption.of("비아키스 2관문", "biackiss_2"), SelectOption.of("비아키스 3관문", "biackiss_3")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("비아키스 " + gateway + "관문").addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void kuoku(SlashCommandInteractionEvent event) {
		Integer gateway = event.getOption("gateway") == null ? 1 : event.getOption("gateway").getAsInt();
		try {
			String imagePath = "classpath:named/kuoku_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "kuoku_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("쿠크세이튼 1관문", "kuoku_1"),
					SelectOption.of("쿠크세이튼 2관문", "kuoku_2"), SelectOption.of("쿠크세이튼 3관문", "kuoku_3")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("쿠크세이튼 " + gateway + "관문").addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void abrelshud(SlashCommandInteractionEvent event) {
		Integer gateway = event.getOption("gateway") == null ? 1 : event.getOption("gateway").getAsInt();
		try {
			String imagePath = "classpath:named/abrelshud_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
					SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
					SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
					SelectOption.of("아브렐슈드 6관문", "abrelshud_6"), SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
					SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
					SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
					SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("아브렐슈드 " + gateway + "관문").addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void kayangel(SlashCommandInteractionEvent event) {
		String gateway = event.getOption("gateway") == null ? "griffon" : event.getOption("gateway").getAsString();
		try {
			String imagePath = "classpath:named/kayangel_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "kayangel_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(Arrays.asList(
					SelectOption.of("카양겔 천공의 파수꾼", "kayangel_griffon"), SelectOption.of("카양겔 티엔", "kayangel_tien"),
					SelectOption.of("카양겔 프리우나", "kayangel_priuna"), SelectOption.of("카양겔 라우리엘", "kayangel_lauriel")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("카양겔 " + gateway).addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void illiakan(SlashCommandInteractionEvent event) {
		Integer gateway = event.getOption("gateway") == null ? 1 : event.getOption("gateway").getAsInt();

		try {
			String imagePath = "classpath:named/illiakan_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
					SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
					SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"), SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
					SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd3")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("일리아칸 " + gateway + " 관문").addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void tower(SlashCommandInteractionEvent event) {
		Integer gateway = event.getOption("gateway") == null ? 1 : event.getOption("gateway").getAsInt();

		try {
			String imagePath = "classpath:named/tower_" + gateway + ".png";
			Resource resource = resourceLoader.getResource(imagePath);
			InputStream inputStream = resource.getInputStream();
			FileUpload fileUpload = FileUpload.fromData(inputStream, "tower_" + gateway + ".png");
			List<SelectOption> options = new ArrayList<>(
					Arrays.asList(SelectOption.of("상아탑 1관문", "tower_1"), SelectOption.of("상아탑 2관문", "tower_2"),
							SelectOption.of("상아탑 3관문", "tower_3"), SelectOption.of("상아탑 4관문", "tower_4")));

			SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
			event.reply("상아탑 " + gateway + " 관문").addFiles(fileUpload).addActionRow(menu).setEphemeral(true).queue();
		} catch (Exception e) {
			event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
		}

	}

	public void choiceFormate(StringSelectInteractionEvent event) {
		String value = event.getValues().get(0);

		switch (value) {
		case "valtan_1": {
			try {
				String imagePath = "classpath:named/valtan_1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "valtan_1.png");
				List<SelectOption> options = new ArrayList<>(
						Arrays.asList(SelectOption.of("발탄 1관문", "valtan_1"), SelectOption.of("발탄 2관문", "valtan_2")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("발탄 1관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;
		}
		case "valtan_2": {
			try {
				String imagePath = "classpath:named/valtan_2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "valtan_2.png");
				List<SelectOption> options = new ArrayList<>(
						Arrays.asList(SelectOption.of("발탄 1관문", "valtan_1"), SelectOption.of("발탄 2관문", "valtan_2")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("발탄 2관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;
		}
		case "biackiss_1": {
			try {
				String imagePath = "classpath:named/biackiss_1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "biackiss_1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("비아키스 1관문", "biackiss_1"),
						SelectOption.of("비아키스 2관문", "biackiss_2"), SelectOption.of("비아키스 3관문", "biackiss_3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("비아키스 1관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "biackiss_2": {
			try {
				String imagePath = "classpath:named/biackiss_2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "biackiss_1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("비아키스 1관문", "biackiss_1"),
						SelectOption.of("비아키스 2관문", "biackiss_2"), SelectOption.of("비아키스 3관문", "biackiss_3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("비아키스 2관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "biackiss_3": {
			try {
				String imagePath = "classpath:named/biackiss_3.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "biackiss_1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("비아키스 1관문", "biackiss_1"),
						SelectOption.of("비아키스 2관문", "biackiss_2"), SelectOption.of("비아키스 3관문", "biackiss_3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("비아키스 3관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}

		case "kuoku_1": {
			try {
				String imagePath = "classpath:named/kuoku_1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kuoku_1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("쿠크세이튼 1관문", "kuoku_1"),
						SelectOption.of("쿠크세이튼 2관문", "kuoku_2"), SelectOption.of("쿠크세이튼 3관문", "kuoku_3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("쿠크세이튼 1관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "kuoku_2": {
			try {
				String imagePath = "classpath:named/kuoku_2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kuoku_2.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("쿠크세이튼 1관문", "kuoku_1"),
						SelectOption.of("쿠크세이튼 2관문", "kuoku_2"), SelectOption.of("쿠크세이튼 3관문", "kuoku_3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("쿠크세이튼 2관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "kuoku_3": {
			try {
				String imagePath = "classpath:named/kuoku_3.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kuoku_3.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("쿠크세이튼 1관문", "kuoku_1"),
						SelectOption.of("쿠크세이튼 2관문", "kuoku_2"), SelectOption.of("쿠크세이튼 3관문", "kuoku_3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("쿠크세이튼 3관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_1": {
			try {
				String imagePath = "classpath:named/abrelshud_1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 1관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_2": {
			try {
				String imagePath = "classpath:named/abrelshud_2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_2.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 2관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_3": {
			try {
				String imagePath = "classpath:named/abrelshud_3.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_3.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 3관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_4": {
			try {
				String imagePath = "classpath:named/abrelshud_4.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_4.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 4 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_5": {
			try {
				String imagePath = "classpath:named/abrelshud_5.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_5.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 5 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_6": {
			try {
				String imagePath = "classpath:named/abrelshud_6.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_6.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 6 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_hd12": {
			try {
				String imagePath = "classpath:named/abrelshud_hd12.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_hd12.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 하드 1,2 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_hd34": {
			try {
				String imagePath = "classpath:named/abrelshud_hd34.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_hd34.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 3,4 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_hd5": {
			try {
				String imagePath = "classpath:named/abrelshud_hd5.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_hd5.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 5 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "abrelshud_hd6": {
			try {
				String imagePath = "classpath:named/abrelshud_hd6.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "abrelshud_hd6.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("아브렐슈드 1관문", "abrelshud_1"),
						SelectOption.of("아브렐슈드 2관문", "abrelshud_2"), SelectOption.of("아브렐슈드 3관문", "abrelshud_3"),
						SelectOption.of("아브렐슈드 4관문", "abrelshud_4"), SelectOption.of("아브렐슈드 5관문", "abrelshud_5"),
						SelectOption.of("아브렐슈드 6관문", "abrelshud_6"),
						SelectOption.of("아브렐슈드 하드 1,2관문", "abrelshud_hd12"),
						SelectOption.of("아브렐슈드 하드 3,4관문", "abrelshud_hd34"),
						SelectOption.of("아브렐슈드 하드 5관문", "abrelshud_hd5"),
						SelectOption.of("아브렐슈드 하드 6관문", "abrelshud_hd6")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("아브렐슈드 6 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "kayangel_griffon": {
			try {
				String imagePath = "classpath:named/kayangel_griffon.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kayangel_griffon.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(
						SelectOption.of("카양겔 천공의 파수꾼", "kayangel_griffon"), SelectOption.of("카양겔 티엔", "kayangel_tien"),
						SelectOption.of("카양겔 프리우나", "kayangel_priuna"),
						SelectOption.of("카양겔 라우리엘", "kayangel_lauriel")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("카양겔 천공의 파수꾼").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "kayangel_tien": {
			try {
				String imagePath = "classpath:named/kayangel_tien.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kayangel_tien.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(
						SelectOption.of("카양겔 천공의 파수꾼", "kayangel_griffon"), SelectOption.of("카양겔 티엔", "kayangel_tien"),
						SelectOption.of("카양겔 프리우나", "kayangel_priuna"),
						SelectOption.of("카양겔 라우리엘", "kayangel_lauriel")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("카양겔 티엔").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "kayangel_priuna": {
			try {
				String imagePath = "classpath:named/kayangel_priuna.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kayangel_priuna.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(
						SelectOption.of("카양겔 천공의 파수꾼", "kayangel_griffon"), SelectOption.of("카양겔 티엔", "kayangel_tien"),
						SelectOption.of("카양겔 프리우나", "kayangel_priuna"),
						SelectOption.of("카양겔 라우리엘", "kayangel_lauriel")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("카양겔 프리우나").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "kayangel_lauriel": {
			try {
				String imagePath = "classpath:named/kayangel_lauriel.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "kayangel_lauriel.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(
						SelectOption.of("카양겔 천공의 파수꾼", "kayangel_griffon"), SelectOption.of("카양겔 티엔", "kayangel_tien"),
						SelectOption.of("카양겔 프리우나", "kayangel_priuna"),
						SelectOption.of("카양겔 라우리엘", "kayangel_lauriel")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("카양겔 라우리엘").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "illiakan_1": {
			try {
				String imagePath = "classpath:named/illiakan_1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
						SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
						SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"),
						SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
						SelectOption.of("일리아칸 하드 3 관문", "illiakan_hd3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("일리아칸 1 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "illiakan_2": {
			try {
				String imagePath = "classpath:named/illiakan_2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_2.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
						SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
						SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"),
						SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
						SelectOption.of("일리아칸 하드 3 관문", "illiakan_hd3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("일리아칸 2 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "illiakan_3": {
			try {
				String imagePath = "classpath:named/illiakan_3.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_3.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
						SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
						SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"),
						SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
						SelectOption.of("일리아칸 하드 3 관문", "illiakan_hd3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("일리아칸 3 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "illiakan_hd1": {
			try {
				String imagePath = "classpath:named/illiakan_hd1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_hd1.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
						SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
						SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"),
						SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
						SelectOption.of("일리아칸 하드 3 관문", "illiakan_hd3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("일리아칸 하드 1 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "illiakan_hd2": {
			try {
				String imagePath = "classpath:named/illiakan_hd2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_hd2.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
						SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
						SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"),
						SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
						SelectOption.of("일리아칸 하드 3 관문", "illiakan_hd3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("일리아칸 하드 2 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "illiakan_hd3": {
			try {
				String imagePath = "classpath:named/illiakan_hd3.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "illiakan_hd3.png");
				List<SelectOption> options = new ArrayList<>(Arrays.asList(SelectOption.of("일리아칸 1관문", "illiakan_1"),
						SelectOption.of("일리아칸 2관문", "illiakan_2"), SelectOption.of("일리아칸 3관문", "illiakan_3"),
						SelectOption.of("일리아칸 하드 1 관문", "illiakan_hd1"),
						SelectOption.of("일리아칸 하드 2 관문", "illiakan_hd2"),
						SelectOption.of("일리아칸 하드 3 관문", "illiakan_hd3")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("일리아칸 하드 3 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "tower_1": {
			try {
				String imagePath = "classpath:named/tower_1.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "tower_1.png");
				List<SelectOption> options = new ArrayList<>(
						Arrays.asList(SelectOption.of("상아탑 1관문", "tower_1"), SelectOption.of("상아탑 2관문", "tower_2"),
								SelectOption.of("상아탑 3관문", "tower_3"), SelectOption.of("상아탑 4관문", "tower_4")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("상아탑 1 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "tower_2": {
			try {
				String imagePath = "classpath:named/tower_2.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "tower_2.png");
				List<SelectOption> options = new ArrayList<>(
						Arrays.asList(SelectOption.of("상아탑 1관문", "tower_1"), SelectOption.of("상아탑 2관문", "tower_2"),
								SelectOption.of("상아탑 3관문", "tower_3"), SelectOption.of("상아탑 4관문", "tower_4")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("상아탑 2 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "tower_3": {
			try {
				String imagePath = "classpath:named/tower_3.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "tower_3.png");
				List<SelectOption> options = new ArrayList<>(
						Arrays.asList(SelectOption.of("상아탑 1관문", "tower_1"), SelectOption.of("상아탑 2관문", "tower_2"),
								SelectOption.of("상아탑 3관문", "tower_3"), SelectOption.of("상아탑 4관문", "tower_4")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("상아탑 3 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}
		case "tower_4": {
			try {
				String imagePath = "classpath:named/tower_4.png";
				Resource resource = resourceLoader.getResource(imagePath);
				InputStream inputStream = resource.getInputStream();
				FileUpload fileUpload = FileUpload.fromData(inputStream, "tower_4.png");
				List<SelectOption> options = new ArrayList<>(
						Arrays.asList(SelectOption.of("상아탑 1관문", "tower_1"), SelectOption.of("상아탑 2관문", "tower_2"),
								SelectOption.of("상아탑 3관문", "tower_3"), SelectOption.of("상아탑 4관문", "tower_4")));

				SelectMenu menu = StringSelectMenu.create("선택").setPlaceholder("관문").addOptions(options).build();
				event.editMessage("상아탑 4 관문").setFiles(fileUpload).setActionRow(menu).queue();
			} catch (Exception e) {
				event.reply("이미지를 불러 오는데 실패 했습니다! 다시 시도 해 주세요.").setEphemeral(true).queue();
			}
			break;

		}

		}

	}




	public void saveUserChat(String id, String chat, String response) {
		UserChatEntity entity = new UserChatEntity();
		entity.setUserId(id);
		entity.setRequestChat(chat);
		entity.setResponseChat(response);
		userChatRepository.save(entity);
	}

}
