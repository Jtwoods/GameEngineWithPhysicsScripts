
import eventModel.Event;
import eventModel.EventManager;
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;

public class Death extends EventMethod {

	public Death() {
		super();
		super.signature = new String[2];
		signature[0] = "Alive";
		signature[1] = "Position";
		
		//Time is added to the additional array at index 0;
	}
	
	@Override
	public String getName() {
		return "Death";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		
		//Unpack parameters.
		ObjectParameter player = params.objectParameters.get(0);
		
		boolean alive = (boolean)player.properties.get(0)[0];
		float x = (float) player.properties.get(1)[0];
		float y = (float) player.properties.get(1)[1];
		
		x = 10000;
		y = 10000;
		alive = false;
		
		//Create a spawn event.
		
		// Get the event manager.
		EventManager eManager = EventManager.getManager();
		

		
		// Create object parameters for the player.
		ObjectParameter pOne = new ObjectParameter();
		pOne.GUID = player.GUID;

		// Create a Spawn event parameter.
		EventParameter param = new EventParameter();
		param.objectParameters.add(pOne);
		
		param.additional = new Object[1];
		
		//Add the time with an additional elapsed time to wait before spawning.
		param.additional[0] = (double)300.0;
		
		// Add the event to the EventManager's queue.
		eManager.addEvent(new Event("Spawn", param));
		
		
		
		//Pack up the parameters.
		Object[] living = new Object[1];
		Object[] pos = new Object[2];
		
		living[0] = alive;
		pos[0] = x;
		pos[1] = y;
		
		player.properties.set(0, living);
		player.properties.set(1, pos);
		
		params.objectParameters.set(0, player);
		return params;
	}

}
