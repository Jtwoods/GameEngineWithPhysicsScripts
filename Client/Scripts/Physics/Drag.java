
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import processing.core.PVector;

public class Drag extends ActionMethod {

	private static final float DRAG_COOEF = .00065f;
	
	public Drag() {
		super();
		signature = new String[3];
		signature[0] = "Velocity";
		signature[1] = "Acceleration";
		signature[2] = "OnTop";
	}
	
	@Override
	public String getName() {
		return "Drag";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {
		//Unpack the parameters.
		PVector velocity = (PVector) params.properties.get(0)[0];
		PVector acceleration = (PVector) params.properties.get(1)[0];
		boolean onTop = (boolean) params.properties.get(2)[0];
		
		float dragX = 0;
		float dragY = 0;
		//Make the calculations.
		if(acceleration.x > 0)
		dragX = DRAG_COOEF*velocity.x*velocity.x;
		dragY = DRAG_COOEF*velocity.y*velocity.y;	
		
		if(velocity.x > 0)
			dragX = -dragX;
		if(velocity.y > 0)
			dragY = -dragY;
		
		if(Math.abs(velocity.mag()) < .5 && onTop) {
			velocity = new PVector(velocity.x,0,0);
		}
		
		float accelx = acceleration.x + dragX;
		float accely = acceleration.y + dragY;
		
		//Re-pack the parameters that need to be saved.
		PVector accelToSave = new PVector(accelx, accely, 0);
		Object[] toSaveAccel = new Object[1];
		Object[] toSaveVel = new Object[1];
		toSaveAccel[0] = accelToSave;
		toSaveVel[0] = velocity;
		
		params.properties.add(0, toSaveVel);
		params.properties.add(1, toSaveAccel);
		
		return params;
	}

}
