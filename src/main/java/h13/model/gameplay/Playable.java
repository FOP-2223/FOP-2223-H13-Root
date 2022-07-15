package h13.model.gameplay;

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
    void update(final long now);

    /**
     * resumes the object after pausing. This is necessary, when the game resumes after a pause.
     *
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    void resume(final long now);
}
