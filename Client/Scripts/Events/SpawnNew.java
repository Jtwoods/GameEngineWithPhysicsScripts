
import objectModel.ObjectMap;
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import factories.GameObjectFactory;



public class SpawnNew extends EventMethod {

	public SpawnNew() {
		super();
		signature = new String[1];
		signature[0] = "ObjectMap";
	}
	@Override
	public String getName() {
		return "SpawnNew";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		//UnPack the object map.
		 ObjectParameter toResolve = params.objectParameters.get(0);
		 ObjectMap map = (ObjectMap) toResolve.properties.get(0)[0];
		 
		 //Get the object factory.
		 GameObjectFactory factory = GameObjectFactory.getFactory();
		 map.addCharacter(factory.buildCharacter(toResolve.GUID, "NewPlayer", "playerObject.xml"), toResolve.GUID);
		 
		 //Now we create a spawn event to spawn the character on the map.
			// Get the event manager.
			EventManager eManager = EventManager.getManager();
			

			
			// Create object parameters for the player.
			ObjectParameter pOne = new ObjectParameter();
			pOne.GUID = toResolve.GUID;

			// Create a Spawn event parameter.
			EventParameter param = new EventParameter();
			param.objectParameters.add(pOne);
			
			param.additional = new Object[1];
			//Add the time with an additional elapsed time to wait before spawning.
			param.additional[0] = (double)params.additional[0];
			
			// Add the event to the EventManager's queue.
			eManager.addEvent(new Event("Spawn", param));
		 
		//Pack up the parameters.
		return params;
	}

}
