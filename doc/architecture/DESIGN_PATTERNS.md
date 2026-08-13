# Padrões de Projeto Utilizados

Este documento resume os principais padrões de projeto (Design Patterns) e de concorrência que foram aplicados no desenvolvimento do sistema de carteira digital.

| Nome do pattern | Descrição do uso |
| :--- | :--- |
| **Facade** | Para prover uma interface simplificada e unificada que orquestra as operações do sistema (criação de conta, transferências, etc.), escondendo a complexidade interna do gerenciamento de múltiplas estruturas de dados. |
| **Repository** | Para gerenciar a coleção de entidades de domínio (contas), abstraindo a fonte de dados (neste caso, um mapa em memória) e centralizando o acesso e a persistência dos objetos. |
| **Strategy** | Para definir algoritmos de comparação de forma intercambiável. Foi usado para criar uma estratégia específica de ordenação para a fila de prioridade, permitindo que a lógica de "maior gastador" fosse encapsulada e reutilizável. |
| **State** | Para alterar o comportamento de um objeto (a conta) com base em seu estado interno. Foi usado para impedir transações de débito quando uma conta está no estado "bloqueada", sem a necessidade de condicionais espalhadas pelo código cliente. |
| **Monitor Object** | Para garantir a consistência interna de um objeto em um ambiente concorrente. Foi usado para proteger o estado de cada conta, assegurando que apenas uma thread possa modificar seu saldo e históricos por vez, evitando condições de corrida. |
| **Ordered Locking** | Para prevenir deadlocks ao adquirir múltiplos bloqueios. Foi usado para garantir que as travas em duas contas durante uma transferência sejam sempre obtidas na mesma ordem, eliminando a possibilidade de um ciclo de espera mortal entre threads. |
