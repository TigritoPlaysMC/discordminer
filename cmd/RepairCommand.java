package fr.glowning.discordminer.cmd;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Inventory;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class RepairCommand {

	public RepairCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(u.getName(), null, u.getAvatarUrl());
			embed.setColor(event.getMember().getColor());

			if (miner.isBroken(miner.getPickaxe())) {
				Inventory inv = miner.getInv();

				switch (miner.getPickaxe()) {
					case "wooden": {
						if (inv.getCoal() >= 100) {
							inv.addCoal(-100);
							miner.setDurability("wooden", 500);
							embed.addField("Pickaxe repaired!", "It costed 100 " + Assets.COAL.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 100 " + Assets.COAL.getValue(), true);
						}

						break;
					}

					case "stone": {
						if (inv.getStone() >= 700) {
							inv.addStone(-700);
							miner.setDurability("stone", 1350);
							embed.addField("Pickaxe repaired!", "It costed 700 " + Assets.STONE.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 700 " + Assets.STONE.getValue(), true);
						}

						break;
					}

					case "gold": {
						if (inv.getCoal() >= 750 && inv.getGold() >= 100) {
							inv.addCoal(-750);
							inv.addGold(-100);
							miner.setDurability("gold", 3500);
							embed.addField("Pickaxe repaired!", "It costed 750 " + Assets.COAL.getValue() + " and 100 " + Assets.GOLD.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 750 " + Assets.COAL.getValue() + " and 100 " + Assets.GOLD.getValue(), true);
						}

						break;
					}

					case "iron": {
						if (inv.getIron() >= 300) {
							inv.addIron(-300);
							miner.setDurability("iron", 8000);
							embed.addField("Pickaxe repaired!", "It costed 300 " + Assets.IRON.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 300 " + Assets.IRON.getValue(), true);
						}

						break;
					}

					case "diamond": {
						if (inv.getDiamond() >= 350) {
							inv.addDiamond(-350);
							miner.setDurability("diamond", 15000);
							embed.addField("Pickaxe repaired!", "It costed 350 " + Assets.DIAMOND.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 350 " + Assets.DIAMOND.getValue(), true);
						}

						break;
					}

					case "emerald": {
						if (inv.getDiamond() >= 2500) {
							inv.addDiamond(-2500);
							miner.setDurability("emerald", 50000);
							embed.addField("Pickaxe repaired!", "It costed 2500 " + Assets.DIAMOND.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 2500 " + Assets.DIAMOND.getValue(), true);
						}

						break;
					}

					case "ruby": {
						if (inv.getDiamond() >= 5000) {
							inv.addDiamond(-5000);
							miner.setDurability("ruby", 75000);
							embed.addField("Pickaxe repaired!", "It costed 5000 " + Assets.DIAMOND.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 5000 " + Assets.DIAMOND.getValue(), true);
						}

						break;
					}

					case "ultimate": {
						if (inv.getObsidian() >= 3500 && inv.getDiamond() >= 8000) {
							inv.addObsidian(-3500);
							inv.addDiamond(-8000);
							miner.setDurability("ultimate", 150000);
							embed.addField("Pickaxe repaired!", "It costed 3500 " + Assets.OBSIDIAN.getValue() + " and 8000 " + Assets.DIAMOND.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 3500 " + Assets.OBSIDIAN.getValue() + " and 8000 " + Assets.DIAMOND.getValue(), true);
						}

						break;
					}

					case "crate": {
						if (inv.getDiamond() >= 1000) {
							inv.addDiamond(-1000);
							miner.setDurability("crate", 30000);
							embed.addField("Pickaxe repaired!", "It costed 1000 " + Assets.DIAMOND.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 1000 " + Assets.DIAMOND.getValue(), true);
						}

						break;
					}

					case "donator": {
						if (inv.getDiamond() >= 2500) {
							inv.addDiamond(-2500);
							miner.setDurability("donator", 50000);
							embed.addField("Pickaxe repaired!", "It costed 2500 " + Assets.DIAMOND.getValue(), true);
						} else {
							embed.addField("Not enough resources!", "This reparation costs 2500 " + Assets.DIAMOND.getValue(), true);
						}

						break;
					}
				}

				event.reply(embed.build());
				miner.update();
			} else {
				event.reply("Your pickaxe isn't broken!");
			}
		}
	}

}
