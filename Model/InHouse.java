package Model;
/**
 *
 * @author Cody Biller
 */
/** represents a inHouse part **/
public class InHouse extends Part {
    /** machineID **/
    private int machineId;

    /**
     *
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock available
     * @param min minimum amount of parts
     * @param max maximum amount of parts
     * @param machineId machineID part belongs to
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     *
     * @param machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     *
     * @return machineId
     */
    public int getMachineId() {
        return this.machineId;
    }
}
