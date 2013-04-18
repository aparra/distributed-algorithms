package br.ufscar.sorocaba.appia.services.talk;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.initAndGo;

import java.net.InetSocketAddress;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.core.message.Message;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import br.ufscar.sorocaba.appia.services.talk.events.ReceiveConfirmEvent;
import br.ufscar.sorocaba.appia.services.talk.events.SendRequestEvent;
import br.ufscar.sorocaba.appia.services.talk.model.SimpleMessage;

public class ReceiveApplicationSession extends Session {

	private InetSocketAddress home = creanteSocketAddress("localhost", 8899);
	
	public ReceiveApplicationSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			handleChannelInit((ChannelInit) event);
		} else if (event instanceof SendRequestEvent) {
			handleSendRequest((SendRequestEvent) event);
		}
	}

	private void handleChannelInit(ChannelInit init) {
		go(init);

		try {
			RegisterSocketEvent registerSocket = new RegisterSocketEvent(init.getChannel(), Direction.DOWN, this);
			registerSocket.port = home.getPort();
			registerSocket.localHost = home.getAddress();
			
			go(registerSocket);
		} catch (AppiaEventException e) {
			throw new RuntimeException(e);
		}

		System.out.println("\nReceive channel is up and running\n");
	}

	private void handleSendRequest(SendRequestEvent request) {
		SimpleMessage simpleMessage = (SimpleMessage) request.getMessage().popObject();
		
		System.out.println(String.format("[ReceiveApplication] received message: %s", simpleMessage.getContent()));

		Message message = new Message();
		message.pushObject(simpleMessage);
		
		sendReceiveConfirm(request, message);
	}

	private void sendReceiveConfirm(SendRequestEvent request, Message message) {
		ReceiveConfirmEvent confirmEvent = new ReceiveConfirmEvent();
		confirmEvent.setChannel(request.getChannel());
		confirmEvent.setDir(Direction.DOWN);
		confirmEvent.setSourceSession(this);
		
		confirmEvent.to("localhost:9988").from(home).setMessage(message);
		
		initAndGo(confirmEvent);
	}
}
