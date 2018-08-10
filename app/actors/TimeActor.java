package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;
import akka.event.Logging;
import akka.event.LoggingAdapter;
public class TimeActor extends AbstractActorWithTimers{

	private static List<ActorRef> userActors = new ArrayList<ActorRef>();
	
private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	
	
	public static Props props() {
		return Props.create(TimeActor.class);
	}
	
	private static final class Tick {
	
	}
	
	static public class RegisterMsg {
	
	}
	
	public void preStart() {
		System.out.println("Time actor started...");
		getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(5, TimeUnit.SECONDS));
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(RegisterMsg.class, msg -> userActors.add(sender()))
			    .match(Tick.class, msg -> {
			    	UserActor2.TimeMessage tMsg = new UserActor2.TimeMessage(LocalDateTime.now().toString());
			    	log.info(LocalDateTime.now().toString());
			        userActors.forEach(ar -> ar.tell(tMsg, self()));
			    })
				.build();
	}


}
