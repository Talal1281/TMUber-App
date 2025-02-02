//Talal Malhi
//501256062
abstract public class TMUberService {
  private Driver driver;
  private String from;
  private String to;
  private User user;
  private String type; // Currently Ride or Delivery but other services could be added
  private int distance; // Units are City Blocks
  private double cost; // Cost of the service

  public TMUberService(Driver driver, String from, String to, User user, int distance, double cost, String type) {
    this.driver = driver;
    this.from = from;
    this.to = to;
    this.user = user;
    this.distance = distance;
    this.cost = cost;
    this.type = type;
  }

  // Subclasses define their type (e.g. "RIDE" OR "DELIVERY")
  abstract public String getServiceType();

  // Getters and Setters
  public Driver getDriver() {
    return driver;
  }

  public void setDriver(Driver driver) {
    this.driver = driver;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  // Compare 2 service requests based on distance
  // Add the appropriate method
  public int compareTo(TMUberService other) {
    // Integer.compare to return the comparison result of the distance between the
    // current object and the other object.
    return Integer.compare(this.distance, other.distance);
  }

  // Check if 2 service requests are equal (this and other)
  // They are equal if its the same type and the same user
  // Make sure to check the type first
  public boolean equals(Object other) {
    // Fill in the code
    if (this == other) {
      return true;
    }

    if (!(other instanceof TMUberService)) {
      return false;
    }

    TMUberService serviceRequests = (TMUberService) other;

    return this.type.equals(serviceRequests.type) && this.user.equals(serviceRequests.user);
  }

  // Print Information
  public void printInfo() {
    System.out.printf("\nType: %-9s From: %-15s To: %-15s", type, from, to);
    System.out.print("\nUser: ");
    user.printInfo();
    System.out.print("\nDriver: ");
    driver.printInfo();
  }
}
