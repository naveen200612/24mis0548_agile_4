public class LoanProcessingSystem {

    public static class Customer {
        public String customerId;
        public int age;
        public double monthlySalary;
        public double existingLoanAmount;
        public int creditScore;
        public String employmentType; // "Salaried", "Self-Employed"
        public double requestedLoanAmount;
        public int loanTenureMonths;

        public Customer(String id, int age, double salary, double existingLoan, int score, String empType, double requestedLoan, int tenure) {
            this.customerId = id;
            this.age = age;
            this.monthlySalary = salary;
            this.existingLoanAmount = existingLoan;
            this.creditScore = score;
            this.employmentType = empType;
            this.requestedLoanAmount = requestedLoan;
            this.loanTenureMonths = tenure;
        }
    }

    public static class LoanResult {
        public double dtiRatio;
        public double eligibleLoanAmount;
        public double interestRate;
        public double emi;
        public boolean approved;
        public String statusReason;

        public LoanResult(double dti, double eligible, double rate, double emi, boolean approved, String reason) {
            this.dtiRatio = dti;
            this.eligibleLoanAmount = eligible;
            this.interestRate = rate;
            this.emi = emi;
            this.approved = approved;
            this.statusReason = reason;
        }
    }

    public static LoanResult processLoan(Customer c) {
        if (c.age < 21 || c.age > 60) {
            throw new IllegalArgumentException("Age must be between 21 and 60.");
        }
        if (c.monthlySalary <= 0) {
            throw new IllegalArgumentException("Salary must be positive.");
        }
        if (c.requestedLoanAmount <= 0 || c.loanTenureMonths <= 0) {
            throw new IllegalArgumentException("Invalid loan amount or tenure.");
        }

        double dti = (c.existingLoanAmount / c.monthlySalary) * 100;
        if (c.creditScore < 600) {
            return new LoanResult(dti, 0, 0, 0, false, "Rejected: Credit score below 600");
        }
        if (dti > 50) {
            return new LoanResult(dti, 0, 0, 0, false, "Rejected: Debt-to-income ratio exceeds 50%");
        }

        double maxEligible = c.monthlySalary * 20;
        if (c.employmentType.equalsIgnoreCase("Self-Employed")) {
            maxEligible *= 0.8;
        }

        double eligibleLoan = Math.min(c.requestedLoanAmount, maxEligible);
        
        // Base Interest Rate
        double annualRate = 10.0;
        if (c.creditScore > 750) annualRate -= 1.5;
        if (c.employmentType.equalsIgnoreCase("Salaried")) annualRate -= 0.5;

        // Calculate Monthly EMI: P * r * (1+r)^n / ((1+r)^n - 1)
        double r = (annualRate / 12) / 100;
        int n = c.loanTenureMonths;
        double emi = (eligibleLoan * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);

        return new LoanResult(dti, eligibleLoan, annualRate, emi, true, "Approved");
    }
}
    public double getEmi() { return emi; }
    public String getApprovalStatus() { return approvalStatus; }
}
