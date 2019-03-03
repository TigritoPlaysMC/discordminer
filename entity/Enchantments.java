package fr.glowning.discordminer.entity;

import org.json.JSONObject;

public class Enchantments {

	private int unbreaking, efficiency, fortune, luck;

	public Enchantments(JSONObject object) {
		this.unbreaking = object.getInt("unbreaking");
		this.efficiency = object.getInt("efficiency");
		this.fortune = object.getInt("fortune");
		this.luck = object.getInt("luck");
	}

	public int getUnbreaking() {
		return unbreaking;
	}

	public void setUnbreaking(int unbreaking) {
		this.unbreaking = unbreaking;
	}

	public int getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}

	public int getFortune() {
		return fortune;
	}

	public void setFortune(int fortune) {
		this.fortune = fortune;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	// Generate JSONObject's string
	public String generate() {
		JSONObject object = new JSONObject();
		object.put("unbreaking", getUnbreaking());
		object.put("efficiency", getEfficiency());
		object.put("fortune", getFortune());
		object.put("luck", getLuck());

		return object.toString();
	}

	public JSONObject generateAsJSONObject() {
		JSONObject object = new JSONObject();
		object.put("unbreaking", getUnbreaking());
		object.put("efficiency", getEfficiency());
		object.put("fortune", getFortune());
		object.put("luck", getLuck());

		return object;
	}

}
