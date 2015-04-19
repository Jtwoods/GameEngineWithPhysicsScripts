
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import processing.core.PConstants;
import processing.core.PVector;

public class KeyReleased extends EventMethod {

	public KeyReleased() {
		super();
		super.signature = new String[1];
		signature[0] = "Acceleration";
	}
	@Override
	public String getName() {
		return "KeyReleased";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		
		int KeyPress = (int) params.additional[0];
		
		ObjectParameter player = params.objectParameters.get(0);
		
		if(KeyPress == PConstants.RIGHT || KeyPress == PConstants.LEFT) {
			//Unpack the params.
			PVector acceleration = (PVector)player.properties.get(0)[0];
			
			//Modify them.
			acceleration = new PVector(0, acceleration.y, 0);
			
			//Pack them up.
			Object[] toReturn = new Object[1];
			toReturn[0] = acceleration;
			player.properties.set(0, toReturn);
			params.objectParameters.set(0, player);
		}
		
		
		return params;
	}

}
