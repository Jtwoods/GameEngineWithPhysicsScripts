
import eventModel.EventMethod;
import eventModel.EventParameter;
import eventModel.ObjectParameter;
import processing.core.PConstants;
import processing.core.PVector;

public class KeyPressed extends EventMethod {

	public KeyPressed() {
		super();
		super.signature = new String[5];
		signature[0] = "Velocity";
		signature[1] = "Acceleration";
		signature[2] = "OnTop";
		signature[3] = "MAX_ACCEL";
		signature[4] = "JUMP_VEL";
	}
	
	@Override
	public String getName() {
		return "KeyPressed";
	}

	@Override
	public EventParameter invoke(EventParameter params) {
		
		int KeyPress = (int) params.additional[0];
		int Key = (char) params.additional[1];
		
		ObjectParameter player = params.objectParameters.get(0);
		
		boolean onTop = (boolean)player.properties.get(2)[0];
		
		if(KeyPress == PConstants.LEFT) {
			//Unpack the params.
			PVector acceleration = (PVector)player.properties.get(1)[0];
			float max_accel = (float)player.properties.get(3)[0];
			
			//Modify them.
			acceleration = new PVector(-max_accel, acceleration.y, 0);
			
			//Pack them up.
			Object[] toReturn = new Object[1];
			toReturn[0] = acceleration;
			player.properties.set(1, toReturn);
			params.objectParameters.set(0, player);
			
		}
		else if(KeyPress == PConstants.RIGHT) {
			
			//Unpack the params.
			PVector acceleration = (PVector)player.properties.get(1)[0];
			float max_accel = (float)player.properties.get(3)[0];
			
			//Modify them.
			acceleration = new PVector(max_accel, acceleration.y, 0);
			
			//Pack them up.
			Object[] toReturn = new Object[1];
			toReturn[0] = acceleration;
			player.properties.set(1, toReturn);
			params.objectParameters.set(0, player);
			
		}
		if(Key == ' ' && onTop) {
			//Unpack the params.
			PVector velocity = (PVector)player.properties.get(0)[0];
			float jump_vel = (float)player.properties.get(4)[0];
			
			//Modify them.
			velocity = new PVector(velocity.x, jump_vel, 0);
			
			//Pack them up.
			Object[] toReturn = new Object[1];
			toReturn[0] = velocity;
			Object[] onTopReturn = new Object[1];
			onTopReturn[0] = onTop;
			player.properties.set(0, toReturn);
			player.properties.set(2, onTopReturn);
			params.objectParameters.set(0, player);
		}
		
		return params;
	}

}