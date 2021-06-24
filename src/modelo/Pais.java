package modelo;

public class Pais implements Imprimivel, Comparable<Pais> {
    private String codigo;
    private String nome;

    public Pais(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }

    // Implementação da interface Imprimivel
    // Neste caso, basta chamar toString
    //@Override
    public void imprimir() {
        System.out.println(toString());
    }

    // Define o critério de comparação entre duas
    // aeronaves (usado em Collections.sort(), por exemplo
    @Override
    public int compareTo(Pais outra) {
        return nome.compareTo(outra.nome);
    }
}
