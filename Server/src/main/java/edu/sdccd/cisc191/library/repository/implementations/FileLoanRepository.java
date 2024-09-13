package edu.sdccd.cisc191.library.repository.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.sdccd.cisc191.library.exceptions.LoanAlreadyExistsException;
import edu.sdccd.cisc191.library.exceptions.LoanFileException;
import edu.sdccd.cisc191.library.exceptions.LoanNotFoundException;
import edu.sdccd.cisc191.library.model.Loan;
import edu.sdccd.cisc191.library.repository.LoanRepository;
import edu.sdccd.cisc191.library.utils.LocalDateTypeAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLoanRepository implements LoanRepository {
    private final Path filePath;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

    public FileLoanRepository(Path filePath) {
        this.filePath = filePath;
        initializeFile();
    }

    private void initializeFile() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                saveAllLoans(new HashMap<>());
            }
        } catch (IOException e) {
            throw new LoanFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Map<String, Loan> getAllLoans() {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Type loanMapType = new TypeToken<HashMap<String, Loan>>() {}.getType();
            HashMap<String, Loan> loans = gson.fromJson(reader, loanMapType);
            return (loans != null ? loans : new HashMap<>());
        } catch (IOException e) {
            throw new LoanFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Loan getLoanById(String loanId) {
        Map<String, Loan> loans = getAllLoans();
        Loan loan = loans.get(loanId);
        if (loan == null) {
            throw new LoanNotFoundException("Loan with ID " + loanId + " not found");
        }
        return loan;
    }

    @Override
    public List<Loan> getLoansByUserId(String userId) {
        Map<String, Loan> allLoans = getAllLoans();
        List<Loan> userLoans = new ArrayList<>();
        for (Loan loan:allLoans.values()) {
            if (loan.getUserId().equals(userId)) {
                userLoans.add(loan);
            }
        }
        return userLoans;
    }

    @Override
    public List<Loan> getOverdueLoansByUserId(String userId) {
        Map<String, Loan> allLoans = getAllLoans();
        List<Loan> overdueUserLoans = new ArrayList<>();
        for (Loan loan:allLoans.values()) {
            if (loan.getUserId().equals(userId) && loan.isOverdue()) {
                overdueUserLoans.add(loan);
            }
        }
        return overdueUserLoans;
    }

    @Override
    public Loan addLoan(Loan loan) throws IOException {
        Map<String, Loan> loans = getAllLoans();
        if (loans.containsKey(loan.getLoanId())) {
            throw new LoanAlreadyExistsException("Loan with ID " + loan.getLoanId() + " already exists");
        }
        loans.put(loan.getLoanId(), loan);
        saveAllLoans(loans);
        return loan;
    }

    @Override
    public Loan updateLoan(Loan updatedLoan) throws IOException {
        Map<String, Loan> loans = getAllLoans();
        if (!loans.containsKey(updatedLoan.getLoanId())) {
            throw new LoanNotFoundException("Loan with ID " + updatedLoan.getLoanId() + " not found");
        }
        loans.put(updatedLoan.getLoanId(), updatedLoan);
        saveAllLoans(loans);
        return updatedLoan;
    }

    @Override
    public void deleteLoan(String loanId) throws IOException {
        Map<String, Loan> loans = getAllLoans();
        if (loans.remove(loanId) == null) {
            throw new LoanNotFoundException("Loan with ID " + loanId + " not found");
        }
        saveAllLoans(loans);
    }

    private void saveAllLoans(Map<String, Loan> loans) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(loans, writer);
        } catch (IOException e) {
            throw new LoanFileException("Failed to save loans to file: " + filePath, e);
        }
    }
}
