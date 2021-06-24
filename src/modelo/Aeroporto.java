package modelo;

public class Aeroporto implements Comparable<Aeroporto> {
    private String codigo;
    private String nome;
    private Geo loc;
    private Pais pais;

    public Aeroporto(String codigo, String nome, Geo loc) {
        this.codigo = codigo;
        this.nome = nome;
        this.loc = loc;
    }

    public Aeroporto(String codigo, String nome, Geo loc, Pais p) {
        this.codigo = codigo;
        this.nome = nome;
        this.loc = loc;
        this.pais = p;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public Geo getLocal() {
        return loc;
    }

    public Pais getPaisId() {
        return pais;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome + " [" + loc + "]" + pais.getCodigo();
    }

    @Override
    public int compareTo(Aeroporto outro) {
        return this.nome.compareTo(outro.nome);
    }
}