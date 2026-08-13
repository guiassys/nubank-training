package service;

import java.util.List;

/**
 * Interface para operações de consulta e relatórios.
 */
public interface IReportingService {
    int spentInWindow(String accountId, long windowSizeMs, long currentTimestamp);
    List<String> topSpenders(int k);
}