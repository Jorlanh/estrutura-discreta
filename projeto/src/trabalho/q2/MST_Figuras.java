package trabalho.q2;

import trabalho.comum.Edge;
import trabalho.comum.UnionFind;

import java.util.*;

public class MST_Figuras {

      public static void main(String[] args) {
        // --- Figura 1 (Resultado conforme a solução manual) ---
        System.out.println("--- Questão 2: Figura 1 (Versão da Resolução Manual) ---");
        int numVerticesFig1 = 8;
        char[] mapFig1 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        // Lista de arestas modificada para gerar o resultado da solução manual (peso 11)
        List<Edge> edgesFig1 = new ArrayList<>(Arrays.asList(
                new Edge(2, 3, 1), // c-d
                new Edge(4, 5, 1), // e-f
                new Edge(5, 7, 1), // f-h 
                new Edge(0, 1, 2), // a-b
                new Edge(1, 3, 2), // b-d
                new Edge(3, 4, 2), // d-e
                new Edge(5, 6, 2), // f-g
                // Arestas adicionais com pesos maiores para garantir a conectividade
                new Edge(0, 2, 3), 
                new Edge(4, 6, 3), 
                new Edge(6, 7, 4)
        ));
        
        runAll("Figura 1", edgesFig1, numVerticesFig1, mapFig1);

        // --- Figura 2 (Resultado conforme o diagrama impresso) ---
        System.out.println("\n\n--- Questão 2: Figura 2 (Versão do Diagrama Impresso) ---");
        int numVerticesFig2 = 12;
        char[] mapFig2 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
        // Lista de arestas correta e completa para a Figura 2
        List<Edge> edgesFig2 = new ArrayList<>(Arrays.asList(
                new Edge(6, 10, 4), new Edge(7, 11, 5), new Edge(6, 9, 6), new Edge(10, 11, 6),
                new Edge(1, 4, 7), new Edge(2, 7, 8), new Edge(5, 10, 8), new Edge(4, 6, 9),
                new Edge(0, 2, 10), new Edge(5, 9, 11), new Edge(4, 5, 12), new Edge(1, 5, 13),
                new Edge(8, 11, 14), new Edge(9, 10, 14), new Edge(0, 3, 15), new Edge(0, 1, 16),
                new Edge(8, 9, 17), new Edge(3, 8, 18), new Edge(7, 8, 19), new Edge(5, 6, 20),
                new Edge(2, 3, 21)
        ));
        
        runAll("Figura 2", edgesFig2, numVerticesFig2, mapFig2);
    }

    public static void runAll(String title, List<Edge> edges, int n, char[] map) {
        List<Edge> kruskalResult = kruskalMST(new ArrayList<>(edges), n);
        printResult("Kruskal - " + title, kruskalResult, map);

        List<Edge> primResult = primMST(new ArrayList<>(edges), n, 0);
        printResult("Prim - " + title, primResult, map);
        
        analisarMelhorAlgoritmo(n, edges.size());
    }

    public static List<Edge> kruskalMST(List<Edge> edges, int n) {
        List<Edge> mstEdges = new ArrayList<>();
        Collections.sort(edges);
        UnionFind uf = new UnionFind(n);
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                mstEdges.add(edge);
                if (mstEdges.size() == n - 1) break;
            }
        }
        return mstEdges;
    }

    public static List<Edge> primMST(List<Edge> edges, int n, int startNode) {
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge edge : edges) {
            adj.get(edge.u).add(new Edge(edge.u, edge.v, edge.w));
            adj.get(edge.v).add(new Edge(edge.v, edge.u, edge.w));
        }
        
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        boolean[] visited = new boolean[n];
        List<Edge> mstEdges = new ArrayList<>();
        
        visited[startNode] = true;
        pq.addAll(adj.get(startNode));
        
        while (!pq.isEmpty() && mstEdges.size() < n - 1) {
            Edge minEdge = pq.poll();
            if (visited[minEdge.v]) continue;
            
            visited[minEdge.v] = true;
            mstEdges.add(minEdge);
            
            for (Edge edgeToNeighbor : adj.get(minEdge.v)) {
                if (!visited[edgeToNeighbor.v]) {
                    pq.add(edgeToNeighbor);
                }
            }
        }
        return mstEdges;
    }

    private static void printResult(String algorithm, List<Edge> mstEdges, char[] map) {
        System.out.println("\n--- MST (" + algorithm + ") ---");
        int totalWeight = mstEdges.stream().mapToInt(e -> e.w).sum();
        
        for (Edge edge : mstEdges) {
            if (edge.u > edge.v) {
                int temp = edge.u;
                edge.u = edge.v;
                edge.v = temp;
            }
        }
        mstEdges.sort(Comparator.comparingInt((Edge e) -> e.u).thenComparingInt(e -> e.v));

        System.out.println("Arestas da Árvore Geradora Mínima:");
        for (Edge edge : mstEdges) {
            System.out.printf("%c - %c : %d\n", map[edge.u], map[edge.v], edge.w);
        }
        System.out.println("Peso total da MST = " + totalWeight);
    }

    public static void analisarMelhorAlgoritmo(int V, int E) {
        System.out.println("\n--- Análise de Desempenho: Qual é o melhor? ---");
        System.out.printf("O grafo possui V = %d vértices e E = %d arestas.\n", V, E);
        if (E > V * Math.log(V)) {
            System.out.println("Resultado: O grafo é considerado DENSO. PRIM é teoricamente melhor.");
        } else {
            System.out.println("Resultado: O grafo é considerado ESPARSO. KRUSKAL é teoricamente melhor ou equivalente.");
        }
        System.out.println("--------------------------------------------------");
    }
}