import org.junit.jupiter.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanProcessingQA {

    @Test
    void testValidLoanApproval() {
        LoanProcessingSystem.Customer c = new LoanProcessingSystem.Customer("C1", 30, 5000, 500, 780, "Salaried", 20000, 24);
        LoanProcessingSystem.LoanResult res = LoanProcessingSystem.processLoan(c);
        assertTrue(res.approved);
        assertEquals(8.0, res.interestRate);
        assertTrue(res.emi > 0);
    }

    @Test
    void testMinMaxAgeBoundaries() {
        LoanProcessingSystem.Customer young = new LoanProcessingSystem.Customer("C2", 20, 4000, 0, 700, "Salaried", 10000, 12);
        assertThrows(IllegalArgumentException.class, () -> LoanProcessingSystem.processLoan(young));

        LoanProcessingSystem.Customer old = new LoanProcessingSystem.Customer("C3", 61, 4000, 0, 700, "Salaried", 10000, 12);
        assertThrows(IllegalArgumentException.class, () -> LoanProcessingSystem.processLoan(old));
    }

    @Test
    void testInvalidSalary() {
        LoanProcessingSystem.Customer c = new LoanProcessingSystem.Customer("C4", 30, -100, 0, 700, "Salaried", 10000, 12);
        assertThrows(IllegalArgumentException.class, () -> LoanProcessingSystem.processLoan(c));
    }

    @Test
    void testPoorCreditScore() {
        LoanProcessingSystem.Customer c = new LoanProcessingSystem.Customer("C5", 35, 6000, 200, 550, "Salaried", 10000, 12);
        LoanProcessingSystem.LoanResult res = LoanProcessingSystem.processLoan(c);
        assertFalse(res.approved);
    }

    @Test
    void testHighDTIRatio() {
        LoanProcessingSystem.Customer c = new LoanProcessingSystem.Customer("C6", 40, 3000, 2000, 720, "Salaried", 5000, 12);
        LoanProcessingSystem.LoanResult res = LoanProcessingSystem.processLoan(c);
        assertFalse(res.approved);
    }
}
