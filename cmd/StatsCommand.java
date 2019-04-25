package fr.glowning.discordminer.cmd;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONObject;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class StatsCommand {

	public StatsCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(u.getName(), null, u.getAvatarUrl());
			embed.setColor(event.getMember().getColor());

			JSONObject stats = miner.getStats();

			StringBuilder ores = new StringBuilder();
			ores.append("Stone mined: " + stats.getLong("stone") + " " + Assets.STONE.getValue() + "\n");
			ores.append("Coal mined: " + stats.getLong("coal") + " " + Assets.COAL.getValue() + "\n");
			ores.append("Iron mined: " + stats.getLong("iron") + " " + Assets.IRON.getValue() + "\n");
			ores.append("Gold mined: " + stats.getLong("gold") + " " + Assets.GOLD.getValue() + "\n");
			ores.append("Total Lapis Lazuli: " + stats.getLong("lapis") + " " + Assets.LAPIS.getValue() + "\n");
			ores.append("Total Redstone: " + stats.getLong("redstone") + " " + Assets.REDSTONE.getValue() + "\n");
			ores.append("Obsidian mined: " + stats.getLong("obsidian") + " " + Assets.OBSIDIAN.getValue() + "\n");
			ores.append("Diamond mined: " + stats.getLong("diamond") + " " + Assets.DIAMOND.getValue() + "\n");
			ores.append("Total Emeralds: " + stats.getLong("emerald") + " " + Assets.EMERALD.getValue());

			StringBuilder other = new StringBuilder();
			other.append("Join date: " + joinDate(miner.getJoinDate()) + "\n");
			other.append("Pickaxe owned: " + stats.getInt("pickaxe_owned") + "\n");
			other.append("Total votes: " + stats.getInt("votes") + "\n");
			other.append("Mining chest found: " + stats.getInt("chest_found"));

			embed.addField("Resources stats", ores.toString(), true);
			embed.addField("Other stats", other.toString(), true);
			event.reply(embed.build());

		}
	}

	private String joinDate(long joinDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(joinDate);
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));

		String date = zero(cal.get(Calendar.DAY_OF_MONTH)) + "/" + zero(cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.YEAR) + " at " + zero(cal.get(Calendar.HOUR_OF_DAY)) + ":"
				+ zero(cal.get(Calendar.MINUTE)) + ":" + zero(cal.get(Calendar.SECOND));

		return date;
	}

	private String zero(int i) {
		return (i < 10 ? "0" + i : String.valueOf(i));
	}

}
