package fr.glowning.discordminer.cmd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.glowning.discordminer.Main;
import fr.glowning.discordminer.entity.Miner;

public class GiveDonatorPickaxeCommand {

	public GiveDonatorPickaxeCommand() {
		try {
			Connection conn = Main.getConnection();
			Statement state = conn.createStatement();

			ResultSet rs = state.executeQuery("SELECT * FROM donators");
			while (rs.next()) {
				try {
					Miner miner = new Miner(Main.getShards().getUserById(rs.getLong("user_id")).getIdLong());
					if (miner.exists()) {
						if (!miner.has("donator")) {
							miner.setDurability("donator", 50000);
							miner.getStats().put("pickaxe_owned", miner.getStats().getInt("pickaxe_owned") + 1);
							miner.addCrates("donator", 10);

							System.out.println("Pickaxe given to: " + rs.getLong("user_id"));
						}

						miner.update();
					}
				} catch (NullPointerException e) {
				}
			}

			state.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
