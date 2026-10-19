package com.csc161j.Exam2;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

public class FileTree implements Iterable<FileNode> {

    private FileNode root;

    public FileTree(String path) {
        root = new FileNode(path);
        buildTree(root);
    }

    /**
     * Return a depth first post-order traversal iterator
     */
    @Override
    public Iterator<FileNode> iterator() {
        return new DepthFirstIterator();
    }

    /**
     * Use recursion to build the tree from the directory structure.
     */
    private void buildTree(FileNode fileNode) {

        File[] files = fileNode.getFile().listFiles();

        // A regular file has no children.
        if (files == null) {
            return;
        }

        for (File file : files) {

            // Create a node for the file/folder.
            FileNode childNode = new FileNode(file);

            // Add it as a child of the current node.
            fileNode.getChildNodes().add(childNode);

            // If it is a directory, recursively build its children.
            if (file.isDirectory()) {
                buildTree(childNode);
            }
        }
    }

    /**
     * Iterator that performs a depth-first post-order traversal
     * using two stacks.
     */
    private class DepthFirstIterator implements Iterator<FileNode> {

        private Deque<FileNode> postOrderStack;

        public DepthFirstIterator() {

            Deque<FileNode> firstStack = new ArrayDeque<>();
            postOrderStack = new ArrayDeque<>();

            firstStack.push(root);

            while (!firstStack.isEmpty()) {

                FileNode current = firstStack.pop();

                // Put current node into second stack.
                postOrderStack.push(current);

                // Put all children into first stack.
                for (FileNode child : current.getChildNodes()) {
                    firstStack.push(child);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !postOrderStack.isEmpty();
        }

        @Override
        public FileNode next() {
            return postOrderStack.pop();
        }
    }

    /**
     * Returns an iterator that does a breadth-first traversal.
     */
    public Iterator<FileNode> breadthFirstIterator() {
        return new BreadthFirstIterator();
    }

    /**
     * Iterator that performs a breadth-first traversal using a queue.
     */
    private class BreadthFirstIterator implements Iterator<FileNode> {

        private Queue<FileNode> queue;

        public BreadthFirstIterator() {

            queue = new ArrayDeque<>();

            // Start at the root.
            queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public FileNode next() {

            FileNode current = queue.remove();

            // Add the children so they are visited after
            // the nodes already in the queue.
            queue.addAll(current.getChildNodes());

            return current;
        }
    }
}