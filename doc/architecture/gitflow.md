# 📌 Guia de Versionamento — GitFlow

Este documento descreve o fluxo de trabalho no GitHub para versionar código fonte.

---

## 🚀 Passo a Passo de Execução

### 1. Criar e publicar a Tag
A partir da branch `feature/level-1`, registramos a tag `v1.0-level1` para marcar o estado estável da aplicação ao fim do desenvolvimento do Nível 1.

```bash
# Cria a tag anotada para a versão 1.0
git tag -a v1.0-level1 -m "Release do Nível 1 - Criar conta e consultar saldo"

# Envia a tag recém-criada para o repositório remoto
git push origin 1.0-level1
```

---

### 2. Atualizar e integrar com a branch `main`
Mudamos para a branch `main`, sincronizamos com o servidor remoto e realizamos o *merge* das implementações.

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


## 🎯 Resultado Esperado
* **Tag:** `v2.0-level2` criada e enviada ao servidor remoto.
* **Branch `main`:** Atualizada com todas as funcionalidades do Nível 2.
* **Branch Atual:** `feature/level-3` pronta para o desenvolvimento do Nível 3.