# 📌 Guia de Release e Versionamento — Transição do Nível 2 para o Nível 3

Este documento descreve o fluxo de trabalho no Git para registrar a release do **Nível 2**, integrar as alterações na branch principal (`main`) e preparar a nova branch de trabalho para o desenvolvimento do **Nível 3**.

---

## 🚀 Passo a Passo de Execução

### 1. Criar e publicar a Tag do Nível 2
A partir da branch `feature/level-2`, registramos a tag `v2.0-level2` para marcar o estado estável da aplicação ao fim do desenvolvimento do Nível 2.

```bash
# Cria a tag anotada para a versão 2.0
git tag -a v2.0-level2 -m "Release do Nível 2 - Top Spenders e Payment"

# Envia a tag recém-criada para o repositório remoto
git push origin v2.0-level2
```

---

### 2. Atualizar e integrar com a branch `main`
Mudamos para a branch `main`, sincronizamos com o servidor remoto e realizamos o *merge* das implementações do Nível 2.

```bash
# Alterna para a branch principal
git checkout main

# Atualiza a main local com o servidor remoto
git pull origin main

# Integra as alterações do Nível 2 na main
git merge feature/level-2

# Envia a branch main atualizada para o servidor remoto
git push origin main
```

---

### 3. Criar a nova branch para o Nível 3
A partir da `main` atualizada, criamos a nova branch de funcionalidade `feature/level-3` e a publicamos no repositório remoto.

```bash
# Cria e altera para a nova branch a partir da main
git checkout -b feature/level-3

# Publica a nova branch no repositório remoto e define o upstream
git push -u origin feature/level-3
```

---

## 🎯 Resultado Esperado
* **Tag:** `v2.0-level2` criada e enviada ao servidor remoto.
* **Branch `main`:** Atualizada com todas as funcionalidades do Nível 2.
* **Branch Atual:** `feature/level-3` pronta para o desenvolvimento do Nível 3.