
import java.awt.Rectangle;
import java.util.concurrent.locks.Lock;

import objectModel.ObjectMap;
import objectModel.Property;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import processing.core.PVector;
import synchronization.ObjectSynchronizationManager;

public class Collisions extends ActionMethod {

	public Collisions() {
		super();
		super.signature = new String[3];
		signature[0] = "Position";
		signature[1] = "BoundingBox";
		signature[2] = "ObjectMap";
	}

	@Override
	public String getName() {
		return "Collisions";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {
		
		// Unpack the parameters for this object.
		Object[] position = params.properties.get(0);
		Object[] boundingBox = params.properties.get(1);
		Object[] objects = params.properties.get(2);
		

		float x = (float) position[0];
		float y = (float) position[1];
		Rectangle box = (Rectangle) boundingBox[0];
		ObjectMap map = (ObjectMap) objects[0];
		
		//Translate the Objects rectangle.
		box.x = (int)(x - box.width*.5);
		box.y = (int)(y - box.height*.5);

		// Get the ObjectSynchronizationManager.
		ObjectSynchronizationManager oManager = ObjectSynchronizationManager
				.getManager();

		// Now get the rest of the objects
		Iterable<String> guids = oManager.getUnlockedForwardIteratorAfter(params.GUID);

		for (String guid : guids) {
			
			// If this is not the current object.
			if (guid != null && !guid.equals(params.GUID)) {

				// Lock the object.
				Lock lock = oManager.getLock(guid);
				lock.lock();

				try {
					Property rec = null;
					Property pos = null;
					Property col = null;
					try {
						rec = map.get("BoundingBox");
						pos = map.get("Position");
						col = map.get("Collisions");
					} catch (NullArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (rec != null && pos != null && col != null) {
						Object[] recToCheck = rec.getParameters(guid);
						Object[] posToCheck = pos.getParameters(guid);
						Object[] collisions = col.getParameters(guid);

						if (recToCheck != null && posToCheck != null && collisions != null && (boolean)collisions[0]) {
							Rectangle rectangleTwo = (Rectangle) recToCheck[0];
							float[] positionTwo = new float[2];
							positionTwo[0] = (float) posToCheck[0];
							positionTwo[1] = (float) posToCheck[1];
							
							
							//Xlate the rectangle.
							rectangleTwo.x = (int) (positionTwo[0] - rectangleTwo.width*.5);
							rectangleTwo.y = (int) (positionTwo[1] - rectangleTwo.height*.5);
							
							//Now we can check for collision.
							if(box.intersects(rectangleTwo)) {
								
								//Get the event manager.
								EventManager eManager = EventManager.getManager();
								
								//Create object parameters for both objects.
								ObjectParameter pOne = new ObjectParameter();
								pOne.GUID = params.GUID;
								ObjectParameter pTwo = new ObjectParameter();
								pTwo.GUID = guid;
								
								//Create a collision event parameter.
								EventParameter param = new EventParameter();
								param.objectParameters.add(pOne);
								param.objectParameters.add(pTwo);
								
								//Create the collision event.
								
								//Add the event to the EventManager's queue.
								eManager.addEvent(new Event("Collision", param));
								
							}
							
						}
					}
				} finally {
					// Unlock the object.
					lock.unlock();
				}
			}
		}

		return params;
	}

}
