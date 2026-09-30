import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VisualizerTest {
    static int checks;

    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }

    static void draw(JPanel panel) {
        Dimension size = panel.getPreferredSize();
        panel.setSize(size);
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        panel.paint(g);
        g.dispose();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var stack = new DataStructureVisualizer.StackPanel();
            stack.pop();
            check(stack.status.getText().equals("Stack empty"), "Empty stack");
            stack.input.setText(" 10 "); stack.push();
            stack.input.setText("20"); stack.push();
            stack.peek();
            check(stack.status.getText().equals("Top: 20"), "Stack peek");
            stack.pop();
            check(stack.stack.peek() == 10, "Stack removes newest value");
            stack.input.setText("abc"); stack.push();
            check(stack.stack.size() == 1, "Invalid input leaves stack unchanged");
            for (int i = 0; i < 30; i++) { stack.input.setText("" + i); stack.push(); }
            check(stack.getPreferredSize().height > 600, "Stack grows vertically");
            draw(stack);
            stack.reset();
            check(stack.stack.isEmpty(), "Stack reset");

            var queue = new DataStructureVisualizer.QueuePanel();
            queue.dequeue();
            check(queue.status.getText().equals("Queue empty"), "Empty queue");
            queue.input.setText("10"); queue.enqueue();
            queue.input.setText("20"); queue.enqueue();
            queue.dequeue();
            check(queue.queue.peek() == 20, "Queue removes oldest value");
            for (int i = 0; i < 20; i++) { queue.input.setText("" + i); queue.enqueue(); }
            check(queue.getPreferredSize().width > 950, "Queue grows horizontally");
            draw(queue);
            queue.reset();
            check(queue.queue.isEmpty(), "Queue reset");

            var list = new DataStructureVisualizer.LinkedListPanel();
            list.input.setText("99"); list.delete();
            check(list.status.getText().equals("Not found: 99"), "Missing list value");
            for (int value : new int[]{10, 20, 20, 30}) {
                list.input.setText("" + value); list.insert();
            }
            list.input.setText("20"); list.delete();
            check(list.size == 3 && list.head.next.value == 20, "Delete first duplicate only");
            list.input.setText("10"); list.delete();
            check(list.head.value == 20, "Delete head");
            list.input.setText("30"); list.delete();
            check(list.tail == list.head && list.tail.next == null, "Delete tail");
            list.input.setText("20"); list.delete();
            check(list.head == null && list.tail == null && list.size == 0, "Delete last node");
            list.input.setText("40"); list.insert(); list.search();
            check(list.status.getText().equals("Found 40"), "Insert after emptying list");
            list.input.setText("2147483648"); list.insert();
            check(list.size == 1, "Out-of-range input leaves list unchanged");
            for (int i = 0; i < 15; i++) { list.input.setText("" + i); list.insert(); }
            check(list.getPreferredSize().width > 950, "List grows horizontally");
            draw(list);
            list.reset();
            check(list.head == null && list.tail == null && list.size == 0, "List reset");

            var tree = new DataStructureVisualizer.BSTPanel();
            for (int value : new int[]{20, 10, 30, 15}) {
                tree.input.setText("" + value); tree.insert();
            }
            tree.input.setText("20"); tree.insert();
            check(tree.nodeCount == 4 && tree.status.getText().equals("Already in tree: 20"), "Duplicate tree value");
            tree.input.setText("15"); tree.search();
            check(tree.status.getText().equals("Found 15"), "Tree finds nested value");
            tree.input.setText("99"); tree.search();
            check(tree.status.getText().equals("Not found"), "Missing tree value");
            tree.reset();
            for (int i = 0; i < 50; i++) { tree.input.setText("" + i); tree.insert(); }
            check(tree.getPreferredSize().width > 950 && tree.getPreferredSize().height > 600, "Unbalanced tree grows both ways");
            check(tree.root.right.x > tree.root.x && tree.root.right.y > tree.root.y, "Tree layout preserves hierarchy");
            draw(tree);
            tree.reset();
            check(tree.root == null && tree.nodeCount == 0, "Tree reset");

            JPanel tab = DataStructureVisualizer.createTab(new DataStructureVisualizer.StackPanel());
            JScrollPane scroll = (JScrollPane) ((BorderLayout) tab.getLayout()).getLayoutComponent(BorderLayout.CENTER);
            check(scroll.getViewport().getView() instanceof DataStructureVisualizer.StackPanel, "Drawing is scrollable");
            check(((JPanel) scroll.getViewport().getView()).getComponentCount() == 0, "Controls stay outside scroll area");
            System.out.println("Passed " + checks + " checks.");
        });
    }
}
