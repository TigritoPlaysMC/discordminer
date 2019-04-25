package fr.glowning.discordminer.cmd;

import java.util.Calendar;
import java.util.TimeZone;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Inventory;
import fr.glowning.discordminer.entity.Miner;
import fr.glowning.discordminer.items.Pickaxe;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class InventoryCommand {

	public InventoryCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			Inventory inv = miner.getInv();

			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(u.getName(), null, u.getAvatarUrl());
			embed.setColor(event.getMember().getColor());

			// First field: miner's information
			StringBuilder profile = new StringBuilder();
			profile.append("**Pickaxe:** " + new Pickaxe(miner.getPickaxe()).getAppearance() + "\n");
			profile.append("**Durability:** " + miner.getDurability(miner.getPickaxe()) + "\n");
			profile.append("**Level:** " + miner.getLevel() + "\n");
			profile.append("**XP: **" + miner.getXp() + " / " + miner.getXpMax() + "\n");
			profile.append("**Money:** $" + miner.getMoney());

			if (!miner.getBonus().getType().equals("None")) {
				String type = miner.getBonus().getType();
				profile.append("\n\n");

				if (type.equals("auto")) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(miner.getBonus().getContent() - System.currentTimeMillis());
					cal.setTimeZone(TimeZone.getTimeZone("UTC"));

					int time = cal.get(Calendar.HOUR_OF_DAY) * 2 + cal.get(Calendar.MINUTE);

					profile.append("**Current bonus:**\n");
					profile.append("Auto-Mine\n");
					profile.append(time + " minutes left");
				} else if (type.equals("unbreakable")) {
					profile.append("**Current bonus:**\n");
					profile.append("Unbreakable Pickaxe\n");
					profile.append(miner.getBonus().getContent() + " uses left");
				}
			}

			// Second field: ores
			StringBuilder ores = new StringBuilder();
			ores.append("**Stone:** " + inv.getStone() + " " + Assets.STONE.getValue() + "\n");
			ores.append("**Coal:** " + inv.getCoal() + " " + Assets.COAL.getValue() + "\n");
			ores.append("**Iron:** " + inv.getIron() + " " + Assets.IRON.getValue() + "\n");
			ores.append("**Gold:** " + inv.getGold() + " " + Assets.GOLD.getValue() + "\n");
			ores.append("**Redstone:** " + inv.getRedstone() + " " + Assets.REDSTONE.getValue() + "\n");
			ores.append("**Lapis Lazuli:** " + inv.getLapis() + " " + Assets.LAPIS.getValue() + "\n");
			ores.append("**Obsidian:** " + inv.getObsidian() + " " + Assets.OBSIDIAN.getValue() + "\n");
			ores.append("**Diamond:** " + inv.getDiamond() + " " + Assets.DIAMOND.getValue() + "\n");
			ores.append("**Emerald:** " + inv.getEmerald() + " " + Assets.EMERALD.getValue());

			// Adding fields
			embed.addField("Information", profile.toString(), true);
			embed.addField("Resources", ores.toString(), true);

			// Send message
			event.reply(embed.build());
		} else {
			event.reply("This profile doesn't exist. If you want to see your inventory, do `m!start` first.");
		}
	}

}
