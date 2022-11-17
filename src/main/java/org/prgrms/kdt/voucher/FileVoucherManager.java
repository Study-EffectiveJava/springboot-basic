package org.prgrms.kdt.voucher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Profile("local")
@Repository
public class FileVoucherManager implements VoucherManager {

    private static final Logger logger = LoggerFactory.getLogger(VoucherManager.class);

    public static final String DELIMITER = ", ";
    private final String filePath;

    public FileVoucherManager(@Value("${voucher.file-path}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Voucher voucher) {
        File vouchersCsv = loadFile();

        write(voucher, vouchersCsv);
    }

    private File loadFile() {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                logger.info("file created. [FILE PATH]: " + filePath);
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot find file. Please check there is file those name is " + file.getName() + ".[File Path]: " + filePath, exception);
        }
        return file;
    }

    private void write(Voucher voucher, File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            bufferedWriter.append(String.valueOf(voucher.getId()));
            bufferedWriter.append(DELIMITER);
            bufferedWriter.append(voucher.getType().getType().toUpperCase());
            bufferedWriter.append(DELIMITER);
            bufferedWriter.append(String.valueOf(voucher.getAmount().getValue()));
            bufferedWriter.append(System.lineSeparator());
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot find file. Please check there is file those name is " + file.getName() + ".[File Path]: " + filePath, exception);
        }
    }

    @Override
    public List<Voucher> findAll() {
        File vouchersCsv = loadFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(vouchersCsv))) {
            return bufferedReader.lines()
                    .map(FileVoucherManager::mapToVoucher)
                    .toList();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot find file. Please check there is file those name is " + vouchersCsv.getName(), exception);
        }
    }

    @Override
    public Optional<Voucher> findById(long id) {
        File vouchersCsv = loadFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(vouchersCsv))) {
            return bufferedReader.lines()
                    .map(FileVoucherManager::mapToVoucher)
                    .filter(voucher -> voucher.getId() == id)
                    .findFirst();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot find file. Please check there is file those name is " + vouchersCsv.getName(), exception);
        }
    }

    private static Voucher mapToVoucher(String line) {
        try {
            String[] tokens = line.split(DELIMITER);
            return Voucher.getInstance(Long.parseLong(tokens[0]), VoucherType.of(tokens[1]), new VoucherAmount(tokens[2]));
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new RuntimeException("Invalid File. Please write the file in following format. [Format]: Id, Type, Amount", exception);
        }
    }
}
