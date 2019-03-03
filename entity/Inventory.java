package fr.glowning.discordminer.entity;

import org.json.JSONObject;

public class Inventory {

	private long stone, coal, iron, gold, lapis, redstone, obsidian, diamond, emerald;

	public Inventory(JSONObject object) {
		this.stone = object.getLong("stone");
		this.coal = object.getLong("coal");
		this.iron = object.getLong("iron");
		this.gold = object.getLong("gold");
		this.lapis = object.getLong("lapis");
		this.redstone = object.getLong("redstone");
		this.obsidian = object.getLong("obsidian");
		this.diamond = object.getLong("diamond");
		this.emerald = object.getLong("emerald");
	}

	public long getStone() {
		return stone;
	}

	public void setStone(long stone) {
		this.stone = stone;
	}

	public void addStone(long stone) {
		this.stone += stone;
	}

	public long getCoal() {
		return coal;
	}

	public void setCoal(long coal) {
		this.coal = coal;
	}

	public void addCoal(long coal) {
		this.coal += coal;
	}

	public long getIron() {
		return iron;
	}

	public void setIron(long iron) {
		this.iron = iron;
	}

	public void addIron(long iron) {
		this.iron += iron;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public void addGold(long gold) {
		this.gold += gold;
	}

	public long getLapis() {
		return lapis;
	}

	public void setLapis(long lapis) {
		this.lapis = lapis;
	}

	public void addLapis(long lapis) {
		this.lapis += lapis;
	}

	public long getRedstone() {
		return redstone;
	}

	public void setRedstone(long redstone) {
		this.redstone = redstone;
	}

	public void addRedstone(long redstone) {
		this.redstone += redstone;
	}

	public long getObsidian() {
		return obsidian;
	}

	public void setObsidian(long obsidian) {
		this.obsidian = obsidian;
	}

	public void addObsidian(long obsidian) {
		this.obsidian += obsidian;
	}

	public long getDiamond() {
		return diamond;
	}

	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}

	public void addDiamond(long diamond) {
		this.diamond += diamond;
	}

	public long getEmerald() {
		return emerald;
	}

	public void setEmerald(long emerald) {
		this.emerald = emerald;
	}

	public void addEmerald(long emerald) {
		this.emerald += emerald;
	}

	// Generate JSONObject's string
	public String generate() {
		JSONObject object = new JSONObject();
		object.put("stone", getStone());
		object.put("coal", getCoal());
		object.put("iron", getIron());
		object.put("gold", getGold());
		object.put("lapis", getLapis());
		object.put("redstone", getRedstone());
		object.put("obsidian", getObsidian());
		object.put("diamond", getDiamond());
		object.put("emerald", getEmerald());

		return object.toString();
	}

}
