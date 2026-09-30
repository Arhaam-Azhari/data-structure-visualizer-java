import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DataStructureVisualizer extends JFrame {

    public DataStructureVisualizer() {
        setTitle("Interactive Data Structure Visualizer");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Stack", createTab(new StackPanel()));
        tabs.add("Queue", createTab(new QueuePanel()));
        tabs.add("Linked List", createTab(new LinkedListPanel()));
        tabs.add("BST", createTab(new BSTPanel()));

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DataStructureVisualizer().setVisible(true);
        });
    }

    static JPanel createTab(JPanel drawing) {
        JPanel tab = new JPanel(new BorderLayout());
        BorderLayout layout = (BorderLayout) drawing.getLayout();
        Component controls = layout.getLayoutComponent(BorderLayout.NORTH);
        Component status = layout.getLayoutComponent(BorderLayout.SOUTH);
        // Keep the buttons and status visible while the drawing scrolls.
        drawing.remove(controls);
        drawing.remove(status);
        tab.add(controls, BorderLayout.NORTH);
        tab.add(new JScrollPane(drawing), BorderLayout.CENTER);
        tab.add(status, BorderLayout.SOUTH);
        return tab;
    }

    static void resizeDrawing(JPanel panel, int width, int height) {
        // Scrollbars appear when the drawing needs more room than the window.
        panel.setPreferredSize(new Dimension(Math.max(950, width), Math.max(600, height)));
        panel.revalidate();
    }

    // STACK PANEL

    static class StackPanel extends JPanel {

        Deque<Integer> stack = new ArrayDeque<>();
        JTextField input = new JTextField(8);
        JLabel status = new JLabel("Ready");

        StackPanel(){
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(950, 600));

            JPanel controls = new JPanel();
            controls.add(new JLabel("Value"));
            controls.add(input);

            JButton push = new JButton("Push");
            JButton pop = new JButton("Pop");
            JButton peek = new JButton("Peek");
            JButton reset = new JButton("Reset");

            controls.add(push);
            controls.add(pop);
            controls.add(peek);
            controls.add(reset);

            add(controls, BorderLayout.NORTH);
            add(status, BorderLayout.SOUTH);

            push.addActionListener(e->push());
            pop.addActionListener(e->pop());
            peek.addActionListener(e->peek());
            reset.addActionListener(e->reset());
        }

        void push(){
            try{
                int val=Integer.parseInt(input.getText().trim());
                stack.push(val);
                resizeDrawing(this, 950, 150 + stack.size() * 50);
                status.setText("Pushed "+val);
                repaint();
            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        void pop(){
            if(stack.isEmpty()){
                status.setText("Stack empty");
                return;
            }
            status.setText("Popped "+stack.pop());
            resizeDrawing(this, 950, 150 + stack.size() * 50);
            repaint();
        }

        void peek(){
            if(stack.isEmpty()){
                status.setText("Stack empty");
                return;
            }
            status.setText("Top: "+stack.peek());
        }

        void reset(){
            stack.clear();
            resizeDrawing(this, 950, 600);
            status.setText("Stack reset");
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int x=getWidth()/2-50;
            int y=90;
            g.drawString(stack.isEmpty() ? "Stack empty" : "Top", x, 70);
            // The iterator starts at the top of the stack. Draw that item first.

            int i=0;

            for(int val:stack){
                g.drawRect(x,y+i*50,120,40);
                g.drawString(""+val,x+10,y+i*50+25);
                i++;
            }
        }
    }

    // QUEUE PANEL

    static class QueuePanel extends JPanel {

        Queue<Integer> queue=new LinkedList<>();
        JTextField input=new JTextField(8);
        JLabel status=new JLabel("Ready");

        QueuePanel(){
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(950, 600));

            JPanel controls=new JPanel();

            controls.add(new JLabel("Value"));
            controls.add(input);

            JButton enqueue=new JButton("Enqueue");
            JButton dequeue=new JButton("Dequeue");
            JButton peek=new JButton("Peek");
            JButton reset=new JButton("Reset");

            controls.add(enqueue);
            controls.add(dequeue);
            controls.add(peek);
            controls.add(reset);

            add(controls,BorderLayout.NORTH);
            add(status,BorderLayout.SOUTH);

            enqueue.addActionListener(e->enqueue());
            dequeue.addActionListener(e->dequeue());
            peek.addActionListener(e->peek());
            reset.addActionListener(e->reset());
        }

        void enqueue(){
            try{
                int val=Integer.parseInt(input.getText().trim());
                queue.offer(val);
                resizeDrawing(this, 160 + queue.size() * 140, 600);
                status.setText("Enqueued "+val);
                repaint();
            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        void dequeue(){
            if(queue.isEmpty()){
                status.setText("Queue empty");
                return;
            }

            status.setText("Dequeued "+queue.poll());
            resizeDrawing(this, 160 + queue.size() * 140, 600);
            repaint();
        }

        void peek(){
            if(queue.isEmpty()){
                status.setText("Queue empty");
                return;
            }

            status.setText("Front: "+queue.peek());
        }

        void reset(){
            queue.clear();
            resizeDrawing(this, 950, 600);
            status.setText("Queue reset");
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int x=80;
            int y=getHeight()/2;

            for(int val:queue){
                g.drawRect(x,y,120,40);
                g.drawString(""+val,x+10,y+25);
                x+=140;
            }
        }
    }

    // LINKED LIST PANEL

    static class LinkedListPanel extends JPanel {

        static class Node {
            final int value;
            Node next;
            Node(int value) { this.value = value; }
        }

        Node head;
        Node tail;
        int size;
        JTextField input=new JTextField(8);
        JLabel status=new JLabel("Ready");

        LinkedListPanel(){
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(950, 600));

            JPanel controls=new JPanel();

            controls.add(new JLabel("Value"));
            controls.add(input);

            JButton insert=new JButton("Insert");
            JButton delete=new JButton("Delete");
            JButton search=new JButton("Search");
            JButton reset=new JButton("Reset");

            controls.add(insert);
            controls.add(delete);
            controls.add(search);
            controls.add(reset);

            add(controls,BorderLayout.NORTH);
            add(status,BorderLayout.SOUTH);

            insert.addActionListener(e->insert());
            delete.addActionListener(e->delete());
            search.addActionListener(e->search());
            reset.addActionListener(e->reset());
        }

        void insert(){
            try{
                int val=Integer.parseInt(input.getText().trim());
                Node node = new Node(val);
                if (tail == null) head = node;
                else tail.next = node;
                tail = node;
                size++;
                resizeDrawing(this, 160 + size * 160, 600);
                status.setText("Inserted "+val);
                repaint();
            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        void delete(){
            try{
                int val=Integer.parseInt(input.getText().trim());
                Node previous = null;
                Node current = head;
                // Remove the first matching node, keeping any later duplicates.
                while (current != null && current.value != val) {
                    previous = current;
                    current = current.next;
                }
                if (current == null) {
                    status.setText("Not found: " + val);
                    return;
                }
                if (previous == null) head = current.next;
                else previous.next = current.next;
                if (current == tail) tail = previous;
                size--;
                resizeDrawing(this, 160 + size * 160, 600);
                status.setText("Deleted "+val);
                repaint();
            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        void search(){
            try{
                int val=Integer.parseInt(input.getText().trim());
                if(contains(val))
                    status.setText("Found "+val);
                else
                    status.setText("Not found");
            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        void reset(){
            head = null;
            tail = null;
            size = 0;
            resizeDrawing(this, 950, 600);
            status.setText("Reset list");
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int x=60;
            int y=getHeight()/2;

            for(Node node = head; node != null; node = node.next){
                g.drawRect(x,y,120,40);
                g.drawString(""+node.value,x+10,y+25);

                g.drawLine(x+120,y+20,x+150,y+20);
                g.drawLine(x+150,y+20,x+143,y+15);
                g.drawLine(x+150,y+20,x+143,y+25);

                x+=160;
            }
            g.drawString("null", x, y+25);
        }

        boolean contains(int value) {
            for (Node node = head; node != null; node = node.next) {
                if (node.value == value) return true;
            }
            return false;
        }
    }

    // BST PANEL

    static class BSTPanel extends JPanel {

        class Node{
            int val;
            Node left,right;
            int x, y;
            Node(int v){val=v;}
        }

        Node root=null;
        int nodeCount;
        int deepestLevel;

        JTextField input=new JTextField(8);
        JLabel status=new JLabel("Ready");

        BSTPanel(){

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(950, 600));

            JPanel controls=new JPanel();

            controls.add(new JLabel("Value"));
            controls.add(input);

            JButton insert=new JButton("Insert");
            JButton search=new JButton("Search");
            JButton reset=new JButton("Reset");

            controls.add(insert);
            controls.add(search);
            controls.add(reset);

            add(controls,BorderLayout.NORTH);
            add(status,BorderLayout.SOUTH);

            insert.addActionListener(e->insert());
            search.addActionListener(e->search());
            reset.addActionListener(e->reset());
        }

        Node insertRec(Node node,int val){

            if(node==null) return new Node(val);

            if(val<node.val)
                node.left=insertRec(node.left,val);
            else if(val>node.val)
                node.right=insertRec(node.right,val);

            return node;
        }

        void insert(){
            try{
                int val=Integer.parseInt(input.getText().trim());
                if (searchRec(root, val)) {
                    status.setText("Already in tree: " + val);
                    return;
                }
                root=insertRec(root,val);
                nodeCount++;
                deepestLevel = 0;
                positionNodes(root, 0, 0);
                resizeDrawing(this, 120 + nodeCount * 140, 180 + deepestLevel * 100);
                status.setText("Inserted "+val);
                repaint();
            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        boolean searchRec(Node node,int val){

            if(node==null) return false;

            if(val==node.val) return true;

            if(val<node.val)
                return searchRec(node.left,val);

            return searchRec(node.right,val);
        }

        void search(){
            try{
                int val=Integer.parseInt(input.getText().trim());

                if(searchRec(root,val))
                    status.setText("Found "+val);
                else
                    status.setText("Not found");

            }catch(NumberFormatException e){
                status.setText("Invalid input");
            }
        }

        void reset(){
            root=null;
            nodeCount = 0;
            resizeDrawing(this, 950, 600);
            status.setText("BST reset");
            repaint();
        }

        int positionNodes(Node node, int position, int level) {
            if (node == null) return position;
            // In-order positions give each node its own horizontal space.
            position = positionNodes(node.left, position, level + 1);
            node.x = 60 + position * 140;
            node.y = 90 + level * 100;
            deepestLevel = Math.max(deepestLevel, level);
            return positionNodes(node.right, position + 1, level + 1);
        }

        void drawTree(Graphics g, Node node) {
            if (node == null) return;
            if (node.left != null)
                g.drawLine(node.x+60,node.y+40,node.left.x+60,node.left.y);
            if (node.right != null)
                g.drawLine(node.x+60,node.y+40,node.right.x+60,node.right.y);
            g.drawOval(node.x,node.y,120,40);
            g.drawString(""+node.val,node.x+15,node.y+25);
            drawTree(g,node.left);
            drawTree(g,node.right);
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            drawTree(g,root);
        }
    }
}
