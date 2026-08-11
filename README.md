# Nubank Training

> **Objetivo:** Projeto voltado para preparação em processos seletivos técnicos, focado em algoritmos, estruturas de dados, lógica de negócio e desenvolvimento Java (estilo CodeSignal).

---

## 🛠️ Ambiente de Desenvolvimento

| Componente | Especificação |
| :--- | :--- |
| **Java** | 21 |
| **Maven** | 3.8.7 |
| **IDE** | IntelliJ IDEA |
| **SO** | Ubuntu (WSL2) |

---

## 🎯 Metodologia de Treinamento

O treinamento será realizado de forma incremental, priorizando as seguintes etapas:

1. **Entendimento do problema**
2. **Modelagem**
3. **Escolha das estruturas de dados**
4. **Implementação**
5. **Testes**
6. **Análise de complexidade**
7. **Evolução da solução**

---

# 💳 Simulado 01 — Sistema de Carteiras Digitais

## Contexto

Estamos implementando o núcleo de um sistema de carteiras digitais.

O sistema deverá permitir a criação de contas e operações financeiras sobre elas. Inicialmente, todas as contas são criadas com saldo igual a `0`.

---

## 🟢 Nível 1 — Operações Básicas

### 1. `CREATE`
* **Sintaxe:** `CREATE <accountId>`
* **Descrição:** Cria uma nova conta.
* **Retorno:**
    * `true`: Conta criada com sucesso.
    * `false`: A conta já existe.

### 2. `DEPOSIT`
* **Sintaxe:** `DEPOSIT <accountId> <amount>`
* **Descrição:** Adiciona o valor especificado à conta.
* **Retorno:**
    * `int`: Novo saldo da conta.
    * `-1`: Caso a conta não exista.

### 3. `TRANSFER`
* **Sintaxe:** `TRANSFER <from> <to> <amount>`
* **Descrição:** Transfere dinheiro entre duas contas. A operação só ocorre se ambas as contas existirem e a conta de origem possuir saldo suficiente.
* **Retorno:**
    * `true`: Transferência realizada com sucesso.
    * `false`: Falha na operação.

### 4. `BALANCE`
* **Sintaxe:** `BALANCE <accountId>`
* **Descrição:** Retorna o saldo atual da conta.
* **Retorno:**
    * `int`: Saldo da conta.
    * `-1`: Caso a conta não exista.

---

### 📝 Exemplo Prático

**Entrada (Operações):**
```
CREATE A
CREATE B
DEPOSIT A 100
TRANSFER A B 40
BALANCE A
BALANCE B
```

**Saída esperada:**
```
true
true
100
true
60
40
```

---

# 📐 Estratégia de Desenvolvimento

O exercício será resolvido de forma incremental.

### Etapa 1 — Criar conta
Implementar a assinatura:
```java
boolean create(String accountId);
```

* **Pergunta de design:** Precisamos armazenar várias contas e localizá-las pelo `accountId`. Qual estrutura de dados Java é mais adequada para armazenar e localizar as contas?
* **Decisão:** _A preencher após análise._
* **Justificativa:** _A preencher após análise._

---

### Etapa 2 — Depósito
Implementar a assinatura:
```java
int deposit(String accountId, int amount);
```
> _A preencher após implementação._

---

### Etapa 3 — Transferência
Implementar a assinatura:
```java
boolean transfer(String from, String to, int amount);
```
> _A preencher após implementação._

---

### Etapa 4 — Consulta de saldo
Implementar a assinatura:
```java
int balance(String accountId);
```
> _A preencher após implementação._

---

