package fr.glowning.discordminer.shop;

import fr.glowning.discordminer.Assets;

public class SPickaxe {
	StringBuilder sb = new StringBuilder();

	public SPickaxe() {
		sb.append("1. Emerald Pickaxe | Price: 375 " + Assets.EMERALD.getValue() + "\n");
		sb.append("2. Ruby Pickaxe | Price: 600 " + Assets.EMERALD.getValue() + "\n");
		sb.append("3. Ultimate Pickaxe | Price: 1000 " + Assets.EMERALD.getValue() + "\n");
	}

	public String send() {
		return sb.toString();
	}

}
