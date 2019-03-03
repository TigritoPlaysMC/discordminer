package fr.glowning.discordminer.cmd;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Miner;
import fr.glowning.discordminer.shop.SBonus;
import fr.glowning.discordminer.shop.SEnchant;
import fr.glowning.discordminer.shop.SPickaxe;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

public class ShopCommand {

	public ShopCommand(CommandEvent event) {
		User u = event.getUser();
		Miner miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			if (event.getArgs().length == 1) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setAuthor(u.getName(), null, u.getAvatarUrl());
				embed.setColor(event.getMember().getColor());

				if (event.getArgs()[0].equalsIgnoreCase("pickaxe")) {
					SPickaxe spick = new SPickaxe();
					embed.addField("Pickaxes Shop", spick.send(), true);
					embed.setFooter("To purchase a pickaxe, type » m!shop pickaxe <id>", null);
				}

				else if (event.getArgs()[0].equalsIgnoreCase("enchant")) {
					SEnchant sench = new SEnchant(miner);
					embed.addField("Enchantments Shop", sench.send(), true);
					embed.setFooter("To purchase an enchantment, type » m!shop enchant <id>", null);
				}

				else if (event.getArgs()[0].equalsIgnoreCase("bonus")) {
					SBonus sbonus = new SBonus();
					embed.addField("Bonuses Shop", sbonus.send(), true);
					embed.setFooter("To purchase an enchantment, type » m!shop bonus <id>", null);
				}

				else {
					embed.addField("This shop doesn't exist!",
							"To see the different shops, type `m!shop pickaxe`, `m!shop enchant` or `m!shop bonus`.",
							true);
				}

				event.reply(embed.build());
			} else if (event.getArgs().length == 2) {
				if (event.getArgs()[0].equalsIgnoreCase("pickaxe")) {
					switch (event.getArgs()[1]) {
						case "1": {
							if (miner.getInv().getEmerald() >= 375) {
								miner.setDurability("emerald", 50000);
								miner.setPickaxe("emerald");

								JSONObject object = new JSONObject();
								object.put("efficiency", 0);
								object.put("unbreaking", 0);
								object.put("fortune", 0);
								object.put("luck", 0);
								miner.getFullEnchant().put("emerald", object);

								event.reply(u.getAsMention() + " » You successfully purchased the Emerald Pickaxe "
										+ Assets.PEMERALD.getValue());

								miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
								miner.getInv().addEmerald(-375);
								miner.update();
							} else {
								event.reply("You don't have enough resources (375 " + Assets.EMERALD.getValue()
										+ " required)");
							}

							break;
						}

						case "2": {
							if (miner.getInv().getEmerald() >= 600) {
								miner.setDurability("ruby", 75000);
								miner.setPickaxe("ruby");

								JSONObject object = new JSONObject();
								object.put("efficiency", 0);
								object.put("unbreaking", 0);
								object.put("fortune", 0);
								object.put("luck", 0);
								miner.getFullEnchant().put("ruby", object);

								event.reply(u.getAsMention() + " » You successfully purchased the Ruby Pickaxe "
										+ Assets.PRUBY.getValue());

								miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
								miner.getInv().addEmerald(-600);
								miner.update();
							} else {
								event.reply("You don't have enough resources (600 " + Assets.EMERALD.getValue()
										+ " required)");
							}

							break;
						}

						case "3": {
							if (miner.getInv().getEmerald() >= 1000) {
								miner.setDurability("ultimate", 150000);
								miner.setPickaxe("ultimate");

								JSONObject object = new JSONObject();
								object.put("efficiency", 0);
								object.put("unbreaking", 0);
								object.put("fortune", 0);
								object.put("luck", 0);
								miner.getFullEnchant().put("ultimate", object);

								event.reply(u.getAsMention() + " » You successfully purchased the Ultimate Pickaxe "
										+ Assets.PULTIMATE.getValue());

								miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
								miner.getInv().addEmerald(-1000);
								miner.update();
							} else {
								event.reply("You don't have enough resources (1000 " + Assets.EMERALD.getValue()
										+ " required)");
							}
						}
					}
				}

				if (event.getArgs()[0].equalsIgnoreCase("enchant")) {
					int level, price;

					switch (event.getArgs()[1]) {
						case "1": {
							level = miner.getEnchant().getUnbreaking();
							if (level < 5) {
								price = new SEnchant(miner).getPrice(level);

								if (miner.getInv().getLapis() >= price) {
									event.reply(u.getAsMention()
											+ " » You successfully purchased the enchantment Unbreaking " + (level + 1)
											+ "!");

									miner.getInv().addLapis(-price);
									miner.getEnchant().setUnbreaking(level + 1);
									miner.update();
								} else {
									event.reply("You don't have enough resources (" + price + " "
											+ Assets.LAPIS.getValue() + " required)");
								}
							} else {
								event.reply(u.getAsMention()
										+ " » You already purchased the max level for this enchantment.");
							}

							break;
						}

						case "2": {
							level = miner.getEnchant().getEfficiency();
							if (level < 5) {
								price = new SEnchant(miner).getPrice(level);

								if (miner.getInv().getLapis() >= price) {
									event.reply(u.getAsMention()
											+ " » You successfully purchased the enchantment Efficiency " + (level + 1)
											+ "!");

									miner.getInv().addLapis(-price);
									miner.getEnchant().setEfficiency(level + 1);
									miner.update();
								} else {
									event.reply("You don't have enough resources (" + price + " "
											+ Assets.LAPIS.getValue() + " required)");
								}
							} else {
								event.reply(u.getAsMention()
										+ " » You already purchased the max level for this enchantment.");
							}

							break;
						}

						case "3": {
							level = miner.getEnchant().getFortune();
							if (level < 3) {
								price = new SEnchant(miner).getPrice(level);

								if (miner.getInv().getLapis() >= price) {
									event.reply(
											u.getAsMention() + " » You successfully purchased the enchantment Fortune "
													+ (level + 1) + "!");

									miner.getInv().addLapis(-price);
									miner.getEnchant().setFortune(level + 1);
									miner.update();
								} else {
									event.reply("You don't have enough resources (" + price + " "
											+ Assets.LAPIS.getValue() + " required)");
								}
							} else {
								event.reply(u.getAsMention()
										+ " » You already purchased the max level for this enchantment.");
							}

							break;
						}

						case "4": {
							level = miner.getEnchant().getLuck();
							if (level < 3) {
								price = new SEnchant(miner).getPrice(level);

								if (miner.getInv().getLapis() >= price) {
									event.reply(u.getAsMention() + " » You successfully purchased the enchantment Luck "
											+ (level + 1) + "!");

									miner.getInv().addLapis(-price);
									miner.getEnchant().setLuck(level + 1);
									miner.update();
								} else {
									event.reply("You don't have enough resources (" + price + " "
											+ Assets.LAPIS.getValue() + " required)");
								}
							} else {
								event.reply(u.getAsMention()
										+ " » You already purchased the max level for this enchantment.");
							}

							break;
						}
					}
				}

				if (event.getArgs()[0].equalsIgnoreCase("bonus")) {
					switch (event.getArgs()[1]) {
						case "1": {
							if (miner.getBonus().getType().equals("None")) {
								if (miner.getInv().getRedstone() >= 250) {
									miner.getInv().addRedstone(-250);
									miner.getBonus().setType("auto");
									miner.getBonus().setContent(System.currentTimeMillis() + (30 * 60 * 1000));
									event.reply(u.getAsMention()
											+ " » You succesfully purchased an Auto-Mine. Come back in 30 minutes!");

									checkAdvancement(event, miner);

									new Timer().schedule(new TimerTask() {

										@Override
										public void run() {
											event.reply(u.getAsMention() + " » Your Auto-Mine is over.");
											new MineCommand(u).doMine(event, 30);
										}
									}, 30 * 60 * 1000);
								}
							}

							break;
						}

						case "2": {
							if (miner.getBonus().getType().equals("None")) {
								if (miner.getInv().getRedstone() >= 400) {
									miner.getInv().addRedstone(-400);
									miner.getBonus().setType("auto");
									miner.getBonus().setContent(System.currentTimeMillis() + (120 * 60 * 1000));
									event.reply(u.getAsMention()
											+ " » You succesfully purchased an Auto-Mine. Come back in 2 hours!");

									checkAdvancement(event, miner);

									new Timer().schedule(new TimerTask() {

										@Override
										public void run() {
											event.reply(u.getAsMention() + " » Your Auto-Mine is over.");
											try {
												new MineCommand(u).doMine(event, 120);
											} catch (NullPointerException e) {
												e.printStackTrace();
											}
										}
									}, 120 * 60 * 1000);
								}
							}

							break;
						}

						case "3": {
							if (miner.getBonus().getType().equals("None")) {
								if (miner.getInv().getRedstone() >= 150) {
									miner.getInv().addRedstone(-150);
									miner.getBonus().setType("unbreakable");
									miner.getBonus().setContent(50);
									event.reply(u.getAsMention()
											+ " » You succesfully purchased an Unbreakable Pickaxe which has 50 uses.");

									checkAdvancement(event, miner);
								}
							}

							break;
						}

						case "4": {
							if (miner.getBonus().getType().equals("None")) {
								if (miner.getInv().getRedstone() >= 200) {
									miner.getInv().addRedstone(-200);
									miner.getBonus().setType("unbreakable");
									miner.getBonus().setContent(100);
									event.reply(u.getAsMention()
											+ " » You succesfully purchased an Unbreakable Pickaxe which has 100 uses.");

									checkAdvancement(event, miner);
								}
							}

							break;
						}

					}

					miner.update();
				}
			} else {
				event.reply("To see the different shops, type `m!shop pickaxe`, `m!shop enchant` or `m!shop bonus`.");
				return;
			}
		}
	}

	private void checkAdvancement(CommandEvent event, Miner miner) {
		miner.getAdv().addBonus();
		int bonus = miner.getAdv().getBonus();

		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl());
		embed.setColor(event.getMember().getColor());

		if (bonus == 35) {
			embed.addField("Advancement get!", "Purchase 35 bonuses\n+5 " + Assets.EMERALD.getValue(), true);
			miner.getInv().addEmerald(5);
		} else if (bonus == 100) {
			embed.addField("Advancement get!", "Purchase 100 bonuses\n+10 " + Assets.EMERALD.getValue(), true);
			miner.getInv().addEmerald(10);
		} else if (bonus == 250) {
			embed.addField("Advancement get!", "Purchase 250 bonuses\n+15 " + Assets.EMERALD.getValue(), true);
			miner.getInv().addEmerald(15);
		} else if (bonus == 500) {
			embed.addField("Advancement get!", "Purchase 500 bonuses\n+20 " + Assets.EMERALD.getValue(), true);
			miner.getInv().addEmerald(20);
		}

		if (!embed.isEmpty())
			event.reply(embed.build());
	}

}
