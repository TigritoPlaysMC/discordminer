package fr.glowning.discordminer.shop;

import fr.glowning.discordminer.Assets;

public class SBonus {
	StringBuilder sb = new StringBuilder();

	public SBonus() {
		sb.append("1. Auto-Mine (30 minutes) | Price: 250 " + Assets.REDSTONE.getValue() + "\n");
		sb.append("2. Auto-Mine (2 hours) | Price: 400 " + Assets.REDSTONE.getValue() + "\n");
		sb.append("3. Unbreakable pickaxe (50 uses) | Price: 100 " + Assets.REDSTONE.getValue() + "\n");
		sb.append("4. Unbreakable pickaxe (100 uses) | Price: 150 " + Assets.REDSTONE.getValue() + "\n");
	}

	public String send() {
		return sb.toString();
	}

}
