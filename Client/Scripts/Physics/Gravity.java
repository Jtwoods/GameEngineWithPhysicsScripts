
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import processing.core.PVector;

public class Gravity extends ActionMethod {

	private static final float MAX_GRAVITY = 0.000821f;

	public Gravity() {
		super();
		signature = new String[2];
		signature[0] = "Acceleration";
		signature[1] = "OnTop";
	}
	@Override
	public String getName() {
		return "Gravity";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {
		
		PVector accel = (PVector)params.properties.get(0)[0];
		boolean stable = (boolean)params.properties.get(1)[0];
		
		if(!stable) {
			accel = new PVector(accel.x, MAX_GRAVITY, 0);
			Object[] acceleration = new Object[1];
			acceleration[0] = accel;
			params.properties.set(0,acceleration);
		} else {
			accel = new PVector(accel.x, 0, 0);
			Object[] acceleration = new Object[1];
			acceleration[0] = accel;
			params.properties.set(0,acceleration);
		}
		
		return params;
	}

}
