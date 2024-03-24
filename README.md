# moa-scp-mh

Sistema de geração de soluções para instâncias do problema de cobertura de conjuntos,
utilizando a meta-heurística algoritmos genéticos.

Lucas Wolschick, 2024.

Desenvolvido para a terceira avaliação de Modelagem e Otimização Algorítmica, do curso de
Bacharelado em Ciência da Computação da Universidade Estadual de Maringá.

## Execução

Esta é uma aplicação Java. Para executá-la, certifique-se que possui no mínimo um JRE da
versão 11.0.22.

Abra um terminal e execute o comando na pasta raiz da aplicação para realizar um
teste simples com as configurações padrões de execução (pop=100, ger=10):

```bash
$ java -jar moa_scp_mh.jar input/Teste_01.dat
```

Todas as opções que podem ser passadas para a aplicação estão dispostas a seguir e podem
ser vistas passando-se o argumento de linha de comando `-h` no momento da invocação.

```h
usage: MOA-SCP-MH [-h] [--semente SEMENTE] [--tamPop TAMPOP]
                  [--maxGer MAXGER] [--gerador {aleatorio}]
                  [--avaliador {custo}]
                  [--selecionador {torneio,classificacao}]
                  [--cruzador {removerRedundancias}]
                  [--mutador {trocaColunasAleatorio,trocaUmaColunaAleatorio}]
                  [--buscaLocal {bestImprovement,firstImprovement}]
                  [--atualizador {elitista}] entrada

Obtém uma solução aproximada  para  o  problema  de  cobertura de conjuntos
usando Algoritmos Genéticos.

positional arguments:
  entrada                O problema a ser resolvido.

named arguments:
  -h, --help             show this help message and exit

  --semente SEMENTE      A  semente  a  ser  utilizada  pelo  solucionador.
                         (default: momento de execução)

  --tamPop TAMPOP, -p TAMPOP
                         O tamanho da população. (default: 100)

  --maxGer MAXGER, -g MAXGER
                         O número máximo de gerações. (default: 10)

  --gerador {aleatorio}  O gerador de soluções iniciais. (default: aleatorio)

  --avaliador {custo}    O avaliador de soluções. (default: custo)

  --selecionador {torneio,classificacao}
                         O selecionador de soluções. (default: torneio)

  --cruzador {removerRedundancias}
                         O cruzador de soluções. (default: removerRedundancias)

  --mutador {trocaColunasAleatorio,trocaUmaColunaAleatorio}
                         O mutador de soluções. (default: trocaColunasAleatorio)

  --buscaLocal {bestImprovement,firstImprovement}
                         A busca local a ser utilizada.

  --atualizador {elitista}
                         O atualizador de população. (default: elitista)
```

## Testes

A classe `Tester.py` realiza uma bateria de 196 testes quando executada. As saídas de
cada teste são geradas em um subdiretório `output/`. Este repositório contém saídas das
maiorias dos casos de teste.

A planilha `data.ods` contém uma análise dos casos de teste executados.

## Relatório

O relatório do trabalho se encontra no arquivo `Relatório.pdf`, disponível na pasta raiz.
