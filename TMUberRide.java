//Talal Malhi
//501256062
public class TMUberRide extends TMUberService {
  private int numPassengers;
  private boolean requestedXL;

  public static final String TYPENAME = "RIDE";

  // Constructor to initialize all inherited and new instance variables
  public TMUberRide(Driver driver, String from, String to, User user, int distance, double cost) {
    // Fill in the code - make use of the super method
    super(driver, from, to, user, distance, cost, TYPENAME);
  }

  public String getServiceType() {
    return TYPENAME;
  }

  public int getNumPassengers() {
    return numPassengers;
  }

  public void setNumPassengers(int numPassengers) {
    this.numPassengers = numPassengers;
  }

  public boolean isRequestedXL() {
    return requestedXL;
  }

  public void setRequestedXL(boolean requestedXL) {
    this.requestedXL = requestedXL;
  }
}