# 🧠 Aprendizados & Anotações

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | Foi escolhido **`HashMap<String, model.Account>`** para armazenar as contas, pois o `accountId` é uma **chave única** e será utilizado frequentemente para localizar uma conta. Essa estrutura oferece busca, inserção e remoção com complexidade esperada **O(1)**. A escolha evita buscas lineares que ocorreriam com uma `ArrayList`, cuja complexidade seria **O(n)** para localizar uma conta pelo `accountId`. Para percorrer todas as contas, podem ser utilizados `entrySet()`, `values()` ou `keySet()`, sendo a iteração **O(n)**. Também foi reforçada a diferença entre **busca por chave** (`HashMap`) e **acesso por índice** (`ArrayList`). |
| **Complexidade** | **`create()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`deposit()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`getAccountById()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`balance()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`transfer()`**: tempo **O(1)** esperado, pois realiza duas consultas por chave no `HashMap` e executa operações constantes sobre as entidades. O espaço adicional utilizado por `transfer()` é **O(1)**. O `service.WalletService` possui espaço total **O(n)**, onde `n` representa a quantidade de contas armazenadas. |
| **Decisões de Design** | O projeto foi dividido em duas responsabilidades principais. **`service.WalletService`** coordena os casos de uso da aplicação, como criar contas, localizar contas, realizar depósitos, consultar saldo e orquestrar transferências. **`model.Account`** representa a entidade de domínio e é responsável por manter seu próprio estado consistente. Na transferência, o serviço coordena a operação entre duas contas, enquanto cada `model.Account` decide como alterar seu próprio saldo por meio de `deposit()` e `withdraw()`. |
| **Princípios de Orientação a Objetos** | O estado interno de uma conta (`balance`) não deve ser alterado diretamente por outras classes. As alterações são realizadas exclusivamente pelos métodos da própria entidade, como `deposit()` e `withdraw()`. O método `withdraw()` encapsula a regra de que uma conta não pode retirar um valor superior ao saldo disponível. Dessa forma, a entidade protege suas próprias **invariantes** e impede que seu estado fique inconsistente. |
| **Encapsulamento** | A responsabilidade de modificar o saldo pertence à classe **`model.Account`**. O `service.WalletService` não altera diretamente o atributo `balance`; ele solicita operações à entidade por meio de `deposit()` e `withdraw()`. Isso reduz o acoplamento, centraliza as regras de negócio da conta e facilita futuras alterações sem impactar o restante do sistema. |
| **Transferência** | O método **`transfer(String from, String to, int amount)`** representa um **caso de uso** que envolve duas entidades. Ele valida a existência das contas de origem e destino, verifica se o valor é positivo e impede transferências para a própria conta. Após as validações, solicita `withdraw(amount)` à conta de origem. Se o saque falhar por saldo insuficiente, a operação retorna `false`. Caso contrário, o valor é depositado na conta de destino e o método retorna `true`. |
| **Tratamento de Erros** | A ausência de uma conta faz parte do fluxo normal do negócio e, por isso, métodos como `deposit()`, `balance()` e `transfer()` retornam valores indicando falha, sem lançar exceções. Já valores inválidos (`amount <= 0`) representam uso incorreto da API da entidade e são tratados com `IllegalArgumentException` em `deposit()` e `withdraw()`. |
| **Regras de Negócio** | Uma transferência somente pode ocorrer quando as contas de origem e destino existem, são diferentes, o valor é positivo e a conta de origem possui saldo suficiente. Caso qualquer condição não seja atendida, nenhuma alteração de saldo deve ocorrer e `transfer()` retorna `false`. Quando todas as condições são satisfeitas, o saldo é debitado da origem, creditado no destino e a operação é concluída com sucesso. |
| **Atomicidade** | Uma transferência envolve duas alterações de estado: retirar dinheiro da conta de origem e adicionar dinheiro à conta de destino. Em sistemas reais, essas operações devem ser tratadas de forma **atômica**, garantindo que ambas ocorram ou que nenhuma seja aplicada, evitando inconsistências em caso de falhas. Esse conceito será aprofundado posteriormente com transações e persistência de dados. |
| **Boas Práticas Java** | Programar contra interfaces (`Map<String, model.Account>` em vez de `HashMap<String, model.Account>`), utilizar `final` para atributos que não precisam ser reassociados, encapsular atributos privados, utilizar nomes claros para métodos e variáveis e manter responsabilidades bem definidas entre entidades e serviços. Também foi utilizado `Map.Entry<String, model.Account>` para percorrer simultaneamente as chaves e os valores do `HashMap`. |
| **Aprendizados de Entrevista** | Em entrevistas técnicas, não basta afirmar que uma estrutura é "mais performática"; é importante justificar a escolha com base no padrão de acesso da aplicação. Também é importante utilizar a terminologia correta, diferenciando **chave** de **índice**, **consulta** de **busca linear**, **entidade** de **serviço** e **caso de uso** de **comportamento da entidade**. As decisões de design devem ser justificadas por princípios de encapsulamento, responsabilidade única e análise de complexidade. |
| **Pontos para Revisar** | - Diferença entre **complexidade de tempo** e **complexidade de espaço**.<br>- Entender por que `HashMap` possui complexidade esperada **O(1)** e em quais situações ela pode se degradar devido a colisões de hash.<br>- Revisar quando utilizar `HashMap`, `TreeMap`, `HashSet` e `ArrayList`.<br>- Estudar encapsulamento, invariantes e responsabilidade das entidades em orientação a objetos.<br>- Entender a diferença entre uma regra de negócio pertencente à entidade e uma regra de orquestração pertencente ao serviço.<br>- Estudar o conceito de **atomicidade** e transações.<br>- Continuar praticando a análise de complexidade antes de iniciar a implementação de cada solução.<br>- Praticar a identificação de casos-limite antes da implementação.<br>- Aprimorar a comunicação técnica, utilizando terminologia precisa durante entrevistas. |

