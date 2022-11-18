package org.prgrms.memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.prgrms.exception.FileNotExistException;
import org.prgrms.voucher.discountType.Amount;
import org.prgrms.voucher.voucherType.Voucher;
import org.prgrms.voucher.voucherType.VoucherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("local")
public class VoucherFileMemory implements Memory {

  private final File file;

  private static final int TYPE_INDEX = 0;
  private static final int ID_INDEX = 1;
  private static final int AMOUNT_INDEX = 2;
  private static final int LENGTH = 3;

  public VoucherFileMemory(@Value("${file.voucher.file-path}") String filePath) {
    this.file = new File(filePath);
  }

  public Voucher save(Voucher voucher) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
      writer.write(voucher.getClass().getSimpleName() + "," + voucher.getVoucherId() + ","
          + voucher.getVoucherAmount().getValue() + System.lineSeparator());
    } catch (IOException e) {
      throw new FileNotExistException(e);
    }
    return voucher;
  }

  public List<Voucher> findAll() {
    List<Voucher> voucherList = new ArrayList<>();

    if (file.exists()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

        String line;

        while ((line = reader.readLine()) != null) {
          voucherList.add(extractVoucherFromFile(verifyArrayLength(line.split(","))));
        }
      } catch (IOException e) {
        throw new FileNotExistException(e);
      }
    }
    return voucherList;
  }

  public Voucher extractVoucherFromFile(String[] lineArray) {
    String savedVoucherType = lineArray[TYPE_INDEX].toUpperCase();
    UUID savedId = UUID.fromString(lineArray[ID_INDEX]);
    int savedAmount = Integer.parseInt(lineArray[AMOUNT_INDEX]);

    return createVoucher(savedVoucherType, savedId, savedAmount);

  }

  private Voucher createVoucher(String savedVoucherType, UUID savedId, int savedAmount) {
    VoucherType voucherType = VoucherType.of(savedVoucherType);
    Amount amount = voucherType.generateAmount(savedAmount);

    return voucherType.generateFileVoucher(savedId, amount);
  }

  private String[] verifyArrayLength(String[] lineArray) {
    if (lineArray.length == LENGTH) {
      return lineArray;
    }
    throw new ArrayIndexOutOfBoundsException(
        "Check the format of the information stored in the file");
  }

}
