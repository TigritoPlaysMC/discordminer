package fr.glowning.discordminer.cmd;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.Main;
import fr.glowning.discordminer.entity.Inventory;
import fr.glowning.discordminer.entity.Miner;
import fr.glowning.discordminer.items.Pickaxe;
import fr.glowning.discordminer.shop.SEnchant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class MineCommand {
	private Miner miner;
	private Main main;
	private User u;
	private String pickaxe;

	public MineCommand(User u) {
		this.miner = new Miner(u.getIdLong());
		this.u = u;
		miner.getBonus().setType("None");
	}

	public MineCommand(CommandEvent event, Main main) {
		this.u = event.getUser();
		this.miner = new Miner(u.getIdLong());
		this.main = main;

		HashMap<Long, Long> mineCooldown = main.getMineCooldown();

		if (miner.exists()) {
			int efficiency = new SEnchant(miner).getEfficiencyRatio();
			long cd = (mineCooldown.containsKey(u.getIdLong()) ? mineCooldown.get(u.getIdLong()) + efficiency : 0);

			if (!miner.isBroken(miner.getPickaxe())) {
				if (mineCooldown.containsKey(u.getIdLong()) && System.currentTimeMillis() <= cd) {
					long time = cd - System.currentTimeMillis();

					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(time);

					event.reply("**STOP:** You can mine again in " + cal.get(Calendar.SECOND) + "." + cal.get(Calendar.MILLISECOND) + " seconds.");
				} else {
					if (!main.getMines().containsKey(u.getIdLong()) || main.getMines().get(u.getIdLong()) < 100) {
						doMine(event, 1);
					} else {
						if (main.getMines().get(u.getIdLong()) < 110) {
							Random rnd = new Random();
							int captcha = rnd.nextInt(9999 - 1000) + 1000;

							main.getCaptcha().put(u.getIdLong(), captcha);

							EmbedBuilder embed = new EmbedBuilder();
							embed.setAuthor(u.getName(), null, u.getAvatarUrl());
							embed.setColor(event.getMember().getColor());
							embed.addField("Anti-bot verification!", "Please, do `m!verify <code>` where the code is\nthe number displayed in the image.", true);
							embed.setImage("https://glowning.dev/discordminer/webhook/image.php?code=" + captcha);
							event.reply(embed.build());

							main.getMines().put(u.getIdLong(), main.getMines().get(u.getIdLong()) + 1);
						}
					}
				}
			} else {
				event.reply("Your pickaxe is broken. Type `m!repair` to repair it!");
			}
		} else {
			event.reply("You can't mine without a pickaxe. Do `m!start` to get one!");
		}
	}

	public void doMine(CommandEvent event, int times) {
		Pickaxe pick = new Pickaxe(miner.getPickaxe());
		long durability = miner.getDurability(miner.getPickaxe());
		long mined = 0, totalMined = 0, xp = 0;
		this.pickaxe = miner.getPickaxe();

		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl());
		embed.setColor(event.getMember().getColor());

		StringBuilder sb = new StringBuilder();

		for (String str : pick.getMinedOres()) {
			mined = 0;
			String[] content = str.split(";");

			String ore = content[0];
			int min = Integer.parseInt(content[1]);
			int max = Integer.parseInt(content[2]);

			if (ore.equalsIgnoreCase("emerald")) {
				Random rnd = new Random();
				for (int i = 0; i < times; i++) {
					int luck = rnd.nextInt(100 - 1) + 1;

					if (miner.getPickaxe().equals("donator") && luck <= 7) {
						mined += rnd.nextInt(max - min) + min; // Generating the number
						totalMined += mined;

						miner.getInv().addEmerald(mined);
					} else if (miner.getPickaxe().equals("crate") && luck <= 5) {
						mined += rnd.nextInt(max - min) + min; // Generating the number
						totalMined += mined;

						miner.getInv().addEmerald(mined);
					}
				}

				if (mined > 0) {
					sb.append(String.format("%s %s x%d\n", Assets.valueOf(ore.toUpperCase()).getValue(), StringUtils.capitalize(ore), mined));
				}
			} else {
				Random rnd = new Random(); // Calling Random to generate a random number between min and max
				for (int i = 0; i < times; i++) {
					mined += rnd.nextInt(max - min) + min; // Generating the number
					totalMined += mined;
				}

				sb.append(String.format("%s %s x%d\n", Assets.valueOf(ore.toUpperCase()).getValue(), StringUtils.capitalize(ore), mined * new SEnchant(miner).getFortuneRatio())); // If the user purchased Fortune, multiply the
																																													// mined ores without to affect XP
				Inventory inv = miner.getInv();
				switch (ore) {
					case "stone":
						inv.addStone(mined * new SEnchant(miner).getFortuneRatio());
						miner.getStats().put("stone", miner.getStats().getLong("stone") + mined);
						xp += 1 * mined;
						break;

					case "coal":
						inv.addCoal(mined * new SEnchant(miner).getFortuneRatio());
						miner.getStats().put("coal", miner.getStats().getLong("coal") + mined);
						xp += 2 * mined;
						break;

					case "iron":
						inv.addIron(mined * new SEnchant(miner).getFortuneRatio());
						miner.getStats().put("iron", miner.getStats().getLong("iron") + mined);
						xp += 4 * mined;
						break;

					case "gold":
						inv.addGold(mined * new SEnchant(miner).getFortuneRatio());
						miner.getStats().put("gold", miner.getStats().getLong("gold") + mined);
						xp += 5 * mined;
						break;

					case "obsidian":
						inv.addObsidian(mined * new SEnchant(miner).getFortuneRatio());
						miner.getStats().put("obsidian", miner.getStats().getLong("obsidian") + mined);
						xp += 7 * mined;
						break;

					case "diamond":
						inv.addDiamond(mined * new SEnchant(miner).getFortuneRatio());
						miner.getStats().put("diamond", miner.getStats().getLong("diamond") + mined);
						xp += 10 * mined;
						break;
				}
			}
		}

		embed.addField("You mined", sb.toString() + "With your " + pick.getAppearance(), true);

		Random rnd = new Random();
		int chests = 0, lapis = 0, redstone = 0;
		for (int i = 0; i < times; i++) {
			int luck = rnd.nextInt(100 - 1) + 1;

			if (luck <= (5 + (5 * miner.getEnchant().getLuck()))) {
				// 5% + (5 * luck) chance to get a chest while mining
				chests++;
				lapis += rnd.nextInt(5 - 1) + 1;
				redstone += rnd.nextInt(10 - 1) + 1;
			}
		}

		if (chests > 0) {
			miner.getInv().addRedstone(redstone);
			miner.getInv().addLapis(lapis);

			miner.getStats().put("chest_found", miner.getStats().getInt("chest_found") + chests);
			miner.getStats().put("redstone", miner.getStats().getInt("redstone") + redstone);
			miner.getStats().put("lapis", miner.getStats().getInt("lapis") + lapis);

			embed.addField("Chest found!", "You found " + chests + " " + Assets.CHEST.getValue() + "\nduring your expedition.\n+" + redstone + " " + Assets.REDSTONE.getValue() + " / +" + lapis + " " + Assets.LAPIS.getValue(), true);
		}

		if (!miner.getBonus().getType().equals("unbreakable")) {
			miner.setDurability(miner.getPickaxe(), durability - (totalMined * (1 - new SEnchant(miner).getUnbreakingRatio() / 100))); // Remove x% with "a * (1 - x/100)" formula
		} else {
			miner.getBonus().setContent(miner.getBonus().getContent() - 1);

			if (miner.getBonus().getContent() <= 0) {
				miner.getBonus().setType("None");
				event.reply(u.getAsMention() + " » Your bonus expired!");
			}
		}

		miner.addXp(xp);

		if (miner.canLevelUp()) {
			embed.addField("You leveled up!", "You are now level " + miner.getLevel() + "!\n+5 " + Assets.EMERALD.getValue() + winNewPickaxe(), true);
		}

		if (miner.isBroken(miner.getPickaxe())) {
			embed.addField("Pickaxe broken!", "Your pickaxe is broken.\nType `m!repair` to repair it!\nYou can also wait 30 minutes.", true);

			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					Miner m = new Miner(u.getIdLong());
					Pickaxe pick = new Pickaxe(pickaxe);

					if (m.getDurability(pickaxe) == 0) {
						event.reply(u.getAsMention() + " » Your " + pick.getAppearance() + " repaired itself!");
						m.setDurability(pickaxe, getPickaxeMax());
						m.update();
					}
				}
			}, 30 * 60 * 1000);
		}

		if (times == 1) {
			miner.getAdv().addMine();
			int mine = miner.getAdv().getMine();

			if (mine == 1000) {
				embed.addField("Advancement get!", "Mine 1000 times\n+5 " + Assets.EMERALD.getValue(), true);
				miner.getInv().addEmerald(5);
			} else if (mine == 5000) {
				embed.addField("Advancement get!", "Mine 5000 times\n+10 " + Assets.EMERALD.getValue(), true);
				miner.getInv().addEmerald(10);
			} else if (mine == 13500) {
				embed.addField("Advancement get!", "Mine 13500 times\n+15 " + Assets.EMERALD.getValue(), true);
				miner.getInv().addEmerald(15);
			} else if (mine == 25000) {
				embed.addField("Advancement get!", "Mine 25000 times\n+20 " + Assets.EMERALD.getValue(), true);
				miner.getInv().addEmerald(20);
			}

			main.getMineCooldown().put(event.getUser().getIdLong(), System.currentTimeMillis());

			if (!main.getMines().containsKey(u.getIdLong())) {
				main.getMines().put(u.getIdLong(), 1);
			} else {
				main.getMines().put(u.getIdLong(), main.getMines().get(u.getIdLong()) + 1);
			}
		}

		event.reply(embed.build());
		miner.update();
	}

	private long getPickaxeMax() {
		switch (miner.getPickaxe()) {
			case "wooden":
				return 500;

			case "stone":
				return 1350;

			case "gold":
				return 3500;

			case "iron":
				return 8000;

			case "diamond":
				return 15000;

			case "crate":
				return 30000;

			case "emerald":
			case "donator":
				return 50000;

			case "ruby":
				return 75000;

			case "ultimate":
				return 150000;

			default:
				return 0;
		}
	}

	private String winNewPickaxe() {
		if (miner.getLevel() == 5) {
			miner.setPickaxe("stone");
			miner.setDurability("stone", 1350);

			JSONObject object = new JSONObject();
			object.put("efficiency", 0);
			object.put("unbreaking", 0);
			object.put("fortune", 0);
			object.put("luck", 0);
			miner.getFullEnchant().put("stone", object);
			miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);

			Guild g = Main.getShards().getGuildById(492972328746090496L);
			if ((g.getMember(u)) != null) {
				Role r = g.getRoleById(545260862416355340L);
				g.getController().addSingleRoleToMember(g.getMember(u), r).queue();
			}

			return "\nYou unlocked the " + Assets.PSTONE.getValue();
		}

		if (miner.getLevel() == 20) {
			miner.setPickaxe("gold");
			miner.setDurability("gold", 3500);

			JSONObject object = new JSONObject();
			object.put("efficiency", 0);
			object.put("unbreaking", 0);
			object.put("fortune", 0);
			object.put("luck", 0);
			miner.getFullEnchant().put("gold", object);
			miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);

			Guild g = Main.getShards().getGuildById(492972328746090496L);
			if ((g.getMember(u)) != null) {
				Role r = g.getRoleById(545260860944416769L);
				g.getController().addSingleRoleToMember(g.getMember(u), r).queue();
			}

			return "\nYou unlocked the " + Assets.PGOLD.getValue();
		}

		if (miner.getLevel() == 50) {
			miner.setPickaxe("iron");
			miner.setDurability("iron", 8000);

			JSONObject object = new JSONObject();
			object.put("efficiency", 0);
			object.put("unbreaking", 0);
			object.put("fortune", 0);
			object.put("luck", 0);
			miner.getFullEnchant().put("iron", object);
			miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);

			Guild g = Main.getShards().getGuildById(492972328746090496L);
			if ((g.getMember(u)) != null) {
				Role r = g.getRoleById(545260859312832527L);
				g.getController().addSingleRoleToMember(g.getMember(u), r).queue();
			}

			return "\nYou unlocked the " + Assets.PIRON.getValue();
		}

		if (miner.getLevel() == 100) {
			miner.setPickaxe("diamond");
			miner.setDurability("diamond", 15000);

			JSONObject object = new JSONObject();
			object.put("efficiency", 0);
			object.put("unbreaking", 0);
			object.put("fortune", 0);
			object.put("luck", 0);
			miner.getFullEnchant().put("diamond", object);
			miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);

			Guild g = Main.getShards().getGuildById(492972328746090496L);
			if ((g.getMember(u)) != null) {
				Role r = g.getRoleById(545260857068879884L);
				g.getController().addSingleRoleToMember(g.getMember(u), r).queue();
			}

			return "\nYou unlocked the " + Assets.PDIAMOND.getValue();
		}

		if (miner.getLevel() == 150) {
			miner.setPickaxe("sapphire");
			miner.setDurability("sapphire", 25000);

			JSONObject object = new JSONObject();
			object.put("efficiency", 0);
			object.put("unbreaking", 0);
			object.put("fortune", 0);
			object.put("luck", 0);
			miner.getFullEnchant().put("sapphire", object);
			miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);

			Guild g = Main.getShards().getGuildById(492972328746090496L);
			if ((g.getMember(u)) != null) {
				Role r = g.getRoleById(545260846117552128L);
				g.getController().addSingleRoleToMember(g.getMember(u), r).queue();
			}

			return "\nYou unlocked the " + Assets.PSAPPHIRE.getValue();
		}

		return "";
	}

}
