Sistema de Controle de Despesas

Entrar Despesa-
Permite inserir uma nova despesa com detalhes como descrição, valor, data de vencimento e categoria.

Anotar Pagamento-
Registra um pagamento para uma despesa específica, incluindo a data e o valor do pagamento.

Listar Despesas em Aberto-
Exibe todas as despesas que estão pendentes de pagamento.

Listar Despesas Pagas-
Mostra todas as despesas que já foram pagas.

Gerenciar Tipos de Despesa-
Permite criar, editar, listar e excluir tipos de despesa. Os tipos são armazenados em um arquivo de texto separado.

Gerenciar Usuários-
Cadastra, edita e lista usuários com login e senha. As senhas são criptografadas antes de serem armazenadas.

Sair-
Encerra o sistema.

SistemaControleDespesas
│
├── src/
│   ├── SistemaControleDespesas.java
│   ├── Despesa.java
│   ├── DespesaImpl.java
│   ├── Pagamento.java
│   ├── CriptografiaUtil.java
│   └── GerenciadorArquivos.java
│
├── data/
│   ├── despesas.txt
│   ├── tipos_despesa.txt
│   └── usuarios.txt
│
└── README.md

