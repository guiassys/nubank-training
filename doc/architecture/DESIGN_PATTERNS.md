# Padrões de Projeto Utilizados

Este documento resume os principais padrões de projeto (Design Patterns) e de concorrência que foram aplicados no desenvolvimento do sistema de carteira digital.

| Nome do pattern | Descrição do uso |
| :--- | :--- |
| **Facade** | A interface `IWalletService` atua como uma fachada, provendo uma interface simplificada e unificada que orquestra as operações do sistema (criação de conta, transferências, etc.). Ela esconde a complexidade interna do gerenciamento de múltiplas estruturas de dados e da lógica de negócio. |
| **Interface Segregation Principle (ISP)** | As funcionalidades da carteira foram divididas em interfaces menores e mais coesas (`IAccountAdminService`, `IAccountFinancialService`, `ITransactionService`, `IReportingService`). Isso evita que as classes clientes dependam de métodos que não utilizam, promovendo um design mais limpo e desacoplado. |
| **Singleton** | A classe `WalletService` é implementada como um Singleton implícito no contexto da aplicação. Isso garante que exista apenas uma instância do serviço gerenciando o estado de todas as contas e transações, evitando inconsistências. |
| **Repository** | A classe `WalletService` gerencia a coleção de entidades de domínio (contas e transações), abstraindo a fonte de dados (neste caso, mapas em memória) e centralizando o acesso e a persistência dos objetos. |
| **Strategy** | O `MIN_HEAP_SPENDER_COMPARATOR` é uma implementação do padrão Strategy. Ele encapsula o algoritmo de comparação usado na fila de prioridade para determinar os "top spenders", permitindo que a lógica de ordenação seja definida e trocada de forma independente. |
| **State** | O `Account` utiliza o padrão State para gerenciar o estado de "bloqueado". Isso permite que o comportamento do objeto mude dinamicamente (impedindo débitos), sem a necessidade de condicionais (`if/else`) espalhadas pelo código que utiliza a conta. |
| **Command** | O `CashbackEvent` funciona como um objeto de comando. Ele encapsula toda a informação necessária para executar uma ação (aplicar o cashback) em um momento futuro. A `cashbackQueue` atua como um agendador, processando esses comandos quando eles "maturam". |
| **Monitor Object** | O `Account` atua como um monitor, usando blocos `synchronized` para proteger seu estado interno (saldo, histórico) em um ambiente concorrente. Isso garante que apenas uma thread possa modificar seus dados por vez, prevenindo condições de corrida. |
| **Ordered Locking** | Na operação de `transfer`, o padrão de bloqueio ordenado é usado para prevenir deadlocks. Ao adquirir os locks das contas de origem e destino sempre na mesma ordem (baseada no ID da conta), elimina-se a possibilidade de um ciclo de espera mortal entre as threads. |
