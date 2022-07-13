package h13.model;

/**
 * An interface that defines the methods that a playable object must implement.
 * A Playable Object is an object that needs to update its state every frame.
 */
public interface Playable {
    /**
     * Updates the object.
     *
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    void update(long now);
}
