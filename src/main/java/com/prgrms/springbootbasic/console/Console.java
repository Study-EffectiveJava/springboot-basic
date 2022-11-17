package com.prgrms.springbootbasic.console;

import com.prgrms.springbootbasic.customer.domain.Customer;
import com.prgrms.springbootbasic.voucher.VoucherType;

import java.text.MessageFormat;
import java.util.List;

import com.prgrms.springbootbasic.voucher.domain.Voucher;
import org.springframework.stereotype.Component;

@Component
public class Console {

    private static final String COMMAND_NOT_SUPPORTED = "Command not supported yet.";
    private static final String VOUCHER_NOT_SUPPORTED = "Voucher not supported yet.";
    private static final String MENU = "=== Voucher Program ===\n" +
            "Type **exit** to exit the program.\n" +
            "Type **create** to create a new voucher.\n" +
            "Type **list** to list all vouchers.\n" +
            "Type **blacklist** to list blacklist.";
    private static final String EXIT_MESSAGE = "Exit program. Bye.";
    private static final String TYPE_VOUCHER_MESSAGE = "Type 'fixedAmount' for fixed amount voucher, or type 'percent' for percent voucher";
    private static final String TYPE_FIXED_AMOUNT_MESSAGE = "Chose fixedAmount. Type fixed amount(1 ~ 10000). Amount must be an integer.";
    private static final String TYPE_PERCENT_MESSAGE = "Chose percent. Type percent amount(1 ~ 99(%)). Amount must be an integer.";
    private static final String CREATE_SUCCESS_MESSAGE = "New voucher created!";
    private static final String VOUCHER_LIST_MESSAGE = "Voucher Type = {0} / discount amount = {1}";
    private static final String VOUCHER_EMPTY_MESSAGE = "You don't have any voucher";
    private static final String BLACKED_USER_MESSAGE = "User id = {0} / name = {1}";
    private static final String BLACKLIST_EMPTY_MESSAGE = "There is no blacked user";

    private final Reader reader;
    private final Printer printer;

    public Console(Reader reader, Printer printer) {
        this.reader = reader;
        this.printer = printer;
    }

    public String getCommand() {
        return reader.read();
    }

    public String getInput() {
        return reader.read();
    }

    public void printCommendNotSupported() {
        printer.printMessage(COMMAND_NOT_SUPPORTED);
    }

    public void printMenu() {
        printer.printMessage(MENU);
    }

    public void printExitMessage() {
        printer.printMessage(EXIT_MESSAGE);
    }

    public void printChoosingVoucher() {
        printer.printMessage(TYPE_VOUCHER_MESSAGE);
    }

    public void printDiscountAmountMessage(VoucherType voucherType) {
        switch (voucherType) {
            case FIXED_AMOUNT ->
                    printer.printMessage(MessageFormat.format(TYPE_FIXED_AMOUNT_MESSAGE, voucherType.getInputValue()));
            case PERCENT ->
                    printer.printMessage(MessageFormat.format(TYPE_PERCENT_MESSAGE, voucherType.getInputValue()));
            default -> printer.printMessage(VOUCHER_NOT_SUPPORTED);
        }
    }

    public void printExceptionMessage(String exceptionMessage) {
        printer.printMessage(exceptionMessage);
    }

    public void printCreateSuccessMessage() {
        printer.printMessage(CREATE_SUCCESS_MESSAGE);
    }

    public void printVoucherList(List<Voucher> vouchers) {
        if (vouchers.isEmpty()) {
            printer.printMessage(VOUCHER_EMPTY_MESSAGE);
        } else {
            vouchers
                    .forEach(voucher -> System.out.println(
                            MessageFormat.format(VOUCHER_LIST_MESSAGE, voucher.getClass().getSimpleName(), voucher.getDiscountRate())));
        }
    }

    public void printBlackList(List<Customer> blacklist) {
        if (blacklist.isEmpty()) {
            printer.printMessage(BLACKLIST_EMPTY_MESSAGE);
        } else {
            blacklist
                    .forEach(user -> System.out.println(
                            MessageFormat.format(BLACKED_USER_MESSAGE, user.getId(), user.getName())));
        }
    }
}
