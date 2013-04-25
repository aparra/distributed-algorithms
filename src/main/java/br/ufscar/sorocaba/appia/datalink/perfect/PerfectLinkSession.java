package br.ufscar.sorocaba.appia.datalink.perfect;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;

import java.util.ArrayList;
import java.util.List;

import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.SendRequestEvent;
import br.ufscar.sorocaba.appia.datalink.model.SimpleMessage;
import br.ufscar.sorocaba.appia.util.AppiaUtils;

public class PerfectLinkSession extends Session {

	private List<SimpleMessage> delivered = new ArrayList<SimpleMessage>();
	
	public PerfectLinkSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			go(event);
		} else if (event instanceof SendRequestEvent) {
			handleSendEvent((SendRequestEvent) event);
		}
	}

	private void handleSendEvent(SendRequestEvent event) {
		SimpleMessage message = (SimpleMessage) AppiaUtils.clone(event).getMessage().popObject();
		
		if (!delivered.contains(message)) {
			delivered.add(message);
			go(event);
		} else {
			System.out.println(String.format("[PerfectLinkSession] message %s already delivered.", message.getId()));
		}
	}
}
