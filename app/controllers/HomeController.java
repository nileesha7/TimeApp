package controllers;

import javax.inject.Inject;

import actors.TimeActor;
import actors.UserActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.*;


import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	@Inject private ActorSystem actorSystem;
    @Inject private Materializer materializer; 
    
	@Inject public HomeController(ActorSystem system) {
    	system.actorOf(TimeActor.props(), "timeActor");
    }
	public Result index() {
		return ok(index.render(request()));
	}
	   
	public WebSocket ws() {
		return WebSocket.Json.accept(request -> ActorFlow.actorRef(UserActor::props, actorSystem, materializer));
	}

}
