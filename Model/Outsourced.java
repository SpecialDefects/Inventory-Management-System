package Model;
/**
 *
 * @author Cody Biller
 */
/** represents an outsourced part **/
public class Outsourced extends Part {
    private String companyName;

    /**
     *
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock available
     * @param min minimum amount of parts
     * @param max maximum amount of parts
     * @param companyName name of company that manufactures the part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     *
     * @param companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     *
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
