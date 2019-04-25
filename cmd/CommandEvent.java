package fr.glowning.discordminer.cmd;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandEvent {

	private final Guild guild;
	private final TextChannel channel;
	private final User user;
	private final Member member;
	private final Message message;
	private final String textMessage;
	private final String[] args;

	public CommandEvent(Guild g, TextChannel chan, User u, Member m, Message msg, String[] args) {
		this.guild = g;
		this.channel = chan;
		this.user = u;
		this.member = m;
		this.message = msg;
		this.textMessage = msg.getContentRaw();
		this.args = args;
	}

	/* GETTERS AND SETTERS */

	public Guild getGuild() {
		return guild;
	}

	public TextChannel getChannel() {
		return channel;
	}

	public User getUser() {
		return user;
	}

	public Member getMember() {
		return member;
	}

	public Message getMessage() {
		return message;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public String[] getArgs() {
		return args;
	}

	/*
	 * Reply methods 
	 * ------------- 
	 * Used to send a message to Discord instead of
	 * using base method, with the possibility 
	 * to forget the queue or complete
	 * method at the end
	 */

	public void reply(String message) {
		// Reply with a string
		getChannel().sendMessage(message).queue();
	}

	public void reply(MessageEmbed embed) {
		// Reply with an embedded message
		getChannel().sendMessage(embed).queue();
	}

}
