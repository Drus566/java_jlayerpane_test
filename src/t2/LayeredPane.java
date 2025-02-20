package t2;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class LayeredPane extends JFrame {
    JLayeredPane lp;
    JPanel m_panel;
    public LayeredPane() {
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lp = new JLayeredPane();
        lp.setPreferredSize(new Dimension(400,300));
        lp.setBackground(Color.gray);
        lp.setBounds(0, 0, getWidth(), getHeight());

        JButton b = new JButton("GG");
        b.setFocusable(false);
        b.setBounds(0,0,20,40);
        b.setOpaque(false);
        lp.add(b, 1);

        JPanel np = new JPanel(new MigLayout("insets 10"));
        np.setBounds(30, 30, 50, 200);
        np.setBackground(Color.YELLOW);
        np.setOpaque(true);

        lp.add(np, 2);
        lp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
        });

        m_panel = new JPanel(new MigLayout("insets 10"));
        m_panel.setBackground(Color.WHITE);
        JTextField t = new JTextField("GGWP");
        t.requestFocusInWindow();
//        m_panel.setComponentZOrder(t, 1);
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                b.revalidate();
                b.repaint();
            }

            public void focusLost(FocusEvent e) {
                b.revalidate();
                b.repaint();
            }

        });
        t.setOpaque(true);
        t.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                b.revalidate();
                b.repaint();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                b.revalidate();
                b.repaint();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                b.revalidate();
                b.repaint();

            }
        });
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                b.revalidate();
                b.repaint();

            }
        });
        m_panel.add(t);
        m_panel.setBounds(0, 0, getWidth(), getHeight());



        String[] columnNames = { "Имя", "Фамилия", "Телефон" };

        // Данные для таблицы
        Object[][] data = {
                { "John", "Smith", "1112221" },
                { "Ivan", "Black", "2221111" },
                { "George", "White", "3334444" },
                { "Bolvan", "Black", "2235111" }
        };

        // Создаем JTable с данными
        JTable table = new JTable(data, columnNames);
        // Добавляем таблицу в панель прокрутки
        JScrollPane scrollPane = new JScrollPane(table);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                np.revalidate();
                np.repaint();
            }
        });


        // Добавляем панель прокрутки в окно
//        getContentPane().add(scrollPane);

        m_panel.add(scrollPane);

        setLayeredPane(lp);
//        setContentPane(m_panel);
        lp.add(m_panel, -1);
        pack();
        setVisible(true);
        setBackground(Color.white);

        // Создаем LayerUI для кастомизации
        LayerUI<JTable> layerUI = new LayerUI<>() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Rectangle clipArea = new Rectangle(20,40,100,100);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setClip(clipArea);
                Point p = c.getLocationOnScreen();
                Point p1 = np.getLocationOnScreen();

//                Rectangle.intersect();


//                    np.revalidate();
//                    np.repaint();


                super.paint(g, c);
                // Рисуем полупрозрачный прямоугольник
