package fr.glowning.discordminer.cmd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import fr.glowning.discordminer.Main;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class TopCommand {

	public TopCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			if (event.getArgs().length >= 1) {
				HashMap<Long, Long> map = new HashMap<Long, Long>();
				int top = 25, place = 0;

				if (event.getArgs().length == 2) {
					try {
						top = Integer.parseInt(event.getArgs()[1]);
					} catch (NumberFormatException e) {
						top = 25;
					}
				}

				if (top > 25)
					top = 25;

				if (top < 1)
					top = 1;

				// Checking which top to see
				if (event.getArgs()[0].equalsIgnoreCase("emerald") || event.getArgs()[0].equalsIgnoreCase("emeralds")) {
					try {
						Connection conn = Main.getConnection();
						Statement state = conn.createStatement();

						ResultSet rs = state.executeQuery("SELECT * FROM miners");
						while (rs.next()) {
							try {
								Miner tminer = new Miner(rs.getLong("id"));
								map.put(rs.getLong("id"), tminer.getInv().getEmerald());
							} catch (NullPointerException e) {
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (event.getArgs()[0].equalsIgnoreCase("level")) {
					try {
						Connection conn = Main.getConnection();
						Statement state = conn.createStatement();

						ResultSet rs = state.executeQuery("SELECT * FROM miners");
						while (rs.next()) {
							Miner tminer = new Miner(rs.getLong("id"));
							map.put(rs.getLong("id"), (long) tminer.getLevel());
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					event.reply("Correct usage: `m!top <level / emeralds> [1 - 25]`");
					return;
				}

				// Order the HashMap
				Map<Long, Long> sorted = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

				// Displaying the values
				StringBuilder sb = new StringBuilder();
				int i = 1;
				for (Map.Entry<Long, Long> a : sorted.entrySet()) {
					if (i <= top) {
						try {
							String name = Main.getShards().getUserById(a.getKey()).getName().replace("`", "");
							sb.append(i + ". `" + name + "` - " + displayStat(a.getValue(), event.getArgs()[0]) + "\n");
						} catch (NullPointerException e) {
						}
					}

					if (a.getKey() == u.getIdLong())
						place = i;

					i++;
				}

				// Sending message
				EmbedBuilder embed = new EmbedBuilder();
				embed.setAuthor("Top Command", null, event.getGuild().getSelfMember().getUser().getAvatarUrl());
				embed.setColor(event.getMember().getColor());
				embed.setDescription(sb.toString());
				embed.setFooter("Your place: " + place + " / " + i, u.getAvatarUrl());
				event.reply(embed.build());
			} else {
				event.reply("Correct usage: `m!top <level / emeralds> [1 - 25]`");
				return;
			}
		}
	}

	private String displayStat(Long l, String args) {
		if (args.equalsIgnoreCase("emerald") || args.equalsIgnoreCase("emeralds")) {
			return "**" + l + "** Emeralds";
		} else if (args.equalsIgnoreCase("level")) {
			return "Level **" + l + "**";
		}

		return null;
	}

}
