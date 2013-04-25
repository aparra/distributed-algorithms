package br.ufscar.sorocaba.appia.datalink.perfect;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;

import java.util.Timer;
import java.util.TimerTask;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.datalink.events.TicTacEvent;

public class ClockSession extends Session {

	private Timer timer = new Timer();

	public ClockSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			handleChannelInit((ChannelInit) event);
		}
	}

	private void handleChannelInit(final ChannelInit init) {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					new TicTacEvent().asyncGo(init.getChannel(), Direction.DOWN);
				} catch (AppiaEventException e) {
					throw new RuntimeException(e);
				}
			}
		}, 0, 5000);

		go(init);
	}
}
