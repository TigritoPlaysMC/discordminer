package fr.glowning.discordminer.items;

import java.util.ArrayList;

import fr.glowning.discordminer.Assets;

public class Pickaxe {
	private final ArrayList<String> minedOres = new ArrayList<String>();
	private final String appearance;

	public Pickaxe(String pickaxe) {
		switch (pickaxe) {
			case "wooden": {
				this.minedOres.add("stone;5;11");
				this.minedOres.add("coal;1;8");
				this.appearance = Assets.PWOOD.getValue();
				break;
			}

			case "stone": {
				this.minedOres.add("stone;10;17");
				this.minedOres.add("coal;5;12");
				this.minedOres.add("iron;1;5");
				this.appearance = Assets.PSTONE.getValue();
				break;
			}

			case "gold": {
				this.minedOres.add("coal;10;22");
				this.minedOres.add("iron;7;12");
				this.minedOres.add("gold;5;7");
				this.appearance = Assets.PGOLD.getValue();
				break;
			}

			case "iron": {
				this.minedOres.add("gold;18;26");
				this.minedOres.add("iron;10;18");
				this.minedOres.add("diamond;2;5");
				this.appearance = Assets.PIRON.getValue();
				break;
			}

			case "diamond": {
				this.minedOres.add("iron;28;36");
				this.minedOres.add("obsidian;18;26");
				this.minedOres.add("diamond;5;10");
				this.appearance = Assets.PDIAMOND.getValue();
				break;
			}

			case "emerald": {
				this.minedOres.add("obsidian;16;24");
				this.minedOres.add("diamond;8;16");
				this.appearance = Assets.PEMERALD.getValue();
				break;
			}

			case "ruby": {
				this.minedOres.add("obsidian;48;78");
				this.minedOres.add("diamond;24;48");
				this.appearance = Assets.PRUBY.getValue();
				break;
			}

			case "ultimate": {
				this.minedOres.add("stone;64;128");
				this.minedOres.add("coal;64;128");
				this.minedOres.add("iron;64;96");
				this.minedOres.add("gold;64;96");
				this.minedOres.add("obsidian;64;78");
				this.minedOres.add("diamond;32;64");
				this.appearance = Assets.PULTIMATE.getValue();
				break;
			}

			case "crate": {
				this.minedOres.add("obsidian;16;24");
				this.minedOres.add("diamond;8;24");
				this.minedOres.add("emerald;1;3");
				this.appearance = Assets.PCRATE.getValue();
				break;
			}

			case "donator": {
				this.minedOres.add("obsidian;24;32");
				this.minedOres.add("diamond;16;24");
				this.minedOres.add("emerald;1;4");
				this.appearance = Assets.PDONATOR.getValue();
				break;
			}

			default: {
				this.appearance = null;
			}
		}
	}

	public ArrayList<String> getMinedOres() {
		return minedOres;
	}

	public String getAppearance() {
		return appearance;
	}
}
