package fr.glowning.discordminer.cmd;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class DailyChestCommand {

	public DailyChestCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			if (System.currentTimeMillis() >= miner.getChest() + (24 * 60 * 60 * 1000)) {
				Random rnd = new Random();
				int reward = rnd.nextInt(3 - 1) + 1; // Generate a random number to get the type of reward

				EmbedBuilder embed = new EmbedBuilder();
				embed.setAuthor(u.getName(), null, u.getAvatarUrl());
				embed.setColor(event.getMember().getColor());

				if (reward == 1) { // Emeralds as reward
					if (System.currentTimeMillis() <= miner.getChest() + (36 * 60 * 60 * 1000)) {
						// If the user open two chests within 36 hours, add a multiplier
						miner.addChestMultiplier();
					} else {
						// Otherwise, put it to 0
						miner.setChestMultiplier(0);
					}

					int emeralds = rnd.nextInt(5 - 1) + 1;
					miner.getInv().addEmerald(emeralds + miner.getChestMultiplier());
					miner.getStats().put("emerald",
							miner.getStats().getInt("emerald") + emeralds + miner.getChestMultiplier());
					embed.addField("You opened your chest!",
							"Reward: " + (emeralds + miner.getChestMultiplier()) + " " + Assets.EMERALD.getValue(),
							true);
					embed.addField("Chest Multiplier", String.valueOf(miner.getChestMultiplier()), true);
				} else if (reward == 2) { // Redstone as reward
					if (System.currentTimeMillis() <= miner.getChest() + (36 * 60 * 60 * 1000)) {
						// If the user open two chests within 36 hours, add a multiplier
						miner.addChestMultiplier();
					} else {
						// Otherwise, put it to 0
						miner.setChestMultiplier(0);
					}

					int redstone = rnd.nextInt(10 - 1) + 1;
					miner.getInv().addRedstone(redstone + miner.getChestMultiplier());
					miner.getStats().put("redstone",
							miner.getStats().getInt("redstone") + redstone + miner.getChestMultiplier());
					embed.addField("You opened your chest!",
							"Reward: " + (redstone + miner.getChestMultiplier()) + " " + Assets.REDSTONE.getValue(),
							true);
					embed.addField("Chest Multiplier", String.valueOf(miner.getChestMultiplier()), true);
				} else if (reward == 3) { // Lapis Lazuli as reward
					if (System.currentTimeMillis() <= miner.getChest() + (36 * 60 * 60 * 1000)) {
						// If the user open two chests within 36 hours, add a multiplier
						miner.addChestMultiplier();
					} else {
						// Otherwise, put it to 0
						miner.setChestMultiplier(0);
					}

					int lapis = rnd.nextInt(7 - 1) + 1;
					miner.getInv().addLapis(lapis + miner.getChestMultiplier());
					miner.getStats().put("lapis",
							miner.getStats().getInt("lapis") + lapis + miner.getChestMultiplier());
					embed.addField("You opened your chest!",
							"Reward: " + (lapis + miner.getChestMultiplier()) + " " + Assets.LAPIS.getValue(), true);
					embed.addField("Chest Multiplier", String.valueOf(miner.getChestMultiplier()), true);
				}

				miner.getAdv().addChest();
				int chest = miner.getAdv().getChest();

				if (chest == 14) {
					embed.addField("Advancement get!", "Open 14 chests\n+5 " + Assets.EMERALD.getValue(), true);
					miner.getInv().addEmerald(5);
				} else if (chest == 30) {
					embed.addField("Advancement get!", "Open 30 chests\n+10 " + Assets.EMERALD.getValue(), true);
					miner.getInv().addEmerald(10);
				} else if (chest == 60) {
					embed.addField("Advancement get!", "Open 60 chests\n+15 " + Assets.EMERALD.getValue(), true);
					miner.getInv().addEmerald(15);
				} else if (chest == 120) {
					embed.addField("Advancement get!", "Open 120 chests\n+20 " + Assets.EMERALD.getValue(), true);
					miner.getInv().addEmerald(20);
				}

				event.reply(embed.build());
				miner.setChest(System.currentTimeMillis());
				miner.update();
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(miner.getChest() + (24 * 60 * 60 * 1000) - System.currentTimeMillis());
				cal.setTimeZone(TimeZone.getTimeZone("UTC"));
				String until = cal.get(Calendar.HOUR_OF_DAY) + ":" + zero(cal.get(Calendar.MINUTE)) + ":"
						+ zero(cal.get(Calendar.SECOND));

				event.reply(":x: | You can open your chest in **" + until + "**.");
			}
		}
	}

	private String zero(int i) {
		return (i < 10 ? "0" + i : String.valueOf(i));
	}

}
