package fr.glowning.discordminer.cmd;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.Main;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class StartCommand {

	public StartCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(event.getUser().getIdLong());

		if (!miner.exists()) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(u.getName(), null, u.getAvatarUrl());
			embed.setColor(event.getMember().getColor());
			embed.setDescription("Welcome to Discord Miner, **" + u.getName() + "**!\n" + "You received your " + Assets.PWOOD.getValue() + "\n" + "You can now type `m!mine` to dive under the rock.");

			event.reply(embed.build());

			// Generate the JSONObjects
			JSONObject durability = new JSONObject();
			durability.put("wooden", 500);

			JSONObject ores = new JSONObject();
			ores.put("stone", 0);
			ores.put("coal", 0);
			ores.put("iron", 0);
			ores.put("gold", 0);
			ores.put("lapis", 0);
			ores.put("redstone", 0);
			ores.put("obsidian", 0);
			ores.put("diamond", 0);
			ores.put("emerald", 0);

			JSONObject enchant = new JSONObject();
			enchant.put("unbreaking", 0);
			enchant.put("efficiency", 0);
			enchant.put("fortune", 0);
			enchant.put("luck", 0);

			JSONObject fullEnchant = new JSONObject();
			fullEnchant.put("wooden", enchant);
			fullEnchant.put("donator", enchant);

			JSONObject advancements = new JSONObject();
			advancements.put("mine", 0);
			advancements.put("chest", 0);
			advancements.put("crate", 0);
			advancements.put("bonus", 0);
			advancements.put("potion", 0);

			JSONObject stats = new JSONObject();
			stats.put("stone", 0);
			stats.put("coal", 0);
			stats.put("iron", 0);
			stats.put("gold", 0);
			stats.put("lapis", 0);
			stats.put("redstone", 0);
			stats.put("obsidian", 0);
			stats.put("diamond", 0);
			stats.put("emerald", 0);
			stats.put("votes", 0);
			stats.put("chest_found", 0);
			stats.put("pickaxe_owned", 1);

			// Send SQL query
			String sql = String.format("INSERT INTO miners VALUES(%d, '%s', %d, 1, 0, 'wooden', '0;0;0', 0, '%s', '%s', '%s', 'None', 0, '%s', '%s', 0, 0)", u.getIdLong(), u.getName().replace("'", ""), System.currentTimeMillis(), durability.toString(), ores.toString(), fullEnchant.toString(), advancements.toString(), stats.toString());

			try {
				Connection conn = Main.getConnection();
				Statement state = conn.createStatement();
				state.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				event.reply("An error occured. Please, join the official server by doing `m!server` and send this message in <#498019529906192385>:\n" + "`SQLException: m!start`");
			}
		}
	}

}