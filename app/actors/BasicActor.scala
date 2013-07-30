package actors

import akka.actor.Actor

case class Username(value : String)

class BasicActor extends Actor {
    def receive = {
        
        // We can handle Get messages
        case Username(value) => {
          	println("Basic Actor Got: "+ value)
            sender ! value.toUpperCase
        }
            
        
        // Unknown Message Handling
        case _ =>
            throw new Exception
        
    }
}
