
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import objectModel.ObjectMap;
import objectModel.Property;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;
import processing.core.PVector;

public class OnTop extends EventMethod {

	// This is how far from the top of the object the character can be
	// While still being considered on top of it.
	private static float HEIGHT_RANGE = 0;

	public OnTop() {
		super();
		super.signature = new String[5];
		signature[0] = "OnTop";
		signature[1] = "Position";
		signature[2] = "BoundingBox";
		signature[3] = "ObjectMap";
		signature[4] = "Velocity";
	}

	@Override
	public String getName() {
		return "OnTop";
	}

	@Override
	public EventParameter invoke(EventParameter params) {

		// Create a flag to store whether the character is on top of the
		// obstacle or not.
		boolean isOn = false;

		// Unpack the characters parameters.
		ObjectParameter character = params.objectParameters.get(0);

		if (character != null) {
			boolean onTop = (boolean) character.properties.get(0)[0];
			float x = (float) character.properties.get(1)[0];
			float y = (float) character.properties.get(1)[1];
			Rectangle recOne = (Rectangle) character.properties.get(2)[0];

			ObjectMap oMap = (ObjectMap) character.properties.get(3)[0];

			PVector vel = (PVector) character.properties.get(4)[0];

			String obstacleGuid = (String) params.additional[0];

			// Adjust y.
			y += recOne.height * .5f;

			// Get the position and bounding box properties for the obstacle.
			Property pos;
			try {
				pos = oMap.get("Position");

				Property rect = oMap.get("BoundingBox");

				Object[] obsPos = pos.getParameters(obstacleGuid);

				float xObs = (float) obsPos[0];
				float yObs = (float) obsPos[1];

				Rectangle objRec = (Rectangle) rect.getParameters(obstacleGuid)[0];

				// Check to make sure the character is still on top of the
				// obstacle.

				// Is above or close to.
				if (y + .5 * recOne.height > (yObs - .5 * objRec.height)) {

					// Must be on top of the box.
					if (x + recOne.width * .5f > (xObs - .5 * objRec.width)
							&& x - recOne.width * .5f < (xObs + .5f * objRec.width)) {

						if (vel.y >= 0) {
							vel = new PVector(vel.x, 0, 0);			
						}
						
						y -= (y + .5 * recOne.height)
								- (yObs - .5 * objRec.height);

						isOn = true;
					}

				}

			} catch (NullArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (isOn) {
				// Create an OnTop event.

				// Get the event manager.
				EventManager eManager = EventManager.getManager();

				// Add the event to the EventManager's queue.
				eManager.addEvent(new Event("OnTop", params));
			}

			// Now we can set onTop as needed.
			onTop = isOn;

			// Pack the parameters back into the EventParameter.
			Object[] onePosReturn = new Object[2];
			onePosReturn[0] = x;
			onePosReturn[1] = y;
			Object[] oneVelReturn = new Object[1];
			oneVelReturn[0] = vel;
			Object[] onReturn = new Object[1];
			onReturn[0] = onTop;
			Object[] recReturn = new Object[1];
			recReturn[0] = recOne;
			Object[] map = new Object[1];
			map[0] = oMap;

			character.parameterList = character.parameterList;
			character.properties.set(0, onReturn);
			character.properties.set(1, onePosReturn);
			character.properties.set(2, recReturn);
			character.properties.set(3, map);
			character.properties.set(4, oneVelReturn);
			character.GUID = character.GUID;

			params.objectParameters.set(0, character);
		}
		return params;

	}
}
