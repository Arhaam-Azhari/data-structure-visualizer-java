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

        tabs.add("Stack", new StackPanel());
        tabs.add("Queue", new QueuePanel());
        tabs.add("Linked List", new LinkedListPanel());
        tabs.add("BST", new BSTPanel());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DataStructureVisualizer().setVisible(true);
        });
    }

    // STACK PANEL

    static class StackPanel extends JPanel {

        Deque<Integer> stack = new ArrayDeque<>();
        JTextField input = new JTextField(8);
        JLabel status = new JLabel("Ready");

        StackPanel(){
            setLayout(new BorderLayout());

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
                int val=Integer.parseInt(input.getText());
                stack.push(val);
                status.setText("Pushed "+val);
                repaint();
            }catch(Exception e){
                status.setText("Invalid input");
            }
        }

        void pop(){
            if(stack.isEmpty()){
                status.setText("Stack empty");
                return;
            }
            status.setText("Popped "+stack.pop());
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
            status.setText("Stack reset");
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int x=getWidth()/2-50;
            int y=getHeight()-120;

            int i=0;

            for(int val:stack){
                g.drawRect(x,y-i*50,100,40);
                g.drawString(""+val,x+45,y-i*50+25);
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
                int val=Integer.parseInt(input.getText());
                queue.offer(val);
                status.setText("Enqueued "+val);
                repaint();
            }catch(Exception e){
                status.setText("Invalid input");
            }
        }

        void dequeue(){
            if(queue.isEmpty()){
                status.setText("Queue empty");
                return;
            }

            status.setText("Dequeued "+queue.poll());
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
            status.setText("Queue reset");
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int x=80;
            int y=getHeight()/2;

            for(int val:queue){
                g.drawRect(x,y,80,40);
                g.drawString(""+val,x+35,y+25);
                x+=100;
            }
        }
    }

    // LINKED LIST PANEL

    static class LinkedListPanel extends JPanel {

        java.util.List<Integer> list=new ArrayList<>();
        JTextField input=new JTextField(8);
        JLabel status=new JLabel("Ready");

        LinkedListPanel(){
            setLayout(new BorderLayout());

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
                int val=Integer.parseInt(input.getText());
                list.add(val);
                status.setText("Inserted "+val);
                repaint();
            }catch(Exception e){
                status.setText("Invalid input");
            }
        }

        void delete(){
            try{
                int val=Integer.parseInt(input.getText());
                list.remove(Integer.valueOf(val));
                status.setText("Deleted "+val);
                repaint();
            }catch(Exception e){
                status.setText("Invalid input");
            }
        }

        void search(){
            try{
                int val=Integer.parseInt(input.getText());
                if(list.contains(val))
                    status.setText("Found "+val);
                else
                    status.setText("Not found");
            }catch(Exception e){
                status.setText("Invalid input");
            }
        }

        void reset(){
            list.clear();
            status.setText("Reset list");
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            int x=60;
            int y=getHeight()/2;

            for(int i=0;i<list.size();i++){
                g.drawRect(x,y,70,40);
                g.drawString(""+list.get(i),x+30,y+25);

                if(i<list.size()-1){
                    g.drawLine(x+70,y+20,x+100,y+20);
                }

                x+=110;
            }
        }
    }

    // BST PANEL

    static class BSTPanel extends JPanel {

        class Node{
            int val;
            Node left,right;
            Node(int v){val=v;}
        }

        Node root=null;

        JTextField input=new JTextField(8);
        JLabel status=new JLabel("Ready");

        BSTPanel(){

            setLayout(new BorderLayout());

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
                int val=Integer.parseInt(input.getText());
                root=insertRec(root,val);
                status.setText("Inserted "+val);
                repaint();
            }catch(Exception e){
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
                int val=Integer.parseInt(input.getText());

                if(searchRec(root,val))
                    status.setText("Found "+val);
                else
                    status.setText("Not found");

            }catch(Exception e){
                status.setText("Invalid input");
            }
        }

        void reset(){
            root=null;
            status.setText("BST reset");
            repaint();
        }

        void drawTree(Graphics g,Node node,int x,int y,int offset){

            if(node==null) return;

            g.drawOval(x,y,40,40);
            g.drawString(""+node.val,x+15,y+25);

            if(node.left!=null){
                g.drawLine(x+20,y+40,x-offset+20,y+100);
                drawTree(g,node.left,x-offset,y+100,offset/2);
            }

            if(node.right!=null){
                g.drawLine(x+20,y+40,x+offset+20,y+100);
                drawTree(g,node.right,x+offset,y+100,offset/2);
            }
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            drawTree(g,root,getWidth()/2,60,200);
        }
    }
}
