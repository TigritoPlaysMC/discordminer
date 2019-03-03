package fr.glowning.discordminer.entity;

import org.json.JSONObject;

public class Advancements {

	private int mine, chest, crate, bonus, potion;

	public Advancements(JSONObject object) {
		this.mine = object.getInt("mine");
		this.chest = object.getInt("chest");
		this.crate = object.getInt("crate");
		this.bonus = object.getInt("bonus");
		this.potion = object.getInt("potion");
	}

	public int getMine() {
		return mine;
	}

	public void addMine() {
		this.mine += 1;
	}

	public int getChest() {
		return chest;
	}

	public void addChest() {
		this.chest += 1;
	}

	public int getCrate() {
		return crate;
	}

	public void addCrate() {
		this.crate += 1;
	}

	public int getBonus() {
		return bonus;
	}

	public void addBonus() {
		this.bonus += 1;
	}

	public int getPotion() {
		return potion;
	}

	public void addPotion() {
		this.potion += 1;
	}

	// Generate JSONObject's string
	public String generate() {
		JSONObject object = new JSONObject();
		object.put("mine", getMine());
		object.put("chest", getChest());
		object.put("crate", getCrate());
		object.put("bonus", getBonus());
		object.put("potion", getPotion());

		return object.toString();
	}

}
