package fr.glowning.discordminer.cmd;

import java.util.Random;

import fr.glowning.discordminer.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class VerifyCommand {

	public VerifyCommand(CommandEvent event, Main main) {
		User u = event.getUser();

		if (main.getCaptcha().containsKey(u.getIdLong())) {
			if (main.getMines().get(u.getIdLong()) <= 110) {
				if (event.getArgs()[0].equals(String.valueOf(main.getCaptcha().get(u.getIdLong())))) {
					event.reply("Okay, you're not a bot. Have fun playing!");
					main.getMines().remove(u.getIdLong());
					main.getCaptcha().remove(u.getIdLong());
				}
			} else {
				Random rnd = new Random();
				int captcha = rnd.nextInt(9999 - 1000) + 1000;

				main.getCaptcha().put(u.getIdLong(), captcha);

				EmbedBuilder embed = new EmbedBuilder();
				embed.setAuthor(u.getName(), null, u.getAvatarUrl());
				embed.setColor(event.getMember().getColor());
				embed.addField("Wrong code!",
						"Please, do `m!verify <code>` where the code is\nthe number displayed in the image.", true);
				embed.setImage("https://discordminer.com/webhook/image.php?code=" + captcha);
				event.reply(embed.build());

				main.getMines().put(u.getIdLong(), main.getMines().get(u.getIdLong()) + 1);
			}
		}
	}

}
