import java.io.*;
import java.util.*;

// Abstract Employee
abstract class Employee {
    int id;
    String name;

    Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    abstract double calculateSalary();
    abstract String toCSV();

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Salary: ₹" + calculateSalary();
    }
}

// Full-Time Employee
class FullTimeEmployee extends Employee {
    double monthlySalary;

    FullTimeEmployee(int id, String name, double monthlySalary) {
        super(id, name);
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    @Override
    double calculateSalary() {
        return monthlySalary;
    }

    @Override
    String toCSV() {
        return id + "," + name + ",FullTime," + monthlySalary + ",,";
    }
}

// Part-Time Employee
class PartTimeEmployee extends Employee {
    int hoursWorked;
    double hourlyRate;

    PartTimeEmployee(int id, String name, int hoursWorked, double hourlyRate) {
        super(id, name);
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    @Override
    double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    String toCSV() {
        return id + "," + name + ",PartTime,," + hoursWorked + "," + hourlyRate;
    }
}

// Payroll System Main
public class PayrollSystemFile {
    static ArrayList<Employee> employees = new ArrayList<>();
    static final String FILE_NAME = "employees.csv";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadFromFile();

        while (true) {
            System.out.println("\n===== Employee Payroll System =====");
            System.out.println("1. Add Full-Time Employee");
            System.out.println("2. Add Part-Time Employee");
            System.out.println("3. Remove Employee");
            System.out.println("4. Display All Employees");
            System.out.println("5. Display Full-Time Employees");
            System.out.println("6. Display Part-Time Employees");
            System.out.println("7. Show Total Monthly Payout");
            System.out.println("8. Find Employee by ID");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addFullTimeEmployee(sc);
                case 2 -> addPartTimeEmployee(sc);
                case 3 -> removeEmployee(sc);
                case 4 -> displayEmployees();
                case 5 -> displayFullTimeEmployees();
                case 6 -> displayPartTimeEmployees();
                case 7 -> showTotalPayout();
                case 8 -> findEmployeeById(sc);
                case 9 -> {
                    saveToFile();
                    System.out.println("Exiting. Data saved!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String type = parts[2];
                    if ("FullTime".equalsIgnoreCase(type)) {
                        double salary = Double.parseDouble(parts[3]);
                        employees.add(new FullTimeEmployee(id, name, salary));
                    } else {
                        int hours = Integer.parseInt(parts[4]);
                        double rate = Double.parseDouble(parts[5]);
                        employees.add(new PartTimeEmployee(id, name, hours, rate));
                    }
                }
                System.out.println("✅ Loaded " + employees.size() + " employees from file.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            createDefaultEmployees();
            saveToFile();
            System.out.println("✅ Created default 50 employees.");
        }
    }

    static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write("id,name,type,monthlySalary,hoursWorked,hourlyRate\n");
            for (Employee emp : employees) {
                bw.write(emp.toCSV() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add Employees
    static void addFullTimeEmployee(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Monthly Salary: ");
        double salary = sc.nextDouble();
        employees.add(new FullTimeEmployee(id, name, salary));
        saveToFile();
        System.out.println("✅ Full-Time Employee Added!");
    }

    static void addPartTimeEmployee(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Hours Worked: ");
        int hours = sc.nextInt();
        System.out.print("Enter Hourly Rate: ");
        double rate = sc.nextDouble();
        employees.add(new PartTimeEmployee(id, name, hours, rate));
        saveToFile();
        System.out.println("✅ Part-Time Employee Added!");
    }

    static void removeEmployee(Scanner sc) {
        System.out.print("Enter ID to Remove: ");
        int id = sc.nextInt();
        Employee toRemove = null;
        for (Employee emp : employees) {
            if (emp.id == id) {
                toRemove = emp;
                break;
            }
        }
        if (toRemove != null) {
            employees.remove(toRemove);
            saveToFile();
            System.out.println("✅ Employee Removed!");
        } else {
            System.out.println("❌ Employee not found!");
        }
    }

    // Display Methods
    static void displayEmployees() {
        System.out.println("\n--- All Employees ---");
        for (Employee emp : employees) System.out.println(emp);
    }

    static void displayFullTimeEmployees() {
        System.out.println("\n--- Full-Time Employees ---");
        for (Employee emp : employees) {
            if (emp instanceof FullTimeEmployee) System.out.println(emp);
        }
    }

    static void displayPartTimeEmployees() {
        System.out.println("\n--- Part-Time Employees ---");
        for (Employee emp : employees) {
            if (emp instanceof PartTimeEmployee) System.out.println(emp);
        }
    }

    static void showTotalPayout() {
        double total = 0;
        for (Employee emp : employees) total += emp.calculateSalary();
        System.out.println("💰 Total Monthly Payout: ₹" + total);
    }

    static void findEmployeeById(Scanner sc) {
        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();
        for (Employee emp : employees) {
            if (emp.id == id) {
                System.out.println("✅ Employee Found: " + emp);
                return;
            }
        }
        System.out.println("❌ Employee Not Found!");
    }

    // 50 Default Employees
    static void createDefaultEmployees() {
        String[] names = {
            "Aarav Sharma","Isha Verma","Rohan Patel","Priya Singh","Kiran Reddy",
            "Meena Kumari","Suresh Kumar","Neha Gupta","Vikram Joshi","Anjali Nair",
            "Arjun Mehta","Pooja Yadav","Ravi Teja","Sneha Rao","Karthik Iyer",
            "Shalini Pandey","Harish Choudhary","Divya Kapoor","Manoj Kumar","Swathi Reddy",
            "Nikhil Jain","Deepa Mishra","Ashok Kumar","Radha Sharma","Rahul Khanna",
            "Seema Bhat","Vinod Reddy","Kavita Nair","Ajay Singh","Ritu Verma",
            "Gaurav Gupta","Pallavi Joshi","Sanjay Sharma","Neelam Kumari","Amit Kumar",
            "Sunita Reddy","Rajesh Iyer","Komal Mehta","Shivam Patel","Anita Sharma",
            "Ramesh Kumar","Lata Yadav","Arvind Rao","Pinky Singh","Devendra Joshi",
            "Megha Kapoor","Siddharth Sharma","Geeta Kumari","Vivek Reddy","Nisha Patel"
        };
        for (int i = 0; i < 50; i++) {
            int id = 101 + i;
            if (i % 2 == 0) {
                double salary = 40000 + (i * 1000);
                employees.add(new FullTimeEmployee(id, names[i], salary));
            } else {
                int hours = 20 + (i % 10);
                double rate = 150 + (i * 5);
                employees.add(new PartTimeEmployee(id, names[i], hours, rate));
            }
        }
    }
}
