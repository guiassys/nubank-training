package service;

/**
 * Interface Facade que unifica todas as funcionalidades do serviço de carteira.
 * Esta interface herda de interfaces segregadas pelo Princípio da Segregação de Interfaces (ISP).
 */
public interface IWalletService extends
        IAccountAdminService,
        IAccountFinancialService,
        ITransactionService,
        IReportingService {
    // Esta interface agora está vazia, atuando como um ponto de união para todas as outras.
}