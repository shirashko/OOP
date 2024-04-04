package pepse.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Manages the energy levels of a game entity, such as the player's avatar.
 * This class is responsible for handling energy consumption, regeneration, and ensuring
 * that the energy level stays within predefined bounds. It also notifies an external listener
 * about any changes to the energy level.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
class EnergyManager {
    // Constants for the energy levels.
    private static final float MAX_ENERGY = 100; // Maximum energy level the entity can have.
    private static final float MIN_ENERGY = 0; // Minimum energy level the entity can have.

    // Fields
    private float energy; // Current energy level of the entity.

    // List of observers to notify when the energy level changes.
    private final List<Consumer<Float>> energyObservers = new ArrayList<>();

    /**
     * Constructs an EnergyManager with a specified listener for energy updates.
     * The maximum energy level is set to the default value.
     *
     */
    public EnergyManager() {
        this.energy = MAX_ENERGY; // Initializes the current energy level to the maximum.
    }


    /**
     * Consumes a specified amount of energy, ensuring that the energy level does not fall below the minimum.
     *
     * @param amount The amount of energy to be consumed.
     */
    public void consume(float amount) {
        adjustEnergy(-amount);
    }

    /**
     * Regenerates a specified amount of energy, ensuring that the energy level does not exceed the maximum.
     *
     * @param amount The amount of energy to be regenerated.
     */
    public void regenerate(float amount) {
        adjustEnergy(amount);
    }

    /**
     * Checks if the entity can consume a specified amount of energy without falling below the minimum
     * energy level.
     *
     * @param amount The amount of energy to check for consumption feasibility.
     * @return True if the entity has enough energy to consume the specified amount; otherwise, false.
     */
    public boolean canConsume(float amount) {
        return energy >= amount;
    }

    /**
     * Registers an observer to receive notifications of energy level changes.
     * @param observer The observer to be notified of energy level changes. It is a Consumer of Float
     *                 that accepts the new energy level as a parameter.
     */
    public void addEnergyObserver(Consumer<Float> observer) {
        this.energyObservers.add(observer);
        observer.accept(energy);
    }

    /**
     * Unregisters an observer from receiving notifications of energy level changes.
     * @param observer The observer to be removed.
     */
    public void removeEnergyObserver(Consumer<Float> observer) {
        this.energyObservers.remove(observer);
    }

    /*
     * Adjusts the current energy level by a specified amount, ensuring that the final energy level
     * remains within the defined minimum and maximum bounds. If the adjustment results in a change
     * in the energy level, all registered observers are notified of the new energy level.
     *
     * This method can be used both for consuming energy (by passing a negative amount) and for
     * regenerating energy (by passing a positive amount).
     *
     * @param amount The amount to adjust the energy level by. Positive values increase the energy level,
     *               while negative values decrease it. The method automatically clamps the adjusted
     *               energy level to stay within the [MIN_ENERGY, MAX_ENERGY] range.
     */
    private void adjustEnergy(float amount) {
        float oldEnergy = this.energy;
        // Adjust the energy level, ensuring it remains within bounds
        this.energy = Math.max(MIN_ENERGY, Math.min(MAX_ENERGY, this.energy + amount));

        // Check if the energy level actually changed, to avoid unnecessary notifications
        if (oldEnergy != this.energy) {
            // Notify all observers of the energy level change
            notifyAllEnergyObservers();
        }
    }

    /*
     * Notifies all registered observers of the current energy level.
     */
    private void notifyAllEnergyObservers() {
        for (Consumer<Float> listener : energyObservers) {
            listener.accept(energy);
        }
    }
}
