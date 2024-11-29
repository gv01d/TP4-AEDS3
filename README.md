# TP3 - Buscas e Relacionamento N:N

Este projeto expande as funcionalidades do sistema de gerenciamento de tarefas ao adicionar um relacionamento N:N entre tarefas, categorias e rotulos, permitindo que uma tarefa esteja vinculada a várias categorias e vice-versa. Além disso, foi implementado um mecanismo de buscas por termos nas tarefas utilizando listas invertidas, otimizando a eficiência na recuperação de informações. O uso de estruturas como as listas invertidas para os termos e uma tabela de associação para gerenciar as relações N:N garante maior flexibilidade e desempenho na manipulação e busca dos dados, mesmo em cenários de grande volume.

## Alunos

- Arthur Clemente Machado
- Gabriel Cunha Schlegel
- Lucas Henrique Rocha Hauck

## Estrutura das Classes
---

### Classe `ArquivoTarefas`
Estende a classe `Arquivo` para gerenciar os dados de tarefas.
- **Métodos**:
- `create(Tarefa tarefa):` Insere uma nova tarefa e a associa a uma categoria.
- `createN(int idTarefa, ArrayList<Integer> id2):` Vincula uma tarefa a múltiplos rótulos.
- `getN(int idTarefa):` Retorna os IDs dos rótulos associados a uma tarefa.
- `delete(int id):` Remove uma tarefa e atualiza os índices relacionados.
- `deleteN(int idTarefa):` Remove os vínculos de uma tarefa com seus rótulos.
- `update(Tarefa novaTarefa):` Atualiza uma tarefa, incluindo vínculos com categorias e índices.
- `updateN(int idTarefa, ArrayList<Integer> id2):` Atualiza os rótulos vinculados a uma tarefa.
- `buscarPorCategoria(int idCategoria):` Lista as tarefas de uma categoria específica.
- `printTarefas():` Exibe todas as tarefas disponíveis.
- `getTarefas():` Retorna uma lista com todas as tarefas.
- `getTarefasbyRotulos(ArrayList<Integer> idRotulo):` Retorna tarefas associadas a todos os rótulos fornecidos.

---

### Classe `Tarefas_ctl`
- **Métodos**:
- `Tarefas_ctl(ArquivoTarefas arqTarefas, ArquivoCategorias arqCategorias, ArquivoRotulos arqRotulos)`: Construtor que inicializa o controlador com instâncias de `ArquivoTarefas`, `ArquivoCategorias` e `ArquivoRotulos`.
- `adicionarTarefa(Tarefa tarefa, ArrayList<Integer> rotulos)`: Adiciona uma tarefa e associa rótulos a ela chamando os métodos `create` e `createN` de `ArquivoTarefas`.
- `atualizarTarefa(Tarefa tarefa, ArrayList<Integer> rotulos)`: Atualiza uma tarefa e seus rótulos, chamando os métodos `update` e `updateN` de `ArquivoTarefas`.
- `excluirTarefa(int idTarefa)`: Exclui uma tarefa e os rótulos associados a ela, chamando os métodos `delete` e `deleteN` de `ArquivoTarefas`.
- `getRotulosId(int idTarefa)`: Retorna os rótulos associados a uma tarefa, chamando o método `getN` de `ArquivoTarefas`.
- `getTarefaByID(int id)`: Retorna uma tarefa com o ID especificado, chamando o método `read` de `ArquivoTarefas`.
- `getTarefas()`: Retorna todas as tarefas, chamando o método `getTarefas` de `ArquivoTarefas`.
- `getTarefasByCategoria(int idCategoria)`: Retorna as tarefas associadas a uma categoria, chamando o método `buscarPorCategoria` de `ArquivoTarefas`.
- `getTarefasByRotulos(ArrayList<Integer> idRotulo)`: Retorna as tarefas associadas a uma lista de rótulos, chamando o método `getTarefasbyRotulos` de `ArquivoTarefas`.
- `getCategorias()`: Retorna todas as categorias, chamando o método `getCategorias` de `ArquivoCategorias`.
- `getRotulos()`: Retorna todos os rótulos, chamando o método `getRotulos` de `ArquivoRotulos`.

---

