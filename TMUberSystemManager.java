
//Talal Malhi
//501256062
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TMUberSystemManager {
  private ArrayList<User> users;
  private ArrayList<Driver> drivers;

  private ArrayList<TMUberService> serviceRequests;

  public double totalRevenue; // Total revenues accumulated via rides and deliveries

  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  // These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager() {
    users = new ArrayList<User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<TMUberService>();

    TMUberRegistered.loadPreregisteredUsers(users);
    TMUberRegistered.loadPreregisteredDrivers(drivers);

    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is
  // invalid
  // (e.g. user does not exist, invalid address etc.)
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage() {
    return errMsg;
  }

  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId) {
    // Fill in the code
    for (User user : users) {
      if (user.getAccountId().equals(accountId)) {
        return user;
      }
    }
    return null;
  }

  // Check for duplicate user
  private boolean userExists(User user) {
    // Fill in the code
    for (User existingUser : users) {
      if (existingUser.equals(user)) {
        return true;
      }
    }

    return false;
  }

  // Check for duplicate driver
  private boolean driverExists(Driver driver) {
    // Fill in the code
    for (Driver existingDriver : drivers) {
      if (existingDriver.equals(driver)) {
        return true;
      }
    }

    return false;
  }

  // Given a user, check if user ride/delivery request already exists in service
  // requests
  private boolean existingRequest(TMUberService req) {
    // Fill in the code
    for (TMUberService requestAlreadyExists : serviceRequests) {
      if (requestAlreadyExists.equals(req)) {
        return true;
      }
    }

    return false;
  }

  // Calculate the cost of a ride or of a delivery based on distance
  private double getDeliveryCost(int distance) {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance) {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver() {
    // Fill in the code
    for (Driver driver : drivers) {
      if (driver.getStatus() == Driver.Status.AVAILABLE) {
        return driver;
      }
    }

    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers() {
    System.out.println();

    for (int i = 0; i < users.size(); i++) {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      users.get(i).printInfo();
      System.out.println();
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers() {
    // Fill in the code
    System.out.println();
    for (int i = 0; i < drivers.size(); i++) {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      System.out.println();
    }

  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests() {
    // Fill in the code
    System.out.println();

    for (int i = 0; i < serviceRequests.size(); i++) {
      System.out.print((i + 1) + ". ------------------------------------------------------------");
      serviceRequests.get(i).printInfo();
      System.out.println();
      System.out.println();
    }
  }

  // Add a new user to the system
  public boolean registerNewUser(String name, String address, double wallet) {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    // set errMsg string variable to "Invalid Address "
    // return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!
    if (name == null || name.isEmpty()) {
      errMsg = "Invalid User Name";
      return false;
    }

    if (address == null || address.isEmpty() || !CityMap.validAddress(address)) {
      errMsg = "Invalid User Address";
      return false;
    }

    if (wallet < 0) {
      errMsg = "Invalid Money in Wallet";
      return false;
    }

    User newRegisteredUser = new User(Integer.toString(userAccountId++), name, address, wallet);

    if (userExists(newRegisteredUser)) {
      errMsg = "User Already Exists in System";
      return false;
    }

    users.add(newRegisteredUser);
    return true;
  }

  // Add a new driver to the system
  public boolean registerNewDriver(String name, String carModel, String carLicencePlate) {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser

    if (name == null || name.isEmpty()) {
      errMsg = "Invalid Driver Name";
      return false;
    }

    if (carModel == null || carModel.isEmpty()) {
      errMsg = "Invalid Car Model";
      return false;
    }

    if (carLicencePlate == null || carLicencePlate.isEmpty()) {
      errMsg = "Invalid Car Licence Plate";
      return false;
    }

    String newDriverId = TMUberRegistered.generateDriverId(drivers);
    Driver newRegisteredDriver = new Driver(newDriverId, name, carModel, carLicencePlate);

    if (driverExists(newRegisteredDriver)) {
      errMsg = "Driver Already Exists in System";
      return false;
    }

    drivers.add(newRegisteredDriver);
    return true;
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public boolean requestRide(String accountId, String from, String to) {
    // Check for valid parameters
    // Use the account id to find the user object in the list of users
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    // Find an available driver
    // Create the TMUberRide object
    // Check if existing ride request for this user - only one ride request per user
    // at a time!
    // Change driver status
    // Add the ride request to the list of requests
    // Increment the number of rides for this user
    User user = getUser(accountId);
    Driver driver = getAvailableDriver();
    int distance = CityMap.getDistance(from, to);
    double serviceCost = getRideCost(distance);

    if (user == null) {
      errMsg = "User Account Not Found";
      return false;
    }

    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {
      errMsg = "Invalid Address";
      return false;
    }

    TMUberRide requestRide = new TMUberRide(driver, from, to, user, distance, serviceCost);

    if (existingRequest(requestRide)) {
      errMsg = "User Already Has Ride Request";
      return false;
    }

    if (driver == null) {
      errMsg = "No Drivers Available";
      return false;
    }

    if (distance <= 1) {
      errMsg = "Insufficient Travel Distance";
      return false;
    }

    if (user.getWallet() < serviceCost) {
      errMsg = "Insufficient Funds";
      return false;
    }

    driver.setStatus(Driver.Status.DRIVING);
    serviceRequests.add(requestRide);
    user.addRide();

    return true;
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId) {
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food
    // order id
    // Increment the number of deliveries the user has had
    User user = getUser(accountId);
    int distance = CityMap.getDistance(from, to);
    Driver driver = getAvailableDriver();
    TMUberDelivery foodDelivery = new TMUberDelivery(driver, from, to, user, distance, getDeliveryCost(distance),
        restaurant, foodOrderId);
    double serviceCost = getRideCost(foodDelivery.getDistance());

    if (existingRequest(foodDelivery)) {
      errMsg = "User Already Has Delivery Request at Restaurant with this Food Order";
      return false;
    }

    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {
      errMsg = "Invalid Address";
      return false;
    }

    if (driver == null) {
      errMsg = "No Drivers Available";
      return false;
    }

    if (user == null) {
      errMsg = "User Account Not Found";
      return false;
    }

    if (distance <= 1) {
      errMsg = "Insufficient Travel Distance";
      return false;
    }

    if (user.getWallet() < serviceCost) {
      errMsg = "Insufficient Funds";
      return false;
    }

    driver.setStatus(Driver.Status.DRIVING);

    serviceRequests.add(foodDelivery);

    user.addDelivery();

    return true;
  }

  // Cancel an existing service request.
  // parameter int request is the index in the serviceRequests array list
  public boolean cancelServiceRequest(int request) {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    int index = request - 1;
    TMUberService serviceRequest = serviceRequests.remove(index);
    User user = serviceRequest.getUser();

    if (serviceRequests.isEmpty()) {
      errMsg = "No Service Requests Available";
      return false;
    }

    if (index < 0 || index > serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
    }

    if (serviceRequest instanceof TMUberRide) {
      user.decrementRides();
    }

    else if (serviceRequest instanceof TMUberDelivery) {
      user.decrementDeliveries();
    }

    return true;
  }

  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(int request) {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    int index = request - 1;
    TMUberService serviceRequest = serviceRequests.get(index);
    double serviceCost = 0;
    Driver driver = serviceRequest.getDriver();
    User user = serviceRequest.getUser();

    if (index < 0 || index > serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
    }

    if (serviceRequest instanceof TMUberRide) {
      TMUberRide newRide = (TMUberRide) serviceRequest;
      serviceCost = getRideCost(newRide.getDistance());
    }

    else if (serviceRequest instanceof TMUberDelivery) {
      TMUberDelivery newDelivery = (TMUberDelivery) serviceRequest;
      serviceCost = getDeliveryCost(newDelivery.getDistance());
    }

    double driverPayment = serviceCost * PAYRATE;

    totalRevenue += serviceCost;
    totalRevenue -= driverPayment;
    user.payForService(serviceCost);
    driver.pay(driverPayment);
    serviceRequests.remove(index);
    driver.setStatus(Driver.Status.AVAILABLE);

    return true;
  }

  // Sort users by name
  // Then list all users
  public void sortByUserName() {
    Collections.sort(users, new NameComparator());

    System.out.println("Users sorted by name: ");

    for (User user : users) {
      user.printInfo();
      System.out.println();
    }
  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User> {
    public int compare(User user1, User user2) {
      String name1 = user1.getName();
      String name2 = user2.getName();

      return name1.compareTo(name2);
    }
  }

  // Sort users by number amount in wallet
  // Then ist all users
  public void sortByWallet() {
    Collections.sort(users, new UserWalletComparator());

    System.out.println("Users sorted by wallet amount: ");
    for (User user : users) {
      user.printInfo();
      System.out.println();
    }
  }

  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator<User> {
    public int compare(User userWallet1, User userWallet2) {
      return Double.compare(userWallet1.getWallet(), userWallet2.getWallet());
    }
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance() {

    Collections.sort(serviceRequests, new DistanceComparator());

    System.out.println("Service requests sorted by distance: ");

    for (TMUberService service : serviceRequests) {
      service.printInfo();
      System.out.println();
    }

  }

  private class DistanceComparator implements Comparator<TMUberService> {
    public int compare(TMUberService service1, TMUberService service2) {
      return Double.compare(service1.getDistance(), service2.getDistance());
    }
  }

}
