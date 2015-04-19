
import objectModel.ObjectMap;
import objectModel.Property;
import synchronization.ObjectSynchronizationManager;
import time.TimeManager;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import exceptions.NullArgumentException;

public class Spawn extends EventMethod {

	public Spawn() {
		super();
		signature = new String[2];
		signature[0] = "Position";
		signature[1] = "ObjectMap";
	}

	@Override
	public String getName() {
		return "Spawn";
	}

	@Override
	public EventParameter invoke(EventParameter params) {

		// Unpack.
		ObjectParameter character = params.objectParameters.get(0);
		float x = (float) character.properties.get(0)[0];
		float y = (float) character.properties.get(0)[1];
		ObjectMap map = (ObjectMap) character.properties.get(1)[0];
		
		//Create spawn point position.
		float spawnX = x;
		float spawnY = y;
		
		// Check the time.
		double time = (double) params.additional[0];

	
		double timeTo = (double)10.0;
		if (time <= timeTo) {
			
			// Get the list of spawn points.

			try {
				Property spawnPoints = map.get("SpawnPoint");
				// Get a spawn point.
				String guid = spawnPoints.getRandomGUID();
				
				if (guid != null) {
				
				// Get the position of a spawn point.
				Property positions = map.get("Position");
				Object[] pos = positions.getParameters(guid);
				
				spawnX = (float) pos[0];
				spawnY = (float) pos[1];
				
				}
				
				
				
			} catch (NullArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	


			// Move dead character to a random spawn position.
			x = spawnX;
			y = spawnY;

			// Pack up if needed.
			Object[] posToReturn = new Object[2];
			posToReturn[0] = x;
			posToReturn[1] = y;
			
			character.properties.set(0, posToReturn);
			params.objectParameters.set(0, character);
		} else {
			time -= timeTo;
			//Create another event.
			// Get the event manager.
			EventManager eManager = EventManager.getManager();
			

			
			// Create object parameters for the player.
			ObjectParameter pOne = new ObjectParameter();
			pOne.GUID = character.GUID;

			// Create a Spawn event parameter.
			EventParameter param = new EventParameter();
			param.objectParameters.add(pOne);
			
			param.additional = new Object[1];
			//Add the time with an additional elapsed time to wait before spawning.
			param.additional[0] = time;
			
			// Add the event to the EventManager's queue.
			eManager.addEvent(new Event("Spawn", param));
		}
		
		return params;
	}

}
