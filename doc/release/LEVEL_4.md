# Release Notes - Nível 4

## Resumo do Nível 4

O Nível 4 robustece o sistema para um ambiente de produção, introduzindo **segurança contra concorrência** e preparando o terreno para regras de negócio complexas. Esta é a base para um sistema financeiro confiável e escalável.

### Melhorias Implementadas (Etapa 1):

- **Thread Safety em `model.Account`**: Todos os métodos que acessam ou modificam o estado da conta (saldo, histórico de gastos) foram sincronizados, garantindo que as operações em uma única conta sejam atômicas e consistentes.
- **Coleções Concorrentes e Locks Granulares em `service.WalletService`**: As estruturas de dados principais (`HashMap`, `PriorityQueue`) foram substituídas por suas contrapartes concorrentes (`ConcurrentHashMap`, `PriorityBlockingQueue`). A sincronização global nos métodos de serviço foi removida para eliminar o gargalo de concorrência (*lock contention*), mantendo a sincronização isolada apenas no bloco do `cashbackQueue`.
- **Prevenção de Deadlock em Transferências**: Foi implementado um mecanismo de bloqueio ordenado (`ordered locking`) no método `transfer`, que trava os recursos (contas) em uma ordem consistente (lexicográfica) para evitar o risco de *deadlocks* durante transferências simultâneas.

---

## Aprendizados & Anotações — Nível 4 (Etapa 1)

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Concorrência vs. Performance** | A remoção do `synchronized` nos métodos públicos do `WalletService` evita que requisições de contas distintas entrem em fila de espera desnecessária. O uso de coleções concorrentes combinado com bloqueios granulares (no `cashbackQueue` e dentro do próprio objeto `Account`) maximiza o *throughput* concorrente mantendo a consistência. |
| **Prevenção de Deadlock** | O padrão de *ordered locking* é uma técnica clássica e essencial para evitar deadlocks. Ao adquirir os bloqueios sempre na mesma ordem (neste caso, pela ordem lexicográfica dos `accountId`), garantimos que um ciclo de espera mortal entre duas ou mais threads nunca ocorra. |
| **Atomicidade** | O uso de `ConcurrentHashMap.putIfAbsent()` para a criação de contas é um exemplo de como aproveitar operações atômicas já fornecidas pelas coleções concorrentes para simplificar o código e garantir a correção. |
| **Design para Concorrência** | A decisão de tornar a entidade `Account` responsável por sua própria consistência interna (sincronizando seus métodos) e deixar o `WalletService` orquestrar as operações entre múltiplas contas é uma separação de responsabilidades clara que facilita a manutenção e o raciocínio sobre o código concorrente. |