---



# 🟡 Nível 2 — Agregadores, Histórico e Métricas (Top K)

## Contexto

Com o núcleo do sistema operacional, precisamos adicionar inteligência financeira à plataforma. O sistema agora deve suportar transações temporais com `PAYMENT` e ser capaz de extrair relatórios de alto desempenho sobre o comportamento dos usuários através da consulta `TOP_SPENDERS`.

---

## 🛠️ Especificação de Novos Comandos

### 1. `PAYMENT`
* **Sintaxe:** `PAYMENT <accountId> <amount> <timestamp>`
* **Descrição:** Executa um pagamento a partir da conta informada no instante de tempo especificado. O valor é debitado permanentemente da conta e contabilizado no total gasto (*total spent*) do usuário.
* **Retorno:**
  * `true`: Pagamento realizado com sucesso (conta existe e possui saldo suficiente).
  * `false`: Falha na operação (conta inexistente ou saldo insuficiente).

### 2. `TOP_SPENDERS`
* **Sintaxe:** `TOP_SPENDERS <k>`
* **Descrição:** Retorna a lista dos $K$ usuários que mais gastaram recursos no sistema (soma de todas as operações de `PAYMENT` e `TRANSFER` enviadas com sucesso).
* **Regra de Desempate:** Se duas ou mais contas possuírem exatamente o mesmo total gasto, a prioridade deve ser resolvida por **ordem alfabética (lexicográfica crescente)** do `accountId`.
* **Retorno:**
  * `List<String>`: Lista contendo os identificadores formatados no padrão `["accountId1(totalSpent1)", "accountId2(totalSpent2)"]`. Se existirem menos de $K$ contas com gastos, retorna todas as contas que registraram algum gasto.

---

# 🟡 Nível 2 — Agregadores, Histórico e Métricas (Top K)

## Contexto

Com o núcleo do sistema operacional, precisamos adicionar inteligência financeira à plataforma. O sistema agora deve suportar transações temporais com `PAYMENT` e ser capaz de extrair relatórios de alto desempenho sobre o comportamento dos usuários através da consulta `TOP_SPENDERS`.

---

## 🛠️ Especificação de Novos Comandos

