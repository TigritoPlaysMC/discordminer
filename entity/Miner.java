package fr.glowning.discordminer.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import fr.glowning.discordminer.Main;

public class Miner {
	private boolean exists;
	private long joinDate, xp, xpMax, id, chest;
	private int level, chestMultiplier;
	private String pickaxeAtBeginning, pickaxe, name, crates;
	private JSONObject durability, fullEnchant, stats;
	private Inventory inv;
	private Enchantments enchant;
	private Bonus bonus;
	private Advancements adv;

	public Miner(long id) { // Retrieving the user with its user ID
		try {
			Connection conn = Main.getConnection();
			Statement state = conn.createStatement();

			String sql = String.format("SELECT * FROM miners WHERE id = %d", id);
			ResultSet rs = state.executeQuery(sql);

			if (rs.next()) {
				try {
					this.exists = true;
					this.id = id;
					this.name = Main.getShards().getUserById(id).getName();
					this.joinDate = rs.getLong("joinDate");
					this.level = rs.getInt("level");
					this.xp = rs.getLong("xp");
					this.xpMax = (long) (50 + (150 * 80 / 100) + (3 * Math.pow(getLevel() - 1, 3)));
					this.pickaxeAtBeginning = rs.getString("pickaxe");
					this.pickaxe = rs.getString("pickaxe");
					this.crates = rs.getString("crates");
					this.durability = new JSONObject(rs.getString("durability"));
					this.inv = new Inventory(new JSONObject(rs.getString("ores")));
					this.fullEnchant = new JSONObject(rs.getString("enchantments"));
					this.enchant = new Enchantments(new JSONObject(rs.getString("enchantments")).getJSONObject(pickaxe));
					this.bonus = new Bonus(rs.getString("bonus_type"), rs.getLong("bonus_content"));
					this.adv = new Advancements(new JSONObject(rs.getString("advancements")));
					this.chest = rs.getLong("chest");
					this.chestMultiplier = rs.getInt("chest_multiplier");
					this.stats = new JSONObject(rs.getString("stats"));
				} catch (NullPointerException e) {
					this.exists = false;
				}
			} else {
				this.exists = false;
			}

			state.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean exists() {
		return exists;
	}

	public String getName() {
		return name;
	}

	public long getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(long joinDate) {
		this.joinDate = joinDate;
	}

	public long getXp() {
		return xp;
	}

	public void setXp(long xp) {
		this.xp = xp;
	}

	public void addXp(long xp) {
		this.xp += xp;
	}

	public long getXpMax() {
		return xpMax;
	}

	public long getDurability(String pickaxe) {
		return durability.getLong(pickaxe);
	}

	public void setDurability(String pickaxe, long durability) {
		this.durability.put(pickaxe, durability);
	}

	public JSONObject getFullEnchant() {
		return fullEnchant;
	}

	public boolean has(String pickaxe) {
		if (this.durability.has(pickaxe))
			return true;
		else
			return false;
	}

	public int getLevel() {
		return level;
	}

	public void addLevel() {
		this.level += 1;
	}

	public String getPickaxe() {
		return pickaxe;
	}

	public void setPickaxe(String pickaxe) {
		this.pickaxe = pickaxe;
	}

	public long getChest() {
		return chest;
	}

	public void setChest(long chest) {
		this.chest = chest;
	}

	public int getChestMultiplier() {
		return chestMultiplier;
	}

	public void addChestMultiplier() {
		this.chestMultiplier += 1;
	}

	public void setChestMultiplier(int chestMultiplier) {
		this.chestMultiplier = chestMultiplier;
	}

	public Object getCrates(String type) {
		String[] types = crates.split(";");

		if (type.equals("vote")) {
			return Integer.parseInt(types[0]);
		} else if (type.equals("donator")) {
			return Integer.parseInt(types[1]);
		} else if (type.equals("shop")) {
			return Integer.parseInt(types[2]);
		} else if (type.equals("all")) {
			return types[0] + ";" + types[1] + ";" + types[2];
		} else {
			return 0;
		}
	}

	public void addCrates(String type, int crates) {
		String[] types = this.crates.split(";");

		if (type.equals("vote")) {
			this.crates = (Integer.parseInt(types[0]) + crates) + ";" + types[1] + ";" + types[2];
		} else if (type.equals("donator")) {
			this.crates = types[0] + ";" + (Integer.parseInt(types[1]) + crates) + ";" + types[2];
		} else if (type.equals("shop")) {
			this.crates = types[0] + ";" + types[1] + ";" + (Integer.parseInt(types[2]) + crates);
		}
	}

	public Inventory getInv() {
		return inv;
	}

	public void setInv(Inventory inv) {
		this.inv = inv;
	}

	public Enchantments getEnchant() {
		return enchant;
	}

	public void setEnchant(Enchantments enchant) {
		this.enchant = enchant;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	public Advancements getAdv() {
		return adv;
	}

	public void setAdv(Advancements adv) {
		this.adv = adv;
	}

	public JSONObject getStats() {
		return stats;
	}

	// Checking if the user can level up
	public boolean canLevelUp() {
		if (getXp() >= getXpMax()) {
			// It is the case, give 5 emeralds
			getInv().addEmerald(5);
			// Remove the surplus of XP
			setXp(getXp() - getXpMax());
			// And add a level
			addLevel();

			// Return true to trigger the level up message
			return true;
		}

		return false;
	}

	// Checking if the pickaxe is broken
	public boolean isBroken(String pickaxe) {
		if (getDurability(pickaxe) <= 0) {
			setDurability(pickaxe, 0);
			return true;
		}

		return false;
	}

	// Update miner's profile: enter new data in the database
	public void update() {
		try {
			fullEnchant.put(pickaxeAtBeginning, getEnchant().generateAsJSONObject());

			String sql = String.format("UPDATE miners SET name = '%s', level = %d, xp = %d, pickaxe = '%s', crates = '%s', durability = '%s', ores = '%s', " + "enchantments = '%s', bonus_type = '%s', bonus_content = %d, advancements = '%s', stats = '%s', chest = %d, chest_multiplier = %d WHERE id = %d", name.replace("'", "\'"), getLevel(), getXp(), getPickaxe(), getCrates("all"), durability.toString(), getInv().generate(), fullEnchant, getBonus().getType(), getBonus().getContent(), getAdv().generate(), getStats().toString(), getChest(), getChestMultiplier(), id);

			Connection conn = Main.getConnection();
			Statement state = conn.createStatement();

			state.executeUpdate(sql);

			state.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
