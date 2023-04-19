package idealist.dir_explorer.lib;

import java.util.*;

public class FileSystem_CMD<T> {
    private class Node {
        T data;
        String type;
        String name;
        String path;
        Node parentNode;
        Vector<Node> children;
    }

    private final Node root;

    private void addChild(Node parentNode, Node child, String path) {

        if (Objects.equals(parentNode.path, path)) {
            if (locateNode(this.root, (path + child.name)) != null) {
                System.out.println("\n[INFO] " + child.type + ": " + child.name + " in path" +
                        path + " already exists.");
                return;
            }
            if (Objects.equals(parentNode.type, "file")) {
                System.out.println("Cannot append a file to another file!");
                return;
            }

            child.parentNode = parentNode;
            child.path = path + child.name;
            parentNode.children.add(child);

            System.out.println("Insertion of " + child.type + ": " + child.name + " done.");
            return;
        }

        for (Node x: parentNode.children) {
            addChild(x, child, path);
        }
    }

    private void print_CMD_mode(Node node) {
        System.out.println("\nName: " + node.name);
        System.out.println("Type: " + node.type);
        System.out.println("Path: " + node.path);

        if (Objects.equals(node.type, "file"))
            System.out.println("Value: " + node.data);

        if (node.parentNode != null)
            System.out.println("Parent: " + node.parentNode.path);

        for (Node x: node.children)
            print_CMD_mode((x));
    }

    private void printFileTree
        (
            Node node,
            Node currentParent,
            int tabCount,
            String selectedPath
        )
    {
        Node nodePtr = currentParent;

        if (node == this.root)
            System.out.println("\33[2C" + node.name + " (root)");
        else {
            System.out.print("\r");

            while (nodePtr != null) {
                if (nodePtr == node.parentNode) {
                    tabCount--;
                    break;
                }

                nodePtr = nodePtr.parentNode;
            }

            for (int i = 0; i < tabCount; i++) {
                System.out.print("\33[8C");
            }

            System.out.print("\33[1B|\33[1D\33[1B|");
            System.out.print("--> ");

            System.out.print(node.name);

            if (Objects.equals(node.path, selectedPath))
                System.out.print(" * ");

//            System.out.print("\n");
            System.out.println();
        }

        tabCount++;

        if (node.children.size() == 0)
            return;

        if (Objects.equals(node.type, "file"))
            currentParent = node.parentNode;

        for (Node childNode: node.children)
            printFileTree(childNode, currentParent, tabCount, selectedPath);
    }

    private void editNodeName(String path, String newName) {
        Node searchNode;
        Node foundNode = locateNode(this.root, path);

        if (foundNode == null) {
            System.out.println("\n[INFO] File not found.");
            return;
        }

        String fileToSearch = foundNode.parentNode.path + newName;
        searchNode = locateNode(this.root, fileToSearch);

        if (searchNode != null) {
            System.out.println("\n[INFO] File with name: " + newName +
                    " already exists in specified directory.");
            return;
        }

        foundNode.name = newName;
        foundNode.path = foundNode.parentNode.path + newName;

        System.out.println("\n[INFO] File renamed.");
    }

    private void deleteNode(Vector<Node> children, int index) {
        children.remove(index);
    }

    private void verifyDeletion(Vector<Node> children, int index) {
        int choice = 0;
        char userInput;

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n[INFO] Directory is not empty.");

        System.out.print("Press [Y] to override: ");
        userInput = scanner.next().charAt(0);

        if (userInput == 'Y' || userInput == 'y') {
            System.out.println("\n[INFO] File deletion aborted.");
            return;
        }

        deleteNode(children, index);
        System.out.println("\n[INFO] Folder deleted");
    }

    private Node locateNode(Node node, String path) {
        if (Objects.equals(node.path, path)) {
            return node;
        }

        for (Node x: node.children)
            return locateNode(x, path);

        return null;
    }

    public FileSystem_CMD(char driveLetter) {
        this.root = new Node();

        this.root.type = "dir";
        this.root.name = driveLetter + ":\\";
        this.root.path = this.root.name;
        this.root.parentNode = null;
        this.root.children = new Vector<>();
    }

    public void add(T data, String type, String name, String parentName) {
        Node newNode = new Node();

        newNode.type = type;
        newNode.data = data;
        newNode.name = name;
        newNode.children = new Vector<>();

        addChild(this.root, newNode, parentName);
    }

    public void print(int mode, String selectedPath) {
        switch (mode) {
            case 0 -> {
                System.out.println("\n[PRINTING CMD MODE]\n");
                print_CMD_mode(this.root);
            }

            case 1 -> {
                System.out.println("\n[PRINTING FILE TREE]");
                printFileTree(this.root, null, 0, selectedPath);
            }
        }
    }

    public void rename(String path, String newName) {
        editNodeName(path, newName);
    }

    public void delete(String path) {
        Node nodeToDelete = locateNode(this.root, path);

        if (nodeToDelete == null) {
            System.out.println("\n[INFO] File/Dir does not exist.");
            return;
        }

        Vector<Node> children = nodeToDelete.parentNode.children;

        for (int i = 0; i < children.size(); i++) {
            if (!Objects.equals(children.elementAt(i).path, path))
                continue;

            if (Objects.equals(children.elementAt(i).type, "dir")) {
                if (children.elementAt(i).children.size() != 0) {
                    verifyDeletion(children, i);
                }
                else {
                    deleteNode(children, i);
                }
                break;
            }
        }
    }

    public boolean verifyPath(String path) {
        Node foundNode = locateNode(this.root, path);

        return foundNode != null;
    }
}
