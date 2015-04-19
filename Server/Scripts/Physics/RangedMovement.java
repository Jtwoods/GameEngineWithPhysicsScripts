
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import processing.core.PVector;

public class RangedMovement extends ActionMethod {

	public RangedMovement() {
		super();
		signature = new String[4];
		signature[0] = "Velocity";
		signature[1] = "Position";
		signature[2] = "Range";
		signature[3] = "BoundingBox";
	}
	
	@Override
	public String getName() {
		return "RangedMovement";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {
		
		PVector velocity = (PVector)params.properties.get(0)[0];
		Object[] position = (Object[])params.properties.get(1);
		Object[] range = (Object[])params.properties.get(2);
		

		if((float)position[0] < (float)range[0]) {
			velocity = new PVector(adjustVelocity(velocity.x), velocity.y, 0);
			position[0] = range[0];
			
			//////////////////// FIRST EVENT TEST: Note how many operations it takes
			//////////////////// just to create an event with one parameter.
			
			
//			//Test the event handling.
//			//Get the EventManager.
//			EventManager eManager = EventManager.getManager();
//			//Create the event parameter.
//			EventParameter eParameter = new EventParameter();
//			//Create an object parameter.
//			ObjectParameter toAdd = new ObjectParameter();
//			//Add the GUID of the object to the object parameter.
//			toAdd.GUID = params.GUID;
//			//Add the ObjectParameter to the EventParameter.
//			eParameter.objectParameters.add(toAdd);
//			//Add an Event, with the parameter, to the EventManager queue.
//			eManager.addEvent(new Event("Test", eParameter));
			
		} else if ((float)position[0] > (float)range[1]) {
			velocity = new PVector( adjustVelocity(velocity.x), velocity.y, 0);
			position[0] = range[1];
		}
		if((float)position[1] < (float)range[2]) {
			velocity = new PVector(velocity.x,  adjustVelocity(velocity.y), 0);
			position[1] = range[2];
		}else if ((float)position[1] > (float)range[3]) {
			velocity = new PVector(velocity.x, adjustVelocity(velocity.y), 0);
			position[1] = range[3];
		}
		
		Object[] toAddOne = new Object[1];
		toAddOne[0] = (Object)velocity;
		
		Object[] toAddTwo = new Object[2];
		toAddTwo[0] = position[0];
		toAddTwo[1] = position[1];
		
		params.properties.set(0, toAddOne);
		params.properties.set(1, toAddTwo);
		
		return params;
	}
	
	public float adjustVelocity(float vel) {
		return  -vel;
	}
}