//                System.out.println("DRAW: " );
//                if (c.getClass().getSimpleName() == "JTable") {
//                    np.revalidate();
//                    np.repaint();
//                }
                g.setColor(new Color(0, 0, 0, 50));
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

            public void installUI(JComponent c) {
                super.installUI(c);
                long allEventMask = AWTEvent.MOUSE_EVENT_MASK |
                        AWTEvent.MOUSE_MOTION_EVENT_MASK |
                        AWTEvent.MOUSE_WHEEL_EVENT_MASK |
                        AWTEvent.KEY_EVENT_MASK |
                        AWTEvent.FOCUS_EVENT_MASK |
                        AWTEvent.ACTION_EVENT_MASK |
                        AWTEvent.INPUT_METHOD_EVENT_MASK |
                        AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK |
                        AWTEvent.ADJUSTMENT_EVENT_MASK |
                        AWTEvent.COMPONENT_EVENT_MASK |
                        AWTEvent.CONTAINER_EVENT_MASK |
                        AWTEvent.INVOCATION_EVENT_MASK |
                        AWTEvent.PAINT_EVENT_MASK |
                        AWTEvent.HIERARCHY_EVENT_MASK |
                        AWTEvent.ITEM_EVENT_MASK |
                        AWTEvent.TEXT_EVENT_MASK |
                        AWTEvent.WINDOW_EVENT_MASK |
                        AWTEvent.WINDOW_FOCUS_EVENT_MASK |
                        AWTEvent.WINDOW_STATE_EVENT_MASK;
                // enable mouse motion events for the layer's subcomponents
                ((JLayer) c).setLayerEventMask(allEventMask);
            }

            public void uninstallUI(JComponent c) {
                super.uninstallUI(c);
                // reset the layer event mask
                ((JLayer) c).setLayerEventMask(0);
            }

            // overridden method which catches MouseMotion events
            public void eventDispatched(AWTEvent e, JLayer<? extends JTable> l) {
                System.out.println("AWTEvent detected: " + e);
                System.out.println("CLASS: " + e.getSource().getClass().getSimpleName());
                System.out.println();

                String c = e.getSource().getClass().getSimpleName();
                if (c.equalsIgnoreCase("JTable") ||
                c.equalsIgnoreCase("UIResource") ||
                c.equalsIgnoreCase("CellRendererPane") ||
                c.equalsIgnoreCase("JTextField")) {
                    np.revalidate();
                    np.repaint();
//                    s.setText("HI, YOU CLICKED TO JBUTTON +DD");
                } else {
//                    s.setText("NOTHING");
                }
            }

//            @Override
//            protected void processFocusEvent(FocusEvent e, JLayer<? extends JTable> l) {
////                if (e.getID() == FocusEvent.FOCUS_GAINED) {
////                    // Предотвращаем дальнейшую обработку события
////                    System.out.println("Focus gained - preventing further processing.");
////                    return; // Не вызываем super.processFocusEvent
////                }
////                super.processFocusEvent(e, l); // Обрабатываем другие события
//                np.revalidate();
//                np.repaint();
//            }
//
//            @Override
//            protected void processHierarchyBoundsEvent(HierarchyEvent e, JLayer<? extends JTable> l) {
////                super.processHierarchyBoundsEvent(e, l);
//                np.revalidate();
//                np.repaint();
//            }
//
//            @Override
//            protected void processHierarchyEvent(HierarchyEvent e, JLayer<? extends JTable> l) {
////                super.processHierarchyEvent(e, l);
//                np.revalidate();
//                np.repaint();
//            }
//
//            @Override
//            protected void processComponentEvent(ComponentEvent e, JLayer<? extends JTable> l) {
////                super.processComponentEvent(e, l);
//                np.revalidate();
//                np.repaint();
//
//            }
//
//            @Override
//            protected void processInputMethodEvent(InputMethodEvent e, JLayer<? extends JTable> l) {
////                super.processInputMethodEvent(e, l);
//                np.revalidate();
//                np.repaint();
//
//            }
//
//            @Override
//            protected void processKeyEvent(KeyEvent e, JLayer<? extends JTable> l) {
////                super.processKeyEvent(e, l);
//                np.revalidate();
//                np.repaint();
//
//            }
//
//            @Override
//            protected void processMouseEvent(MouseEvent e, JLayer<? extends JTable> l) {
////                super.processMouseEvent(e, l);
//                np.revalidate();
//                np.repaint();
//
//            }
//
//            @Override
//            protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JTable> l) {
////                super.processMouseMotionEvent(e, l);
//                np.revalidate();
//                np.repaint();
//
//            }
//
//            @Override
//            protected void processMouseWheelEvent(MouseWheelEvent e, JLayer<? extends JTable> l) {
////                super.processMouseWheelEvent(e, l);
//                np.revalidate();
//                np.repaint();
//
//            }



        };

        // Оборачиваем JPanel в JLayer
        JLayer<JTable> jLayer = new JLayer<>(table, layerUI);
        m_panel.add(jLayer);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> m_panel.setBounds(0, 0, getWidth(), getHeight()));
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> m_panel.setBounds(0, 0, getWidth(), getHeight()));

            }

            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> m_panel.setBounds(0, 0, getWidth(), getHeight()));

            }

            @Override
            public void componentHidden(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> m_panel.setBounds(0, 0, getWidth(), getHeight()));

            }
        });

    }

}