### 1. `PAYMENT`
* **Sintaxe:** `PAYMENT <accountId> <amount> <timestamp>`
* **Descrição:** Executa um pagamento a partir da conta informada no instante de tempo especificado. O valor é debitado permanentemente da conta e contabilizado no total gasto (*total spent*) do usuário.
* **Retorno:**
  * `true`: Pagamento realizado com sucesso (conta existe e possui saldo suficiente).
  * `false`: Falha na operação (conta inexistente ou saldo insuficiente).

### 2. `TOP_SPENDERS`
* **Sintaxe:** `TOP_SPENDERS <k>`
* **Descrição:** Retorna a lista dos $K$ usuários que mais gastaram recursos no sistema (soma de todas as operações de `PAYMENT` e `TRANSFER` enviadas com sucesso).
* **Regra de Desempate:** Se duas ou mais contas possuírem exatamente o mesmo total gasto, a prioridade deve ser resolvida por **ordem alfabética (lexicográfica crescente)** do `accountId`.
* **Retorno:**
  * `List<String>`: Lista contendo os identificadores formatados no padrão `["accountId1(totalSpent1)", "accountId2(totalSpent2)"]`. Se existirem menos de $K$ contas com gastos, retorna todas as contas que registraram algum gasto.

---

### 📝 Exemplo Prático (Nível 2)

**Entrada (Operações):**

```
CREATE A
CREATE B
CREATE C
DEPOSIT A 500
DEPOSIT B 500
DEPOSIT C 500
PAYMENT A 100 1000000
TRANSFER A B 150
PAYMENT B 250 1000005
PAYMENT C 250 1000010
TOP_SPENDERS 2
```


**Saída esperada:**
```
true
true
true
500
500
500
true
true
true
true
["A(250)", "B(250)"]
```
*(Nota: 'A' e 'B' empataram com 250 acumulados em gastos. Pela regra de desempate alfabético, 'A' precede 'B'. C também gastou 250, mas como K=2, apenas as 2 primeiras entram na lista).*

---

# 📐 Estratégia de Desenvolvimento (Nível 2)

### Etapa 5 — Pagamentos Temporais
Implementar a assinatura:
```java
boolean payment(String accountId, int amount, long timestamp);
```

* **Pergunta de design:** Como o `PAYMENT` afeta a entidade `model.Account` em termos de modelo de dados e encapsulamento?
* **Decisão:** Reuso do método `withdraw(amount)` existente na classe `Account`. O método `payment` valida as entradas no serviço e delega a alteração de saldo e incremento de `totalSpent` diretamente para a própria conta.
* **Justificativa:** Garantir o encapsulamento do modelo. Centralizar a lógica de débito em `withdraw(amount)` evita duplicação de regras de validação (saldo suficiente, montante positivo) e assegura que tanto transferências quanto pagamentos atualizem a métrica `totalSpent` de maneira uniforme e atômica.

---

### Etapa 6 — Ranking dos Mais Gastadores (Top K)
Implementar a assinatura:
```java
List<String> topSpenders(int k);
```

* **Pergunta de design:** Qual estrutura de dados e abordagem algorítmica devemos utilizar para obter as top $K$ contas com maior volume de gastos sem ordenar desnecessariamente todas as contas do sistema em $O(N \log N)$?
* **Decisão:** Utilização de uma Min-Heap (`PriorityQueue`) com capacidade limitada a $K$ elementos associada a um `Comparator` estático privado (`MIN_HEAP_SPENDER_COMPARATOR`).
* **Justificativa:** A Min-Heap fixada em $K$ elementos reduz a complexidade temporal para $O(N \log K)$ e a espacial para $O(K)$, pois descarta instantaneamente em tempo $O(\log K)$ qualquer candidato inferior ao topo. Isolar o comparador em um membro `private static final` evita alocações redundantes de objetos a cada execução e melhora a legibilidade e o reúso de memória.

---

