package fr.glowning.discordminer.cmd;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Miner;
import fr.glowning.discordminer.items.Pickaxe;
import net.dv8tion.jda.core.entities.User;

public class PickaxeCommand {
	private Miner miner;

	public PickaxeCommand(CommandEvent event) {
		User u = event.getUser();
		miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			if (event.getArgs().length == 1) {
				String pick = event.getArgs()[0];

				if ((pick.equalsIgnoreCase("wooden") || pick.equalsIgnoreCase("stone") || pick.equalsIgnoreCase("gold") || pick.equalsIgnoreCase("iron") || pick.equalsIgnoreCase("diamond") || pick.equalsIgnoreCase("emerald") || pick.equalsIgnoreCase("ruby") || pick.equalsIgnoreCase("ultimate") || pick.equalsIgnoreCase("crate") || pick.equalsIgnoreCase("donator")) && hasPickaxe(pick)) {
					Pickaxe pickaxe = new Pickaxe(pick);
					event.reply("You equipped a new pickaxe: " + pickaxe.getAppearance());
					miner.setPickaxe(pick);
					miner.update();
				}

			} else {
				StringBuilder sb = new StringBuilder();

				sb.append("**Level Pickaxes**\n");
				sb.append("\t" + Assets.PWOOD.getValue() + " " + Assets.GREEN.getValue() + " `m!pickaxe wooden`\n");
				sb.append("\t" + Assets.PSTONE.getValue() + " " + (hasPickaxe("stone") ? Assets.GREEN.getValue() + " `m!pickaxe stone`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PGOLD.getValue() + " " + (hasPickaxe("gold") ? Assets.GREEN.getValue() + " `m!pickaxe gold`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PIRON.getValue() + " " + (hasPickaxe("iron") ? Assets.GREEN.getValue() + " `m!pickaxe iron`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PDIAMOND.getValue() + " " + (hasPickaxe("diamond") ? Assets.GREEN.getValue() + " `m!pickaxe diamond`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PSAPPHIRE.getValue() + " " + (hasPickaxe("sapphire") ? Assets.GREEN.getValue() + " `m!pickaxe sapphire`" : Assets.RED.getValue()));

				sb.append("\n\n");

				sb.append("**Purchasable Pickaxes**\n");
				sb.append("\t" + Assets.PEMERALD.getValue() + " " + (hasPickaxe("emerald") ? Assets.GREEN.getValue() + " `m!pickaxe emerald`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PRUBY.getValue() + " " + (hasPickaxe("ruby") ? Assets.GREEN.getValue() + " `m!pickaxe ruby`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PULTIMATE.getValue() + " " + (hasPickaxe("ultimate") ? Assets.GREEN.getValue() + " `m!pickaxe ultimate`" : Assets.RED.getValue()));

				sb.append("\n\n");

				sb.append("**Special Pickaxes**\n");
				sb.append("\t" + Assets.PDONATOR.getValue() + " " + (hasPickaxe("donator") ? Assets.GREEN.getValue() + " `m!pickaxe donator`" : Assets.RED.getValue()) + "\n");
				sb.append("\t" + Assets.PCRATE.getValue() + " " + (hasPickaxe("crate") ? Assets.GREEN.getValue() + " `m!pickaxe crate`" : Assets.RED.getValue()) + "\n");

				event.reply(sb.toString());
			}
		}
	}

	private boolean hasPickaxe(String pick) {
		switch (pick) {
			case "wooden":
				return true;

			case "stone": {
				if (miner.getLevel() >= 5)
					return true;
				else
					return false;
			}

			case "gold": {
				if (miner.getLevel() >= 20)
					return true;
				else
					return false;
			}

			case "iron": {
				if (miner.getLevel() >= 50)
					return true;
				else
					return false;
			}

			case "diamond": {
				if (miner.getLevel() >= 100)
					return true;
				else
					return false;
			}

			case "sapphire": {
				if (miner.getLevel() >= 150)
					return true;
				else
					return false;
			}

			case "emerald": {
				return miner.has("emerald");
			}

			case "ruby": {
				return miner.has("ruby");
			}

			case "ultimate": {
				return miner.has("ultimate");
			}

			case "crate": {
				return miner.has("crate");
			}

			case "donator": {
				return miner.has("donator");
			}
		}
		return false;
	}

}
