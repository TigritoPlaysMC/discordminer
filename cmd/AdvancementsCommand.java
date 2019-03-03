package fr.glowning.discordminer.cmd;

import fr.glowning.discordminer.Assets;
import fr.glowning.discordminer.entity.Miner;
import net.dv8tion.jda.core.entities.User;

public class AdvancementsCommand {
	Miner miner;

	public AdvancementsCommand(CommandEvent event) {
		User u = event.getUser();
		miner = new Miner(u.getIdLong());

		if (miner.exists()) {
			StringBuilder sb = new StringBuilder();
			sb.append("== [ ADVANCEMENTS ] ==\n\n");
			sb.append(minerAdvancement() + "\n\n");
			sb.append(alchemistAdvancement() + "\n\n");
			sb.append(dailyWorkerAdvancement() + "\n\n");
			sb.append(supporterAdvancement() + "\n\n");

			event.reply(sb.toString());
		}
	}

	private String minerAdvancement() {
		String str = "";
		int i = miner.getAdv().getMine();

		if (i < 1000) {
			str = "**Miner** [1 / 5] » You mined " + i + " times out of 1000\n" + "\t" + generateProgressBar(i, 1000);
		} else if (i < 5000) {
			str = "**Miner** [2 / 5] » You mined " + i + " times out of 5000\n" + "\t" + generateProgressBar(i, 5000);
		} else if (i < 13500) {
			str = "**Miner** [3 / 5] » You mined " + i + " times out of 13500\n" + "\t" + generateProgressBar(i, 13500);
		} else if (i < 25000) {
			str = "**Miner** [4 / 5] » You mined " + i + " times out of 25000\n" + "\t" + generateProgressBar(i, 25000);
		} else {
			str = "**Miner** [5 / 5] » You mined " + i + " times";
		}

		return str;
	}

	private String alchemistAdvancement() {
		String str = "";
		int i = miner.getAdv().getBonus();

		if (i < 35) {
			str = "**Alchemist** [1 / 5] » You purchased " + i + " bonuses out of 35\n" + "\t"
					+ generateProgressBar(i, 25);
		} else if (i < 100) {
			str = "**Alchemist** [2 / 5] » You purchased " + i + " bonuses out of 100\n" + "\t"
					+ generateProgressBar(i, 100);
		} else if (i < 250) {
			str = "**Alchemist** [3 / 5] » You purchased " + i + " bonuses out of 250\n" + "\t"
					+ generateProgressBar(i, 250);
		} else if (i < 500) {
			str = "**Alchemist** [4 / 5] » You purchased " + i + " bonuses out of 500\n" + "\t"
					+ generateProgressBar(i, 500);
		} else {
			str = "**Alchemist** [5 / 5] » You purchased " + i + " bonuses";
		}

		return str;
	}

	private String dailyWorkerAdvancement() {
		String str = "";
		int i = miner.getAdv().getChest();

		if (i < 14) {
			str = "**Daily Worker** [1 / 5] » You opened " + i + " chests out of 14\n" + "\t"
					+ generateProgressBar(i, 14);
		} else if (i < 30) {
			str = "**Daily Worker** [2 / 5] » You opened " + i + " chests out of 30\n" + "\t"
					+ generateProgressBar(i, 30);
		} else if (i < 60) {
			str = "**Daily Worker** [3 / 5] » You opened " + i + " chests out of 60\n" + "\t"
					+ generateProgressBar(i, 60);
		} else if (i < 120) {
			str = "**Daily Worker** [4 / 5] » You opened " + i + " chests out of 120\n" + "\t"
					+ generateProgressBar(i, 120);
		} else {
			str = "**Daily Worker** [5 / 5] » You opened " + i + " chests";
		}

		return str;
	}

	private String supporterAdvancement() {
		String str = "";
		int i = miner.getAdv().getCrate();

		if (i < 30) {
			str = "**Supporter** [1 / 5] » You opened " + i + " crates out of 30\n" + "\t" + generateProgressBar(i, 30);
		} else if (i < 60) {
			str = "**Supporter** [2 / 5] » You opened " + i + " crates out of 60\n" + "\t" + generateProgressBar(i, 60);
		} else if (i < 120) {
			str = "**Supporter** [3 / 5] » You opened " + i + " crates out of 120\n" + "\t"
					+ generateProgressBar(i, 120);
		} else if (i < 240) {
			str = "**Supporter** [4 / 5] » You opened " + i + " crates out of 240\n" + "\t"
					+ generateProgressBar(i, 240);
		} else {
			str = "**Supporter** [5 / 5] » You opened " + i + " crates";
		}

		return str;
	}

	private String generateProgressBar(double i, double j) {
		String bar = "";

		double p = i / j * 10;
		for (int a = 1; a <= 10; a++) {
			if (a == 1) {
				if (p >= 1)
					bar += Assets.BAR_ON_0.getValue();
				else
					bar += Assets.BAR_OFF_0.getValue();
			}

			if (a >= 2 && a < 10) {
				if (p >= a)
					bar += Assets.BAR_ON_1.getValue();
				else
					bar += Assets.BAR_OFF_1.getValue();
			}

			if (a == 10) {
				if (p == 10)
					bar += Assets.BAR_ON_2.getValue();
				else
					bar += Assets.BAR_OFF_2.getValue();
			}
		}

		return bar;
	}

}
