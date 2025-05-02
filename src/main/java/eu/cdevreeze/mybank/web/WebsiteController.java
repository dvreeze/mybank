package eu.cdevreeze.mybank.web;

import eu.cdevreeze.mybank.model.Transaction;
import eu.cdevreeze.mybank.service.TransactionService;
import eu.cdevreeze.mybank.web.forms.TransactionForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.List;

@Controller
public class WebsiteController {

    private final TransactionService transactionService;

    public WebsiteController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/account/{userId}")
    public String findTransactionsByReceivingUserId(
            @PathVariable("userId") String receivingUserId,
            Model model
    ) {
        List<Transaction> accountTransactions = transactionService.findByReceivingUserId(receivingUserId);

        model.addAttribute("transactionForm", new TransactionForm());
        model.addAttribute("receivingUserId", receivingUserId);
        model.addAttribute("accountTransactions", accountTransactions);
        return "account.html";
    }

    @PostMapping(value = "/account/{userId}")
    public String createTransaction(
            @ModelAttribute TransactionForm transactionForm,
            BindingResult bindingResult,
            @PathVariable("userId") String receivingUserId,
            Model model
    ) {
        List<Transaction> accountTransactions = transactionService.findByReceivingUserId(receivingUserId);

        model.addAttribute("receivingUserId", receivingUserId);
        model.addAttribute("accountTransactions", accountTransactions);

        if (bindingResult.hasErrors()) {
            return "account.html";
        }
        transactionService.create(
                transactionForm.getAmount(),
                Instant.now(),
                transactionForm.getReference(),
                transactionForm.getReceivingUserId()
        );
        return String.format("redirect:/account/%s", receivingUserId);
    }
}
