package fr.glowning.discordminer.cmd;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class SellCommand {

	public SellCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());
		String[] args = event.getArgs();

		if (miner.exists()) {
			if (args.length > 0) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setAuthor(u.getName(), null, u.getAvatarUrl());
				embed.setColor(event.getMember().getColor());

				if (!args[0].equalsIgnoreCase("all")) {
					long amount = -1;

					if (args.length == 2) {
						try {
							amount = Long.parseLong(args[1]);
							if (amount < 0)
								amount = -1;
						} catch (NumberFormatException e) {
							amount = -1;
						}
					}

					switch (args[0].toLowerCase()) {
						case "stone": {
							if (amount > miner.getInv().getStone() || amount == -1)
								amount = miner.getInv().getStone();

							miner.addMoney(1 * amount);
							miner.getInv().addStone(-amount);
							embed.addField("Your sold your Stone!", amount + " " + Assets.STONE.getValue() + " sold\n+ $" + (1 * amount), true);
							break;
						}

						case "coal": {
							if (amount > miner.getInv().getCoal() || amount == -1)
								amount = miner.getInv().getCoal();

							miner.addMoney(2 * amount);
							miner.getInv().addCoal(-amount);
							embed.addField("Your sold your Coal!", amount + " " + Assets.COAL.getValue() + " sold\n+ $" + (2 * amount), true);
							break;
						}

						case "iron": {
							if (amount > miner.getInv().getIron() || amount == -1)
								amount = miner.getInv().getIron();

							miner.addMoney(4 * amount);
							miner.getInv().addIron(-amount);
							embed.addField("Your sold your Iron!", amount + " " + Assets.IRON.getValue() + " sold\n+ $" + (4 * amount), true);
							break;
						}

						case "gold": {
							if (amount > miner.getInv().getGold() || amount == -1)
								amount = miner.getInv().getGold();

							miner.addMoney(5 * amount);
							miner.getInv().addGold(-amount);
							embed.addField("Your sold your Gold!", amount + " " + Assets.GOLD.getValue() + " sold\n+ $" + (5 * amount), true);
							break;
						}

						case "obsidian": {
							if (amount > miner.getInv().getObsidian() || amount == -1)
								amount = miner.getInv().getObsidian();

							miner.addMoney(7 * amount);
							miner.getInv().addObsidian(-amount);
							embed.addField("Your sold your Obsidian!", amount + " " + Assets.OBSIDIAN.getValue() + " sold\n+ $" + (7 * amount), true);
							break;
						}

						case "diamond": {
							if (amount > miner.getInv().getDiamond() || amount == -1)
								amount = miner.getInv().getDiamond();

							miner.addMoney(10 * amount);
							miner.getInv().addDiamond(-amount);
							embed.addField("Your sold your Diamond!", amount + " " + Assets.DIAMOND.getValue() + " sold\n+ $" + (10 * amount), true);
							break;
						}
					}
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append(miner.getInv().getStone() + " " + Assets.STONE.getValue() + " for $" + (1 * miner.getInv().getStone()) + "\n");
					sb.append(miner.getInv().getCoal() + " " + Assets.COAL.getValue() + " for $" + (2 * miner.getInv().getCoal()) + "\n");
					sb.append(miner.getInv().getIron() + " " + Assets.IRON.getValue() + " for $" + (4 * miner.getInv().getIron()) + "\n");
					sb.append(miner.getInv().getGold() + " " + Assets.GOLD.getValue() + " for $" + (5 * miner.getInv().getGold()) + "\n");
					sb.append(miner.getInv().getObsidian() + " " + Assets.OBSIDIAN.getValue() + " for $" + (7 * miner.getInv().getObsidian()) + "\n");
					sb.append(miner.getInv().getDiamond() + " " + Assets.DIAMOND.getValue() + " for $" + (10 * miner.getInv().getDiamond()) + "\n");

					long total = (1 * miner.getInv().getStone()) + (2 * miner.getInv().getCoal()) + (4 * miner.getInv().getIron()) + (5 * miner.getInv().getGold()) + (7 * miner.getInv().getObsidian()) + (10 * miner.getInv().getDiamond());

					sb.append("Total: $" + total);

					miner.addMoney(total);
					miner.getInv().setStone(0);
					miner.getInv().setCoal(0);
					miner.getInv().setIron(0);
					miner.getInv().setGold(0);
					miner.getInv().setObsidian(0);
					miner.getInv().setDiamond(0);

					embed.addField("Your sold all your resources!", sb.toString(), true);
				}

				event.reply(embed.build());
				miner.update();
			}
		}
	}

}