# 🧠 Aprendizados & Anotações — Nível 2

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | Utilização de `PriorityQueue` (Min-Heap) de tamanho máximo $K$. A raiz da heap guarda o "pior" candidato do Top K atual, permitindo descarte eficiente com `.poll()`. |
| **Complexidade** | **`payment()`**: tempo **$O(1)$** e espaço **$O(1)$**.<br>**`topSpenders(k)`**: tempo **$O(N \log K)$** (onde $N$ é o total de contas com `totalSpent > 0`) e espaço adicional **$O(K)$** para a `PriorityQueue`. |
| **Decisões de Design** | - Extração do `Comparator` para a constante `MIN_HEAP_SPENDER_COMPARATOR` (`private static final`), melhorando performance e mantendo o método limpo.<br>- Manutenção do atributo `totalSpent` encapsulado na entidade `Account`, sendo atualizado automaticamente a cada chamada de `withdraw()`. |
| **Comparatores e Empates** | Para a Min-Heap de tamanho $K$, o pior elemento fica na raiz para ser removido se a fila exceder tamanho $K$. Por isso, no desempate de valor gasto igual, a conta com ID lexicograficamente **maior** (ex: `"B" > "A"`) é considerada "pior" na Heap (`a2.getId().compareTo(a1.getId())`). Após a descarregada da Heap e a chamada a `Collections.reverse()`, a ordem alfabética crescente final (`"A"`, `"B"`) é restaurada. |
| **Tratamento de Erros e Borda**| - Verificação para $K \le 0$ retornando `Collections.emptyList()`.<br>- Filtro para ignorar contas com `totalSpent <= 0`.<br>- Suporte transparente para quando $K$ for maior do que o total de contas elegíveis com gastos registrados. |


---

# 🟠 Nível 3 — Operações Temporais Avançadas, Cashbacks e Janelas Deslizantes

## Contexto

Com o sistema básico e relatórios agregados funcionando, a plataforma precisa de mecanismos financeiros mais avançados e realistas: transações com tempo de expiração/agendamento, funcionalidade de rollback (estorno) de pagamentos e cálculo de métricas em tempo real sobre janelas de tempo deslizantes (*Sliding Windows*).

---

## 🛠️ Especificação de Novos Comandos

### 1. `PAYMENT_WITH_CASHBACK`
* **Sintaxe:** `PAYMENT_WITH_CASHBACK <accountId> <amount> <timestamp> <cashbackPercent>`
* **Descrição:** Executa um pagamento debitando o `amount` da conta. No entanto, concede um retorno de saldo (*cashback*) calculado sobre a porcentagem `cashbackPercent` (padrão inteiro `0-100`, arredondado para baixo `floor`). O valor do cashback deve ser creditado de volta na conta exatamente após **86.400.000 ms (24 horas)** a partir do `timestamp` do pagamento.
* **Retorno:**
  * `true`: Pagamento processado com sucesso.
  * `false`: Falha (conta inexistente, saldo insuficiente ou porcentagem inválida).
* **Nota de Requisito:** Se uma operação subsequente no tempo $T_2$ for chamada, todo cashback com tempo de liquidação $T_{liquidacao} \le T_2$ deve ser aplicado **antes** de processar a nova operação.

### 2. `REFUND`
* **Sintaxe:** `REFUND <accountId> <transactionId> <timestamp>`
* **Descrição:** Realiza o estorno parcial ou total de um `PAYMENT` efetuado anteriormente.
* **Regras:**
  * O valor retornado é creditado no saldo e deduzido do `totalSpent` do usuário.
  * Se a transação original gerou cashback pendente ainda não creditado, esse cashback pendente deve ser cancelado proporcionalmente ou totalmente.
  * Não é permitido reembolsar uma transação mais de uma vez ou reembolsar um valor maior do que o pagamento original.
* **Retorno:**
  * `true`: Reembolso processado com sucesso.
  * `false`: Transação inexistente, conta divergente, ou reembolso já processado.

