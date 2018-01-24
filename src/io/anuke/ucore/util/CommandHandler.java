package io.anuke.ucore.util;

import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.ucore.function.Consumer;

import java.util.Arrays;

public class CommandHandler{
	private final ObjectMap<String, Command> commands = new ObjectMap<>();
	private final String prefix;
	
	public CommandHandler(String prefix){
		this.prefix = prefix;
	}
	
	public Response handleMessage(String message){
		if(message == null || (!message.startsWith(prefix)))
			return new Response(ResponseType.noCommand, null);

		message = message.substring(prefix.length());

		String[] args = message.split(" ");
		message = args[0].toLowerCase();
		args = Arrays.copyOfRange(args, 1, args.length);
		
		Command command = commands.get(message);
		
		if(command != null){
			if(args.length == command.paramLength){
				command.runner.accept(args);
				return new Response(ResponseType.valid, command);
			}else{
				return new Response(ResponseType.invalidArguments, command);
			}
		}else{
			return new Response(ResponseType.unknownCommand, null);
		}
	}
	
	public void register(String text, String params, Consumer<String[]> runner){
		commands.put(text.toLowerCase(), new Command(text, params, null, runner));
	}
	
	public void register(String text, String params, String description, Consumer<String[]> runner){
		commands.put(text.toLowerCase(), new Command(text, params, description, runner));
	}
	
	public Iterable<Command> getCommandList(){
		return commands.values();
	}
	
	public static class Command{
		public final String text;
		public final String params;
		public final String description;
		public final int paramLength;
		public final Consumer<String[]> runner;
		
		public Command(String text, String params, String description, Consumer<String[]> runner){
			this.text = text;
			this.params = params;
			this.runner = runner;
			this.description = description;
			
			paramLength = params.length() == 0 ? 0 : (params.length() - params.replaceAll(" ", "").length() + 1);
		}
	}
	
	public static class Response{
		public final ResponseType type;
		public final Command command;
		
		public Response(ResponseType type, Command command){
			this.type = type;
			this.command = command;
		}
	}
	
	public enum ResponseType{
		noCommand, unknownCommand, invalidArguments, valid;
	}
}
