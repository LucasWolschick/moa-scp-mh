package lucaswolschick.moa_scp_mh.resolvedor;

public class ResolvedorConfiguracao {
    private final int tamanhoPopulacao;
    private final int maxGeracoes;
    private final Gerador gerador;
    private final Operadores.Avaliacao avaliador;
    private final Operadores.Selecao selecionador;
    private final Operadores.Cruzamento cruzador;
    private final Operadores.Mutacao mutador;
    private final Operadores.BuscaLocal buscaLocal;
    private final Operadores.Atualizacao atualizador;

    public ResolvedorConfiguracao(
            int tamanhoPopulacao,
            int maxGeracoes,
            Gerador gerador,
            Operadores.Avaliacao avaliador,
            Operadores.Selecao selecionador,
            Operadores.Cruzamento cruzador,
            Operadores.Mutacao mutador,
            Operadores.BuscaLocal buscaLocal,
            Operadores.Atualizacao atualizador) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.maxGeracoes = maxGeracoes;
        this.gerador = gerador;
        this.avaliador = avaliador;
        this.selecionador = selecionador;
        this.cruzador = cruzador;
        this.mutador = mutador;
        this.buscaLocal = buscaLocal;
        this.atualizador = atualizador;
    }

    public int tamanhoPopulacao() {
        return tamanhoPopulacao;
    }

    public int maxGeracoes() {
        return maxGeracoes;
    }

    public Gerador gerador() {
        return gerador;
    }

    public Operadores.Avaliacao avaliador() {
        return avaliador;
    }

    public Operadores.Selecao selecionador() {
        return selecionador;
    }

    public Operadores.Cruzamento cruzador() {
        return cruzador;
    }

    public Operadores.Mutacao mutador() {
        return mutador;
    }

    public Operadores.BuscaLocal buscaLocal() {
        return buscaLocal;
    }

    public Operadores.Atualizacao atualizador() {
        return atualizador;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tamanhoPopulacao;
        result = prime * result + maxGeracoes;
        result = prime * result + ((gerador == null) ? 0 : gerador.hashCode());
        result = prime * result + ((avaliador == null) ? 0 : avaliador.hashCode());
        result = prime * result + ((selecionador == null) ? 0 : selecionador.hashCode());
        result = prime * result + ((cruzador == null) ? 0 : cruzador.hashCode());
        result = prime * result + ((mutador == null) ? 0 : mutador.hashCode());
        result = prime * result + ((buscaLocal == null) ? 0 : buscaLocal.hashCode());
        result = prime * result + ((atualizador == null) ? 0 : atualizador.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResolvedorConfiguracao other = (ResolvedorConfiguracao) obj;
        if (tamanhoPopulacao != other.tamanhoPopulacao)
            return false;
        if (maxGeracoes != other.maxGeracoes)
            return false;
        if (gerador == null) {
            if (other.gerador != null)
                return false;
        } else if (!gerador.equals(other.gerador))
            return false;
        if (avaliador == null) {
            if (other.avaliador != null)
                return false;
        } else if (!avaliador.equals(other.avaliador))
            return false;
        if (selecionador == null) {
            if (other.selecionador != null)
                return false;
        } else if (!selecionador.equals(other.selecionador))
            return false;
        if (cruzador == null) {
            if (other.cruzador != null)
                return false;
        } else if (!cruzador.equals(other.cruzador))
            return false;
        if (mutador == null) {
            if (other.mutador != null)
                return false;
        } else if (!mutador.equals(other.mutador))
            return false;
        if (buscaLocal == null) {
            if (other.buscaLocal != null)
                return false;
        } else if (!buscaLocal.equals(other.buscaLocal))
            return false;
        if (atualizador == null) {
            if (other.atualizador != null)
                return false;
        } else if (!atualizador.equals(other.atualizador))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ResolvedorConfiguracao [tamanhoPopulacao=" + tamanhoPopulacao + ", maxGeracoes=" + maxGeracoes
                + ", gerador=" + gerador + ", avaliador=" + avaliador + ", selecionador=" + selecionador + ", cruzador="
                + cruzador + ", mutador=" + mutador + ", buscaLocal=" + buscaLocal + ", atualizador=" + atualizador
                + "]";
    }

}