### 3. `SPENT_IN_WINDOW`
* **Sintaxe:** `SPENT_IN_WINDOW <accountId> <windowSizeMs> <currentTimestamp>`
* **Descrição:** Retorna o valor total gasto (*total spent*) pela conta apenas no intervalo de tempo referente à janela deslizante: `[currentTimestamp - windowSizeMs, currentTimestamp]`.
* **Retorno:**
  * `int`: Valor total acumulado de débitos (`PAYMENT` e `TRANSFER` enviados) dentro do intervalo de tempo. Se a conta não existir ou não tiver gastos na janela, retorna `0`.

---

# 🟠 Nível 3 — Operações Temporais Avançadas, Cashbacks e Janelas Deslizantes

## Contexto

Com o sistema básico e relatórios agregados funcionando, a plataforma precisa de mecanismos financeiros mais avançados e realistas: transações com tempo de expiração/agendamento, funcionalidade de rollback (estorno) de pagamentos e cálculo de métricas em tempo real sobre janelas de tempo deslizantes (*Sliding Windows*).

---

## 🛠️ Especificação de Novos Comandos

### 1. `PAYMENT_WITH_CASHBACK`
* **Sintaxe:** `PAYMENT_WITH_CASHBACK <accountId> <amount> <timestamp> <cashbackPercent>`
* **Descrição:** Executa um pagamento debitando o `amount` da conta. No entanto, concede um retorno de saldo (*cashback*) calculado sobre a porcentagem `cashbackPercent` (padrão inteiro `0-100`, arredondado para baixo `floor`). O valor do cashback deve ser creditado de volta na conta exatamente após **86.400.000 ms (24 horas)** a partir do `timestamp` do pagamento.
* **Retorno:**
  * `String`: Identificador gerado da transação (ex: `"TX-1"`) em caso de sucesso.
  * `null`: Falha (conta inexistente, saldo insuficiente ou porcentagem inválida).
* **Nota de Requisito:** Se uma operação subsequente no tempo $T_2$ for chamada, todo cashback com tempo de liquidação $T_{liquidacao} \le T_2$ deve ser aplicado **antes** de processar a nova operação.

### 2. `REFUND`
* **Sintaxe:** `REFUND <accountId> <transactionId> <timestamp>`
* **Descrição:** Realiza o estorno de um pagamento efetuado anteriormente.
* **Regras:**
  * O valor retornado é creditado no saldo e deduzido do `totalSpent` do usuário.
  * Se a transação original gerou cashback pendente ainda não creditado, esse cashback pendente deve ser cancelado antes de maturar.
  * Não é permitido reembolsar uma transação mais de uma vez.
* **Retorno:**
  * `true`: Reembolso processado com sucesso.
  * `false`: Transação inexistente, conta divergente, ou reembolso já processado.

### 3. `SPENT_IN_WINDOW`
* **Sintaxe:** `SPENT_IN_WINDOW <accountId> <windowSizeMs> <currentTimestamp>`
* **Descrição:** Retorna o valor total gasto (*total spent*) pela conta apenas no intervalo de tempo referente à janela deslizante: `[currentTimestamp - windowSizeMs, currentTimestamp]`.
* **Retorno:**
  * `int`: Valor total acumulado de débitos (`PAYMENT` e `TRANSFER` enviados) dentro do intervalo de tempo. Se a conta não existir ou não tiver gastos na janela, retorna `0`.

---

### 📝 Exemplo Prático (Nível 3)

**Entrada (Operações):**

```
CREATE A
DEPOSIT A 1000
PAYMENT_WITH_CASHBACK A 200 1000000 10  // Gasta 200, gera cashback de 20 (10%) para t = 1000000 + 86400000
SPENT_IN_WINDOW A 500000 1200000       // Janela [700000, 1200000]. O pagamento de t=1000000 entra.
PAYMENT A 300 87400000                  // Executa em t = 87400000. O cashback de 20 já maturou! Saldo antes do débito: (800 + 20) = 820.
SPENT_IN_WINDOW A 1000000 87400000     // Janela [86400000, 87400000]. Apenas o pagamento de 300 entra.
```

**Saída esperada:**
```
true
1000
TX-1
200
true
300
```

---

