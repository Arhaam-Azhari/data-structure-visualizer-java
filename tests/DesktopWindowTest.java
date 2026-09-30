import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DesktopWindowTest {
    static DataStructureVisualizer window;
    static JTabbedPane tabs;
    static int checks;

    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }

    static void click(Container parent, String label) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JButton && ((JButton) child).getText().equals(label)) {
                ((JButton) child).doClick();
                return;
            }
            if (child instanceof Container && hasButton((Container) child, label)) {
                click((Container) child, label);
                return;
            }
        }
        throw new AssertionError("Button not found: " + label);
    }

    static boolean hasButton(Container parent, String label) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JButton && ((JButton) child).getText().equals(label)) return true;
            if (child instanceof Container && hasButton((Container) child, label)) return true;
        }
        return false;
    }

    static JPanel tab(int index) {
        tabs.setSelectedIndex(index);
        window.validate();
        return (JPanel) tabs.getSelectedComponent();
    }

    static JScrollPane scroll(JPanel panel) {
        return (JScrollPane) ((BorderLayout) panel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
    }

    static void screenshot(String name) throws Exception {
        Rectangle[] bounds = new Rectangle[1];
        SwingUtilities.invokeAndWait(() -> bounds[0] = window.getBounds());
        Robot robot = new Robot();
        robot.waitForIdle();
        BufferedImage image = robot.createScreenCapture(bounds[0]);
        ImageIO.write(image, "png", new File("desktop-screenshots/" + name + ".png"));
    }

    public static void main(String[] args) throws Exception {
        if (GraphicsEnvironment.isHeadless()) throw new AssertionError("Desktop display required");
        new File("desktop-screenshots").mkdirs();
        try {
            SwingUtilities.invokeAndWait(() -> {
                window = new DataStructureVisualizer();
                window.setVisible(true);
                tabs = (JTabbedPane) window.getContentPane().getComponent(0);
                check(window.isShowing(), "Window launches");
                check(tabs.getTabCount() == 4, "Four tabs available");
                JPanel panel = tab(0);
                var stack = (DataStructureVisualizer.StackPanel) scroll(panel).getViewport().getView();
                stack.input.setText("10"); click(panel, "Push");
                stack.input.setText("20"); click(panel, "Push");
                click(panel, "Peek");
                check(stack.status.getText().equals("Top: 20"), "Stack Peek button");
                click(panel, "Pop");
                check(stack.status.getText().equals("Popped 20"), "Stack Pop button");
                stack.input.setText("abc"); click(panel, "Push");
                check(stack.status.getText().equals("Invalid input"), "Invalid input feedback");
                stack.input.setText("20"); click(panel, "Push");
            });
            screenshot("stack");

            SwingUtilities.invokeAndWait(() -> {
                JPanel panel = tab(1);
                var queue = (DataStructureVisualizer.QueuePanel) scroll(panel).getViewport().getView();
                queue.input.setText("10"); click(panel, "Enqueue");
                queue.input.setText("20"); click(panel, "Enqueue");
                click(panel, "Peek");
                check(queue.status.getText().equals("Front: 10"), "Queue Peek button");
                click(panel, "Dequeue");
                check(queue.status.getText().equals("Dequeued 10"), "Queue Dequeue button");
                queue.input.setText("30"); click(panel, "Enqueue");
            });
            screenshot("queue");

            SwingUtilities.invokeAndWait(() -> {
                JPanel panel = tab(2);
                var list = (DataStructureVisualizer.LinkedListPanel) scroll(panel).getViewport().getView();
                for (int value : new int[]{10, 20, 30}) {
                    list.input.setText("" + value); click(panel, "Insert");
                }
                list.input.setText("20"); click(panel, "Search");
                check(list.status.getText().equals("Found 20"), "List Search button");
                click(panel, "Delete");
                check(list.size == 2 && list.head.next.value == 30, "List Delete button reconnects nodes");
                list.input.setText("99"); click(panel, "Delete");
                check(list.status.getText().equals("Not found: 99"), "Missing value feedback in window");
            });
            screenshot("linked-list");

            SwingUtilities.invokeAndWait(() -> {
                JPanel panel = tab(3);
                var tree = (DataStructureVisualizer.BSTPanel) scroll(panel).getViewport().getView();
                for (int value : new int[]{20, 10, 30, 5, 15}) {
                    tree.input.setText("" + value); click(panel, "Insert");
                }
                tree.input.setText("15"); click(panel, "Search");
                check(tree.status.getText().equals("Found 15"), "Tree Search button");
                tree.input.setText("20"); click(panel, "Insert");
                check(tree.status.getText().equals("Already in tree: 20"), "Duplicate feedback in window");
            });
            screenshot("tree");

            SwingUtilities.invokeAndWait(() -> {
                JPanel panel = tab(0);
                var stack = (DataStructureVisualizer.StackPanel) scroll(panel).getViewport().getView();
                for (int i = 0; i < 20; i++) { stack.input.setText("" + i); click(panel, "Push"); }
                window.setSize(700, 500);
                window.validate();
            });
            new Robot().waitForIdle();
            SwingUtilities.invokeAndWait(() -> {
                JPanel panel = tab(0);
                JScrollPane pane = scroll(panel);
                pane.validate();
                check(pane.getVerticalScrollBar().isVisible(), "Large stack scrolls vertically");
                pane.getVerticalScrollBar().setValue(300);
                check(pane.getViewport().getViewPosition().y > 0, "Vertical scrolling moves drawing");
                Component controls = ((BorderLayout) panel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
                check(controls.isShowing() && controls.getY() == 0, "Controls remain visible while scrolling");
                click(panel, "Reset");
                var stack = (DataStructureVisualizer.StackPanel) pane.getViewport().getView();
                check(stack.stack.isEmpty(), "Stack Reset button");
                click(panel, "Pop");
                check(stack.status.getText().equals("Stack empty"), "Empty stack feedback in window");

                panel = tab(1);
                var queue = (DataStructureVisualizer.QueuePanel) scroll(panel).getViewport().getView();
                for (int i = 0; i < 15; i++) { queue.input.setText("" + i); click(panel, "Enqueue"); }
                window.validate();
            });
            new Robot().waitForIdle();
            SwingUtilities.invokeAndWait(() -> {
                JPanel panel = tab(1);
                JScrollPane pane = scroll(panel);
                pane.validate();
                check(pane.getHorizontalScrollBar().isVisible(), "Large queue scrolls horizontally");
                pane.getHorizontalScrollBar().setValue(400);
                check(pane.getViewport().getViewPosition().x > 0, "Horizontal scrolling moves drawing");
                click(panel, "Reset");
                var queue = (DataStructureVisualizer.QueuePanel) pane.getViewport().getView();
                check(queue.queue.isEmpty(), "Queue Reset button");
                panel = tab(2);
                click(panel, "Reset");
                var list = (DataStructureVisualizer.LinkedListPanel) scroll(panel).getViewport().getView();
                check(list.head == null && list.tail == null, "List Reset button");
                panel = tab(3);
                click(panel, "Reset");
                var tree = (DataStructureVisualizer.BSTPanel) scroll(panel).getViewport().getView();
                check(tree.root == null, "Tree Reset button");
                window.setSize(900, 650);
                window.validate();
                check(window.getWidth() == 900 && tabs.isShowing(), "Window resizes and tabs remain visible");
                check(window.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE, "Normal launch exits on close");
                // Avoid ending the test process while checking the close event.
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
                check(!window.isDisplayable(), "Close event disposes window");
            });
            Process closeProbe = new ProcessBuilder(
                    System.getProperty("java.home") + File.separator + "bin" + File.separator + "java",
                    "-cp", System.getProperty("java.class.path"), "CloseWindowProbe")
                    .inheritIO().start();
            boolean closed = closeProbe.waitFor(15, java.util.concurrent.TimeUnit.SECONDS);
            if (!closed) closeProbe.destroyForcibly();
            check(closed && closeProbe.exitValue() == 0, "Closing the app exits its process");
            System.out.println("Passed " + checks + " desktop-window checks.");
        } finally {
            SwingUtilities.invokeAndWait(() -> { if (window != null) window.dispose(); });
        }
    }
}

class CloseWindowProbe {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStructureVisualizer frame = new DataStructureVisualizer();
            frame.setVisible(true);
            Timer close = new Timer(500, e -> frame.dispatchEvent(
                    new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
            close.setRepeats(false);
            close.start();
        });
    }
}
