package PetriObj;

/**
 *
 * @author Anatoliy
 */
public class ModelingTimeState {
    private double currentTime;
    private double modelingTime;

    public ModelingTimeState() {
        currentTime = 0;
        modelingTime = Double.MAX_VALUE - 1;
    }

    public ModelingTimeState(double currentTime, double modelingTime) {
        this.currentTime = currentTime;
        this.modelingTime = modelingTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public double getModelingTime() {
        return modelingTime;
    }

    public void setModelingTime(double modelingTime) {
        this.modelingTime = modelingTime;
    }
}
