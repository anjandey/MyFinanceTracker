import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FinanceTracker {
    private double income;
    private List<Expense> expenses;

    // Constructor to initialize the FinanceTracker
    public FinanceTracker() {
        expenses = new ArrayList<>();
    }

    // Method to set the monthly income
    public void setIncome(double income) {
        if (income < 0) {
            throw new IllegalArgumentException("Income must be a positive number");
        }
        this.income = income;
    }

    // Method to add an expense
    public void addExpense(String category, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Expense amount must be a positive number");
        }
        expenses.add(new Expense(category, amount));
    }

    // Method to calculate total expenses
    public double calculateTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    // Method to calculate remaining balance
    public double calculateRemainingBalance() {
        return income - calculateTotalExpenses();
    }

    // Method to display financial summary
    public void displaySummary() {
        System.out.println("Financial Summary:");
        System.out.printf("Income: $%.2f\n", income);
        System.out.printf("Total Expenses: $%.2f\n", calculateTotalExpenses());
        System.out.printf("Remaining Balance: $%.2f\n", calculateRemainingBalance());

        // Sort expenses by percentage of income
        Collections.sort(expenses, (e1, e2) -> Double.compare(e2.getPercentageOfIncome(income), e1.getPercentageOfIncome(income)));

        System.out.println("\nExpense Breakdown:");
        for (Expense expense : expenses) {
            System.out.printf("%s: $%.2f (%.2f%%)\n", expense.getCategory(), expense.getAmount(), expense.getPercentageOfIncome(income));
        }

        // Provide savings recommendation
        double remainingBalance = calculateRemainingBalance();
        if (remainingBalance > 0) {
            double recommendedSavings = remainingBalance * 0.2;
            System.out.printf("\nRecommended Savings: $%.2f\n", recommendedSavings);
        } else {
            System.out.println("\nWarning: You are overspending. Consider reducing expenses.");
        }

        // Provide advice on high expense categories
        if (!expenses.isEmpty() && expenses.get(0).getPercentageOfIncome(income) > 30) {
            System.out.printf("Consider reducing spending on %s as it accounts for a large portion of your income.\n", expenses.get(0).getCategory());
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FinanceTracker tracker = new FinanceTracker();

        System.out.print("Enter your monthly income: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Consume the invalid input
        }
        tracker.setIncome(scanner.nextDouble());
        scanner.nextLine(); // Consume newline

        while (true) {
            System.out.print("Enter expense category (or 'done' to finish): ");
            String category = scanner.nextLine().trim();
            if (category.equalsIgnoreCase("done")) break;

            System.out.print("Enter expense amount: ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume the invalid input
            }
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline left-over

            tracker.addExpense(category, amount);
        }

        tracker.displaySummary();
        scanner.close();
    }
}

// Class to represent an individual expense
class Expense {
    private String category;
    private double amount;

    public Expense(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentageOfIncome(double income) {
        return (amount / income) * 100;
    }
}
