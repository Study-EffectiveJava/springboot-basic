package prgms.vouchermanagementapp.storage.model;

import prgms.vouchermanagementapp.domain.Voucher;

import java.util.List;

public class MemoryVoucherRecord implements VoucherRecord {

    private final List<Voucher> memoryVouchers;

    public MemoryVoucherRecord(List<Voucher> memoryVouchers) {
        this.memoryVouchers = memoryVouchers;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    public List<Voucher> getMemoryVouchers() {
        return this.memoryVouchers;
    }
}
