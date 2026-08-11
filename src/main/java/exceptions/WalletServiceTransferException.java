package exceptions;

public class WalletServiceTransferException extends RuntimeException {
    public WalletServiceTransferException(String message) {
        super(message);
    }
}