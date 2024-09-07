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
import java.util.List;
import java.util.stream.Collectors;

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
                saveAllLoans(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new LoanFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public List<Loan> getAllLoans() {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Type loanListType = new TypeToken<ArrayList<Loan>>() {}.getType();
            List<Loan> loans = gson.fromJson(reader, loanListType);
            return (loans != null ? loans : new ArrayList<>());
        } catch (IOException e) {
            throw new LoanFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Loan getLoanById(String loanId) {
        return getAllLoans().stream()
                .filter(loan -> loan.getLoanId().equals(loanId))
                .findFirst()
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));
    }

    @Override
    public List<Loan> getLoansByUserId(String userId) {
        return getAllLoans().stream()
                .filter(loan -> loan.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getOverdueLoansByUserId(String userId) {
        return getAllLoans().stream()
                .filter(loan -> loan.getUserId().equals(userId))
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    @Override
    public void addLoan(Loan loan) throws IOException {
        List<Loan> loans = getAllLoans();
        if (loans.stream().anyMatch(l -> l.getLoanId().equals(loan.getLoanId()))) {
            throw new LoanAlreadyExistsException("Loan with ID " + loan.getLoanId() + " already exists");
        }
        loans.add(loan);
        saveAllLoans(loans);
    }

    @Override
    public void updateLoan(Loan updatedLoan) throws IOException {
        List<Loan> loans = getAllLoans();
        boolean loanFound = false;
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getLoanId().equals(updatedLoan.getLoanId())) {
                loans.set(i, updatedLoan);
                loanFound = true;
                break;
            }
        }

        if (!loanFound) {
            throw new LoanNotFoundException("Loan with ID " + updatedLoan.getLoanId() + " not found");
        }

        saveAllLoans(loans);
    }

    @Override
    public void deleteLoan(String loanId) throws IOException {
        List<Loan> loans = getAllLoans();
        boolean loanRemoved = loans.removeIf(loan -> loan.getLoanId().equals(loanId));

        if (!loanRemoved) {
            throw new LoanNotFoundException("Loan with ID " + loanId + " not found");
        }

        saveAllLoans(loans);
    }

    private void saveAllLoans(List<Loan> loans) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(loans, writer);
        } catch (IOException e) {
            throw new LoanFileException("Failed to save loans to file: " + filePath, e);
        }
    }
}
