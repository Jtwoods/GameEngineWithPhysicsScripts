
import java.awt.Rectangle;

import objectModel.ObjectMap;
import objectModel.Property;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;
import processing.core.PVector;

public class Collision extends EventMethod {

	private static final float BUFFER = 25;

	public Collision() {
		super();
		super.signature = new String[5];
		signature[0] = "Position";
		signature[1] = "Velocity";
		signature[2] = "BoundingBox";
		signature[3] = "CollisionType";
		signature[4] = "ObjectMap";

		// Time is being passed in the additional block at index 0.
	}

	@Override
	public String getName() {
		return "Collision";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		// Unpack object one's parameters.
		ObjectParameter one = params.objectParameters.get(0);
		float xOne = (float) one.properties.get(0)[0];
		float yOne = (float) one.properties.get(0)[1];
		PVector velOne = (PVector) one.properties.get(1)[0];
		Rectangle recOne = (Rectangle) one.properties.get(2)[0];
		String typeOne = (String) one.properties.get(3)[0];

		ObjectParameter two = params.objectParameters.get(1);
		float xTwo = (float) two.properties.get(0)[0];
		float yTwo = (float) two.properties.get(0)[1];
		PVector velTwo = (PVector) two.properties.get(1)[0];
		Rectangle recTwo = (Rectangle) two.properties.get(2)[0];
		String typeTwo = (String) two.properties.get(3)[0];
		ObjectMap map = (ObjectMap) two.properties.get(4)[0];

		if ("Character".equals(typeOne) || "Character".equals(typeTwo)) {

			if ("Character".equals(typeOne) && "DeathZone".equals(typeTwo)) {

				// Create a Death event.

				// Get the event manager.
				EventManager eManager = EventManager.getManager();

				// Create object parameters for the character.
				ObjectParameter pOne = new ObjectParameter();
				pOne.GUID = one.GUID;

				// Create a Death event parameter.
				EventParameter param = new EventParameter();
				param.objectParameters.add(pOne);
				// Set the time.
				param.additional = params.additional;

				// Add the event to the EventManager's queue.
				eManager.addEvent(new Event("Death", param));

			} else if ("Character".equals(typeTwo)
					&& "DeathZone".equals(typeOne)) {

				// Create a Death event.

				// Get the event manager.
				EventManager eManager = EventManager.getManager();

				// Create object parameters for the character.
				ObjectParameter pOne = new ObjectParameter();
				pOne.GUID = two.GUID;

				// Create a Death event parameter.
				EventParameter param = new EventParameter();
				param.objectParameters.add(pOne);

				// Add the event to the EventManager's queue.
				eManager.addEvent(new Event("Death", param));

			} else if ("Character".equals(typeOne)) {

				if ("Character".equals(typeTwo)) {

					// Character on character impact.
					float dx = 0;
					float dy = 0;
					float mx = 0;
					float my = 0;

					if (xTwo > xOne) {
						mx = (float) ((xTwo - .5 * recTwo.width) - (xOne + .5 * recOne.width));
					} else {
						mx = (float) ((xOne - .5 * recOne.width) - (xTwo + .5 * recTwo.width));
					}

					if (yTwo > yOne) {
						my = (float) ((yTwo - .5 * recTwo.height) - (yOne + .5 * recOne.height));
					} else {
						my = (float) ((yOne - .5 * recOne.height) - (yTwo + .5 * recTwo.height));
					}

					// Get dx and dy.
					dx = xTwo - xOne;
					dy = yTwo - yOne;

					PVector un = new PVector(dx, dy, 0);
					un = un.normalize(un);

					// Reposition them.
					if (my <= 0) {
						if (yTwo > yOne) {
							yOne += my * .6f;
							yTwo -= my * .6f;
						} else {
							yOne -= my * .6f;
							yTwo += my * .6f;
						}
					}
					if (mx <= 0) {
						if (xTwo > xOne) {
							xOne += mx * .6f;
							xTwo -= mx * .6f;
						} else {
							xOne -= mx * .6f;
							xTwo += mx * .6f;
						}
					}

					// With some help from:
					// https://sites.google.com/site/t3hprogrammer/research/circle-circle-collision-tutorial
					float i = velOne.x * un.x + velOne.y * un.y - velTwo.x
							* un.x - velTwo.y * un.y;

					velOne = new PVector(velOne.x - i * un.x, velOne.y - i
							* un.y, 0);
					velTwo = new PVector(velTwo.x + i * un.x, velTwo.y + i
							* un.y, 0);
					// End of borrowed code.
				} else {
					// Character one on obstacle.

					float oneLeft = xOne - recOne.width * .5f;
					float oneRight = xOne + recOne.width * .5f;
					float oneBottom = yOne + recOne.height * .5f;
					float oneTop = yOne - recOne.height * .5f;

					float twoBottom = yTwo + recTwo.height * .5f;
					float twoTop = yTwo - recTwo.height * .5f;
					float twoLeft = xTwo - recTwo.width * .5f;
					float twoRight = xTwo + recTwo.width * .5f;

					// Is one above.
					if (yOne - BUFFER < twoTop && oneRight > twoLeft
							&& oneLeft < twoRight) {

						boolean onTop = false;
						try {
							onTop = (boolean) map.get("OnTop").getParameters(
									one.GUID)[0];

						} catch (NullArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (!onTop) {
							// Create an OnTop event.

							// Get the event manager.
							EventManager eManager = EventManager.getManager();

							// Create object parameters for both objects.
							ObjectParameter pOne = new ObjectParameter();
							pOne.GUID = one.GUID;

							// Create an OnTop event parameter.
							EventParameter param = new EventParameter();
							param.objectParameters.add(pOne);

							// We will add the GUID of the obstacle as an
							// additional parameter.
							param.additional = new Object[1];
							param.additional[0] = two.GUID;

							// Add the event to the EventManager's queue.
							eManager.addEvent(new Event("OnTop", param));
						}
					} else if (yOne + BUFFER > twoBottom && oneRight > twoLeft
							&& oneLeft < twoRight) {
						// This is the bottom of the box.
						if (velOne.y < 0) {
							velOne = new PVector(velOne.x, -velOne.y, 0);
						}
					} else if (oneTop > twoBottom && oneBottom < twoTop) {

						if (oneLeft < twoLeft) {
							xOne -= oneRight - twoLeft;
							if (velOne.x > 0)
								velOne = new PVector(-velOne.x, velOne.y, 0);
						} else if (oneRight > twoRight) {
							xOne += twoRight - oneLeft;
							if (velOne.x < 0)
								velOne = new PVector(-velOne.x, velOne.y, 0);
						}
					}
				}

			} else {
				// two is a character.

				float oneLeft = xOne - recOne.width * .5f;
				float oneRight = xOne + recOne.width * .5f;
				float oneBottom = yOne + recOne.height * .5f;
				float oneTop = yOne - recOne.height * .5f;

				float twoLeft = xTwo - recTwo.width * .5f;
				float twoRight = xTwo + recTwo.width * .5f;
				float twoBottom = yOne + recTwo.height * .5f;
				float twoTop = yOne - recTwo.height * .5f;

				// Is above.
				if (yTwo - BUFFER < oneTop && twoRight > oneLeft
						&& twoLeft < oneRight) {

					boolean onTop = false;
					try {
						onTop = (boolean) map.get("OnTop").getParameters(
								two.GUID)[0];

					} catch (NullArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!onTop) {
						// Create an OnTop event.
						// Get the event manager.
						EventManager eManager = EventManager.getManager();

						// Create object parameters for both objects.
						ObjectParameter pOne = new ObjectParameter();
						pOne.GUID = two.GUID;

						// Create an OnTop event parameter.
						EventParameter param = new EventParameter();
						param.objectParameters.add(pOne);

						// We will add the GUID of the obstacle as an
						// additional parameter.
						param.additional = new Object[1];
						param.additional[0] = one.GUID;

						// Add the event to the EventManager's queue.
						eManager.addEvent(new Event("OnTop", param));
					}
				} else if (yTwo + BUFFER > oneBottom && twoRight > oneLeft
						&& twoLeft < oneRight) {

					// This is the bottom of the box.
					if (velTwo.y < 0)
						velTwo = new PVector(velTwo.x, -velTwo.y, 0);

				} else if (oneTop < twoBottom && oneBottom > twoTop) {

					if (twoLeft < oneLeft) {
						xTwo -= twoRight - oneLeft;
						if (velTwo.x > 0)
							velTwo = new PVector(-velTwo.x, velTwo.y, 0);
					} else if (twoRight > oneRight) {
						xTwo += oneRight - twoLeft;
						if (velTwo.x < 0)
							velTwo = new PVector(-velTwo.x, velTwo.y, 0);
					}
				}

			}
		}

		// Pack up the parameters.
		Object[] onePosReturn = new Object[2];
		onePosReturn[0] = xOne;
		onePosReturn[1] = yOne;
		Object[] oneVelReturn = new Object[1];
		oneVelReturn[0] = velOne;
		one.properties.set(0, onePosReturn);
		one.properties.set(1, oneVelReturn);

		Object[] twoPosReturn = new Object[2];
		twoPosReturn[0] = xTwo;
		twoPosReturn[1] = yTwo;
		Object[] twoVelReturn = new Object[1];
		twoVelReturn[0] = velTwo;
		two.properties.set(0, twoPosReturn);
		two.properties.set(1, twoVelReturn);

		params.objectParameters.set(0, one);
		params.objectParameters.set(1, two);

		return params;
	}
}
