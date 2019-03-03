package fr.glowning.discordminer.shop;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Enchantments;
import fr.glowning.discordminer.entity.Miner;

public class SEnchant {
	StringBuilder sb = new StringBuilder();
	Miner miner;

	public SEnchant(Miner miner) {
		this.miner = miner;

		Enchantments ench = miner.getEnchant();
		sb.append("1. Unbreaking - " + (ench.getUnbreaking() >= 5 ? "Level max"
				: "Level " + (ench.getUnbreaking() + 1) + " | Price: " + getPrice(ench.getUnbreaking()) + " "
						+ Assets.LAPIS.getValue())
				+ "\n");
		sb.append("2. Efficiency - " + (ench.getEfficiency() >= 5 ? "Level max"
				: "Level " + (ench.getEfficiency() + 1) + " | Price: " + getPrice(ench.getEfficiency()) + " "
						+ Assets.LAPIS.getValue())
				+ "\n");
		sb.append("3. Fortune - " + (ench.getFortune() >= 3 ? "Level max"
				: "Level " + (ench.getFortune() + 1) + " | Price: " + getPrice(ench.getFortune()) + " "
						+ Assets.LAPIS.getValue())
				+ "\n");
		sb.append("4. Luck - " + (ench.getLuck() >= 3 ? "Level max"
				: "Level " + (ench.getLuck() + 1) + " | Price: " + getPrice(ench.getLuck()) + " "
						+ Assets.LAPIS.getValue()));
	}

	public int getPrice(int level) {
		int lapis = 0;

		switch (level) {
			case 0:
				lapis = 50;
				break;

			case 1:
				lapis = 80;
				break;

			case 2:
				lapis = 135;
				break;

			case 3:
				lapis = 200;
				break;

			case 4:
				lapis = 400;
				break;

		}
		return lapis;
	}

	public String send() {
		return sb.toString();
	}

	public int getUnbreakingRatio() {
		switch (miner.getEnchant().getUnbreaking()) {
			case 0:
				return 0;

			case 1:
				return 10;

			case 2:
				return 20;

			case 3:
				return 30;

			case 4:
				return 40;

			case 5:
				return 50;

			default:
				return 0;
		}
	}

	public int getEfficiencyRatio() {
		switch (miner.getEnchant().getEfficiency()) {
			case 0:
				return 5000;

			case 1:
				return 4500;

			case 2:
				return 4000;

			case 3:
				return 3500;

			case 4:
				return 3000;

			case 5:
				return 2500;

			default:
				return 0;
		}
	}

	public int getFortuneRatio() {
		switch (miner.getEnchant().getFortune()) {
			case 0:
				return 1;

			case 1:
				return 2;

			case 2:
				return 3;

			case 3:
				return 5;

			default:
				return 1;
		}
	}

	public int getLuckRatio() {
		switch (miner.getEnchant().getLuck()) {
			case 0:
				return 5;

			case 1:
				return 10;

			case 2:
				return 15;

			case 3:
				return 20;

			default:
				return 5;
		}
	}

}
