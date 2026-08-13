# Release Notes - Nível 2

## Resumo do Nível 2

O Nível 2 expande o sistema com inteligência financeira, introduzindo transações temporais e a capacidade de gerar relatórios de alto desempenho sobre os gastos dos usuários.

### Funcionalidades Implementadas:

- **`PAYMENT <accountId> <amount> <timestamp>`**: Executa um pagamento a partir de uma conta em um instante de tempo específico, debitando o valor do saldo e contabilizando-o no total de gastos do usuário.
- **`TOP_SPENDERS <k>`**: Retorna uma lista com os `k` usuários que mais gastaram no sistema. Em caso de empate no valor, o critério de desempate é a ordem alfabética do `accountId`.

---

## Aprendizados & Anotações — Nível 2

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | Utilização de `PriorityQueue` (Min-Heap) de tamanho máximo $K$ para a funcionalidade `TOP_SPENDERS`. A raiz da heap armazena o "pior" candidato do Top K, permitindo descarte eficiente. |
| **Complexidade** | **`payment()`**: tempo **$O(1)$** e espaço **$O(1)$**.<br>**`topSpenders(k)`**: tempo **$O(N \log K)$** (onde $N$ é o total de contas com gastos) e espaço adicional **$O(K)$** para a `PriorityQueue`. |
| **Decisões de Design** | - O `Comparator` da Min-Heap foi extraído para uma constante `private static final`, melhorando a performance e a legibilidade.<br>- O atributo `totalSpent` foi encapsulado na entidade `Account`, sendo atualizado automaticamente a cada chamada de `withdraw()`. |
| **Comparatores e Empates** | Para a Min-Heap, no desempate de valor gasto igual, a conta com ID lexicograficamente **maior** é considerada "pior" para fins de ordenação interna da heap. A ordem final correta é restaurada ao final do processo. |
| **Tratamento de Casos de Borda**| - Verificação para $K \le 0$ retornando uma lista vazia.<br>- Filtro para ignorar contas com `totalSpent <= 0`.<br>- Suporte para quando $K$ for maior que o número de contas com gastos. |