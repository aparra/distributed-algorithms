package br.ufscar.sorocaba.appia.datalink.perfect;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.initAndGo;

import java.util.ArrayList;
import java.util.List;

import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.SendRequestEvent;
import br.ufscar.sorocaba.appia.datalink.events.TicTacEvent;
import br.ufscar.sorocaba.appia.util.AppiaUtils;

public class StubbornLinkSession extends Session {

	private List<SendableEvent> buffer = new ArrayList<SendableEvent>();
	
	public StubbornLinkSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			go(event);
		} else if (event instanceof SendRequestEvent) {
			handleSendEvent((SendRequestEvent) event);
		} else if (event instanceof TicTacEvent) {
			handleTicTacEvent((TicTacEvent) event);
		}
	}

	private void handleSendEvent(SendRequestEvent event) {
		try {
			buffer.add((SendableEvent) event.cloneEvent());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		go(event);
	}

	private void handleTicTacEvent(TicTacEvent event) {
		for (SendableEvent bufferEvent : buffer) {
			Event sendableEvent = AppiaUtils.clone(bufferEvent);
			sendableEvent.setSourceSession(this);
			
			initAndGo(sendableEvent);
		}
	}
}
