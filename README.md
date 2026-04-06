# ⛏️ MixedMines

Sistema avançado de minas para servidores **Spigot 1.8.8**, com foco em performance, customização e experiência profissional.

---

## 📌 Visão Geral

O **MixedMines** é um plugin completo de minas que permite criar regiões mineráveis com:

* composição personalizada de blocos
* reset automático por tempo ou porcentagem
* sistema de bônus avançado
* ranking de mineradores por mina
* integração com PlaceholderAPI
* efeitos visuais e recompensas configuráveis

Ideal para servidores **Survival, Prison, Rankup e Skyblock**.

---

## 🚀 Recursos

### 🪨 Sistema de Minas

* Criação e redefinição de minas
* Seleção por **p1 / p2**
* Detecção automática da mina atual
* Composição de blocos por porcentagem
* Reset manual, por % e por tempo
* Delay de reset configurável
* Modo silencioso (sem broadcast)

---

### 🎁 Sistema de Bônus

* Suporte a múltiplos tipos de recompensa:

  * `command`
  * `item`
  * `money` (via comando)
  * `effect`
  * `sound`
  * `title`
  * `actionbar`
  * `firework`
  * `message`
* Chance configurável
* Cooldown por jogador
* Permissão por bônus
* Broadcast opcional
* Múltiplas recompensas por bônus

---

### 🥇 Ranking (Top Mineradores)

* Top por mina
* Ranking atualizado em tempo real
* Reset automático ao resetar a mina
* Persistência em `tops.yml`

---

### 🔌 PlaceholderAPI

Suporte completo para integração com:

* Scoreboards
* Hologramas
* TAB / FeatherBoard

#### Exemplos:

```
%mixedmines_current_name%
%mixedmines_current_percentage%

%mixedmines_mina_top_1%
%mixedmines_mina_top_1_amount%

%mixedmines_mina_player_position%
%mixedmines_mina_player_broken%
```

---

### 🎆 Efeitos Visuais

Quando um bônus ativa:

* Som (`Sound`)
* Title / Subtitle
* ActionBar
* Firework
* Mensagem no chat

Tudo configurável.

---

### 📍 Teleporte de Mina

* Definição de spawn da mina
* Teleporte automático no reset
* Comando dedicado

---

### ⚙️ Composição Avançada

Defina múltiplos blocos:

```
/mixedmines setcomposition mina stone;80,iron_ore;15,gold_ore;5
```

✔ Validação automática
✔ Soma obrigatória = 100%

---

## 📦 Comandos

### Administração

```
/mixedmines create <mina>
/mixedmines redefine <mina>
/mixedmines list
/mixedmines info [mina]

/mixedmines p1
/mixedmines p2

/mixedmines setcomposition <mina> <lista>
/mixedmines settp <mina>

/mixedmines reset <mina>
/mixedmines silent <mina> <true/false>

/mixedmines setresetpercent <mina> <0-100>
/mixedmines setresettime <mina> <segundos>
/mixedmines setresetdelay <mina> <segundos>

/mixedmines bonus add <mina> <id>
/mixedmines bonus remove <mina> <id>
/mixedmines bonus list <mina>

/mixedmines reload
```

---

### Jogadores

```
/mixedmines top [mina]
```

---

## ⚙️ Configuração

### Exemplo básico

```yml
mines:
  teste:
    pos1: "world,0,60,0,0,0"
    pos2: "world,10,65,10,0,0"
    tp: "world,5,66,5,0,0"

    silent: false
    reset-percent: 70
    reset-delay: 5
    reset-time: 300

    composition:
      STONE: 80
      IRON_ORE: 15
      GOLD_ORE: 5

    bonuses:
      vip:
        chance: 2.0
        cooldown: 30
        broadcast: false
        rewards:
          - type: command
            value: "give %player% diamond 1"
          - type: sound
            value: "LEVEL_UP"
          - type: title
            title: "&a&lBÔNUS!"
            subtitle: "&fVocê ganhou um diamante"
          - type: actionbar
            value: "&e+1 Diamante"
          - type: firework
            power: 1
```

---

## 📁 Arquivos

```
/plugins/MixedMines/
├── config.yml
├── tops.yml
```

---

## 🧠 Performance

* Não salva a cada bloco quebrado
* Auto-save assíncrono
* Uso de cache interno
* Baixo impacto em servidores 1.8.8

---

## 🔮 Roadmap

* GUI administrativa
* Hologramas automáticos
* Integração com Vault (economia real)
* Sistema de eventos de mina (x2 drops, bônus global)
* API pública para desenvolvedores
* Cache por chunk (alta performance)

---

## 🛠️ Requisitos

* Spigot 1.8.8
* Java 8
* PlaceholderAPI (opcional)

---

## 👨‍💻 Autor

Desenvolvido por **Hognose**

---

## 📜 Licença

Uso privado ou público permitido.
Não redistribuir sem autorização.

---

## ⭐ Dica

Use junto com:

* FeatherBoard
* TAB
* HolographicDisplays

Para extrair o máximo do sistema de placeholders.

---

## 💬 Suporte

Se precisar de melhorias, integração ou versão customizada, só chamar.
