package com.molamil.osonegro.master;

import java.lang.reflect.InvocationTargetException;

import com.molamil.osonegro.context.CommandContext;
import com.molamil.osonegro.utils.ObjectUtil;

public class Execute implements CommandMaster {

	private CommandContext context;
	private Object command;



	public Object build() throws Exception {
		Class<?> myClass = Class.forName(context.getType());
		command = myClass.newInstance();
		ObjectUtil.mergePropsWithObject(context.getProps(), command);
		
		return command;
	}


	public void execute() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		command.getClass().getMethod("execute").invoke(command);
	}

	
	
	public CommandContext getContext() {
		return context;
	}

	public void setContext(CommandContext context) {
		this.context = context;
	}

}
