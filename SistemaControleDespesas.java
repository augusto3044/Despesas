
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


interface Pagamento {
    void registrarPagamento(double valor, LocalDate dataPagamento);
}


abstract class Despesa {
    private String descricao;
    private double valor;
    private LocalDate dataVencimento;
    private String categoria;

    public Despesa(String descricao, double valor, LocalDate dataVencimento, String categoria) {
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public abstract void exibirDetalhes();
}

class DespesaImpl extends Despesa implements Pagamento {
    private double valorPago;
    private boolean paga;

    public DespesaImpl(String descricao, double valor, LocalDate dataVencimento, String categoria) {
        super(descricao, valor, dataVencimento, categoria);
        this.valorPago = 0;
        this.paga = false;
    }

    @Override
    public void registrarPagamento(double valor, LocalDate dataPagamento) {
        this.valorPago += valor;
        if (this.valorPago >= getValor()) {
            this.paga = true;
        }
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Despesa:");
        System.out.println("Descrição: " + getDescricao());
        System.out.println("Valor: " + getValor());
        System.out.println("Data de Vencimento: " + getDataVencimento());
        System.out.println("Status: " + (paga ? "Paga" : "Não Paga"));
    }

    public double getValorPago() {
        return valorPago;
    }

    public boolean isPaga() {
        return paga;
    }
}

class CriptografiaUtil {
    public static String criptografarSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}


class GerenciadorArquivos {
    public static void escreverEmArquivo(String caminho, String conteudo) {
        try (FileWriter fw = new FileWriter(caminho, true)) {
            fw.write(conteudo + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String lerDeArquivo(String caminho) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

public class SistemaControleDespesas {
    private static List<Despesa> despesas = new ArrayList<>();
    private static List<String> tiposDespesa = new ArrayList<>();
    private static List<String> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Menu Principal:");
            System.out.println("1. Entrar Despesa");
            System.out.println("2. Anotar Pagamento");
            System.out.println("3. Listar Despesas em Aberto no período");
            System.out.println("4. Listar Despesas Pagas no período");
            System.out.println("5. Gerenciar Tipos de Despesa");
            System.out.println("6. Gerenciar Usuários");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    
                    System.out.print("Descrição da Despesa: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Valor da Despesa: ");
                    double valor = scanner.nextDouble();
                    System.out.print("Data de Vencimento (YYYY-MM-DD): ");
                    LocalDate dataVencimento = LocalDate.parse(scanner.next());
                    scanner.nextLine();
                    System.out.print("Categoria da Despesa: ");
                    String categoria = scanner.nextLine();
                    Despesa despesa = new DespesaImpl(descricao, valor, dataVencimento, categoria);
                    despesas.add(despesa);
                    GerenciadorArquivos.escreverEmArquivo("despesas.txt", descricao + "," + valor + "," + dataVencimento + "," + categoria);
                    System.out.println("Despesa registrada com sucesso!");
                    break;
                case 2:
                    
                    System.out.print("Descrição da Despesa para pagamento: ");
                    String descPagamento = scanner.nextLine();
                    System.out.print("Valor do Pagamento: ");
                    double valorPagamento = scanner.nextDouble();
                    System.out.print("Data do Pagamento (YYYY-MM-DD): ");
                    LocalDate dataPagamento = LocalDate.parse(scanner.next());
                    scanner.nextLine(); 
                    for (Despesa d : despesas) {
                        if (d.getDescricao().equals(descPagamento)) {
                            ((DespesaImpl) d).registrarPagamento(valorPagamento, dataPagamento);
                            GerenciadorArquivos.escreverEmArquivo("pagamentos.txt", descPagamento + "," + valorPagamento + "," + dataPagamento);
                            System.out.println("Pagamento registrado com sucesso!");
                            break;
                        }
                    }
                    break;
                case 3:
                   
                    System.out.println("Despesas em Aberto:");
                    for (Despesa d : despesas) {
                        if (!((DespesaImpl) d).isPaga()) {
                            d.exibirDetalhes();
                        }
                    }
                    break;
                case 4:
                    
                    System.out.println("Despesas Pagas:");
                    for (Despesa d : despesas) {
                        if (((DespesaImpl) d).isPaga()) {
                            d.exibirDetalhes();
                        }
                    }
                    break;
                case 5:
                   
                    System.out.println("Gerenciar Tipos de Despesa:");
                    System.out.println("1. Adicionar Tipo");
                    System.out.println("2. Remover Tipo");
                    System.out.println("3. Listar Tipos");
                    int opcaoTipo = scanner.nextInt();
                    scanner.nextLine(); 
                    switch (opcaoTipo) {
                        case 1:
                            System.out.print("Digite o novo tipo de despesa: ");
                            String novoTipo = scanner.nextLine();
                            tiposDespesa.add(novoTipo);
                            GerenciadorArquivos.escreverEmArquivo("tipos_despesa.txt", novoTipo);
                            System.out.println("Tipo adicionado com sucesso!");
                            break;
                        case 2:
                            System.out.print("Digite o tipo a ser removido: ");
                            String tipoRemover = scanner.nextLine();
                            tiposDespesa.remove(tipoRemover);
                            
                            System.out.println("Tipo removido com sucesso!");
                            break;
                        case 3:
                            System.out.println("Tipos de Despesa:");
                            for (String tipo : tiposDespesa) {
                                System.out.println(tipo);
                            }
                            break;
                    }
                    break;
                case 6:
                    
                    System.out.println("Gerenciar Usuários:");
                    System.out.println("1. Cadastrar Usuário");
                    System.out.println("2. Listar Usuários");
                    int opcaoUsuario = scanner.nextInt();
                    scanner.nextLine(); 
                    switch (opcaoUsuario) {
                        case 1:
                            System.out.print("Nome do Usuário: ");
                            String nomeUsuario = scanner.nextLine();
                            System.out.print("Senha do Usuário: ");
                            String senhaUsuario = scanner.nextLine();
                            String senhaCriptografada = CriptografiaUtil.criptografarSenha(senhaUsuario);
                            usuarios.add(nomeUsuario + ":" + senhaCriptografada);
                            GerenciadorArquivos.escreverEmArquivo("usuarios.txt", nomeUsuario + ":" + senhaCriptografada);
                            System.out.println("Usuário cadastrado com sucesso!");
                            break;
                        case 2:
                            System.out.println("Usuários:");
                            for (String usuario : usuarios) {
                                System.out.println(usuario.split(":")[0]); 
                            }
                            break;
                    }
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 7);

        scanner.close();
    }
}