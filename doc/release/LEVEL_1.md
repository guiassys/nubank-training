# Release Notes - Nível 1

## Resumo do Nível 1

O Nível 1 introduz o núcleo do sistema de carteiras digitais, permitindo a criação de contas e a realização de operações financeiras básicas.

### Funcionalidades Implementadas:

- **CREATE `<accountId>`**: Cria uma nova conta com saldo inicial zero.
- **DEPOSIT `<accountId> <amount>`**: Adiciona um valor ao saldo de uma conta existente.
- **TRANSFER `<from> <to> <amount>`**: Transfere um valor entre duas contas, validando a existência de ambas e o saldo da conta de origem.
- **BALANCE `<accountId>`**: Consulta o saldo de uma conta.

---

## Aprendizados & Anotações — Nível 1

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | Foi escolhido **`HashMap<String, model.Account>`** para armazenar as contas, pois o `accountId` é uma **chave única** e será utilizado frequentemente para localizar uma conta. Essa estrutura oferece busca, inserção e remoção com complexidade esperada **O(1)**. |
| **Complexidade** | **`create()`**: tempo **O(1)**, espaço **O(1)**. **`deposit()`**: tempo **O(1)**, espaço **O(1)**. **`balance()`**: tempo **O(1)**, espaço **O(1)**. **`transfer()`**: tempo **O(1)**. O espaço total do serviço é **O(n)**, onde `n` é o número de contas. |
| **Decisões de Design** | O projeto foi dividido em duas responsabilidades: **`service.WalletService`** (coordena os casos de uso) e **`model.Account`** (representa a entidade de domínio e protege seu estado). |
| **Princípios de Orientação a Objetos** | O estado interno de uma conta (`balance`) é alterado exclusivamente por seus próprios métodos (`deposit()` e `withdraw()`), garantindo que a entidade proteja suas próprias **invariantes**. |
| **Encapsulamento** | A responsabilidade de modificar o saldo pertence à classe **`model.Account`**. O `service.WalletService` não altera diretamente o atributo `balance`, reduzindo o acoplamento. |
| **Tratamento de Erros** | A ausência de uma conta é tratada como parte do fluxo normal do negócio, retornando valores de falha. Valores de entrada inválidos (`amount <= 0`) são tratados com `IllegalArgumentException`. |
| **Atomicidade** | O conceito de atomicidade em transferências foi identificado como um ponto a ser aprofundado em níveis futuros, garantindo que a operação ocorra por completo ou seja totalmente desfeita em caso de falha. |
| **Boas Práticas Java** | Programar contra interfaces (`Map` em vez de `HashMap`), usar `final` para atributos imutáveis e manter responsabilidades bem definidas. |