package trabalho.q1;

import trabalho.comum.Edge;
import trabalho.comum.UnionFind;

import java.util.*;
import java.util.stream.Collectors;

public class MST_Escocia {

    public static void main(String[] args) {
        System.out.println("--- Questão 1: Grafo da Escócia (6 Cidades) ---\n");
        // Mapeia os nomes das cidades para índices de 0 a 5
        List<String> cities = Arrays.asList("Aberdeen", "Edinburgh", "Fort William", "Glasgow", "Inverness", "Perth");
        
        // Matriz de adjacência com as distâncias
        int[][] distances = {
            {0, 120, 147, 142, 104, 81},
            {120, 0, 132, 42, 157, 45},
            {147, 132, 0, 102, 66, 105},
            {142, 42, 102, 0, 168, 61},
            {104, 157, 66, 168, 0, 112},
            {81, 45, 105, 61, 112, 0}
        };

        // (a) Encontrar a Árvore Geradora Mínima
        System.out.println("a) Árvore Geradora Mínima (MST)");
        List<Edge> mst = findMst(6, distances);
        int mstWeight = mst.stream().mapToInt(e -> e.w).sum();
        
        // Imprime as arestas da MST
        for (Edge edge : mst) {
            System.out.printf("  %-15s - %-15s : %d\n", cities.get(edge.u), cities.get(edge.v), edge.w);
        }
        System.out.println("Custo Total da MST = " + mstWeight + " milhas.\n");

        // (b) Encontrar Limites Inferiores para o TSP
        System.out.println("b) Limites Inferiores para o Problema do Caixeiro Viajante (TSP)");
        int lowerBoundGlasgow = calculateTspLowerBound("Glasgow", cities, distances);
        System.out.println("  Limite inferior removendo Glasgow: " + lowerBoundGlasgow);
        
        int lowerBoundAberdeen = calculateTspLowerBound("Aberdeen", cities, distances);
        System.out.println("  Limite inferior removendo Aberdeen: " + lowerBoundAberdeen + "\n");
        
        // (c) Qual é a solução correta?
        System.out.println("c) Qual é a solução correta?");
        System.out.println("  A melhor (maior) estimativa de limite inferior para o TSP é: " + Math.max(lowerBoundGlasgow, lowerBoundAberdeen));
    }

    /**
     * Encontra a MST de um grafo representado por uma matriz de adjacência usando o algoritmo de Kruskal.
     */
    public static List<Edge> findMst(int numVertices, int[][] adjMatrix) {
        List<Edge> allEdges = new ArrayList<>();
        // Converte a matriz de adjacência para uma lista de arestas
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (adjMatrix[i][j] != 0) {
                    allEdges.add(new Edge(i, j, adjMatrix[i][j]));
                }
            }
        }
        // Ordena as arestas por peso
        Collections.sort(allEdges);

        List<Edge> mstEdges = new ArrayList<>();
        UnionFind uf = new UnionFind(numVertices);
        for (Edge edge : allEdges) {
            if (uf.union(edge.u, edge.v)) {
                mstEdges.add(edge);
                // Otimização: para a execução quando a MST estiver completa
                if (mstEdges.size() == numVertices - 1) break;
            }
        }
        return mstEdges;
    }

    /**
     * Calcula o limite inferior para o TSP removendo uma cidade específica.
     */
    public static int calculateTspLowerBound(String cityToRemove, List<String> cities, int[][] originalDistances) {
        int cityIndexToRemove = cities.indexOf(cityToRemove);
        int numVertices = cities.size();
        
        // 1. Cria um subgrafo sem a cidade removida
        int[][] subMatrix = new int[numVertices - 1][numVertices - 1];
        for (int i = 0, newI = 0; i < numVertices; i++) {
            if (i == cityIndexToRemove) continue;
            for (int j = 0, newJ = 0; j < numVertices; j++) {
                if (j == cityIndexToRemove) continue;
                subMatrix[newI][newJ] = originalDistances[i][j];
                newJ++;
            }
            newI++;
        }

        // 2. Calcula o custo da MST do subgrafo
        List<Edge> subMst = findMst(numVertices - 1, subMatrix);
        int subMstWeight = subMst.stream().mapToInt(e -> e.w).sum();

        // 3. Acha as duas arestas mais baratas para reconectar a cidade removida
        List<Integer> costs = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (i != cityIndexToRemove) {
                costs.add(originalDistances[cityIndexToRemove][i]);
            }
        }
        Collections.sort(costs);
        int twoShortestEdges = costs.get(0) + costs.get(1);

        // 4. O limite inferior é a soma do custo da MST do subgrafo e das duas arestas
        return subMstWeight + twoShortestEdges;
    }
}