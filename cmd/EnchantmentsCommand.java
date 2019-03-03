package fr.glowning.discordminer.cmd;

import fr.glowning.discordminer.entity.Enchantments;
import fr.glowning.discordminer.entity.Miner;
import fr.glowning.discordminer.shop.SEnchant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class EnchantmentsCommand {

	public EnchantmentsCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(u.getName(), null, u.getAvatarUrl());
			embed.setColor(event.getMember().getColor());

			Enchantments ench = miner.getEnchant();
			SEnchant sench = new SEnchant(miner);

			StringBuilder sb = new StringBuilder();
			sb.append("Unbreaking " + ench.getUnbreaking() + "\n");
			sb.append("\\* Your pickaxe is more resistant from " + sench.getUnbreakingRatio() + "%");
			sb.append("\n\n");
			sb.append("Efficiency " + ench.getEfficiency() + "\n");
			sb.append("\\* You can mine every " + ((double) (sench.getEfficiencyRatio() / 1000.0)) + " seconds");
			sb.append("\n\n");
			sb.append("Fortune " + ench.getFortune() + "\n");
			sb.append("\\* Your pickaxe collects " + sench.getFortuneRatio() + " times more resources");
			sb.append("\n\n");
			sb.append("Luck " + ench.getLuck() + "\n");
			sb.append("\\* You have " + sench.getLuckRatio() + "% chance to find a chest while mining");

			embed.addField("Your pickaxe's enchantments", sb.toString(), true);
			event.reply(embed.build());
		}
	}

}
