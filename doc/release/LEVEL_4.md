# Release Notes - Nível 4

## Resumo do Nível 4

O Nível 4 robustece o sistema para um ambiente de produção, introduzindo **segurança contra concorrência** e preparando o terreno para regras de negócio complexas. Esta é a base para um sistema financeiro confiável e escalável.

### Melhorias Implementadas (Etapa 1):

- **Thread Safety em `model.Account`**: Todos os métodos que acessam ou modificam o estado da conta (saldo, histórico de gastos) foram sincronizados, garantindo que as operações em uma única conta sejam atômicas e consistentes.
- **Coleções Concorrentes em `service.WalletService`**: As estruturas de dados principais (`HashMap`, `PriorityQueue`) foram substituídas por suas contrapartes concorrentes (`ConcurrentHashMap`, `PriorityBlockingQueue`) para gerenciar o estado compartilhado do serviço de forma segura e eficiente.
- **Prevenção de Deadlock em Transferências**: Foi implementado um mecanismo de bloqueio ordenado (`ordered locking`) no método `transfer`, que trava os recursos (contas) em uma ordem consistente para evitar o risco de *deadlocks* durante transferências simultâneas.

---

## Aprendizados & Anotações — Nível 4 (Etapa 1)

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Concorrência vs. Performance** | A simples sincronização de todos os métodos no `WalletService` seria um gargalo de performance (*bottleneck*). A abordagem de usar coleções concorrentes e bloqueio refinado (`fine-grained locking`) oferece um equilíbrio muito melhor, garantindo segurança com menor impacto no desempenho. |
| **Prevenção de Deadlock** | O padrão de *ordered locking* é uma técnica clássica e essencial para evitar deadlocks. Ao adquirir os bloqueios sempre na mesma ordem (neste caso, pela ordem lexicográfica dos `accountId`), garantimos que um ciclo de espera mortal entre duas ou mais threads nunca ocorra. |
| **Atomicidade** | O uso de `ConcurrentHashMap.putIfAbsent()` para a criação de contas é um exemplo de como aproveitar operações atômicas já fornecidas pelas coleções concorrentes para simplificar o código e garantir a correção. |
| **Design para Concorrência** | A decisão de tornar a entidade `Account` responsável por sua própria consistência interna (sincronizando seus métodos) e deixar o `WalletService` orquestrar as operações entre múltiplas contas é uma separação de responsabilidades clara que facilita a manutenção e o raciocínio sobre o código concorrente. |
