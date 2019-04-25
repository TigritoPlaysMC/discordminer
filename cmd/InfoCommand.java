package fr.glowning.discordminer.cmd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.TimeZone;

import fr.glowning.discordminer.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

public class InfoCommand {

	public InfoCommand(CommandEvent event) {
		long members = 0, players = 0;

		for (Guild g : Main.getShards().getGuilds()) {
			members += g.getMembers().size();
		}

		try {
			Connection conn = Main.getConnection();
			Statement state = conn.createStatement();

			ResultSet rs = state.executeQuery("SELECT * FROM miners");
			while (rs.next())
				players++;
		} catch (SQLException ex) {
		}

		EmbedBuilder embed = new EmbedBuilder();

		embed.setAuthor(event.getGuild().getJDA().getSelfUser().getName(), "https://glowning.dev/discordminer",
				event.getGuild().getJDA().getSelfUser().getAvatarUrl());
		embed.setColor(event.getMember().getColor());

		embed.addField("Developer", "@Glowning#0806", true);
		embed.addField("Dev. language", "Java", true);
		embed.addField("Library", "JDA", true);

		embed.addField("Servers", String.valueOf(Main.getShards().getGuilds().size()), true);
		embed.addField("Total members", String.valueOf(members), true);
		embed.addField("Registered members", String.valueOf(players), true);

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(System.currentTimeMillis() - Main.uptime);

		String uptime = (cal.get(Calendar.MONTH) > 0 ? cal.get(Calendar.MONTH) + "m" : "")
				+ (cal.get(Calendar.DAY_OF_MONTH) > 1 ? (cal.get(Calendar.DAY_OF_MONTH) - 1) + "d" : "")
				+ (cal.get(Calendar.HOUR_OF_DAY) > 0 ? cal.get(Calendar.HOUR_OF_DAY) + "h" : "")
				+ cal.get(Calendar.MINUTE) + "m" + zero(cal.get(Calendar.SECOND)) + "s";

		long ram = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		embed.addField("Uptime", uptime, true);
		embed.addField(
				"Current shard", event.getGuild().getJDA().getShardInfo().getShardId() + " / "
						+ Main.getShards().getShardsRunning() + " (" + event.getGuild().getJDA().getPing() + " ms)",
				true);
		embed.addField("RAM used", (ram / 1000000) + " MB", true);

		event.reply(embed.build());
	}

	private String zero(int i) {
		return (i < 10 ? "0" + i : String.valueOf(i));
	}
}