### Classe `MenuTarefas`
A classe `MenuTarefas` oferece uma interface interativa para gerenciamento de tarefas, permitindo ao usuário adicionar, excluir, atualizar, listar e buscar tarefas. Ela trabalha em conjunto com as classes `Tarefas_ctl`, `Categorias_ctl`, `Rotulos_ctl` e `ListaInvertida`, além de utilizar a classe `StopWords` para lidar com palavras de parada em alguns contextos relacionados às tarefas.
- **Métodos**:
- `MenuTarefas(Tarefas_ctl ctlTarefas, Categorias_ctl ctlCategorias, Rotulos_ctl ctlRotulos, ListaInvertida list, StopWords stopWords)`: Construtor que inicializa a classe com as instâncias das classes responsáveis pelo controle de tarefas, categorias, rótulos e lista invertida, além de um objeto `StopWords` e um scanner para capturar entradas do usuário.
- `menu()`: Exibe o menu interativo e executa a ação correspondente com base na opção escolhida pelo usuário. O menu apresenta opções como incluir, excluir, atualizar, listar e buscar tarefas.
- `adicionarTarefa()`: Permite ao usuário adicionar uma nova tarefa, pedindo informações como nome, categoria, rótulos, data de conclusão, status e prioridade.
- `atualizarTarefa()`: Permite ao usuário atualizar uma tarefa existente, escolhendo uma tarefa da lista e modificando campos como nome, categoria, rótulos, data de conclusão, status e prioridade.
- `excluirTarefa()`: Exclui uma tarefa específica, caso exista.
- `listarTarefas()`: Exibe todas as tarefas cadastradas no sistema.
- `buscarTarefas()`: Realiza uma busca de tarefas com base em critérios definidos.
- `buscarPorNome()`: Permite ao usuário buscar tarefas por nome, utilizando um cálculo de probabilidade baseado em palavras-chave fornecidas.
- `buscarPorCategoria()`: Permite ao usuário buscar tarefas por categoria, exibindo todas as tarefas relacionadas à categoria escolhida.
- `buscarRotulos()`: Permite ao usuário buscar tarefas por rótulos, exibindo todas as tarefas associadas aos rótulos escolhidos.
- `SearchElement`: Classe interna que representa um elemento de busca, contendo um `id` e a `probabilidade` associada a esse elemento. Implementa a interface `Comparable` para ordenar os resultados de busca.

---

### Classe `ArquivoRotulos`
Estende a classe `Arquivo` para gerenciar os dados de tarefas.
- **Métodos**:
- `create(Rotulo rotulo)`: Insere um novo rótulo e inicializa seus índices.
- `createN(int idRotulo, ArrayList<Integer> id2)`: Vincula um rótulo a múltiplas tarefas.
- `read(int id)`: Retorna o rótulo correspondente ao ID fornecido.
- `getN(int idRotulo)`: Retorna os IDs das tarefas associadas a um rótulo.
- `update(Rotulo rotulo)`: Atualiza as informações de um rótulo.
- `updateN(int idRotulo, ArrayList<Integer> id2)`: Atualiza os vínculos entre um rótulo e suas tarefas.
- `delete(int id)`: Remove um rótulo e seus vínculos com as tarefas.
- `getRotulos()`: Retorna todos os rótulos disponíveis.
- `getRotulosByTarefa(int idTarefa)`: Retorna os rótulos associados a uma tarefa específica.

---

### Classe `MenuRotulos`
- **Métodos**:
- `menu()`: Exibe o menu principal para manipulação de rótulos e processa a escolha do usuário.
- `adicionarRotulo()`: Solicita informações do usuário e adiciona um novo rótulo.
- `atualizarRotulo()`: Permite ao usuário selecionar e atualizar um rótulo existente.
- `excluirTarefa()`: Exclui um rótulo selecionado pelo usuário.
- `listarRotulctlRotulos()`: Lista todos os rótulos armazenados.

---

### Classe `Rotulo`
- **Métodos**:
- `Rotulo()`: Construtor padrão que inicializa o rótulo com ID -1 e nome vazio.
- `Rotulo(int id, String name)`: Construtor que inicializa o rótulo com ID e nome fornecidos.
- `Rotulo(String name)`: Construtor que inicializa o rótulo com um nome fornecido e ID -1.
- `getId()`: Retorna o ID do rótulo.
- `setId(int id)`: Define o ID do rótulo.
- `getNome()`: Retorna o nome do rótulo.
- `setNome(String name)`: Define o nome do rótulo.
- `toString()`: Retorna uma representação em formato de texto do rótulo.
- `toByteArray()`: Serializa o rótulo em um array de bytes.
- `fromByteArray(byte[] ba)`: Reconstrói o rótulo a partir de um array de bytes.
- `compareTo(Object o)`: Compara o rótulo atual com outro objeto do tipo `Rotulo` pelo ID.

---