# 📐 Estratégia de Desenvolvimento (Nível 3)

### Etapa 7 — Histórico de Transações e Liquidação de Cashbacks
Implementar a rastreabilidade de transações por ID único e uma fila de prioridade por tempo para processamento de pendências (*Cashback Queue*).

* **Pergunta de design:** Como garantir que saldos pendentes (como cashbacks agendados) sejam liquidados na ordem cronológica correta antes de qualquer operação de leitura ou escrita sem criar loops infinitos?
* **Decisão:** Criação da classe `CashbackEvent` (implements `Comparable`) mantida em uma `PriorityQueue` ordenada por `maturityTimestamp` e indexada em um `Map<String, CashbackEvent>` para cancelamento rápido. O método utilitário `processPendingCashbacks(currentTimestamp)` foi introduzido no `WalletService` e é obrigatoriamente invocado no início de cada operação pública (`payment`, `refund`, `balance` com timestamp, etc.).
* **Justificativa:** Em simulações discretas de tempo, a abordagem *lazy processing* garante determinismo e evita o overhead e a complexidade de threads de background. Ao processar a fila antes de qualquer cálculo de saldo ou validação de débito, garantimos que os saldos estejam rigorosamente atualizados.

---

### Etapa 8 — Janela Deslizante (Sliding Window Query)
Implementar a consulta eficiente de gastos dentro de um intervalo de tempo fixo $T_{fim} - T_{inicio}$.

* **Pergunta de design:** Como calcular o total de gastos em uma janela de tempo em escala sub-linearmemte ($O(\log N)$) em vez de iterar por todo o histórico de transações da conta $O(N)$?
* **Decisão:** Incorporação de um `NavigableMap<Long, Integer>` (`TreeMap`) interno na classe `Account` para mapear `timestamp -> valorGasto`. A consulta utiliza o método `spendingHistory.subMap(startTimestamp, true, endTimestamp, true)`.
* **Justificativa:** O `TreeMap` mantém as chaves temporais ordenadas por árvore rubro-negra. O método `subMap` obtém a fatia exata de intervalos temporais em tempo $O(\log N)$, permitindo somar apenas as transações válidas do intervalo sem percorrer todo o histórico da conta.

---

# 🧠 Aprendizados & Anotações — Nível 3

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | **`TreeMap<Long, Integer>`**: Utilizado em `Account` para manter a linha do tempo de gastos e realizar cortes por janela com `.subMap()` em $O(\log N)$.<br>**`PriorityQueue<CashbackEvent>`**: Min-Heap para simular a fila de liquidação temporizada de cashbacks.<br>**`Map<String, Transaction>` / `Map<String, CashbackEvent>`**: Mapeamento direto de IDs para estorno atômico e cancelamento $O(1)$ de eventos pendentes. |
| **Complexidade** | **`paymentWithCashback()`**: $O(\log E)$ (inserção na Heap de eventos).<br>**`refund()`**: $O(1)$ para cancelamento de evento pendente + $O(\log T)$ para reajuste na árvore temporária da conta.<br>**`spentInWindow()`**: $O(\log T + V)$ onde $T$ é o total de transações e $V$ é o número de transações na janela. |
| **Decisões de Design** | **Sobrecarga do `balance(accountId, timestamp)`**: Mantida compatibilidade com Níveis 1 e 2 criando uma sobrecarga que avança o tempo antes de consultar o saldo.<br>**Cálculo Exato com Inteiros**: Evitado `double` em porcentagens de cashback `(amount * cashbackPercent) / 100` para prevenir erros de truncamento em ponto flutuante. |
| **Tratamento de Erros e Borda**| - Cancelamento de evento pendente via *flag* `isCancelled()` na liquidação lazy caso ocorra `REFUND` antes de 24h.<br>- Suporte a múltiplos pagamentos no mesmo `timestamp` com `spendingHistory.merge(timestamp, amount, Integer::sum)`. |

> *Nota: Novos requisitos serão adicionados apenas após a finalização do nível atual.*