
import actionModel.ActionMethod;
import actionModel.ActionParameter;
import processing.core.PVector;

public class Accelerations extends ActionMethod {

	private static final float MAX_VEL = .8f;

	public Accelerations() {
		super();
		signature = new String[3];
		signature[0] = "Velocity";
		signature[1] = "Acceleration";
		signature[2] = "Position";
	}

	@Override
	public String getName() {
		return "Accelerations";
	}

	@Override
	public ActionParameter invoke(ActionParameter params) {

		// Unpack everything.
		PVector velocity = (PVector) params.properties.get(0)[0];
		PVector acceleration = (PVector) params.properties.get(1)[0];
		Object[] position = (Object[]) params.properties.get(2);

		float x = (float) position[0];
		float y = (float) position[1];

		// Do calculations.
		if (acceleration.mag() != 0) {
			float xVelFinal = (float)(velocity.x + acceleration.x * params.time);
			float yVelFinal = (float)(velocity.y + acceleration.y * params.time);

			x += .5 * (xVelFinal + velocity.x) * params.time;
			y += .5 * (yVelFinal + velocity.y) * params.time;

			velocity = new PVector(xVelFinal, yVelFinal, 0);

			if (velocity.mag() > MAX_VEL) {
				velocity.normalize();
				velocity.setMag(MAX_VEL);
			} else if (velocity.mag() < -MAX_VEL) {
				velocity.normalize();
				velocity.setMag(-MAX_VEL);
			}
		} else {

			x += (velocity.x) * params.time;
			y += (velocity.y) * params.time;
		}

		// Record and re-pack results.
		Object[] vel = new Object[1];
		vel[0] = (Object) velocity;

		position[0] = x;
		position[1] = y;

		params.properties.set(0, vel);
		params.properties.set(2, position);

		return params;
	}

}
