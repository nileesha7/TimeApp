package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Json;

public final class UserActor extends AbstractActor {
	private final ActorRef ws;
	
	public UserActor(final ActorRef wsOut) {
		ws = wsOut;
	}
	
	public static Props props(final ActorRef wsout) {
		return Props.create(UserActor.class, wsout);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(TimeMessage.class, this::sendTime).build();
	}
	
	private void sendTime(TimeMessage msg) {
		final ObjectNode response = Json.newObject();
		response.put("time", msg.time);
		ws.tell(response, self());
	}
	
	@Override
	public void preStart() {
		context().actorSelection("/user/timeActor/").tell(new TimeActor.RegisterMsg(), self());
	}
	
	static public class TimeMessage {
		public final String time;
		public TimeMessage(String time) {
			this.time = time;
		}
	}
}
