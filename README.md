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

| Tópico                                 | Anotações / Reflexões                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| :------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Estruturas de Dados**                | Foi escolhido **`HashMap<String, Account>`** para armazenar as contas, pois o `accountId` é uma chave única e será utilizado frequentemente para localizar uma conta. Essa estrutura oferece busca, inserção e remoção com complexidade esperada **O(1)**. A escolha evita buscas lineares que ocorreriam com uma `ArrayList`, cuja complexidade seria **O(n)**. Para percorrer todas as contas, podemos utilizar `entrySet()`, `values()` ou `keySet()`. Nesse caso, a complexidade da iteração é **O(n)**. |
| **Complexidade**                       | **`create()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`deposit()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`getAccountById()`**: tempo **O(1)** esperado e espaço adicional **O(1)**. **`transfer()`**: tempo **O(1)** esperado, pois realiza duas buscas no `HashMap` e executa operações de complexidade constante em `Account`. O espaço adicional utilizado por `transfer()` é **O(1)**. **`WalletService`** possui espaço total **O(n)**, onde `n` representa a quantidade de contas armazenadas. |
| **Decisões de Design**                 | O projeto foi dividido em duas responsabilidades principais. **`WalletService`** coordena os casos de uso da aplicação, como criar contas, localizar contas, realizar depósitos e orquestrar transferências. **`Account`** representa a entidade de domínio e é responsável por manter seu próprio estado consistente. Na transferência, o serviço localiza as contas e coordena a operação, enquanto a própria `Account` decide se pode realizar um `withdraw()` e modifica seu próprio saldo. |
| **Princípios de Orientação a Objetos** | O estado interno de uma conta (`balance`) não deve ser alterado diretamente por outras classes. As alterações são realizadas por meio dos métodos da própria entidade, como `deposit()` e `withdraw()`. O método `withdraw()` encapsula a regra de que uma conta não pode retirar um valor superior ao seu saldo. Dessa forma, a entidade protege suas próprias invariantes e evita que regras relacionadas ao seu estado fiquem espalhadas pelo sistema. |
| **Encapsulamento**                     | A responsabilidade de modificar o saldo pertence à classe **`Account`**. O `WalletService` não altera diretamente `balance`; ele solicita operações à entidade por meio de `deposit()` e `withdraw()`. Essa abordagem reduz o acoplamento e facilita futuras alterações nas regras da conta. |
| **Transferência**                      | O método **`transfer(String from, String to, int amount)`** valida a existência das contas de origem e destino, verifica se o valor é positivo e impede transferências para a própria conta. Depois das validações, solicita `withdraw(amount)` à conta de origem. Se o saque não for possível por falta de saldo, a operação retorna `false`. Caso contrário, o valor é depositado na conta de destino e a operação retorna `true`. |
| **Tratamento de Erros**                | A ausência de uma conta não é considerada uma exceção; faz parte do fluxo normal do negócio. Por isso, `deposit()` e `transfer()` retornam valores indicando falha quando a conta não existe. Valores inválidos (`amount <= 0`) são tratados como entrada inválida nas operações da entidade (`deposit()` e `withdraw()`), que lançam `IllegalArgumentException`. No contexto de `transfer()`, optamos por retornar `false` para indicar que a operação não foi realizada. |
| **Regras de Negócio**                  | Uma transferência somente pode ocorrer quando as contas de origem e destino existem, são diferentes, o valor é positivo e a conta de origem possui saldo suficiente. Caso qualquer uma dessas condições não seja atendida, nenhuma alteração de saldo deve ocorrer e `transfer()` retorna `false`. Quando todas as condições são atendidas, o saldo é debitado da origem, creditado no destino e o método retorna `true`. |
| **Atomicidade**                        | Uma transferência envolve duas alterações de estado: retirar dinheiro da conta de origem e adicionar dinheiro à conta de destino. Foi identificado que, em sistemas reais, essas operações precisam ser tratadas de forma **atômica**, para evitar situações em que o dinheiro seja retirado da origem mas não seja creditado no destino. Esse conceito será aprofundado posteriormente, especialmente ao estudar transações e persistência de dados. |
| **Boas Práticas Java**                 | Programar contra interfaces (`Map<String, Account>` em vez de `HashMap<String, Account>`), utilizar `final` para atributos que não precisam ser reassociados, encapsular atributos privados, utilizar nomes claros para métodos e variáveis e manter as responsabilidades das classes bem definidas. Também foi utilizado `Map.Entry<String, Account>` para percorrer simultaneamente as chaves e os valores do `HashMap`. |
| **Pontos para Revisar**                | - Diferença entre **complexidade de tempo** e **complexidade de espaço**.<br>- Entender por que `HashMap` possui complexidade esperada **O(1)** e em quais situações ela pode se degradar devido a colisões de hash.<br>- Revisar quando utilizar `HashMap`, `TreeMap`, `HashSet` e `ArrayList`.<br>- Estudar encapsulamento e responsabilidade das entidades em orientação a objetos.<br>- Entender a diferença entre uma regra de negócio pertencente à entidade e uma regra de orquestração pertencente ao serviço.<br>- Estudar o conceito de **atomicidade** e transações.<br>- Continuar praticando a análise de complexidade antes de iniciar a implementação de cada solução.<br>- Praticar a identificação de casos-limite antes da implementação. |

---

# 🚀 Evolução Futura

Após a conclusão do **Nível 1**, o exercício avançará para novos requisitos:

- [ ] **Top K** contas por saldo
- [ ] Histórico de operações
- [ ] Operação de **UNDO**
- [ ] Consultas por **timestamp**

> *Nota: Novos requisitos serão adicionados apenas após a finalização do nível atual.*