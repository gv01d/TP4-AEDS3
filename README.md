# TP4 - Backup Compactado

O TP4 envolve criar um sistema de backup compactado usando o algoritmo LZW, que processa arquivos como vetores de bytes. Os dados compactados, contendo nome, tamanho e conteúdo, são armazenados em um único arquivo por versão de backup. É necessário implementar rotinas para compactar, descompactar e permitir a recuperação de versões específicas.

## Alunos

- Arthur Clemente Machado
- Gabriel Cunha Schlegel
- Lucas Henrique Rocha Hauck

## Estrutura das Classes
---

### Classe `Backup`
Gerencia operações de backup, incluindo armazenamento e recuperação de arquivos compactados.

- **Atributos**:
  - `id`: Identificador único do backup.
  - `dataCriacao`: Data em que o backup foi criado.
  - `files`: Lista de arquivos envolvidos no backup.
  - `data`: Dados dos arquivos em formato byte.
  - `lzw`: Instância do algoritmo de compactação LZW.
  - `backupFolder`: Pasta onde os backups são armazenados.

- **Métodos**:
  - `setId(int id)`: Define o ID do backup.
  - `getId()`: Retorna o ID do backup.
  - `setFilesFromFolder(String folder)`: Carrega arquivos de uma pasta específica para o backup.
  - `setFiles(ArrayList<String> files)`: Define a lista de arquivos para o backup.
  - `addFile(String file)`: Adiciona um arquivo à lista de arquivos do backup.
  - `listBackups()`: Lista os backups disponíveis no gerenciador.
  - `deleteBackup(int id)`: Deleta um backup específico.
  - `refactor()`: Refatora o gerenciador de backups, removendo backups inválidos.
  - `toByteArray()`: Converte o backup em um array de bytes.
  - `fromByteArray(byte[] ba)`: Converte um array de bytes em um backup.
  - `backupFiles()`: Realiza o backup dos arquivos e atualiza o gerenciador.
  - `loadFile(String file)`: Carrega um arquivo para o formato byte[].
  - `restoreBackup(int id)`: Restaura um backup específico.
  - `restoreBackup(String nome)`: Restaura um backup pelo nome do arquivo.
  - `restoreFile(String file, byte[] data)`: Restaura um arquivo a partir dos dados recuperados.


---

### Classe `MenuBackups`
Gerencia as opções do menu de backups, permitindo criar, excluir, carregar, listar e refatorar backups.

- **Atributos**:
  - `scanner`: Instância de `Scanner` para interação com o usuário.
  - `backup`: Instância da classe `Backup` para realizar operações de backup.
  - `defaultFolder`: Caminho da pasta padrão para backup (".\\dados").

- **Métodos**:
  - `menu()`: Exibe o menu principal e permite ao usuário escolher uma ação relacionada aos backups.
  - `criarBackup()`: Cria um backup com os arquivos da pasta definida.
  - `excluirBackup()`: Exclui um backup existente após o usuário selecionar um da lista.
  - `carregarBackup()`: Carrega um backup selecionado a partir da lista de backups disponíveis.
  - `listarBackups()`: Exibe a lista de backups disponíveis.
  - `refatorar()`: Refatora o gerenciador de backups, removendo backups inválidos.

---

### Classe `IO`
Classe principal utilizada para testar a funcionalidade do projeto. Possui o Menu inicial e chama os menus de categoria, tarefa e rótulo dependendo da opção.

---

## Experiência

- **Dificuldades**: O Java estava dando rollback no arquivo gerenciadorDeBackup.db quando a função refactor() era chamada, já que a função deve apagar os dados inválidos de dentro do arquivo, mas como estava definindo como 0, o próprio java dava rollback para não deixar o arquivo vazio, voltando assim registros antigos já excluídos.
  
- **Funcionamento**: O projeto realiza a compressão de dados e o backup dos mesmos, além disso, temos o refatorar() que serve para limpar backups inválidos.


## Perguntas e Respostas

- Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos? **Sim.**
- Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos? **Sim.**
- O usuário pode escolher a versão a recuperar? **Sim.**
- Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais) **55%.**
- O trabalho está funcionando corretamente? **Sim.**
- O trabalho está completo? **Sim.**
- O trabalho é original e não a cópia de um trabalho de um colega? **Sim.**

