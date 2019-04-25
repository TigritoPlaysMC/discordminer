package fr.glowning.discordminer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.NamingException;
import javax.security.auth.login.LoginException;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.discordbots.api.client.DiscordBotListAPI;

import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;

public class Main {
	private static Main main = new Main(); // Variable to get the Main class
	private static ShardManager shard; // Combine all shards into one single variable
	private static DiscordBotListAPI api;
	private final boolean maintenance = false; // Define if the bot is in maintenance or not (disable commands)
	private boolean enabled = false; // While !enabled, no command is executable

	private final HashMap<Long, Integer> mines = new HashMap<Long, Integer>();
	private final HashMap<Long, Integer> captcha = new HashMap<Long, Integer>();

	private static Connection conn; // SQL connection
	public static long uptime = 0;

	// Mine command variables
	private HashMap<Long, Long> mineCooldown = new HashMap<Long, Long>();

	public static void main(String[] args) throws LoginException, IOException, NamingException {
		DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
		builder.setToken(Assets.TOKEN.getValue());
		builder.addEventListeners(new BotEvents(main));
		
		api = new DiscordBotListAPI.Builder()
				.token(Assets.DBLToken.getValue())
				.botId("492969308201418756")
				.build();
		
		shard = builder.build(); // Define "shard" to all the shards created by the bot
	}

	/* GETTERS AND SETTERS */

	public boolean getMaintenance() {
		return maintenance;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static ShardManager getShards() {
		return shard;
	}

	public static Connection getConnection() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/discord_miner", "root", Assets.PASSWORD.getValue());
			return conn;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateGame() {
		for (JDA jda : getShards().getShards()) {
			new Timer().scheduleAtFixedRate(new TimerTask() {
				int i = 0;

				@Override
				public void run() {

					if (i == 0) {
						jda.getPresence().setGame(Game.watching(getShards().getGuilds().size() + " servers"));

						int guildCount = jda.getGuilds().size();
						int shardId = jda.getShardInfo().getShardId();
						int shardCount = jda.getShardInfo().getShardTotal();
						
						api.setStats(shardId, shardCount, guildCount);
					} else if (i == 1) {
						jda.getPresence().setGame(Game.playing("Type m!help"));
					} else if (i == 2) {
						jda.getPresence().setGame(Game.listening(getTotalPlayers() + " miners"));
					}

					i++;

					if (i > 2)
						i = 0;
				}
			}, 0, 1 * 60 * 1000);
		}
	}

	private long getTotalPlayers() {
		try {
			Connection conn = getConnection();
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery("SELECT * FROM miners");

			long i = 0;
			while (rs.next()) {
				i++;
			}

			return i;
		} catch (SQLException e) {
			return 0;
		}
	}

	// START: Mine command methods

	public HashMap<Long, Long> getMineCooldown() {
		return mineCooldown;
	}

	public void setMineCooldown(HashMap<Long, Long> mineCooldown) {
		this.mineCooldown = mineCooldown;
	}

	public HashMap<Long, Integer> getMines() {
		return mines;
	}

	public HashMap<Long, Integer> getCaptcha() {
		return captcha;
	}

	// END: Mine command methods

	public void checkVotes() {
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					URLConnection connection = new URL("https://glowning.dev/discordminer/webhook/votes.txt").openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
					connection.connect();

					BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

					String line = "";
					while ((line = r.readLine()) != null) {
						System.out.println(line);
						User u = getShards().getUserById(line);
						if (u != null) {
							int crate;

							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(System.currentTimeMillis());
							cal.setTimeZone(TimeZone.getTimeZone("UTC"));

							if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
								crate = 2;
							} else {
								crate = 1;
							}

							Miner miner = new Miner(u.getIdLong());
							if (miner.exists()) {
								System.out.println("Crates given to " + u.getName() + " (" + u.getIdLong() + ")");
								u.openPrivateChannel().complete().sendMessage("Hey! Thank you for your vote.\nAs a reward, I give you **" + crate + " crate" + (crate == 2 ? "s" : "") + "**.").queue();

								miner.addCrates("vote", crate);
								miner.getStats().put("votes", miner.getStats().getInt("votes") + 1);
								miner.update();
							}
						}

						HttpClient httpclient = HttpClients.createDefault();
						HttpPost httppost = new HttpPost("https://glowning.dev/discordminer/webhook/discordbots.php");
						List<NameValuePair> params = new ArrayList<NameValuePair>(2);
						params.add(new BasicNameValuePair("Authorization", "DiscordMiner"));
						params.add(new BasicNameValuePair("user", line));
						httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
						httpclient.execute(httppost);

					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}, 0, 1000);
	}
}
