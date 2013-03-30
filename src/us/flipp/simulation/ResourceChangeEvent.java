package us.flipp.simulation;


public interface ResourceChangeEvent {
    public void notifyOfResourceChange(Resource resource, int amount);

}
