package fr.glowning.discordminer;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.glowning.discordminer.cmd.AdvancementsCommand;
import fr.glowning.discordminer.cmd.CommandEvent;
import fr.glowning.discordminer.cmd.CrateCommand;
import fr.glowning.discordminer.cmd.DailyChestCommand;
import fr.glowning.discordminer.cmd.EnchantmentsCommand;
import fr.glowning.discordminer.cmd.GiveDonatorPickaxeCommand;
import fr.glowning.discordminer.cmd.InfoCommand;
import fr.glowning.discordminer.cmd.InventoryCommand;
import fr.glowning.discordminer.cmd.MineCommand;
import fr.glowning.discordminer.cmd.PickaxeCommand;
import fr.glowning.discordminer.cmd.RepairCommand;
import fr.glowning.discordminer.cmd.ShopCommand;
import fr.glowning.discordminer.cmd.StartCommand;
import fr.glowning.discordminer.cmd.StatsCommand;
import fr.glowning.discordminer.cmd.TopCommand;
import fr.glowning.discordminer.cmd.VerifyCommand;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotEvents extends ListenerAdapter {
	private CommandEvent event;
	private Main main; // Retrieving main class to access methods

	public BotEvents(Main main) {
		this.main = main;
	}

	// Basic events
	public void onReady(ReadyEvent e) {
		ShardManager shard = Main.getShards();

		if (e.getJDA().getShardInfo().getShardId() == shard.getShardsRunning() - 1) {
			Main.uptime = System.currentTimeMillis();

			try {
				Connection conn = Main.getConnection();
				Statement state = conn.createStatement();
				ResultSet rs = state.executeQuery("SELECT * FROM miners WHERE bonus_type LIKE 'auto'");

				while (rs.next()) {
					Miner miner = new Miner(rs.getLong("id"));
					if (miner.exists()) {
						miner.getInv().addRedstone(400);
						miner.getBonus().setType("None");
						miner.update();
					}
				}

				state.close();
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			main.updateGame();
			main.checkVotes();
			System.out.println("Bot enabled!");
		}
	}

	// Commands
	public void onMessageReceived(MessageReceivedEvent e) {
		// Donation received
		if (e.getGuild().getIdLong() == 492972328746090496L) { // Discord Miner server
			if (e.getAuthor().getIdLong() == 502381121607303168L) { // Message from Donohook
				Message msg = e.getMessage();
				MessageEmbed embed = msg.getEmbeds().get(0);

				if (embed.getTitle().equalsIgnoreCase("Donation Received")) {
					User u = null;
					int amount = 0;

					String[] words = embed.getDescription().split(" ");
					for (String word : words) {
						if (word.startsWith("<@") && word.endsWith(">"))
							u = Main.getShards().getUserById(word.replace("<@", "").replace(">", ""));

						if (isAmount(word))
							amount = Integer.parseInt(word);
					}

					if (u != null && amount > 0) {
						try {
							Connection conn = Main.getConnection();
							Statement state = conn.createStatement();

							ResultSet rs = state.executeQuery("SELECT * FROM donators WHERE user_id = " + u.getIdLong());
							if (rs.next()) {
								int total = rs.getInt("amount") + amount;
								int actual = rs.getInt("amount");
								state.executeUpdate("UPDATE donators SET amount = " + total + " WHERE user_id = " + u.getIdLong());

								Miner miner = new Miner(u.getIdLong());

								if (total < 5) {
									// The user already got the first package, don't give anything
									return;
								} else if (total < 10) { // Package 2, between 5 and 9 USD
									// The user has the 1st package, give 5 crates and 75 emeralds
									miner.addCrates("donator", 5);
									miner.getInv().addEmerald(75);
								} else if (total < 25) { // Package 3, between 10 and 24 USD
									if (actual < 5) {
										// The user has the 1st package, give 15 crates and 150 emeralds
										miner.addCrates("donator", 15);
										miner.getInv().addEmerald(150);
									} else if (actual < 10) {
										// The user has the 2nd package, give 10 crates and 75 emeralds
										miner.addCrates("donator", 10);
										miner.getInv().addEmerald(75);
									}
								} else if (total < 50) { // Package 4, between 25 and 49 USD
									if (actual < 5) {
										// The user has the 1st package, give 40 crates and 300 emeralds
										miner.addCrates("donator", 40);
										miner.getInv().addEmerald(300);
									} else if (actual < 10) {
										// The user has the 2nd package, give 35 crates and 225 emeralds
										miner.addCrates("donator", 35);
										miner.getInv().addEmerald(225);
									} else if (actual < 25) {
										// The user has the 3rd package, give 25 crates and 150 emeralds
										miner.addCrates("donator", 25);
										miner.getInv().addEmerald(150);
									}
								} else if (total >= 50) { // Package 5, starting at 50 USD
									if (actual < 5) {
										// The user has the 1st package, give 90 crates and 600 emeralds
										miner.addCrates("donator", 40);
										miner.getInv().addEmerald(300);
									} else if (actual < 10) {
										// The user has the 2nd package, give 85 crates and 525 emeralds
										miner.addCrates("donator", 35);
										miner.getInv().addEmerald(225);
									} else if (actual < 25) {
										// The user has the 3rd package, give 75 crates and 450 emeralds
										miner.addCrates("donator", 25);
										miner.getInv().addEmerald(450);
									} else if (actual < 50) {
										// The user has the 4th package, give 50 crates and 300 emeralds
										miner.addCrates("donator", 50);
										miner.getInv().addEmerald(300);
									}
								}

								miner.update();
							} else {
								state.executeUpdate("INSERT INTO donators VALUES(" + u.getIdLong() + ", " + amount + ")");
								Miner miner = new Miner(u.getIdLong());

								if (amount < 5) {
									miner.setDurability("donator", 50000);
									miner.setPickaxe("donator");
									miner.addCrates("donator", 10);
								} else if (amount < 10) { // Package 2, between 5 and 9 USD
									miner.setDurability("donator", 50000);
									miner.setPickaxe("donator");
									miner.addCrates("donator", 15);
									miner.getInv().addEmerald(75);
								} else if (amount < 25) { // Package 3, between 10 and 24 USD
									miner.setDurability("donator", 50000);
									miner.setPickaxe("donator");
									miner.addCrates("donator", 25);
									miner.getInv().addEmerald(150);
								} else if (amount < 50) { // Package 4, between 25 and 49 USD
									miner.setDurability("donator", 50000);
									miner.setPickaxe("donator");
									miner.addCrates("donator", 50);
									miner.getInv().addEmerald(300);
								} else if (amount >= 50) { // Package 5, starting at 50 USD
									miner.setDurability("donator", 50000);
									miner.setPickaxe("donator");
									miner.addCrates("donator", 100);
									miner.getInv().addEmerald(600);
								}

								miner.update();
							}

							state.close();
							conn.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}

		if (e.getAuthor().isBot())
			return;

		if (Main.uptime > 0) {
			if (e.getChannelType() == ChannelType.TEXT) {
				if (e.getGuild().getSelfMember().hasPermission(e.getTextChannel(), Permission.MESSAGE_WRITE, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EMBED_LINKS)) {
					String[] args = (e.getMessage().getContentRaw().split(" ").length > 1 ? e.getMessage().getContentRaw().split(" ", 2)[1].split(" ") : new String[] {});

					event = new CommandEvent(e.getGuild(), e.getTextChannel(), e.getAuthor(), e.getMember(), e.getMessage(), args);

					if (event.getTextMessage().toLowerCase().startsWith("m!")) {
						String command = event.getTextMessage().toLowerCase(); // Setting the message to lower case: if a message starts with "M!" it will be counted as a command
						command = command.replaceFirst("m! ", "m!").split(" ", 2)[0]; // If someone add a blank space after the prefix, remove it to trigger the command
						command = command.replaceFirst("m!", ""); // Remove the prefix and execute the command

						boolean cmd = executeCommand(command);
						if (!cmd) {
							event.reply("The bot is under maintenance. Type `m!server` to keep updated!");
						} else {
							// TODO Command statistics
						}
					}
				} else {

				}
			}
		}
	}

	private boolean isAmount(String word) {
		try {
			Integer.parseInt(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean executeCommand(String cmd) {
		if (main.getMaintenance() && event.getUser().getIdLong() != 318619541942960128L) {
			return false;
		} else {
			switch (cmd.toLowerCase()) {
				case "start": {
					new StartCommand(event);
					return true;
				}

				case "mine":
				case "m": {
					new MineCommand(event, main);
					return true;
				}

				case "profile":
				case "profil":
				case "inv":
				case "i": {
					new InventoryCommand(event);
					return true;
				}

				case "repair":
				case "r": {
					new RepairCommand(event);
					return true;
				}

				case "pickaxe":
				case "pick": {
					new PickaxeCommand(event);
					return true;
				}

				case "shop":
				case "s": {
					new ShopCommand(event);
					return true;
				}

				case "enchantments":
				case "enchant":
				case "ench": {
					new EnchantmentsCommand(event);
					return true;
				}

				case "chest":
				case "daily":
				case "day": {
					new DailyChestCommand(event);
					return true;
				}

				case "stats": {
					new StatsCommand(event);
					return true;
				}

				case "advancements":
				case "adv": {
					new AdvancementsCommand(event);
					return true;
				}

				case "verify": {
					new VerifyCommand(event, main);
					return true;
				}

				case "info": {
					new InfoCommand(event);
					return true;
				}

				case "crate": {
					new CrateCommand(event, main);
					return true;
				}

				case "top": {
					new TopCommand(event);
				}

				case "donator": {
					// Command for me only
					if (event.getUser().getIdLong() == 318619541942960128L) {
						new GiveDonatorPickaxeCommand();
					}

					return true;
				}

				case "givecrate": {
					// Command for me only
					if (event.getUser().getIdLong() == 318619541942960128L) {
						Miner miner = new Miner(Long.parseLong(event.getArgs()[0]));
						miner.addCrates("donator", Integer.parseInt(event.getArgs()[1]));
						miner.update();
					}

					return true;
				}

				case "checkvote": {
					// Command for me only
					if (event.getUser().getIdLong() == 318619541942960128L) {
						main.checkVotes();
						event.reply("Alright, I check the votes again :thumbsup:");
					}

					return true;
				}

				case "help": {
					event.reply("You need help about a command? Go there: <https://discordminer.com/doc>!\n" + "If you need further help, join the Discord by clicking this link: http://discord.gg/dEXymdC");
					return true;
				}

				case "vote": {
					event.reply("If you want to help Discord Miner to grow, you can vote by clicking this link:\n<https://discordminer.com/vote>");
					return true;
				}

				case "server": {
					Guild g = null;
					int online = 0;

					try {
						g = Main.getShards().getGuildById(492972328746090496L);

						for (Member m : g.getMembers()) {
							if (m.getOnlineStatus() != OnlineStatus.OFFLINE)
								online++;
						}
					} catch (NullPointerException e) {
					}

					EmbedBuilder embed = new EmbedBuilder();
					embed.setColor(Color.CYAN);
					embed.setDescription("[Join the official Discord server!](http://discord.gg/dEXymdC)\n\n" + "Total members: " + (g != null ? g.getMembers().size() : "?") + "\n" + "Online members: " + (g != null ? online : "?") + "\n\n" + "" + "Bot status: " + (main.getMaintenance() ? Assets.RED.getValue() : Assets.GREEN.getValue()));

					event.reply(embed.build());
					return true;
				}

				default:
					return true;
			}
		}
	}

}
