package fr.glowning.discordminer.cmd;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import fr.glowning.discordminer.Main;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.entities.User;

public class CrateCommand {
	CommandEvent event;
	Main main;
	User u;
	Miner miner;

	public CrateCommand(CommandEvent event, Main main) {
		this.event = event;
		this.main = main;
		this.u = event.getUser();
		this.miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			if (event.getArgs().length != 1) {
				StringBuilder sb = new StringBuilder();
				sb.append("Please, specify what type of crate you want to open.\n");
				sb.append("Here is your inventory:\n");
				sb.append("\t**" + miner.getCrates("vote") + "** Voting Crates (`m!crate vote`)\n");
				sb.append("\t**" + miner.getCrates("donator") + "** Donator Crates (`m!crate donator`)\n");
				sb.append("\t**" + miner.getCrates("shop") + "** Purchased Crates (`m!crate shop`)");

				event.reply(sb.toString());
			} else {
				Random rnd = new Random();
				int luck = rnd.nextInt(100 - 1) + 1;

				if (event.getArgs()[0].equalsIgnoreCase("vote")) {
					if ((int) miner.getCrates("vote") > 0) {
						openVotingCrate(luck);
					} else {
						event.reply("You don't have any crate of this type.");
					}
				} else if (event.getArgs()[0].equalsIgnoreCase("donator")) {
					if ((int) miner.getCrates("donator") > 0) {
						openDonatorCrate(luck);
					} else {
						event.reply("You don't have any crate of this type.");
					}
				}
			}
		}
	}

	private void openVotingCrate(int luck) {
		miner.addCrates("vote", -1);
		boolean ok = false;

		while (!ok) {
			if (luck <= 15) {
				// 200 redstones
				miner.getInv().addRedstone(200);
				event.reply("You won **200 Redstone** in your crate!");
				miner.update();
				ok = true;
			} else if (luck <= 25) {
				// 40 lapis
				miner.getInv().addLapis(40);
				event.reply("You won **40 Lapis Lazuli** in your crate!");
				miner.update();
				ok = true;
			} else if (luck <= 26) {
				// Crate Pickaxe
				if (!miner.has("crate")) {
					miner.setDurability("crate", 30000);
					miner.setPickaxe("crate");

					JSONObject enchant = new JSONObject();
					enchant.put("efficiency", 0);
					enchant.put("unbreaking", 0);
					enchant.put("fortune", 0);
					enchant.put("luck", 0);
					miner.getFullEnchant().put("crate", enchant);

					miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
				}

				event.reply("**LUCKY ONE!** You won the **Crate Pickaxe**!");
				miner.update();
				ok = true;
			} else if (luck <= 31) {
				// 120 mines
				event.reply("Wow! You won an **instant 2h Auto-Mine** in your crate!");
				new MineCommand(u).doMine(event, 120);
				ok = true;
			} else if (luck <= 33) {
				// 5h auto mine
				if (miner.getBonus().getType().equals("None")) {
					miner.getBonus().setType("auto");
					miner.getBonus().setContent(System.currentTimeMillis() + (300 * 60 * 1000));
					event.reply("**LUCKY ONE!** You won a **5h Auto-Mine** in your crate!");
					miner.update();

					new Timer().schedule(new TimerTask() {

						@Override
						public void run() {
							event.reply(u.getAsMention() + " » Your Auto-Mine is over.");
							new MineCommand(u).doMine(event, 300);
						}
					}, 300 * 60 * 1000);

					ok = true;
				} else {
					// The user already has a bonus, reroll the crate
					Random rnd = new Random();
					luck = rnd.nextInt(100 - 1) + 1;
					ok = false;
				}
			} else if (luck <= 70) {
				// 10 Emeralds
				miner.getInv().addEmerald(10);
				event.reply("You won **10 Emeralds** in your crate!");
				miner.update();
				ok = true;
			} else if (luck <= 90) {
				// 2 voting crates
				miner.addCrates("vote", 2);
				event.reply("Bonus Crate! You won **2 Crates** in your crate!");
				miner.update();
				ok = true;
			} else if (luck <= 100) {
				// Temp XP boost
				Random rnd = new Random();
				luck = rnd.nextInt(100 - 1) + 1;
				ok = false;
			}
		}
	}

	private void openDonatorCrate(int luck) {
		miner.addCrates("donator", -1);

		if (luck <= 15) {
			// 400 redstones
			miner.getInv().addRedstone(400);
			event.reply("You won **400 Redstone** in your crate!");
			miner.update();
		} else if (luck <= 25) {
			// 80 lapis
			miner.getInv().addLapis(80);
			event.reply("You won **80 Lapis Lazuli** in your crate!");
			miner.update();
		} else if (luck <= 26) {
			// Crate Pickaxe
			if (!miner.has("crate")) {
				miner.setDurability("crate", 30000);
				miner.setPickaxe("crate");

				JSONObject enchant = new JSONObject();
				enchant.put("efficiency", 0);
				enchant.put("unbreaking", 0);
				enchant.put("fortune", 0);
				enchant.put("luck", 0);
				miner.getFullEnchant().put("crate", enchant);

				miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
			}

			event.reply("**LUCKY ONE!** You won the **Crate Pickaxe**!");
			miner.update();
		} else if (luck <= 31) {
			// 120 mines
			event.reply("Wow! You won an **instant 5h Auto-Mine** in your crate!");
			new MineCommand(u).doMine(event, 300);
		} else if (luck <= 60) {
			// Reroll
			Random rnd = new Random();
			luck = rnd.nextInt(100 - 1) + 1;
			openDonatorCrate(luck);
		} else if (luck <= 90) {
			// 10 Emeralds
			miner.getInv().addEmerald(30);
			event.reply("You won **30 Emeralds** in your crate!");
			miner.update();
		} else if (luck <= 98) {
			// 2 voting crates
			miner.addCrates("donator", 2);
			event.reply("Bonus Crate! You won **2 Crates** in your crate!");
			miner.update();
		} else if (luck == 99) {
			// Ultimate Pickaxe
			if (!miner.has("ultimate")) {
				miner.setDurability("ultimate", 150000);
				miner.setPickaxe("ultimate");

				JSONObject enchant = new JSONObject();
				enchant.put("efficiency", 0);
				enchant.put("unbreaking", 0);
				enchant.put("fortune", 0);
				enchant.put("luck", 0);
				miner.getFullEnchant().put("ultimate", enchant);

				miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
			}

			event.reply("**LUCKY ONE!** You won the **Ultimate Pickaxe**!");
			miner.update();
		} else if (luck <= 100) {
			// Reroll
			Random rnd = new Random();
			luck = rnd.nextInt(100 - 1) + 1;
			openDonatorCrate(luck);
		}
	}

}
