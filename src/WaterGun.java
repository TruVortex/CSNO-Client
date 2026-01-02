import java.util.ArrayList;
import java.util.Queue;

public class WaterGun {

    private final String NAME;
    private final int DAMAGE;
    private final int SIZE;
    private int ammo;
    private final double TIME_TO_FIRE;
    private final double MOVEMENT_ERROR;
    private final double RELOAD;
    private final boolean MELEE;
    private double lastTimeFired;
    private double lastTimeReloaded;

    /**
     * Creates a water gun with the specified parameters
     * @param name the name of the water gun
     * @param damage the damage of the water gun
     * @param size the maximum magazine size of the water gun
     * @param timeToFire the delay between shots of the water gun
     * @param movementError the degree of movement error of the water gun
     * @param reload the reload speed of the water gun
     * @param melee if the water gun is melee or not
     */
    public WaterGun(String name, int damage, int size, double timeToFire, int movementError, double reload, boolean melee) {
        NAME = name;
        DAMAGE = damage;
        SIZE = size;
        ammo = size;
        TIME_TO_FIRE = timeToFire;
        MOVEMENT_ERROR = movementError;
        RELOAD = reload;
        MELEE = melee;
    }

    /**
     * Shoots the water gun at the centre of the screen with possible movement error
     * @param polygons the polygons to shoot at
     * @param events the event queue
     * @param time the time the water gun was fired
     * @param moveXY if the player is moving in the XY-plane
     * @param moveZ if the player is moving in the Z-direction
     */
    public void shoot(ArrayList<DPolygon> polygons, Queue<String> events, double time, boolean moveXY, boolean moveZ) {
        if (ammo > 0 || MELEE) {
            if (time - lastTimeFired >= TIME_TO_FIRE) {
                double errorX = (moveXY ? Math.random() * MOVEMENT_ERROR - MOVEMENT_ERROR / 2 : 0) + (moveZ ? Math.random() * MOVEMENT_ERROR - MOVEMENT_ERROR / 2 : 0);
                double errorY = (moveXY ? Math.random() * MOVEMENT_ERROR - MOVEMENT_ERROR / 2 : 0) + (moveZ ? Math.random() * MOVEMENT_ERROR - MOVEMENT_ERROR / 2 : 0);
                for (int i = polygons.size() - 1; i >= 0; i--) {
                    if (polygons.get(i).isDrawn() && !polygons.get(i).isGhost() && polygons.get(i).contains(ClientFrame.SCREEN_SIZE.width / 2 + errorX, ClientFrame.SCREEN_SIZE.height / 2 + errorY)) {
                        if (polygons.get(i).isPlayer() && (!MELEE || polygons.get(i).getDist() < 5)) {
                            if (polygons.get(i).getID() < 0) {
                                events.add(-polygons.get(i).getID() + " " + 4 * DAMAGE);
                            } else {
                                events.add(polygons.get(i).getID() + " " + DAMAGE);
                            }
                        }
                        break;
                    }
                }
                if (!MELEE) {
                    lastTimeFired = time;
                    ammo--;
                }
            }
        }
    }

    /**
     * Sets {@code lastTimeFired} to the current time
     */
    public void reset() {
        lastTimeFired = System.currentTimeMillis() / 1000.0;
    }

    /**
     * Reloads the water gun
     * @param time the time the water gun was reloaded
     */
    public void reload(double time) {
        if (!isFull(time) && !isReloading()) {
            lastTimeReloaded = time;
        }
    }

    /**
     * Reloads the water gun instantaneously
     */
    public void instantReload() {
        ammo = SIZE;
    }

    /**
     * Cancels reloading the water gun
     */
    public void endReload() {
        lastTimeReloaded = 0;
    }

    /**
     * Returns if the player is currently reloading
     * @return if the player is currently reloading
     */
    public boolean isReloading() {
        return ammo != SIZE && lastTimeReloaded > lastTimeFired;
    }

    /**
     * Returns if the water gun is full
     * @param time the time that the fullness was checked
     * @return if the water gun is full
     */
    private boolean isFull(double time) {
        return time - lastTimeReloaded >= RELOAD && isReloading();
    }

    /**
     * Returns the amount of ammo in the water gun
     * @param time the time that the amount of ammo was checked
     * @return the amount of ammo in the water gun
     */
    public int getAmmo(double time) {
        if (isFull(time)) {
            ammo = SIZE;
        }
        return ammo;
    }

    /**
     * Returns the percentage of reloading completed
     * @param time the time that the percentage of reloading was checked
     * @return the percentage of reloading completed
     */
    public double getReloadProgress(double time) {
        return (time - lastTimeReloaded) / RELOAD;
    }

    public int getSize() {
        return SIZE;
    }

    public String getName() {
        return NAME;
    }
}