### Classe `Rotulo_ctl`
- **Métodos**:
- `Rotulos_ctl(ArquivoRotulos arqRotulo, ArquivoTarefas arqTarefas)`: Construtor que inicializa o controlador com instâncias de `ArquivoRotulos` e `ArquivoTarefas`.
- `adicionarRotulo(Rotulo rotulo)`: Adiciona um novo rótulo chamando o método `create` do `ArquivoRotulos`.
- `atualizarRotulo(Rotulo rotulo)`: Atualiza um rótulo chamando o método `update` do `ArquivoRotulos`.
- `excluirRotulo(int idTarefa)`: Exclui um rótulo, chamando o método `delete` do `ArquivoRotulos`.
- `getRotulosByTarefa(int idTarefa)`: Retorna a lista de rótulos associados a uma tarefa, chamando `getRotulosByTarefa` do `ArquivoRotulos`.
- `getRotulos()`: Retorna todos os rótulos disponíveis, chamando `getRotulos` do `ArquivoRotulos`.
- `getTarefas()`: Retorna a lista de tarefas disponíveis, chamando `getTarefas` do `ArquivoTarefas`.

---

### Classe `StopWords`
A classe `StopWords` gerencia palavras consideradas irrelevantes em processos de filtragem de texto, como stopwords. Ela oferece funcionalidades para adicionar, excluir e listar essas palavras, além de permitir a refatoração dos dados para manter apenas palavras ativas.
- **Métodos**:
- `StopWords(String n)`: Construtor que cria o diretório necessário e inicializa o arquivo de armazenamento com o nome especificado. Configura o cabeçalho com a quantidade de palavras.
- `create(String word)`: Adiciona uma única palavra ao arquivo de stopwords, normalizando-a e registrando-a com sua respectiva marcação e tamanho.
- `create(String[] words)`: Adiciona um conjunto de palavras ao arquivo, atualizando o cabeçalho com a nova quantidade total de palavras.
- `read()`: Lê todas as palavras do arquivo, retornando-as em um array de strings. Ignora palavras que foram excluídas (marcadas com "x").
- `delete(String word)`: Exclui uma palavra, marcando-a como deletada no arquivo e atualizando o cabeçalho.
- `getTotalAmount()`: Retorna o total de palavras, incluindo as excluídas, no arquivo.
- `getAmount()`: Retorna a quantidade de palavras não excluídas armazenadas no arquivo.
- `refactorData()`: Refatora o arquivo removendo as palavras excluídas e regravando apenas as palavras ativas.
- `filter(String[] words)`: Filtra um array de palavras, removendo as consideradas stopwords e retornando um novo array com as palavras válidas.

---

### Classe `MenuStopWords`
A classe `MenuStopWords` fornece uma interface de linha de comando interativa para gerenciar palavras de parada (stopwords) usando a classe `StopWords`. Ela permite ao usuário adicionar, excluir, atualizar, listar e refatorar as palavras de parada armazenadas.
- **Métodos**:
- `MenuStopWords(StopWords stopWords)`: Construtor que inicializa o menu com uma instância de `StopWords` e um scanner para captura de entradas do usuário.
- `menu()`: Exibe o menu interativo e executa a ação correspondente com base na opção escolhida pelo usuário.
- `adicionarStopWords()`: Permite ao usuário adicionar palavras ao arquivo de stopwords.
- `excluirStopWords()`: Permite ao usuário excluir palavras do arquivo de stopwords.
- `atualizarStopWords()`: Permite ao usuário atualizar uma palavra existente, deletando a antiga e adicionando a nova.
- `listarStopWords()`: Exibe todas as palavras de stopwords armazenadas no arquivo.
- `RefactorStopWords()`: Refatora os dados, removendo palavras excluídas e mantendo apenas as palavras ativas.

---

### Classe `IO`
Classe principal utilizada para testar a funcionalidade do projeto. Possui o Menu inicial e chama os menus de categoria, tarefa e rótulo dependendo da opção.

---

## Experiência

- **Dificuldades**: Entender e implementar o relacionamento N:N.
  
- **Funcionamento**: O projeto gerencia tarefas com relacionamentos N:N entre tarefas, categorias e rótulos. Utiliza listas invertidas e árvores B+ para otimizar as buscas. O usuário pode adicionar, atualizar, excluir e listar tarefas, categorias, rótulos e palavras de parada, com buscas eficientes por nome, categoria e rótulos.


## Perguntas e Respostas

- O índice invertido com os termos das tarefas foi criado usando a classe ListaInvertida? **Sim.**
- O CRUD de rótulos foi implementado? **Sim.**
- No arquivo de tarefas, os rótulos são incluídos, alterados e excluídos em uma árvore B+? **Sim.**
- É possível buscar tarefas por palavras usando o índice invertido? **Sim.**
- É possível buscar tarefas por rótulos usando uma árvore B+? **Sim.**
- O trabalho está completo? **Sim.**
- O trabalho é original e não a cópia de um trabalho de um colega? **Sim.**

