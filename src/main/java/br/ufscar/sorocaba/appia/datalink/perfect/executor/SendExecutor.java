package br.ufscar.sorocaba.appia.datalink.perfect.executor;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.createQoS;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.start;
import net.sf.appia.core.Appia;
import net.sf.appia.core.AppiaCursorException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.ChannelCursor;
import net.sf.appia.core.Layer;
import net.sf.appia.core.QoS;
import net.sf.appia.protocols.udpsimple.UdpSimpleLayer;
import br.ufscar.sorocaba.appia.datalink.SendApplicationLayer;
import br.ufscar.sorocaba.appia.datalink.perfect.ClockLayer;
import br.ufscar.sorocaba.appia.datalink.perfect.StubbornLinkLayer;

public class SendExecutor {

	public static void main(String[] args) {
		Layer[] queue = { new UdpSimpleLayer(), new StubbornLinkLayer(), new ClockLayer(), new SendApplicationLayer() };

		QoS myQoS = createQoS("talk_stack", queue);
		Channel channel = myQoS.createUnboundChannel("talk_channel");

	    bind((SendApplicationLayer) queue[3], channel);
		start(channel);

		System.out.println("Starting Appia...");
		Appia.run();
	}
	
	private static void bind(SendApplicationLayer layer, Channel channel) {
		ChannelCursor channelCursor = channel.getCursor();
	    channelCursor.top();
	    
	    try {
	      channelCursor.setSession(layer.createSession());
	    } catch (AppiaCursorException e) { throw new RuntimeException(e); }
	}
}
