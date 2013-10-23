package com.molamil.osonegro.master;

import com.molamil.osonegro.context.CommandContext;

public interface CommandMaster {

	public CommandContext getContext();
	public void setContext(CommandContext context);

	public Object build() throws Exception;
	public void execute() throws Exception;
}
