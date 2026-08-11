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

# 🚀 Evolução Futura

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
* **Decisão:** _A preencher após análise._
* **Justificativa:** _A preencher após análise._

---

### Etapa 6 — Ranking dos Mais Gastadores (Top K)
Implementar a assinatura:
```java
List<String> topSpenders(int k);
```

* **Pergunta de design:** Qual estrutura de dados e abordagem algorítmica devemos utilizar para obter as top $K$ contas com maior volume de gastos sem ordenar desnecessariamente todas as contas do sistema em $O(N \log N)$?
* **Decisão:** _A preencher após análise (ex: PriorityQueue / Min-Heap de tamanho K vs Ordenação On-demand vs Manutenção de TreeSet).*
* **Justificativa:** _A preencher após análise._

---

# 🧠 Aprendizados & Anotações — Nível 2

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | _A preencher após implementação (detalhar uso de PriorityQueue / Min-Heap / Comparatores Customizados)._ |
| **Complexidade** | **`payment()`**: tempo **O(1)** esperado e espaço **O(1)**.<br>**`topSpenders(k)`**: tempo **O(N log K)** utilizando Min-Heap de tamanho $K$ (onde $N$ é o número de contas com gastos no sistema). Espaço adicional **O(K)** para a fila de prioridades. |
| **Decisões de Design** | _A preencher após implementação (como rastrear o atributo `totalSpent` dentro da entidade `Account` sem violar o encapsulamento)._ |
| **Comparatores e Empates** | A regra de desempate exige a criação de um `Comparator` customizado no Java. Para um Min-Heap de tamanho $K$, o elemento no topo (raiz) deve ser o **pior candidato a entrar no Top K**. Portanto, em caso de empate no valor gasto, o valor lexicograficamente **maior** (ex: "B" > "A") é considerado "pior" e fica no topo da heap para ser removido primeiro se surgir um candidato melhor. |
| **Tratamento de Erros e Borda**| - O parâmetro $K$ pode ser maior do que o número total de contas ativas com gastos no sistema.<br>- Contas com gasto igual a `0` não devem poluir a lista do `TOP_SPENDERS`.<br>- Parâmetros inválidos como `amount <= 0` ou `k <= 0` devem ser devidamente tratados. |

> *Nota: Novos requisitos serão adicionados apenas após a finalização do nível atual.*