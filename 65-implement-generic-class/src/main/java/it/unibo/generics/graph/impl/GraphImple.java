package it.unibo.generics.graph.impl;

import it.unibo.generics.graph.api.Graph;
import java.util.*;



public class GraphImple<N> implements Graph<N> {


    private Map<N, Set<N>> graph = new HashMap<>();



    /**
     * Adds a node: nothing happens if node is null or already there.
     * 
     * @param node
     *            the node to add
     */
    @Override
    public void addNode(N node){

        if(node != null && !this.graph.containsKey(node)){
            this.graph.put(node, new HashSet<N>());
        }
            
    }

    /**
     * Adds an edge: nothing happens if source or target are null.
     * 
     * @param source
     *            starting node
     * @param target
     *            ending node
     */
    @Override
    public void addEdge(N source, N target){
        if(source == null || target == null) return;

        this.graph.putIfAbsent(source, new HashSet<N>());
        this.graph.putIfAbsent(target, new HashSet<N>());

        this.graph.get(source).add(target);

    }

    /**
     * @return all the nodes
     */
    @Override
    public Set<N> nodeSet(){

        return this.graph.keySet();
    }

    /**
     * Returns all the nodes directly targeted from a node.
     * 
     * @param node
     *            the node
     * @return all the nodes directly targeted from the passed node
     */
    @Override
    public Set<N> linkedNodes(N node){

        return this.graph.getOrDefault(node, Collections.emptySet());

    }

    /**
     * Gets one sequence of nodes connecting source to target.
     * 
     * @param source
     *            the source node
     * @param target
     *            the target node
     * @return a sequence of nodes connecting sources and target
     */
    @Override
    public List<N> getPath(N source, N target){
        BFS bfs = new BFS();
        return bfs.findPath(source, target);
    }

    /**
 * Private nested BFS class for finding paths in the graph.
 */
private class BFS {

    /**
     * Finds a path from source to target using BFS.
     *
     * @param source The starting node.
     * @param target The ending node.
     * @return A list representing the path from source to target, or an empty list if no path exists.
     */
    public List<N> findPath(N source, N target) {
        if (source == null || target == null || !graph.containsKey(source) || !graph.containsKey(target)) {
            return Collections.emptyList(); // Return empty list if invalid input
        }

        // BFS Data Structures
        Queue<N> queue = new LinkedList<>();
        Set<N> visited = new HashSet<>();
        Map<N, N> predecessors = new HashMap<>();

        // Initialize BFS
        queue.add(source);
        visited.add(source);

        // Perform BFS
        while (!queue.isEmpty()) {
            N current = queue.poll();

            // If we reach the target, reconstruct the path
            if (current.equals(target)) {
                return reconstructPath(source, target, predecessors);
            }

            // Explore neighbors
            for (N neighbor : graph.getOrDefault(current, Collections.emptySet())) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    predecessors.put(neighbor, current); // Record the path
                }
            }
        }

        // If we exhaust BFS without finding the target, return an empty list
        return Collections.emptyList();
    }

    /**
     * Reconstructs the path from source to target using the predecessor map.
     *
     * @param source The starting node.
     * @param target The ending node.
     * @param predecessors The map of predecessors.
     * @return A list representing the path from source to target.
     */
    private List<N> reconstructPath(N source, N target, Map<N, N> predecessors) {
        List<N> path = new LinkedList<>();
        N current = target;

        // Backtrack from target to source
        while (current != null) {
            path.add(0, current); // Add to the front of the list
            current = predecessors.get(current);
        }

        // Check if the path starts with the source
        if (!path.isEmpty() && path.get(0).equals(source)) {
            return path;
        }

        return Collections.emptyList(); // If no valid path exists
    }
}


        

    }
